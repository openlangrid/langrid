/*
 * $Id: P2PGridBasisOverUseLimitDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OverUseLimitDao;
import jp.go.nict.langrid.dao.OverUseLimitNotFoundException;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.OverUseLimit;
import jp.go.nict.langrid.dao.entity.OverUseLimitPK;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.OverUseLimitData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisOverUseLimitDao
extends AbstractP2PGridBasisDao<OverUseLimit>
implements DataDao, OverUseLimitDao {
	/**
	 * The constructor.
	 * @param dao
	 */
	public P2PGridBasisOverUseLimitDao(OverUseLimitDao dao, DaoContext context) {
		super(context);
		this.dao = dao;
		setHandler(handler);
	}

	@Override
	synchronized public boolean updateData(Data data) throws DataDaoException, UnmatchedDataTypeException {
		logger.debug("[OverUseLimit] : " + data.getId());
		if(data.getClass().equals(OverUseLimitData.class) == false) {
			throw new UnmatchedDataTypeException(OverUseLimitData.class.toString(), data.getClass().toString());
		}
		return false;
/*		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			try {
				logger.debug("Delete");
				OverUseLimitData overUseLimitData = (OverUseLimitData)data;
				OverUseLimit limit = overUseLimitData.getOverUseLimit();
				removeEntityListener();
				dao.deleteOverUseLimit(limit.getGridId(), limit.getPeriod(), limit.getLimitType());
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

		OverUseLimit limit= null;
		try {
			OverUseLimitData overUseLimitData = (OverUseLimitData)data;
			limit = overUseLimitData.getOverUseLimit();
			logger.debug("Newor UpDate");
			removeEntityListener();
			getDaoContext().beginTransaction();
			getDaoContext().mergeEntity(limit);
			getDaoContext().commitTransaction();
			setEntityListener();
			getController().baseSummaryAdd(data);
			return true;
		} catch (ParseException e) {
			throw new DataDaoException(e);
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}*/
	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void deleteOverUseLimit(String gridId, Period period,
			LimitType limitType) throws OverUseLimitNotFoundException,
			DaoException {
		dao.deleteOverUseLimit(gridId, period, limitType);
	}


	@Override
	public void deleteOverUseLimitsOfGrid(String gridId) throws DaoException {
		dao.deleteOverUseLimitsOfGrid(gridId);
	}

	@Override
	public OverUseLimit getOverUseLimit(String gridId, Period period,
			LimitType limitType) throws OverUseLimitNotFoundException,
			DaoException {
		return dao.getOverUseLimit(gridId, period, limitType);
	}

	@Override
	public List<OverUseLimit> listOverUseLimits(String gridId, Order[] orders)
			throws DaoException {
		return dao.listOverUseLimits(gridId, orders);
	}

	@Override
	public void setOverUseLimit(String gridId, Period period,
			LimitType limitType, int limitValue) throws DaoException {
		dao.setOverUseLimit(gridId, period, limitType, limitValue);
	}

	private OverUseLimitDao dao;
	private GenericHandler<OverUseLimit> handler = new GenericHandler<OverUseLimit>(){
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
				getController().publish(new OverUseLimitData(
						getDaoContext().loadEntity(OverUseLimit.class, id)
						));
				logger.info("published[OverUseLimit(id=" + id + ")]");
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
				OverUseLimitPK pk = (OverUseLimitPK)id;
				getController().revoke(OverUseLimitData.getDataID(null, pk));
				logger.info("revoked[OverUseLimit(id=" + id + ")]");
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

	static private Logger logger = Logger.getLogger(P2PGridBasisOverUseLimitDao.class);
}
