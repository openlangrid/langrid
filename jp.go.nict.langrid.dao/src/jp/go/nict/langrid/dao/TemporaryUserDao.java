/*
 * $Id: TemporaryUserDao.java 214 2010-10-02 14:32:38Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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

import jp.go.nict.langrid.dao.entity.TemporaryUser;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public interface TemporaryUserDao {
	/**
	 * 
	 * 
	 */
	void clear() throws DaoException;

	/**
	 * 
	 * 
	 */
	void clearExpiredUsers() throws DaoException;

	/**
	 * 
	 * 
	 */
	public List<TemporaryUser> listAllUsers(String userGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	TemporaryUserSearchResult searchUsers(
			int startIndex, int maxCount
			, String userGridId, String parentUserId
			, MatchingCondition[] conditions, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	TemporaryUserSearchResult searchUsers(
			int startIndex, int maxCount
			, String userGridId, MatchingCondition[] conditions, Order[] orders)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	boolean isUserExists(String userGridId, String userId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	String getParentUserIdIfUserAvailable(String userGridId, String userId, String password)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	boolean isParent(String userGridId, String ownerUserId, String userId)
	throws DaoException, UserNotFoundException;

	/**
	 * 
	 * 
	 */
	void addUser(TemporaryUser user)
	throws DaoException, UserAlreadyExistsException;

	/**
	 * 
	 * 
	 */
	public void setAvailableDateTime(
			TemporaryUser user, Calendar beginAvailableDateTime
			, Calendar endAvailableDateTime)
	throws DaoException;

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
	void deleteUsersOfGrid(String userGridId)
	throws DaoException;

	/**
	 * 
	 * 
	 */
	TemporaryUser getUser(String userGridId, String userId)
	throws DaoException, UserNotFoundException;
}
