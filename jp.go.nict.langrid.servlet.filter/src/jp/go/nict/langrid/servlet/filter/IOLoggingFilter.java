/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.servlet.filter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.io.DuplicatingOutputStream;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.ws.param.FilterConfigParameterContext;
import jp.go.nict.langrid.commons.ws.servlet.AbstractHttpFilter;
import jp.go.nict.langrid.commons.ws.servlet.AlternateInputHttpServletRequestWrapper;
import jp.go.nict.langrid.commons.ws.servlet.AlternateOutputHttpServletResponseWrapper;
import jp.go.nict.langrid.commons.ws.servlet.ByteArrayServletInputStream;
import jp.go.nict.langrid.commons.ws.servlet.OutputStreamServletOutputStream;

/**
 * Filter for I/O logging.
 * @author Takao Nakaguchi
 */
public class IOLoggingFilter extends AbstractHttpFilter{
	@Override
	public void init(FilterConfig config) throws ServletException {
		ParameterContext c = new FilterConfigParameterContext(config, true);
		logFilePrefix = c.getValue("langrid.logFilePrefix");
		if(logFilePrefix != null){
			logFilePrefix = config.getServletContext().getRealPath(logFilePrefix);
		}
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		long id = idgen.incrementAndGet();
		Date now = new Date();
		String dateTimeString = dateTimeFormat.get().format(now);
		String logFileDateTimeString = logFileDateTimeFormat.get().format(now);
		chain.doFilter(
				loggedRequest(id, logFileDateTimeString, dateTimeString, request)
				, loggedResponse(id, logFileDateTimeString, response)
				);
	}

	@Override
	public void destroy() {
	}

	private HttpServletRequest loggedRequest(long id, String logFileDateTimeString
			, String dateTimeString, HttpServletRequest request)
	throws IOException{
		if(logFilePrefix == null) return request;
		byte[] reqBytes = StreamUtil.readAsBytes(request.getInputStream());
		String path = String.format("%s_%s_%04d_request.log"
				, logFilePrefix, logFileDateTimeString, id);
		try{
			FileOutputStream os = new FileOutputStream(path);
			try{
				OutputStreamWriter w = new OutputStreamWriter(os);
				w.write(createRequestLogHeader(id, dateTimeString, request));
				writeHttpHeaders(w, request);
				w.write("\r\n");
				w.flush();
				os.write(reqBytes);
			} finally{
				os.close();
			}
		} catch(FileNotFoundException e){
			logger.warning("failed to create requestLogFile: " + path);
		} catch(IOException e){
			logger.warning("failed to write request log");
		}
		return new AlternateInputHttpServletRequestWrapper(
				request, new ByteArrayServletInputStream(reqBytes)
				);
	}

	private HttpServletResponse loggedResponse(long id
			, String dateTimeString, HttpServletResponse response) throws IOException{
		if(logFilePrefix == null) return response;
		String path = String.format("%s_%s_%04d_response.log", logFilePrefix, dateTimeString, id);
		try{
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter w = new OutputStreamWriter(fos, "ISO8859-1");
			w.write(createResponseLogHeader(id, dateTimeString));
			w.flush();
			OutputStream os = new DuplicatingOutputStream(
					response.getOutputStream(), fos
					);
			return new AlternateOutputHttpServletResponseWrapper(
					response, new OutputStreamServletOutputStream(os)
					);
		} catch(FileNotFoundException e){
			logger.warning("failed to create responseLogFile: " + path);
			return response;
		}
	}

	private static String createRequestLogHeader(long id, String dateTimeString, HttpServletRequest request){
		return dateTimeString
				+ " [request reqid:" + id + "] " + request.getRemoteAddr()
				+ " - " + request.getRemoteHost()
				+ " - \"" + request.getMethod()
					+ " " + request.getRequestURI()
					+ (request.getQueryString() != null ? request.getQueryString() : "")
					+ " " + request.getProtocol()
					+ "\""
				+ " " + request.getContentLength()
				+ " \"" + request.getHeader("Referrer") + "\""
				+ " \"" + request.getHeader("User-Agent") + "\""
				+ "\n";
	}

	private static void writeHttpHeaders(OutputStreamWriter writer, HttpServletRequest request)
	throws IOException{
		Enumeration<?> e = request.getHeaderNames();
		while(e.hasMoreElements()){
			String key = e.nextElement().toString();
			writer.write(key + ": " + request.getHeader(key) + "\r\n");
		}
	}

	private static String createResponseLogHeader(long id, String dateTimeString){
		return dateTimeString
				+ " [response reqid:" + id + "] \n";
	}

	private String logFilePrefix;
	private static AtomicLong idgen = new AtomicLong(0);
	private static ThreadLocal<DateFormat> dateTimeFormat = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		}};
	private static ThreadLocal<DateFormat> logFileDateTimeFormat = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}};
	private static Logger logger = Logger.getLogger(IOLoggingFilter.class.getName());
}
