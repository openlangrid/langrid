/*
 * $Id: P2PGridBasisTemporaryUserDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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

import org.apache.log4j.Logger;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.TemporaryUserDao;
import jp.go.nict.langrid.dao.TemporaryUserSearchResult;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.TemporaryUser;
import jp.go.nict.langrid.dao.entity.TemporaryUserPK;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.TemporaryUserData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisTemporaryUserDao
extends AbstractP2PGridBasisDao
implements DataDao, TemporaryUserDao {
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
	public P2PGridBasisTemporaryUserDao(TemporaryUserDao dao, DaoContext context) {
		this.dao = dao;
		this.daoContext = context;
	}

	public void setEntityListener() {
		logger.debug("### TemporaryUser : setEntityListener ###");
		daoContext.addEntityListener(TemporaryUser.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### TemporaryUser : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(TemporaryUser.class, handler);
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
	synchronized public boolean updateDataTarget(Data data) throws UnmatchedDataTypeException,
			DataDaoException {
		logger.debug("[TempUser] : " + data.getId());

		if(data.getClass().equals(TemporaryUserData.class) == false) {
			throw new UnmatchedDataTypeException(TemporaryUserData.class.toString(), data.getClass().toString());
		}

		TemporaryUserData userData = (TemporaryUserData) data;
		try{
			if(!isReachableForwardOrBackward(
					this.getController().getSelfGridId(), userData.getGridId())){
				return false;
			}
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		} catch (DaoException e) {
			throw new DataDaoException(e);
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			logger.info("Delete");
			try {
				TemporaryUser user = userData.getUser();
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

		TemporaryUser user = null;
		try {
			user = userData.getUser();
			logger.debug("New or UpDate");
			removeEntityListener();
			daoContext.beginTransaction();
			daoContext.mergeEntity(user);
			daoContext.commitTransaction();
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
	public void addUser(TemporaryUser user) throws DaoException,
			UserAlreadyExistsException {
		dao.addUser(user);
	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void clearExpiredUsers() throws DaoException {
		dao.clearExpiredUsers();
	}

	@Override
	public void deleteUser(String userGridId, String userId)
			throws DaoException, UserNotFoundException {
		dao.deleteUser(userGridId, userId);
	}


	@Override
	public void deleteUsersOfGrid(String userGridId) throws DaoException {
		dao.deleteUsersOfGrid(userGridId);
	}

	@Override
	public String getParentUserIdIfUserAvailable(String userGridId,
			String userId, String password) throws DaoException {
		return dao.getParentUserIdIfUserAvailable(userGridId, userId, password);
	}

	@Override
	public TemporaryUser getUser(String userGridId, String userId)
			throws DaoException, UserNotFoundException {
		return dao.getUser(userGridId, userId);
	}

	@Override
	public boolean isParent(String userGridId, String ownerUserId, String userId)
			throws DaoException, UserNotFoundException {
		return dao.isParent(userGridId, ownerUserId, userId);
	}

	@Override
	public boolean isUserExists(String userGridId, String userId)
			throws DaoException {
		return dao.isUserExists(userGridId, userId);
	}

	@Override
	public List<TemporaryUser> listAllUsers(String userGridId)
			throws DaoException {
		return dao.listAllUsers(userGridId);
	}

	@Override
	public TemporaryUserSearchResult searchUsers(int startIndex, int maxCount,
			String userGridId, String parentUserId,
			MatchingCondition[] conditions, Order[] orders) throws DaoException {
		return dao.searchUsers(startIndex, maxCount, userGridId, parentUserId, conditions, orders);
	}

	@Override
	public TemporaryUserSearchResult searchUsers(int startIndex, int maxCount,
			String userGridId, MatchingCondition[] conditions, Order[] orders)
			throws DaoException {
		return dao.searchUsers(startIndex, maxCount, userGridId, conditions, orders);
	}

	@Override
	public void setAvailableDateTime(TemporaryUser user,
			Calendar beginAvailableDateTime, Calendar endAvailableDateTime)
			throws DaoException {
		dao.setAvailableDateTime(user, beginAvailableDateTime, endAvailableDateTime);
	}

	private TemporaryUserDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<TemporaryUser> handler = new GenericHandler<TemporaryUser>(){
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
				getController().publish(new TemporaryUserData(
						daoContext.loadEntity(TemporaryUser.class, id)
						));
				logger.debug("published[update: TemporaryUser(id=" + id + ")]");
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
				TemporaryUserPK pk = (TemporaryUserPK)id;
				getController().revoke(TemporaryUserData.getDataID(null, pk));
				logger.debug("published[deletion: TemporaryUser(id=" + id + ")]");
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

	static private Logger logger = Logger.getLogger(P2PGridBasisTemporaryUserDao.class);
}
