/*
 * $Id: ServiceInvoker.java 407 2011-08-25 02:21:46Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intragrid.HttpClientUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 407 $
 */
public class ServiceInvoker {
	/**
	 * 
	 * 
	 */
	public static int invoke(
			String selfGridId, URL url, String userName, String password
			, Map<String, String> headers
			, InputStream input, HttpServletResponse output, OutputStream errorOut
			, int connectionTimeout, int soTimeout)
	throws IOException, SocketTimeoutException
	{
		HttpClient client = HttpClientUtil.createHttpClientWithHostConfig(url);
		SimpleHttpConnectionManager manager = new SimpleHttpConnectionManager(true);
		manager.getParams().setConnectionTimeout(connectionTimeout);
		manager.getParams().setSoTimeout(soTimeout);
		manager.getParams().setMaxConnectionsPerHost(client.getHostConfiguration(), 2);
		client.setHttpConnectionManager(manager);
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER
				, new DefaultHttpMethodRetryHandler(0, false));
	
		PostMethod method = new PostMethod(url.getFile());
		method.setRequestEntity(new InputStreamRequestEntity(input));
		for(Map.Entry<String, String> e : headers.entrySet()){
			method.addRequestHeader(e.getKey(), e.getValue());
		}
		if(!headers.containsKey("Content-Type"))
			method.addRequestHeader("Content-Type", "text/xml; charset=utf-8");
		if(!headers.containsKey("Accept"))
			method.addRequestHeader("Accept", "application/soap+xml, application/dime, multipart/related, text/*");
		if(!headers.containsKey("SOAPAction"))
			method.addRequestHeader("SOAPAction", "\"\"");
		if(!headers.containsKey("User-Agent"))
			method.addRequestHeader("User-Agent", "Langrid Service Invoker/1.0");
		if(userName != null){
			int port = url.getPort();
			if(port == -1){
				port = url.getDefaultPort();
				if(port == -1){
					port = 80;
				}
			}
			if(password == null){
				password = "";
			}
			client.getState().setCredentials(
					new AuthScope(url.getHost(), port, null),
					new UsernamePasswordCredentials(userName, password)
					);
			client.getParams().setAuthenticationPreemptive(true);
			method.setDoAuthentication(true);
		}
		
		try{
			int status;
			try {
				status = client.executeMethod(method);
			} catch (SocketTimeoutException e) {
				status = HttpServletResponse.SC_REQUEST_TIMEOUT;
			}
			output.setStatus(status);
			if(status == 200){
				Collection<String> gridTracks = new ArrayList<>();
				for(Header h : method.getResponseHeaders()){
					String name = h.getName();
					if(name.equals(LangridConstants.HTTPHEADER_GRIDTRACK)){
						gridTracks.add(h.getValue());
					}
					if(name.startsWith("X-Langrid")
							|| (!throughHeaders.contains(name.toLowerCase())))
						continue;
					String value = h.getValue();
					output.addHeader(name, value);
				}
				output.addHeader(LangridConstants.HTTPHEADER_GRIDTRACK,
						gridTracks.size() > 0 ?
								("(" + selfGridId + ",(" + StringUtil.join(gridTracks.toArray(new String[]{}), ",") + "))")
								: selfGridId);
				OutputStream os = output.getOutputStream();
				StreamUtil.transfer(method.getResponseBodyAsStream(), os);
				os.flush();
			} else if ( status == HttpServletResponse.SC_REQUEST_TIMEOUT ) {
			} else {
				StreamUtil.transfer(method.getResponseBodyAsStream(), errorOut);
			}
			return status;
		} finally{
			manager.shutdown();
		}
	}

	private static Set<String> throughHeaders = new HashSet<String>();
	static{
		throughHeaders.add("Accept-Ranges".toLowerCase());
		throughHeaders.add("Content-Disposition".toLowerCase());
		throughHeaders.add("Content-Encoding".toLowerCase());
		throughHeaders.add("Content-Range".toLowerCase());
		throughHeaders.add("Content-Type".toLowerCase());
		throughHeaders.add("ETag".toLowerCase());
		throughHeaders.add("Last-Modified".toLowerCase());
	}
}
