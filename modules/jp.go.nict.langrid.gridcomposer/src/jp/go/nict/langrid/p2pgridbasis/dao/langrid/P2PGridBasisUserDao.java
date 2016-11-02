/*
 * $Id: P2PGridBasisUserDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.dao.langrid;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.UserSearchResult;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserPK;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.UserData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisUserDao
extends AbstractP2PGridBasisUpdateManagedEntityDao<User>
implements DataDao, UserDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisUserDao(UserDao dao, DaoContext context) {
		super(context);
		this.dao = dao;
		setHandler(handler);
	}

	@Override
	synchronized public boolean updateData(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.debug("[user] :" + data.getId());
		if(data.getClass().equals(UserData.class) == false) {
			throw new UnmatchedDataTypeException(UserData.class.toString(), data.getClass().toString());
		}

		User entity = null;
		try {
			entity = ((UserData)data).getUser();
			if(entity.getGridId().equals(getSelfGridId())) return false;
			if(!isReachableToOrFrom(entity.getGridId())) return false;
		} catch(Exception e){
			throw new DataDaoException(e);
		}
		return handleData(data, entity);
	}
	
	@Override
	protected boolean beforeUpdateEntity(User entity) {
		entity.setPassword(null);
		return true;
	}

	@Override
	protected boolean beforeSaveEntity(User entity) {
		entity.setPassword(null);
		return true;
	}

	private boolean beforePublish(User user){
		user.setPassword(null);
		return true;
	}

	@Override
	public void addUser(User user, String... userRoles) throws DaoException,
			UserAlreadyExistsException {
		dao.addUser(user, userRoles);
	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void clearExceptAdmins() throws DaoException {
		dao.clearExceptAdmins();
	}

	@Override
	public void deleteUser(String userGridId, String userId)
			throws DaoException, UserNotFoundException {
		dao.deleteUser(userGridId, userId);
	}

	@Override
	public void deleteUsersOfGrid(String gridId) throws DaoException {
		dao.deleteUsersOfGrid(gridId);
	}

	@Override
	public List<User> dumpAllUsers(String userGridId) throws DaoException {
		return dao.dumpAllUsers(userGridId);
	}

	@Override
	public User getUser(String userGridId, String userId) throws DaoException,
			UserNotFoundException {
		return dao.getUser(userGridId, userId);
	}

	@Override
	public boolean hasUserRole(String userGridId, String userId, String role)
			throws DaoException {
		return dao.hasUserRole(userGridId, userId, role);
	}

	@Override
	public boolean isUserExist(String userGridId, String userId)
			throws DaoException {
		return dao.isUserExist(userGridId, userId);
	}

	@Override
	public List<User> listAllUsers(String userGridId) throws DaoException {
		return dao.listAllUsers(userGridId);
	}

	@Override
	public UserSearchResult searchUsers(int startIndex, int maxCount,
			String userGridId, MatchingCondition[] conditions, Order[] orders)
			throws DaoException {
		return dao.searchUsers(startIndex, maxCount, userGridId, conditions, orders);
	}

	@Override
	public UserSearchResult searchUsersShouldChangePassword(int startIndex,
			int maxCount, String userGridId, Calendar dateTime, Order[] orders)
			throws DaoException {
		return dao.searchUsersShouldChangePassword(startIndex, maxCount, userGridId, dateTime, orders);
	}
	
	@Override
	public List<Pair<String, Calendar>> listAllUserIdAndUpdates(String userGridId) throws DaoException {
		return dao.listAllUserIdAndUpdates(userGridId);
	}

	static private Logger logger = Logger.getLogger(P2PGridBasisUserDao.class);

	private UserDao dao;
	private GenericHandler<User> handler = new GenericHandler<User>() {
		protected boolean onNotificationStart() {
			try{
				getDaoContext().beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				User u = getDaoContext().loadEntity(User.class, id);
				if(u != null && beforePublish(u)){
					getController().publish(new UserData(u));
				}
				logger.info("published[User(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to publish instance.", e);
			} catch(DaoException e){
				logger.error("failed to access dao.", e);
			} catch(DataConvertException e){
				logger.error("failed to convert data.", e);
			}
		}

		protected void doRemove(Serializable id){
			try{
				UserPK pk = (UserPK)id;
				getController().revoke(UserData.getDataID(null, pk));
				logger.debug("revoked[User(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to revoke instance.", e);
			} catch(DataNotFoundException e){
				logger.error("failed to find data.", e);
			}
		}

		protected void onNotificationEnd(){
			try{
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};
}
