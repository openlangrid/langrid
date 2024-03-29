/*
 * $Id: UserLogic.java 1506 2015-03-02 16:03:34Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import jp.go.nict.langrid.commons.security.MessageDigestUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.UserSearchResult;
import jp.go.nict.langrid.dao.UserUtil;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.ServicePK;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserAttribute;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class UserLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public UserLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getUserDao().clear();
		getAccessRightDao().clearExceptDefaults();
		getAccessLimitDao().clearExceptDefaults();
		getAccessStateDao().clear();
		getAccessLogDao().clear();
	}

	@DaoTransaction
	public void initialize() throws DaoException{
		getUserDao().clearExceptAdmins();
		getTemporaryUserDao().clear();
		getAccessRightDao().clearExceptDefaults();
		getAccessLimitDao().clearExceptDefaults();
		getAccessStateDao().clear();
		getAccessLogDao().clear();
	}

	@DaoTransaction
	public UserSearchResult searchUsers(
			int startIndex, int maxCount, String userGridId
			, MatchingCondition[] conditions, Order[] orders
	) throws DaoException{
		return getUserDao().searchUsers(
				startIndex, maxCount, userGridId, conditions, orders
				);
	}

	@DaoTransaction
	public UserSearchResult searchUsersShouldChangePassword(
			int startIndex, int maxCount, String userGridId
			, int days, Order[] orders
			)
	throws DaoException{
		Calendar date = CalendarUtil.cloneAndAdd(
				Calendar.getInstance(), Calendar.DATE, -days
				);
		return getUserDao().searchUsersShouldChangePassword(
				startIndex, maxCount, userGridId, date, orders);
	}

	@DaoTransaction
	public User getUser(String gridId, String userId)
	throws DaoException{
		return getUserDao().getUser(gridId, userId);
	}

	@DaoTransaction
	public void addUser(User user, String... roles) throws UserAlreadyExistsException, DaoException{
		String ugid = user.getGridId();
		String uid = user.getUserId();
		if(getTemporaryUserDao().isUserExists(ugid, uid)){
			throw new UserAlreadyExistsException(ugid, uid);
		}
		if(getUserDao().isUserExist(ugid, uid)){
			throw new UserAlreadyExistsException(ugid, uid);
		}
		user.setPassword(MessageDigestUtil.digestBySHA512(user.getPassword()));
		user.setPasswordChangedDate(user.getUpdatedDateTime());
		Iterator<UserAttribute> i = user.getAttributes().iterator();
		while(i.hasNext()){
			if(UserUtil.getUserProperties().containsKey(i.next().getName())){
				// 
				// What's included in the properties is inaccessible.
				// 
				i.remove();
			}
		}
		getUserDao().addUser(user, roles);

		// 
		// 
		Iterable<ServicePK> services = getAccessRightDao().listAccessibleServices(
				ugid, uid);
		for(ServicePK s : services){
			AccessLimitDao ldao = getAccessLimitDao();
			List<AccessLimit> limits = ldao.getAccessLimits(ugid, "*", s.getGridId(), s.getServiceId());
			if(limits.size() == 0){
				// 
				// 
				limits = ldao.getAccessLimits("*", "*", s.getGridId(), s.getServiceId());
			}
			for(AccessLimit l : limits){
				ldao.setAccessLimit(ugid, uid, s.getGridId(), s.getServiceId()
						, l.getPeriod(), l.getLimitType(), l.getLimitCount()
						);
			}
		}
	}

	@DaoTransaction
	public void deleteUser(String userGridId, String userId)
	throws DaoException{
		getAccessRightDao().deleteAccessRightsOfUser(userGridId, userId);
		getAccessLimitDao().deleteAccessLimitsOfUser(userGridId, userId);
		getAccessStateDao().deleteAccessStatOfUser(userGridId, userId);
		getAccessLogDao().deleteAccessLogOfUser(userGridId, userId);
		getUserDao().deleteUser(userGridId, userId);
	}

	@DaoTransaction
	public <T> T transactRead(String userGridId, String userId, Function<User, T> userBlock)
	throws UserNotFoundException, DaoException{
		return userBlock.apply(getUserDao().getUser(userGridId, userId));
	}

	@DaoTransaction
	public <T> T transactRead(String userGridId, String userId
			, Function<User, T> userBlock, Supplier<T> userNotExistBlock)
	throws UserNotFoundException, DaoException{
		UserDao dao = getUserDao();
		if(dao.isUserExist(userGridId, userId)){
			return userBlock.apply(getUserDao().getUser(userGridId, userId));
		} else{
			return userNotExistBlock.get();
		}
	}

	@DaoTransaction
	public void transactUpdate(String userGridId, String userId, Consumer<User> userBlock)
	throws UserNotFoundException, DaoException{
		User u = getUserDao().getUser(userGridId, userId);
		userBlock.accept(u);
		u.touchUpdatedDateTime();
	}

	@DaoTransaction
	public void setUserPassword(String userGridId, String userId, String password)
	throws UserNotFoundException, DaoException{
		User u = getUserDao().getUser(userGridId, userId);
		u.setPassword(MessageDigestUtil.digestBySHA512(password));
		Calendar c = Calendar.getInstance();
		u.setPasswordChangedDate(c);
		u.setUpdatedDateTime(c);
	}
	
	@DaoTransaction
	public Pair<String[], String[]> getNewerAndOlderUserIds(String userGridId, Calendar standardDateTime)
	throws DaoException{
		List<String> newer = new ArrayList<>();
		List<String> older = new ArrayList<>();
		for(Pair<String, Calendar> entry : getUserDao().listAllUserIdAndUpdates(userGridId)){
			if(entry.getSecond().after(standardDateTime)){
				newer.add(entry.getFirst());
			} else{
				older.add(entry.getFirst());
			}
		}
		return Pair.create(newer.toArray(new String[]{}), older.toArray(new String[]{}));
	}
}
