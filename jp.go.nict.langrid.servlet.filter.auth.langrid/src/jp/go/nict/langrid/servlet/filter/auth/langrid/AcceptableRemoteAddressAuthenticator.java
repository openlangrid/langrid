/*
 * $Id: AcceptableRemoteAddressAuthenticator.java 250 2010-10-03 03:11:03Z t-nakaguchi $
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
package jp.go.nict.langrid.servlet.filter.auth.langrid;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;

import jp.go.nict.langrid.commons.ws.BasicAuthUtil;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.dao.AcceptableRemoteAddressDao;
import jp.go.nict.langrid.dao.DaoException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 250 $
 */
public class AcceptableRemoteAddressAuthenticator
extends AbstractLangridAuthenticator{
	@Override
	protected void doFilterWithAuth(ServletServiceContext context,
			HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws DaoException, IOException,  ServletException {
		AcceptableRemoteAddressDao dao = getDaoFactory().createAcceptableRemoteAddressDao();
		String address = request.getRemoteAddr();
		boolean acceptable = false;
		try{
			acceptable = (Boolean)cache.getFromCache(address, 60);
		} catch(NeedsRefreshException e){
			try{
				acceptable = dao.contains(address);
			} finally{
				cache.putInCache(address, acceptable);
			}
		}
		if(acceptable){
			final String authHeader = BasicAuthUtil.encode(address, "");
			final String addr = address;
			final HttpServletRequest req = request;
			chain.doFilter(
					new HttpServletRequestWrapper(request){
						public String getRemoteUser(){
							return addr;
						}

						public String getHeader(String arg0) { 
							if (arg0.equals("Authorization")) {
								return authHeader;
							}else{
								return req.getHeader(arg0);
							}
						}
					}, response
					);
		} else{
			writeForbidden(response);
		}
	}

	private Cache cache = new Cache(true, false, false);
}
