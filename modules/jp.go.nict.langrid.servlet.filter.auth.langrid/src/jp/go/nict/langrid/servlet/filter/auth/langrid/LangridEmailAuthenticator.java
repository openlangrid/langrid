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

import javax.servlet.ServletRequest;

import jp.go.nict.langrid.commons.security.MessageDigestUtil;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.User;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 307 $
 */
public class LangridEmailAuthenticator extends AbstractLangridBasicAuthenticator{
	@Override
	protected boolean doAuthenticate(
			ServletServiceContext context
			, ServletRequest request, String authUser, String authPass)
	throws UserNotFoundException, DaoException
	{
		String authDigestedPass = MessageDigestUtil.digestBySHA512(authPass);
		String selfGridId = context.getSelfGridId();
		User u = getUserDao().getUserByEmail(selfGridId, authUser);
		if(u.getPassword().equals(authDigestedPass)){
			context.setAuthorized(selfGridId, authUser, authPass);
			return true;
		}else if(u.getPassword().equals(authPass)){
			context.setAuthorized(selfGridId, authUser, authPass);
			return true;
		}
		return false;
	}
}
