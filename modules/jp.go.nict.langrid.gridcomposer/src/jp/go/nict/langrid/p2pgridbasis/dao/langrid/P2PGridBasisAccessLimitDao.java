/*
 * $Id: P2PGridBasisAccessLimitDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLimitNotFoundException;
import jp.go.nict.langrid.dao.AccessLimitSearchResult;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessLimitPK;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessLimitData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisAccessLimitDao implements DataDao, AccessLimitDao {
	/**
	 * The constructor.
	 * @param dao
	 */
	public P2PGridBasisAccessLimitDao(AccessLimitDao dao, DaoContext context) {
		this.dao = dao;
		this.daoContext = context;
	}

	private P2PGridController getController() throws ControllerException{
		if (controller == null) {
			controller = JXTAController.getInstance();
		}

		return controller;
	}

	public void setEntityListener() {
		logger.debug("### AccessLimit : setEntityListener ###");
		daoContext.addEntityListener(AccessLimit.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### AccessLimit : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(AccessLimit.class, handler);
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
	synchronized public boolean updateDataTarget(Data data) throws DataDaoException, UnmatchedDataTypeException {
		logger.debug("[AccessLimit] : " + data.getId());
		if(data.getClass().equals(AccessLimitData.class) == false) {
			throw new UnmatchedDataTypeException(AccessLimitData.class.toString(), data.getClass().toString());
		}

		boolean updated = false;
		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
			try {
				logger.debug("Delete");
				AccessLimitData accessLimitData = (AccessLimitData)data;
				AccessLimit limit = accessLimitData.getAccessLimit();
				removeEntityListener();
				dao.deleteAccessLimit(limit.getUserGridId()
									, limit.getUserId()
									, limit.getServiceGridId()
									, limit.getServiceId()
									, limit.getPeriod()
									, limit.getLimitType());
				updated = true;
				setEntityListener();
				getController().baseSummaryAdd(data);
			} catch (DataConvertException e) {
				throw new DataDaoException(e);
			} catch (ServiceNotFoundException e) {
				// 
				// 
				try {
					getController().baseSummaryAdd(data);
				} catch (ControllerException e1) {
					e1.printStackTrace();
				}
			} catch (DaoException e) {
				throw new DataDaoException(e);
			} catch (ParseException e) {
				throw new DataDaoException(e);
			} catch (ControllerException e) {
				throw new DataDaoException(e);
			}
			return updated;
		}

		AccessLimit limit= null;
		try {
			AccessLimitData accessLimitData = (AccessLimitData)data;
			limit = accessLimitData.getAccessLimit();
			logger.debug("New or UpDate");
			removeEntityListener();
			daoContext.beginTransaction();
			daoContext.mergeEntity(limit);
			daoContext.commitTransaction();
			setEntityListener();
			getController().baseSummaryAdd(data);
			updated = true;
		} catch (ParseException e) {
			throw new DataDaoException(e);
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
		return updated;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.dao.AccessLimitDao#clear()
	 */
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void clearExceptDefaults() throws DaoException {
		dao.clearExceptDefaults();
	}

	@Override
	public void deleteAccessLimit(String userGridId, String userId,
			String serviceGridId, String serviceId, Period period,
			LimitType limitType) throws AccessLimitNotFoundException,
			DaoException {
		dao.deleteAccessLimit(userGridId, userId, serviceGridId, serviceId, period, limitType);
	}

	@Override
	public void deleteAccessLimits(String userGridId, String userId,
			String serviceGridId, String serviceId) throws DaoException {
		dao.deleteAccessLimits(userGridId, userId, serviceGridId, serviceId);
	}

	@Override
	public void deleteAccessLimitsOfGrid(String gridId) throws DaoException {
		dao.deleteAccessLimitsOfGrid(gridId);
	}

	@Override
	public void deleteAccessLimitsOfService(String serviceGridId,
			String serviceId) throws DaoException {
		dao.deleteAccessLimitsOfService(serviceGridId, serviceId);

	}

	@Override
	public void deleteAccessLimitsOfUser(String userGridId, String userId)
			throws DaoException {
		dao.deleteAccessLimitsOfUser(userGridId, userId);
	}

	@Override
	public AccessLimit getAccessLimit(String userGridId, String userId,
			String serviceGridId, String serviceId, Period period,
			LimitType limitType) throws AccessLimitNotFoundException,
			DaoException {
		return dao.getAccessLimit(userGridId, userId, serviceGridId, serviceId, period, limitType);
	}

	@Override
	public List<AccessLimit> getAccessLimits(String userGridId, String userId,
			String serviceGridId, String serviceId) throws DaoException {
		return dao.getAccessLimits(userGridId, userId, serviceGridId, serviceId);
	}

	@Override
	public AccessLimit getServiceDefaultAccessLimit(String userGridId,
			String serviceGridId, String serviceId, Period period,
			LimitType limitType) throws DaoException {
		return dao.getServiceDefaultAccessLimit(userGridId, serviceGridId, serviceId, period, limitType);
	}

	@Override
	public List<AccessLimit> getServiceDefaultAccessLimits(String userGridId,
			String serviceGridId, String serviceId) throws DaoException {
		return dao.getServiceDefaultAccessLimits(userGridId, serviceGridId, serviceId);
	}

	@Override
	public List<AccessLimit> listAccessLimits(String serviceGridId)
			throws DaoException {
		return dao.listAccessLimits(serviceGridId);
	}

	@Override
	public AccessLimitSearchResult searchAccessLimits(int startIndex,
			int maxCount, String userGridId, String userId,
			String serviceGridId, String[] serviceIds, Order[] orders)
			throws DaoException {
		return dao.searchAccessLimits(startIndex, maxCount, userGridId, userId, serviceGridId, serviceIds, orders);
	}

	@Override
	public AccessLimit setAccessLimit(String userGridId, String userId,
			String serviceGridId, String serviceId, Period period,
			LimitType limitType, int limitCount) throws DaoException {
		return dao.setAccessLimit(userGridId, userId, serviceGridId, serviceId, period, limitType, limitCount);
	}

	@Override
	public void setServiceDefaultAccessLimit(String userGridId,
			String serviceGridId, String serviceId, Period period,
			LimitType limitType, int limit) throws DaoException {
		dao.setServiceDefaultAccessLimit(userGridId, serviceGridId, serviceId, period, limitType, limit);
	}

	static private Logger logger = Logger.getLogger(P2PGridBasisAccessLimitDao.class);

	private AccessLimitDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<AccessLimit> handler = new GenericHandler<AccessLimit>(){
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
				getController().publish(new AccessLimitData(
						daoContext.loadEntity(AccessLimit.class, id)
						));
				logger.info("published[AccessLimit(id=" + id + ")]");
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
				AccessLimitPK pk = (AccessLimitPK)id;
				getController().revoke(AccessLimitData.getDataID(null, pk));
				logger.info("revoked[AccessLimit(id=" + pk + ")]");
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
