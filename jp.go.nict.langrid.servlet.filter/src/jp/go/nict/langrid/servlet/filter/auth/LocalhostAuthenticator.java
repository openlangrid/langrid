/*
 * $Id: LocalhostAuthenticator.java 498 2012-05-24 04:21:03Z t-nakaguchi $
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
package jp.go.nict.langrid.servlet.filter.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.ws.BasicAuthUtil;
import jp.go.nict.langrid.commons.ws.servlet.AbstractHttpFilter;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 498 $
 */
public class LocalhostAuthenticator extends AbstractHttpFilter{
	@Override
	public void init(FilterConfig config) throws ServletException {
		String pattern = config.getInitParameter("allowedUrlPatterns");
		if(pattern != null){
			for(String s : pattern.split(",")){
				patterns.add(Pattern.compile(s));
			}
		} else{
			patterns.add(Pattern.compile(".*"));
		}
		super.init(config);
	}

	@Override
	protected void doFilter(final HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		do{
			if(request.getRemoteUser() != null) break;
			if(!request.getRemoteAddr().equals("127.0.0.1")
					&& !request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) break;
	
			boolean matched = false;
			String qs = request.getQueryString();
			StringBuffer urlb = request.getRequestURL();
			if(qs != null){
				urlb.append("?").append(qs);
			}
			String url = urlb.toString();
			for(Pattern p : patterns){
				matched = p.matcher(url).matches();
				if(matched) break;
			}
			if(!matched) break;

			final String authHeader = BasicAuthUtil.encode("localhost", "");
			chain.doFilter(
					new HttpServletRequestWrapper(request){
						public String getRemoteUser(){
							return "localhost";
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
			return;
		} while(false);
		chain.doFilter(request, response);
	}

	private List<Pattern> patterns = new ArrayList<Pattern>();
}
