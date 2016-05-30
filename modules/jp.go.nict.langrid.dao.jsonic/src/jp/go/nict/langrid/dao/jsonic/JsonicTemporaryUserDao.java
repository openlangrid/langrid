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
package jp.go.nict.langrid.dao.jsonic;

import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.commons.util.ListUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.TemporaryUserDao;
import jp.go.nict.langrid.dao.TemporaryUserSearchResult;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.TemporaryUser;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicTemporaryUserDao implements TemporaryUserDao {
	public JsonicTemporaryUserDao(JsonicDaoContext context){
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
	}

	@Override
	public void clearExpiredUsers() throws DaoException {
	}

	@Override
	public List<TemporaryUser> listAllUsers(String userGridId)
			throws DaoException {
		return ListUtil.emptyList();
	}

	@Override
	public TemporaryUserSearchResult searchUsers(int startIndex, int maxCount,
			String userGridId, String parentUserId,
			MatchingCondition[] conditions, Order[] orders) throws DaoException {
		return new TemporaryUserSearchResult(new TemporaryUser[]{}, 0, true);
	}

	@Override
	public TemporaryUserSearchResult searchUsers(int startIndex, int maxCount,
			String userGridId, MatchingCondition[] conditions, Order[] orders)
			throws DaoException {
		return new TemporaryUserSearchResult(new TemporaryUser[]{}, 0, true);
	}

	@Override
	public boolean isUserExists(String userGridId, String userId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getParentUserIdIfUserAvailable(String userGridId,
			String userId, String password)
	throws DaoException {
		return null;
	}

	@Override
	public boolean isParent(String userGridId, String ownerUserId, String userId)
	throws DaoException, UserNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addUser(TemporaryUser user)
	throws DaoException, UserAlreadyExistsException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAvailableDateTime(TemporaryUser user,
			Calendar beginAvailableDateTime, Calendar endAvailableDateTime)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteUser(String userGridId, String userId)
			throws DaoException, UserNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteUsersOfGrid(String userGridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public TemporaryUser getUser(String userGridId, String userId)
	throws DaoException, UserNotFoundException {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	private JsonicDaoContext context;
}
