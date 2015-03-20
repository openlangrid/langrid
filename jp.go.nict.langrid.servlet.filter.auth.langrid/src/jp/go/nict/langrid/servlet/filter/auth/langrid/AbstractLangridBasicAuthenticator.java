/*
 * $Id: AbstractLangridBasicAuthenticator.java 250 2010-10-03 03:11:03Z t-nakaguchi $
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
package jp.go.nict.langrid.servlet.filter.auth.langrid;

import java.io.IOException;
import java.util.logging.Level;
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
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.TemporaryUserDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.UserNotFoundException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 250 $
 */
public abstract class AbstractLangridBasicAuthenticator
extends AbstractLangridAuthenticator{
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		try{
			userDao = getDaoFactory().createUserDao();
			temporaryUserDao = getDaoFactory().createTemporaryUserDao();
		} catch(DaoException e){
			logger.log(Level.WARNING, "failed to initialize filter: "
					+ getClass().getName(), e);
			setInitializationException(new ServletException(e));
		}
	}

	public void destroy() {
	}

	@Override
	public void doFilterWithAuth(
			ServletServiceContext context
			, final HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws DaoException, IOException, ServletException{
		if(request.getRemoteUser() != null){
			chain.doFilter(request, response);				
			return;
		}

		boolean authorized = false;
		try{
			authorized = doAuthenticate(context, request);
		} catch(DaoException e){
			logger.log(
					Level.WARNING, "failed to execute authorization process."
					, e);
		}
		if(authorized){
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

	protected abstract boolean doAuthenticate(
			ServletServiceContext context, ServletRequest request
			, String authUser, String authPass)
	throws UserNotFoundException, DaoException;

	protected UserDao getUserDao() {
		return userDao;
	}

	protected TemporaryUserDao getTemporaryUserDao() {
		return temporaryUserDao;
	}

	private boolean doAuthenticate(ServletServiceContext context
			, ServletRequest request)
	throws DaoException
	{
		Pair<String, String> basicAuthUserAndPassword = context.getBasicAuthUserAndPassword();
		if(basicAuthUserAndPassword == null) return false;
		String authUser = basicAuthUserAndPassword.getFirst();
		String authPass = basicAuthUserAndPassword.getSecond();
		Throwable t = null;
		DaoContext daoContext = getDaoContext();
		if(daoContext == null){
			logger.severe("failed to initialize filter: " + getClass().getName());
			return false;
		}
		daoContext.beginTransaction();	
		try{
			return doAuthenticate(
					context, request, authUser, authPass);
		} catch(UserNotFoundException e){
			return false;
		} catch(Throwable e){
			logger.log(
					Level.SEVERE
					, "unexpected exception occurred in authentication process."
					, e);
			t = e;
			return false;
		} finally{
			try{
				if(t == null){
					daoContext.commitTransaction();
				} else{
					daoContext.rollbackTransaction();
				}
			} catch(DaoException e){
				logger.log(Level.SEVERE
						, "unexpected exception occurred in authentication process(commit fase)."
						, e);
			}
		}
	}

	private UserDao userDao;
	private TemporaryUserDao temporaryUserDao;
	private static Logger logger = Logger.getLogger(
			AbstractLangridBasicAuthenticator.class.getName());
}
