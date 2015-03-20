/*
 * $Id: SentinelAuthenticator.java 307 2010-12-01 06:05:27Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.ws.param.FilterConfigParameterContext;
import jp.go.nict.langrid.commons.ws.servlet.AbstractHttpFilter;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 307 $
 */
public class SentinelAuthenticator extends AbstractHttpFilter{
	public void init(FilterConfig config) throws ServletException{
		ParameterContext c = new FilterConfigParameterContext(config, false);
		String ignores = c.getValue("ignorePaths");
		if(ignores != null && !ignores.equals("")){
			ignoreRequests = ignores.replaceAll(" ", "").split(",");
		}
		String p = c.getValue("ignorePattern");
		if(p != null){
			ignorePattern = Pattern.compile(p);
		}
		realmName = c.getString("realmName", "Language Grid Services");
	}

	public void destroy() {
	}

	@Override
	protected void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String uri = request.getRequestURI();
		if(request.getQueryString() != null){
			uri = uri + "?" + request.getQueryString();
		}
		for(String  ignore : ignoreRequests){
			if(uri.startsWith(request.getContextPath() + ignore)){
				chain.doFilter(request, response);
				return;
			}
		}
		if(ignorePattern != null && ignorePattern.matcher(uri).matches()){
			chain.doFilter(request, response);
			return;
		}
			
		if(request.getRemoteUser() != null){
			chain.doFilter(request, response);
		} else if(request.getHeader("Authorization") != null){
			write403(response);
		} else{
			write401(response);
		}
	}

	private void write401(HttpServletResponse response) throws IOException{
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("WWW-Authenticate", "Basic realm=\"" + realmName + "\"");
		response.getOutputStream().write("HTTP 401 Authorization Required.".getBytes());
	}

	private void write403(HttpServletResponse response) throws IOException{
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getOutputStream().write("HTTP 403 Forbidden.".getBytes());
	}

	private String[] ignoreRequests = new String[]{};
	private Pattern ignorePattern;
	private String realmName;
}
