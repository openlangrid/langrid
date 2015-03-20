/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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
package jp.go.nict.langrid.servlet.filter.auth;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.BasicAuthUtil;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.servlet.AbstractHttpFilter;

/**
 * Simple credential-hard-coded authenticator.
 * Only for development use. 
 * @author Takao Nakaguchi
 */
public class SimpleAuthenticator extends AbstractHttpFilter{
	public void init(FilterConfig config) throws ServletException{
		this.username = config.getInitParameter("username");
		this.password = config.getInitParameter("password");
		if(username == null){
			logger.severe("parameterPrefix not set.");
			return;
		}
	}

	public void doFilter(
			final HttpServletRequest request
			, HttpServletResponse response
			, FilterChain chain)
	throws IOException, ServletException{
		// 
		// 
		ServletServiceContext context = new ServletServiceContext(request);
		if(doAuthenticate(context, request)){
			final String user = context.getAuthUser();
			String pass = context.getAuthPass();
			final String authHeader = BasicAuthUtil.encode(user, pass);
			chain.doFilter(
					new HttpServletRequestWrapper(request){
						public String getRemoteUser(){
							return user;
						}

						public String getHeader(String arg0) { 
							if (arg0.equals("Authorization")) {
								return authHeader;
							}else{
								return request.getHeader(arg0);
							}
						}
					}, response
					);
		} else{
			chain.doFilter(request, response);
		}
	}

	private boolean doAuthenticate(ServletServiceContext context
			, ServletRequest request)
	{
		Pair<String, String> basicAuthUserAndPassword = context.getBasicAuthUserAndPassword();
		if(basicAuthUserAndPassword == null) return false;
		String u = basicAuthUserAndPassword.getFirst();
		String p = basicAuthUserAndPassword.getSecond();
		if(u.length() == 0) return false;
		if(u.equals(username) && p.equals(password)){
			context.setAuthorized(null, username, "");
			return true;
		}
		return false;
	}

	private String username;
	private String password;
	private static Logger logger = Logger.getLogger(SimpleAuthenticator.class.getName());
}
