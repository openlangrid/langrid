/*
 * $Id: P2PGridBasisAccessRightDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.AccessRightNotFoundException;
import jp.go.nict.langrid.dao.AccessRightSearchResult;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.AccessRightPK;
import jp.go.nict.langrid.dao.entity.ServicePK;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessRightData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisAccessRightDao
extends AbstractP2PGridBasisUpdateManagedEntityDao<AccessRight>
implements AccessRightDao {
	/**
	 * The constructor.
	 * @param dao
	 */
	public P2PGridBasisAccessRightDao(AccessRightDao dao, DaoContext context) {
		super(context);
		setHandler(handler);
		this.dao = dao;
	}

	@Override
	synchronized public boolean updateData(Data data) throws DataDaoException, UnmatchedDataTypeException {
		logger.debug("[AccessRight] : " + data.getId());
		if(data.getClass().equals(AccessRightData.class) == false) {
			throw new UnmatchedDataTypeException(AccessRightData.class.toString(), data.getClass().toString());
		}

		AccessRight entity = null;
		try {
			entity = ((AccessRightData)data).getAccessRight();
			if(entity.getServiceGridId().equals(getSelfGridId())) return false;
			if(!entity.getUserGridId().equals(getSelfGridId())) return false;
			if(!isReachableTo(entity.getServiceGridId())) return false;
		} catch(Exception e){
			throw new DataDaoException(e);
		}
		return handleData(data, entity);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.dao.AccessRightDao#clear()
	 */
	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void clearExceptDefaults() throws DaoException {
		dao.clearExceptDefaults();
	}

	@Override
	public void deleteAccessRight(String userGridId, String userId,
			String serviceGridId, String serviceId)
			throws AccessRightNotFoundException, DaoException {
		dao.deleteAccessRight(userGridId, userId, serviceGridId, serviceId);
	}

	@Override
	public void deleteAccessRightsOfGrid(String gridId) throws DaoException {
		dao.deleteAccessRightsOfGrid(gridId);
	}

	@Override
	public void deleteAccessRightsOfService(String serviceGridId,
			String serviceId) throws DaoException {
		dao.deleteAccessRightsOfService(serviceGridId, serviceId);
	}

	@Override
	public void deleteAccessRightsOfUser(String userGridId, String userId)
			throws DaoException {
		dao.deleteAccessRightsOfUser(userGridId, userId);
	}

	@Override
	public AccessRight getAccessRight(String userGridId, String userId,
			String serviceGridId, String serviceId) throws DaoException {
		return dao.getAccessRight(userGridId, userId, serviceGridId, serviceId);
	}

	@Override
	public List<AccessRight> listAccessRights(String serviceGridId)
			throws DaoException {
		return dao.listAccessRights(serviceGridId);
	}

	@Override
	public AccessRightSearchResult searchAccessRights(int startIndex,
			int maxCount, String userGridId, String userId,
			String serviceGridId, String[] serviceIds, Order[] orders)
			throws DaoException {
		return dao.searchAccessRights(startIndex, maxCount, userGridId, userId, serviceGridId, serviceIds, orders);
	}

	@Override
	public AccessRightSearchResult searchAccessRightsAccordingToDefaultAndOwner(
			int startIndex, int maxCount, String userGridId, String userId,
			String serviceGridId, String[] serviceIds, String ownerUserId,
			Order[] orders) throws DaoException {
		return dao.searchAccessRightsAccordingToDefaultAndOwner(startIndex, maxCount, userGridId, userId, serviceGridId, serviceIds, ownerUserId, orders);
	}

	@Override
	public AccessRight setAccessRight(String userGridId, String userId,
			String serviceGridId, String serviceId, boolean permitted)
			throws DaoException {
		return dao.setAccessRight(userGridId, userId, serviceGridId, serviceId, permitted);
	}

	@Override
	public Iterable<ServicePK> listAccessibleServices(String userGridId,
			String userid) throws DaoException {
		return dao.listAccessibleServices(userGridId, userid);
	}

	@Override
	public AccessRight getGridDefaultAccessRight(String userGridId,
			String serviceGridId, String serviceId) throws DaoException {
		return dao.getGridDefaultAccessRight(userGridId, serviceGridId, serviceId);
	}

	@Override
	public AccessRight setGridDefaultAccessRight(String userGridId,
			String serviceGridId, String serviceId, boolean permitted)
			throws DaoException {
		return setGridDefaultAccessRight(userGridId, serviceGridId, serviceId, permitted);
	}

	@Override
	public void deleteGridDefaultAccessRight(String userGridId,
			String serviceGridId, String serviceId)
			throws AccessRightNotFoundException, DaoException {
		dao.deleteGridDefaultAccessRight(userGridId, serviceGridId, serviceId);
	}

	@Override
	public void adjustUserRights(String userGridId, String serviceGridId,
			String serviceId, String ownerUserId, boolean permitted)
			throws DaoException {
		dao.adjustUserRights(userGridId, serviceGridId, serviceId, ownerUserId, permitted);
	}

	@Override
	public AccessRight getServiceDefaultAccessRight(String serviceGridId,
			String serviceId) throws DaoException {
		return dao.getServiceDefaultAccessRight(serviceGridId, serviceId);
	}

	@Override
	public AccessRight setServiceDefaultAccessRight(String serviceGridId,
			String serviceId, boolean permitted) throws DaoException {
		return dao.setServiceDefaultAccessRight(serviceGridId, serviceId, permitted);
	}

	@Override
	public void adjustGridDefaultRights(String serviceGridId, String serviceId,
			boolean permitted) throws DaoException {
		dao.adjustGridDefaultRights(serviceGridId, serviceId, permitted);
	}

	private AccessRightDao dao;
	private GenericHandler<AccessRight> handler = new GenericHandler<AccessRight>(){
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
				getController().publish(new AccessRightData(
						getDaoContext().loadEntity(AccessRight.class, id)
						));
				logger.info("published[AccessRight(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to publish instance.", e);
			} catch(DaoException e){
				logger.error("failed to access dao.", e);
			} catch(DataConvertException e){
				logger.error("failed to convert data.", e);
			}
		}

		public void doRemove(Serializable id){
			try {
				AccessRightPK pk = (AccessRightPK)id;
				getController().revoke(AccessRightData.getDataID(null, pk));
				logger.info("revoked[AccessRight(id=" + id + ")]");
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

	private static Logger logger = Logger.getLogger(P2PGridBasisAccessRightDao.class);
}
