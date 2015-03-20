/*
 * $Id: TemporaryUserLogic.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.management.logic;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.commons.lang.block.BlockPR;
import jp.go.nict.langrid.commons.security.MessageDigestUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.TemporaryUserSearchResult;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.TemporaryUser;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class TemporaryUserLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public TemporaryUserLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getTemporaryUserDao().clear();
	}

	@DaoTransaction
	public void clearExpiredUsers() throws DaoException{
		getTemporaryUserDao().clearExpiredUsers();
	}

	@DaoTransaction
	public TemporaryUserSearchResult searchTemporaryUsers(
			int startIndex, int maxCount, String userGridId
			, String parentUserId
			, MatchingCondition[] conditions, Order[] orders
	) throws DaoException{
		return getTemporaryUserDao().searchUsers( 
				startIndex, maxCount, userGridId, parentUserId
				, conditions, orders
				);
	}

	/**
	 * 
	 * 
	 */
	@DaoTransaction
	public void addUser(TemporaryUser temporaryUser)
	throws UserAlreadyExistsException, UserNotFoundException, DaoException{
		String ugid = temporaryUser.getGridId();
		String upid = temporaryUser.getParentUserId();
		String uid = temporaryUser.getUserId();
		if(getTemporaryUserDao().isUserExists(ugid, uid)){
			throw new UserAlreadyExistsException(ugid, uid);
		}
		if(getUserDao().isUserExist(ugid, uid)){
			throw new UserAlreadyExistsException(ugid, uid);
		}
		if(!getUserDao().isUserExist(ugid, upid)){
			throw new UserNotFoundException(ugid, upid);
		}
		temporaryUser.setPassword(MessageDigestUtil.digestBySHA512(temporaryUser.getPassword()));
		getTemporaryUserDao().addUser(temporaryUser);
	}

	@DaoTransaction
	public void deleteUser(String userGridId, String userId)
	throws UserNotFoundException, DaoException{
		getTemporaryUserDao().deleteUser(userGridId, userId);
	}

	@DaoTransaction
	public <T> T transactRead(String userGridId, String userId, BlockPR<TemporaryUser, T> userBlock)
	throws UserNotFoundException, DaoException{
		return userBlock.execute(getTemporaryUserDao().getUser(userGridId, userId));
	}

	@DaoTransaction
	public void transactUpdate(String userGridId, String userId, BlockP<TemporaryUser> userBlock)
	throws UserNotFoundException, DaoException{
		TemporaryUser u = getTemporaryUserDao().getUser(userGridId, userId);
		userBlock.execute(u);
		u.touchUpdatedDateTime();
	}

	@DaoTransaction
	public void setUserPassword(String userGridId, String userId, String password)
	throws UserNotFoundException, DaoException{
		TemporaryUser u = getTemporaryUserDao().getUser(userGridId, userId);
		u.setPassword(MessageDigestUtil.digestBySHA512(password));
		u.touchUpdatedDateTime();
	}
}
