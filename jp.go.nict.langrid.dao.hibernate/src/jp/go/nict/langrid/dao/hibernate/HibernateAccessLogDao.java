/*
 * $Id:HibernateAccessLogDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao.hibernate;

import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.AccessLogDao;
import jp.go.nict.langrid.dao.AccessLogSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.dao.entity.News;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateAccessLogDao
extends HibernateCRUDDao<AccessLog> implements AccessLogDao
{
	/**
	 * 
	 * 
	 */
	public HibernateAccessLogDao(HibernateDaoContext context){
		super(context, AccessLog.class);
	}

	public void clear() throws DaoException{
		getContext().beginTransaction();
		try{
			createDeleteQuery(AccessLog.class).executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<AccessLog> listAccessLogsNewerThanOrEqualsTo(
			String serviceGridId, Calendar dateTime)
	throws DaoException {
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		dateTime = CalendarUtil.toDefaultTimeZone(dateTime);

		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<AccessLog> result = session.createCriteria(AccessLog.class)
					.add(Property.forName("serviceAndNodeGridId").eq(serviceGridId))
					.add(Property.forName("dateTime").ge(dateTime))
					.addOrder(org.hibernate.criterion.Order.asc("dateTime"))
					.setMaxResults(2000)
					.list();
			getContext().commitTransaction();
			return result;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public AccessLogSearchResult searchLimitOverAccessLog(
		int startIndex, int maxCount
		, String userGridId, String userId
		, String serviceGridId, String[] serviceIds
		, Calendar startDateTime, Calendar endDateTime
		, MatchingCondition[] conditions, Order[] orders, int limitCount)
	throws DaoException{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		startDateTime = CalendarUtil.toDefaultTimeZone(startDateTime);
		endDateTime = CalendarUtil.toDefaultTimeZone(endDateTime);
		
		if(orders.length == 0){
			orders = new Order[]{
				new Order("dateTime", OrderDirection.DESCENDANT)
			};
		}
		
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(AccessLog.class);
			addSearchCriterion(c, userGridId, userId, serviceGridId, serviceIds
				, startDateTime, endDateTime, conditions);
			c.add(Property.forName("responseBytes").ge(limitCount));
			List<AccessLog> elements = (List<AccessLog>)CriteriaUtil.getList(
				c, startIndex, maxCount + 1, orders);
			boolean totalCountFixed = true;
			int totalCount = 0;
			AccessLog[] ret = null;
			int sz = elements.size();
			if(sz == 0){
				totalCountFixed = false;
				ret = new AccessLog[]{};
			} else if(sz <= maxCount){
				totalCount = sz + startIndex;
				ret = elements.toArray(new AccessLog[]{});
			} else{
				totalCount = sz + startIndex;
				totalCountFixed = false;
				ret = elements.subList(0, sz - 1).toArray(new AccessLog[]{});
			}
			AccessLogSearchResult r = new AccessLogSearchResult(
				ret, totalCount, totalCountFixed
			);
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}
	@SuppressWarnings("unchecked")
	public AccessLogSearchResult searchAccessLog(
			int startIndex, int maxCount
			, String userGridId, String userId
			, String serviceGridId, String[] serviceIds
			, Calendar startDateTime, Calendar endDateTime
			, MatchingCondition[] conditions, Order[] orders)
	throws DaoException{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		startDateTime = CalendarUtil.toDefaultTimeZone(startDateTime);
		endDateTime = CalendarUtil.toDefaultTimeZone(endDateTime);

		if(orders.length == 0){
			orders = new Order[]{
					new Order("dateTime", OrderDirection.DESCENDANT)
					};
		}

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(AccessLog.class);
			addSearchCriterion(c, userGridId, userId, serviceGridId, serviceIds
					, startDateTime, endDateTime, conditions);
			List<AccessLog> elements = (List<AccessLog>)CriteriaUtil.getList(
					c, startIndex, maxCount + 1, orders);
			boolean totalCountFixed = true;
			int totalCount = 0;
			AccessLog[] ret = null;
			int sz = elements.size();
			if(sz == 0){
				totalCountFixed = false;
				ret = new AccessLog[]{};
			} else if(sz <= maxCount){
				totalCount = sz + startIndex;
				ret = elements.toArray(new AccessLog[]{});
			} else{
				totalCount = sz + startIndex;
				totalCountFixed = false;
				ret = elements.subList(0, sz - 1).toArray(new AccessLog[]{});
			}
			AccessLogSearchResult r = new AccessLogSearchResult(
					ret, totalCount, totalCountFixed
					);
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isLogExist(int newsId) throws DaoException {
		try{
			return getSession().get(News.class, newsId) != null;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			throw new DaoException(e);
		}
	}

	public void addAccessLog(AccessLog log)
		throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
			log.setDateTime(CalendarUtil.toDefaultTimeZone(log.getDateTime()));
			long seq = getContext().generateSequence();
			log.setId((int)seq);
			log.setNodeLocalId((int)seq);
			session.save(log);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteAccessLogBefore(Calendar dateTime)
		throws DaoException
	{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		dateTime = CalendarUtil.toDefaultTimeZone(dateTime);

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(AccessLog.class);
			c.add(Property.forName("dateTime").lt(dateTime));
			for(Object o : c.list()){
				session.delete(o);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteAccessLogsOfGrid(final String gridId)
	throws DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session
						, "where serviceAndNodeGridId=:serviceGridId")
					.setString("serviceGridId", gridId)
					.executeUpdate();
			}
		});
	}

	public void deleteAccessLogsOfService(String serviceGridId, String serviceId)
	throws DaoException {
		getContext().beginTransaction();
		try{
			createDeleteQuery(AccessLog.class
					, "where serviceAndNodeGridId=:serviceGridId and serviceId=:serviceId")
				.setString("serviceGridId", serviceGridId)
				.setString("serviceId", serviceId)
				.executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteAccessLogOfUser(String userGridId, String userId)
	throws DaoException{
		getContext().beginTransaction();
		try{
			createDeleteQuery(AccessLog.class
					, "where userGridId=:userGridId and userId=:userId")
				.setString("userGridId", userGridId)
				.setString("userId", userId)
				.executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteAccessLogOfNode(String nodeGridId, String nodeId)
	throws DaoException {
		getContext().beginTransaction();
		try{
			createDeleteQuery(AccessLog.class
					, "where serviceAndNodeGridId=:nodeGridId and nodeId=:nodeId")
				.setString("nodeGridId", nodeGridId)
				.setString("nodeId", nodeId)
				.executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isLogExistByNodeIds(final String gridId, final String nodeId, final int nodeLocalId)
	throws DaoException {
		return transact(new DaoBlockR<Boolean>() {
			@Override
			public Boolean execute(Session session) throws DaoException {
				return ((Number)session.createCriteria(AccessLog.class)
						.add(Property.forName("serviceAndNodeGridId").eq(gridId))
						.add(Property.forName("nodeId").eq(nodeId))
						.add(Property.forName("nodeLocalId").eq(nodeLocalId))
						.setProjection(Projections.rowCount())
						.uniqueResult()).intValue() > 0;
			}
		});
	}

	@Override
	public void updateAccessLogByNodeIds(final AccessLog log) throws DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				AccessLog orig = (AccessLog)session.createCriteria(AccessLog.class)
					.add(Property.forName("serviceAndNodeGridId").eq(log.getServiceAndNodeGridId()))
					.add(Property.forName("nodeId").eq(log.getNodeId()))
					.add(Property.forName("nodeLocalId").eq(log.getNodeLocalId()))
					.uniqueResult();
				orig.setAddress(log.getAddress());
				orig.setAgent(log.getAgent());
				orig.setCallNest(log.getCallNest());
				orig.setCallTree(log.getCallTree());
				orig.setDateTime(log.getDateTime());
				orig.setFaultCode(log.getFaultCode());
				orig.setFaultString(log.getFaultString());
				orig.setHost(log.getHost());
				orig.setProtocolId(log.getProtocolId());
				orig.setReferer(log.getReferer());
				orig.setRequestBytes(log.getRequestBytes());
				orig.setRequestUri(log.getRequestUri());
				orig.setResponseBytes(log.getResponseBytes());
				orig.setResponseCode(log.getResponseCode());
				orig.setResponseMillis(log.getResponseMillis());
				orig.setServiceAndNodeGridId(log.getServiceAndNodeGridId());
				orig.setServiceId(log.getServiceId());
				orig.setUserGridId(log.getUserGridId());
				orig.setUserId(log.getUserId());
			}
		});
	}

	private static void addSearchCriterion(
			Criteria c, String userGridId, String userId
			, String serviceGridId, String[] serviceIds
			, Calendar startDateTime, Calendar endDateTime
			, MatchingCondition[] conditions)
	{
		c.add(Property.forName("dateTime").ge(startDateTime));
		c.add(Property.forName("dateTime").le(endDateTime));
		if(userGridId.length() > 0){
			c.add(Property.forName("userGridId").eq(userGridId));
			if(userId.length() > 0){
				c.add(Property.forName("userId").eq(userId));
			}
		}
		if(serviceGridId.length() > 0){
			c.add(Property.forName("serviceAndNodeGridId").eq(serviceGridId));
			if(serviceIds.length > 0){
				Disjunction dj = Restrictions.disjunction();
				for(String id : serviceIds){
					dj.add(Property.forName("serviceId").eq(id));
				}
				c.add(dj);
			}
		}
		CriteriaUtil.addMatchingConditions(c, conditions);
	}
}
