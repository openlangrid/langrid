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
extends AbstractP2PGridBasisDao<TemporaryUser>
implements DataDao, TemporaryUserDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisTemporaryUserDao(TemporaryUserDao dao, DaoContext context) {
		super(context);
		this.dao = dao;
		setHandler(handler);
	}

	@Override
	synchronized public boolean updateData(Data data) throws UnmatchedDataTypeException,
			DataDaoException {
		logger.debug("[TempUser] : " + data.getId());
		if(data.getClass().equals(TemporaryUserData.class) == false) {
			throw new UnmatchedDataTypeException(TemporaryUserData.class.toString(), data.getClass().toString());
		}

		TemporaryUser entity = null;
		try {
			entity = ((TemporaryUserData)data).getUser();
			if(entity.getGridId().equals(getSelfGridId())) return false;
			if(!isReachableToOrFrom(entity.getGridId())) return false;
		} catch(Exception e){
			throw new DataDaoException(e);
		}
		return handleData(data, entity);
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
	private GenericHandler<TemporaryUser> handler = new GenericHandler<TemporaryUser>(){
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
				getController().publish(new TemporaryUserData(
						getDaoContext().loadEntity(TemporaryUser.class, id)
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
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisTemporaryUserDao.class);
}
