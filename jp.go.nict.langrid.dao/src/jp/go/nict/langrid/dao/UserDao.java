/*
 * $Id:UserDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao;

import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.entity.User;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public interface UserDao{
	/**
	 * 
	 * 
	 */
	void clear() throws DaoException;

	/**
	 * 
	 * 
	 */
	void clearExceptAdmins() throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<User> dumpAllUsers(String userGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<User> listAllUsers(String userGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	UserSearchResult searchUsers(
			int startIndex, int maxCount
			, String userGridId
			, MatchingCondition[] conditions, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	boolean isUserExist(String userGridId, String userId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	void addUser(User user, String... userRoles)
	throws DaoException, UserAlreadyExistsException;

	/**
	 * 
	 * 
	 */
	void deleteUser(String userGridId, String userId)
	throws DaoException, UserNotFoundException;

	/**
	 * 
	 * 
	 */
	void deleteUsersOfGrid(String gridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	User getUser(String userGridId, String userId)
	throws DaoException, UserNotFoundException;

	/**
	 * 
	 * 
	 */
	boolean hasUserRole(String userGridId, String userId, String role)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	UserSearchResult searchUsersShouldChangePassword(
			int startIndex, int maxCount
			, String userGridId, Calendar dateTime, Order[] orders)
	throws DaoException;
}
