/*
 * $Id: AbstractLangridAuthenticator.java 551 2012-08-06 10:13:35Z t-nakaguchi $
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.servlet.AbstractHttpFilter;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 551 $
 */
public abstract class AbstractLangridAuthenticator
extends AbstractHttpFilter{
	public void init(FilterConfig config) throws ServletException {
		try{
			daoFactory = DaoFactory.createInstance();
			daoContext = daoFactory.getDaoContext();
		} catch(DaoException e){
			logger.log(Level.WARNING, "failed to initialize filter: "
					+ getClass().getName(), e);
			setInitializationException(new ServletException(e));
		}
	}

	public void destroy() {
	}

	public void doFilter(
			final HttpServletRequest request
			, HttpServletResponse response
			, FilterChain chain)
	throws IOException, ServletException
	{
		if(hasInitializationException()){
			chain.doFilter(request, response);
			return;
		}

		// 
		// 
		ServletServiceContext sc = new ServletServiceContext(request);
		try{
			doFilterWithAuth(sc, request, response, chain);
		} catch(DaoException e){
			logger.log(
					Level.WARNING, "failed to execute authorization process."
					, e);
		}
	}

	protected abstract void doFilterWithAuth(ServletServiceContext context
			, HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws DaoException, IOException, ServletException;

	protected DaoContext getDaoContext() {
		return daoContext;
	}

	protected DaoFactory getDaoFactory() {
		return daoFactory;
	}

	protected void writeForbidden(HttpServletResponse response)
	throws IOException
	{
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getOutputStream().write("HTTP 403 Forbidden.".getBytes());
	}

	private DaoContext daoContext;
	private DaoFactory daoFactory;
	private static Logger logger = Logger.getLogger(
			AbstractLangridAuthenticator.class.getName());
}
