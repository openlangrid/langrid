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
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLimitNotFoundException;
import jp.go.nict.langrid.dao.AccessLimitSearchResult;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessLimitPK;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessLimitData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisAccessLimitDao
extends AbstractP2PGridBasisUpdateManagedEntityDao<AccessLimit>
implements AccessLimitDao {
	/**
	 * The constructor.
	 * @param dao
	 */
	public P2PGridBasisAccessLimitDao(AccessLimitDao dao, DaoContext context) {
		super(context);
		setHandler(handler);
		this.dao = dao;
	}

	@Override
	synchronized public boolean updateData(Data data) throws DataDaoException, UnmatchedDataTypeException {
		logger.debug("[AccessLimit] : " + data.getId());
		if(data.getClass().equals(AccessLimitData.class) == false) {
			throw new UnmatchedDataTypeException(AccessLimitData.class.toString(), data.getClass().toString());
		}

		AccessLimit entity = null;
		try{
			entity = ((AccessLimitData)data).getAccessLimit();
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

	private GenericHandler<AccessLimit> handler = new GenericHandler<AccessLimit>(){
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
				getController().publish(new AccessLimitData(
						getDaoContext().loadEntity(AccessLimit.class, id)
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
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};
}
