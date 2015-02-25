/*
 * $Id: ServiceManagerSession.java 406 2011-08-25 02:12:29Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.session;

import java.util.Collection;
import java.util.Set;

import jp.go.nict.langrid.management.web.model.enumeration.UserRole;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public interface ServiceManagerSession{
	/**
	 * 
	 * 
	 */
	public boolean authenticate(String gridId, String username, String password)
	throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	public void clearAuthenticatedParameter();

	/**
	 * 
	 * 
	 */
	public String getPassword();

	/**
	 * 
	 * 
	 */
	public String getSessionId();

	/**
	 * 
	 * 
	 */
	String getUserId();

	boolean isLogin();
	boolean isLoginedAccess();
	void setLoginedAccess(boolean b);
	Set<UserRole> getUserRoles();

	/**
	 * 
	 * 
	 */
	void setPassword(String password);
	
	public boolean isExpiredPassword();
	
	public void setIsExpiredPassword(boolean isExpired);
	
	public String getUserGridId();
}
