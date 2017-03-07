/*
 * $Id: LangridAuthenticator.java 307 2010-12-01 06:05:27Z t-nakaguchi $
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

import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.servlet.filter.auth.ApplicationAuthenticator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 307 $
 */
public class LangridApplicationEmailAuthenticator
extends ApplicationAuthenticator{
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		try{
			userDao = DaoFactory.createInstance().createUserDao();
		} catch(DaoException e){
			setInitializationException(new ServletException(e));
		}
	}

	@Override
	protected String resolveUser(ServiceContext context, String authUser) {
		if(authUser.indexOf('@') == -1) return authUser;
		String selfGridId = context.getSelfGridId();
		try {
			List<User> users = userDao.getUsersByEmail(selfGridId, authUser);
			if(users.size() == 0){
				System.err.println("no user for " + authUser);
				return authUser;
			}
			if(users.size() > 1){
				System.err.println("cannot determine user for " + authUser);
				return authUser;
			}
			return users.get(0).getUserId();
		} catch (DaoException e) {
			e.printStackTrace();
			return authUser;
		}
	}

	private UserDao userDao;
}
