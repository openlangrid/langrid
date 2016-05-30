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
package jp.go.nict.langrid.servlet.filter.auth;

import java.io.IOException;

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
 */
public class PassthroughAuthenticator extends AbstractHttpFilter{
	public void init(FilterConfig config) throws ServletException{
		String authIp = config.getInitParameter("allowIps");
		if(authIp != null){
			ips = authIp.replaceAll(" ", "").split(",");
		}
		userId = config.getInitParameter("userId");
		if(userId == null) throw new ServletException("userId is null.");
		authHeader = BasicAuthUtil.encode(userId, "");
	}

	public void doFilter(
			final HttpServletRequest request
			, HttpServletResponse response
			, FilterChain chain)
	throws IOException, ServletException{
		String accessAddress = request.getRemoteAddr();
		if(ips.length > 0){
			boolean validIp = false;
			for(String ip : ips){
				if(accessAddress.startsWith(ip)){
					validIp = true;
					break;
				}
			}
			if(!validIp){
				response.setStatus(403);
				return;
			}
		}
		chain.doFilter(
				new HttpServletRequestWrapper(request){
					public String getRemoteUser(){
						return userId;
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
	}

	private String[] ips = {};
	private String userId;
	private String authHeader;
}
