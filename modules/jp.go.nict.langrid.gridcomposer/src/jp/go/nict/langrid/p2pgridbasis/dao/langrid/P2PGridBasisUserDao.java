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
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

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
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.UserData;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisUserDao implements DataDao, UserDao {
	private P2PGridController getController() throws ControllerException{
		if (controller == null) {
			controller = JXTAController.getInstance();
		}

		return controller;
	}

	/**
	 * 
	 * 
	 */
	public P2PGridBasisUserDao(UserDao dao, DaoContext context) {
		this.dao = dao;
		this.daoContext = context;
	}

	public void setEntityListener() {
		logger.debug("### User : setEntityListener ###");
		daoContext.addEntityListener(User.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### User : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(User.class, handler);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.dao#updateDataSource(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public boolean updateDataSource(Data data) throws DataDaoException, UnmatchedDataTypeException {
		return updateDataTarget(data);
	}
	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.dao#updateData(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public boolean updateDataTarget(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.debug("[user] :" + data.getId());
		if(data.getClass().equals(UserData.class) == false) {
			throw new UnmatchedDataTypeException(UserData.class.toString(), data.getClass().toString());
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
			logger.debug("Delete");
 			boolean updated = false;
			try {
				UserData userData = (UserData) data;
				User user = userData.getUser();
				removeEntityListener();
				dao.deleteUser(user.getGridId(), user.getUserId());
				updated = true;
				setEntityListener();
				getController().baseSummaryAdd(data);
			} catch (ParseException e) {
				throw new DataDaoException(e);
			} catch (UserNotFoundException e) {
				// 
				// 
				try {
					getController().baseSummaryAdd(data);
				} catch (ControllerException e1) {
					e1.printStackTrace();
				}
			} catch (DaoException e) {
				throw new DataDaoException(e);
			} catch (ControllerException e) {
				throw new DataDaoException(e);
			}
			return updated;
		}

		User user = null;
		try {
			UserData userData = (UserData)data;
			user = userData.getUser();
			removeEntityListener();
			if(dao.isUserExist(user.getGridId(), user.getUserId())){
				logger.debug("UpDate");
				daoContext.updateEntity(user);
			}else{
				logger.debug("New");
				dao.addUser(user);
			}
			setEntityListener();
			getController().baseSummaryAdd(data);
			return true;
		} catch (ParseException e) {
			throw new DataDaoException(e);
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
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
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<User> handler = new GenericHandler<User>() {
		protected boolean onNotificationStart() {
			try{
				daoContext.beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				getController().publish(new UserData(
						daoContext.loadEntity(User.class, id)
						));
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
				daoContext.commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};
}
