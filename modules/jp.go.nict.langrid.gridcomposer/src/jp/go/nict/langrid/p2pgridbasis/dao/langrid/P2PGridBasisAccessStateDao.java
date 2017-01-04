/*
 * $Id: P2PGridBasisAccessStateDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import jp.go.nict.langrid.dao.AccessRankingEntrySearchResult;
import jp.go.nict.langrid.dao.AccessStatDao;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.AccessStat;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.PeerSummaryAdv;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessStateData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisAccessStateDao
extends AbstractP2PGridBasisDao<AccessStat>
implements DataDao, AccessStatDao {
	/**
	 * The constructor.
	 * @param dao
	 * @param context
	 */
	public P2PGridBasisAccessStateDao(AccessStatDao dao, DaoContext context) {
		super(context);
		setHandler(handler);
		this.dao = dao;
	}

	@Override
	synchronized public boolean updateData(Data data) throws DataDaoException, UnmatchedDataTypeException {
		logger.debug("[AccessState] : " + data.getId());
		if(data.getClass().equals(AccessStateData.class) == false) {
			throw new UnmatchedDataTypeException(AccessStateData.class.toString(), data.getClass().toString());
		}
		return true;
/*
		AccessStat entity = null;
		try {
			entity = ((AccessStateData)data).getAccessState();
			if(entity.getServiceAndNodeGridId().equals(getSelfGridId())) return false;
			if(!entity.getUserGridId().equals(getSelfGridId())) return false;
			if(!isReachableTo(entity.getServiceAndNodeGridId())) return false;
		} catch(Exception e){
			throw new DataDaoException(e);
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
			try {
				logger.debug("Delete");
				removeEntityListener();
				try{
					return getDaoContext().removeEntity(
							entity.getClass(), EntityUtil.getId(entity));
				} finally{
					setEntityListener();
					getController().baseSummaryAdd(data);
				}
			} catch (Exception e) {
				throw new DataDaoException(e);
			}
		}

		try {
			logger.debug("New or UpDate");
			removeEntityListener();
			try{
				getDaoContext().beginTransaction();
				try{
					getDaoContext().mergeEntity(entity);
				} finally{
					getDaoContext().commitTransaction();
				}
				return true;
			} finally{
				setEntityListener();
				getController().baseSummaryAdd(data);
			}
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
*/	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void deleteAccessStat(String userGridId, String userId,
			String serviceGridId, String serviceId, Calendar baseDateTime,
			Period period) throws DaoException {
		dao.deleteAccessStat(userGridId, userId, serviceGridId, serviceId, baseDateTime, period);
	}

	@Override
	public void deleteAccessStatOfGrid(String gridId) throws DaoException {
		dao.deleteAccessStatOfGrid(gridId);
	}

	@Override
	public void deleteAccessStatOfService(String serviceGridId,
			String serviceId) throws DaoException {
		dao.deleteAccessStatOfService(serviceGridId, serviceId);
	}

	@Override
	public void deleteAccessStatOfUser(String userGridId, String userId)
			throws DaoException {
		dao.deleteAccessStatOfUser(userGridId, userId);
	}

	@Override
	public AccessStat getAccessStat(String userGridId, String userId,
			String serviceAndNodeGridId, String serviceId, String nodeId,
			Calendar baseDateTime, Period period) throws DaoException {
		return dao.getAccessStat(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId, baseDateTime, period);
	}

	@Override
	public List<AccessStat> getAccessStats(String userGridId, String userId,
			String serviceGridId, String serviceId, Calendar baseDateTime)
			throws DaoException {
		return dao.getAccessStats(userGridId, userId, serviceGridId, serviceId, baseDateTime);
	}

	@Override
	public void increment(String userGridId, String userId,
			String serviceAndNodeGridId, String serviceId, String nodeId,
			int requestBytes, int responseBytes, int responseMillis)
			throws DaoException {
		dao.increment(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId
				, requestBytes, responseBytes, responseMillis);
	}

	@Override
	public void increment(String userGridId, String userId,
			String serviceAndNodeGridId, String serviceId, String nodeId,
			int requestBytes, int responseBytes, int responseMillis,
			Calendar accessDateTime) throws DaoException {
		dao.increment(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId
				, requestBytes, responseBytes, responseMillis, accessDateTime);
	}

	@Override
	public List<AccessStat> listAccessStats(String serviceGridId)
			throws DaoException {
		return dao.listAccessStats(serviceGridId);
	}

	@Override
	public List<AccessStat> listAccessStatsNewerThanOrEqualsTo(
			String serviceGridId, Calendar dateTime) throws DaoException {
		return dao.listAccessStatsNewerThanOrEqualsTo(serviceGridId, dateTime);
	}

	@Override
	public AccessRankingEntrySearchResult searchUserAccessRanking(
			int startIndex, int maxCount, String serviceGridId,
			String serviceId, String userGridId, Calendar startDateTime, Calendar endDateTime,
			Period period, Order[] orders) throws DaoException {
		return dao.searchUserAccessRanking(startIndex, maxCount, serviceGridId, serviceId
				, userGridId, startDateTime, endDateTime, period, orders);
	}

	private void updateCountCheck(Serializable id){
		if(updateCount == 0){
			try {
				firstAccessTime = getDaoContext().loadEntity(AccessStat.class, id).getLastAccessDateTime();
				gridID = getDaoContext().loadEntity(AccessStat.class, id).getServiceAndNodeGridId();
			} catch (DaoException e) {
				e.printStackTrace();
			}
			task  = new minuteTimerTask(dao, getDaoContext());
			timer = new Timer(true);
			timer.schedule(task, 1000 * 60);
		}
		updateCount++;

		if(updateCount >= 100){
			logger.debug("### AccessState updateCount = "  + updateCount + "### publish()### ");
			publish();
		}
	}

	private void publish() {
		timer.cancel();
		task = null;
		updateCount = 0;

		try {
			getDaoContext().beginTransaction();
			for (AccessStat state : listAccessStatsNewerThanOrEqualsTo(gridID, firstAccessTime)) {
				AccessStateData data = new AccessStateData(state);
				getController().stateDataPublish(data);
			}
			getController().summaryPublish(PeerSummaryAdv._stateSummary);
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (DataConvertException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			e.printStackTrace();
		}finally{
			try {
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				e.printStackTrace();
			}
		}
	}

	private AccessStatDao dao;
	private static Timer timer;
	private static TimerTask task;
	private static Calendar firstAccessTime;
	private static int updateCount = 0;
	private static String gridID;
	private GenericHandler<AccessStat> handler = new GenericHandler<AccessStat>(){
		protected boolean onNotificationStart() {
			try{
				getDaoContext().beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
				return false;
			}
		}
// 共有しない
		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			logger.info("updateCountCheck[AccessState(id=" + id + ")]");
//			updateCountCheck(id);
		}

		protected void doRemove(Serializable id){
/*			try{
				AccessStatPK pk = (AccessStatPK)id;
				getController().revoke(AccessStateData.getDataID(null, pk));
				logger.info("revoked[AccessState(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to revoke instance.", e);
			} catch(DataNotFoundException e){
				logger.error("failed to find data.", e);
			}
*/		}

		protected void onNotificationEnd(){
			try{
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisAccessStateDao.class);

	@Override
	public AccessRankingEntrySearchResult searchServiceAccessRanking(
			int startIndex, int maxCount, String serviceGridId, String userId,
			Calendar startDateTime, Calendar endDateTime, Period period,
			Order[] orders) throws DaoException {
		return dao.searchServiceAccessRanking(startIndex, maxCount, serviceGridId, userId, startDateTime, endDateTime, period, orders);
	}
}

/**
 * 
 * 
 */
class minuteTimerTask extends TimerTask{
	/**
	 * The constructor.
	 * @param dao
	 * @param context
	 */
	public minuteTimerTask(AccessStatDao dao, DaoContext context) {
		logger.debug("### AccessState 1min Timer ### Start");
		this.dao = dao;
		this.daoContext = context;
	}

	public void run() {
		logger.debug("### AccessState 1min Timer ### Arrival");
//		new P2PGridBasisAccessStateDao(dao, daoContext).publish();
	}
	private AccessStatDao dao;
	private DaoContext daoContext;

	static private Logger logger = Logger.getLogger(minuteTimerTask.class);
}
