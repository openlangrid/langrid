/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicesupervisor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import jp.go.nict.langrid.commons.io.CascadingIOException;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException;
import jp.go.nict.langrid.servicesupervisor.frontend.FrontEnd;
import jp.go.nict.langrid.servicesupervisor.frontend.ProcessContext;
import jp.go.nict.langrid.servicesupervisor.frontend.SystemErrorException;

/**
 * @author Takao Nakaguchi
 */
public class PostprocessingHttpServletResponseWrapper
extends HttpServletResponseWrapper{
	public PostprocessingHttpServletResponseWrapper(
			HttpServletResponse response
			, ProcessContext processContext, FrontEnd frontEnd
			, boolean enablePostprocess)
	throws IOException{
		super(response);
		this.processContext = processContext;
		this.frontEnd = frontEnd;
		this.enablePostprocess = enablePostprocess;
		this.os = super.getOutputStream();
		this.responseHeadWriter = new PrintWriter(new OutputStreamWriter(this.responseHead, "UTF-8"));
	}

	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int sc) {
		super.setStatus(sc);
		status = sc;
	}

	@Override
	public void setStatus(int sc, String sm) {
		super.setStatus(sc, sm);
		status = sc;
	}

	public int getWrittenBytes(){
		return writtenBytes;
	}

	public Exception getExceptionInProcessing() {
		return exceptionInProcessing;
	}

	public byte[] getResponseHead(){
		return responseHead.toByteArray();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				postprocessIfNeeded(1);
				os.write(b);
				if(responseHead.size() < 40000) responseHead.write(b);
				writtenBytes++;
			}
			@Override
			public void write(byte[] b) throws IOException {
				postprocessIfNeeded(b.length);
				writing = true;
				try{
					super.write(b);
				} finally{
					writing = false;
				}
				if(responseHead.size() < 40000) responseHead.write(b);
			}
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				postprocessIfNeeded(len);
				writing = true;
				try{
					super.write(b, off, len);
				} finally{
					writing = false;
				}
				if(responseHead.size() < 40000) responseHead.write(b, off, len);
			}
			@Override
			public void print(String s) throws IOException {
				postprocessIfNeeded(s.length());
				writing = true;
				try{
					super.print(s);
				} finally{
					writing = false;
				}
				if(responseHead.size() < 40000){
					responseHeadWriter.print(s);
					responseHeadWriter.flush();
				}
			}
			@Override
			public void println(String s) throws IOException {
				postprocessIfNeeded(s.length());
				writing = true;
				try{
					super.println(s);
				} finally{
					writing = false;
				}
				if(responseHead.size() < 40000){
					responseHeadWriter.println(s);
					responseHeadWriter.flush();
				}
			}
			private void postprocessIfNeeded(int bytes) throws IOException{
				if(!writing && enablePostprocess && status == 200){
					try{
						DaoContext dc = processContext.getDaoContext();
						dc.beginTransaction();
						try{
							frontEnd.postprocess(processContext, writtenBytes + bytes);
						} finally{
							dc.commitTransaction();
						}
					} catch(AccessLimitExceededException e){
						exceptionInProcessing = e;
					} catch(DaoException e){
						exceptionInProcessing = e;
					} catch(SystemErrorException e){
						exceptionInProcessing = e;
					}
					if(exceptionInProcessing != null){
						throw new CascadingIOException(exceptionInProcessing);
					}
				}
			}
			@Override
			public boolean isReady() {
				return true;
			}
			@Override
			public void setWriteListener(WriteListener writeListener) {
			}
			private boolean writing;
		};
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(getOutputStream());
	}

	private ProcessContext processContext;
	private FrontEnd frontEnd;
	private boolean enablePostprocess;

	private int status = 200;
	private ServletOutputStream os;
	private int writtenBytes;
	private ByteArrayOutputStream responseHead = new ByteArrayOutputStream();
	private PrintWriter responseHeadWriter;
	private Exception exceptionInProcessing;
}
