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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.commons.io.RegexFileNameFilter;
import jp.go.nict.langrid.commons.security.MessageDigestUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.UserSearchResult;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserAttribute;
import jp.go.nict.langrid.dao.entity.UserRole;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicUserDao implements UserDao {
	public JsonicUserDao(JsonicDaoContext context){
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearExceptAdmins() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<User> dumpAllUsers(String userGridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<User> listAllUsers(String userGridId) throws DaoException {
		List<User> users = new ArrayList<User>();
		File[] files = getUsersBaseDir(userGridId).listFiles(new RegexFileNameFilter(".*\\.json"));
		if(files == null) return users;
		try{
			for(File f : files){
				User u = JsonicUtil.decode(f, User.class);
				u.setGridId(userGridId);
				for(UserAttribute a : u.getAttributes()){
					a.setGridId(userGridId);
				}
				for(UserRole r : u.getRoles()){
					r.setGridId(userGridId);
					r.setUserId(u.getUserId());
				}
				users.add(u);
			}
			return users;
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	@Override
	public UserSearchResult searchUsers(int startIndex, int maxCount,
			String userGridId, MatchingCondition[] conditions, Order[] orders)
			throws DaoException {
		return new UserSearchResult(new User[]{}, 0, true);
	}

	@Override
	public boolean isUserExist(String userGridId, String userId)
			throws DaoException {
		File f = new File(getUsersBaseDir(userGridId), userId + ".json");
		return f.exists() && f.isFile();
	}

	@Override
	public void addUser(User user, String... userRoles) throws DaoException,
			UserAlreadyExistsException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteUser(String userGridId, String userId)
			throws DaoException, UserNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteUsersOfGrid(String gridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public User getUser(String userGridId, String userId) throws DaoException,
			UserNotFoundException {
		File f = new File(getUsersBaseDir(userGridId), userId + ".json");
		if(!f.exists() || !f.isFile()){
			throw new UserNotFoundException(userGridId, userId);
		}
		try{
			final User obj = JsonicUtil.decode(f, User.class);
			obj.setGridId(userGridId);
			String pw = obj.getPassword();
			if (pw != null) {
				obj.setPassword(MessageDigestUtil.digestBySHA512(pw));
			}
			return obj;
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	@Override
	public User getUserByEmail(String userGridId, String email) throws DaoException,
			UserNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasUserRole(String userGridId, String userId, String role)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public UserSearchResult searchUsersShouldChangePassword(int startIndex,
			int maxCount, String userGridId, Calendar dateTime, Order[] orders)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Pair<String, Calendar>> listAllUserIdAndUpdates(String userGridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	private File getUsersBaseDir(String gridId){
		return new File(context.getGridBaseDir(gridId), "users");
	}

	private JsonicDaoContext context;
}
