/*
 * $Id: AbstractHttpFilter.java 194 2010-10-02 11:50:58Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.ws.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 194 $
 */
public abstract class AbstractHttpFilter implements Filter{
	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

	public final void doFilter(
			ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException{
		if(initException != null){
			if(initException instanceof IOException){
				throw (IOException)initException;
			} else{
				throw (ServletException)initException;
			}
		}
		if(!(request instanceof HttpServletRequest)
				|| !(response instanceof HttpServletResponse)){
			chain.doFilter(request, response);
		} else{
			doFilter((HttpServletRequest)request
				, (HttpServletResponse)response, chain);
		}
	}

	protected abstract void doFilter(
			HttpServletRequest request, HttpServletResponse response
			, FilterChain chain)
	throws IOException, ServletException;

	protected void setInitializationException(IOException e){
		initException = e;
	}
			
	protected void setInitializationException(ServletException e){
		initException = e;
	}

	protected boolean hasInitializationException(){
		return initException != null;
	}

	private Exception initException;
}
