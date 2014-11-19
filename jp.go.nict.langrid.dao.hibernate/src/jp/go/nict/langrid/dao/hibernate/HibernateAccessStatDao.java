/*
 * $Id:HibernateAccessRightDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import static jp.go.nict.langrid.dao.entity.Period.DAY;
import static jp.go.nict.langrid.dao.entity.Period.MONTH;
import static jp.go.nict.langrid.dao.entity.Period.YEAR;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.AccessRankingEntry;
import jp.go.nict.langrid.dao.AccessRankingEntrySearchResult;
import jp.go.nict.langrid.dao.AccessStatDao;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.AccessStat;
import jp.go.nict.langrid.dao.entity.AccessStatPK;
import jp.go.nict.langrid.dao.entity.Period;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateAccessStatDao
extends HibernateCRUDDao<AccessStat>
implements AccessStatDao
{
	/**
	 * 
	 * 
	 */
	public HibernateAccessStatDao(HibernateDaoContext context){
		super(context, AccessStat.class);
	}

	public void clear() throws DaoException{
		super.clear();
	}

	public void increment(String userGridId, String userId
			, String serviceAndNodeGridId, String serviceId, String nodeId
			, int requestBytes, int responseBytes, int responseMillis)
	throws DaoException {
		increment(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId
				, requestBytes, responseBytes, responseMillis
				, Calendar.getInstance());
	}

	public void increment(String userGridId, String userId
			, String serviceAndNodeGridId, String serviceId, String nodeId
			, int requestBytes, int responseBytes, int responseMillis
			, Calendar accessDateTime)
	throws DaoException
	{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		accessDateTime = CalendarUtil.toDefaultTimeZone(accessDateTime);

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Calendar dateTimeDay = toBeginning(accessDateTime, DAY);
			Calendar dateTimeMonth = toBeginning(accessDateTime, MONTH);
			Calendar dateTimeYear = toBeginning(accessDateTime, YEAR);
/*
			// 明示的にテーブルロックをかけるバージョン
			session.createSQLQuery("lock table accessstate in EXCLUSIVE MODE").executeUpdate();
			Query q = session.getNamedQuery("AccessState.incrementQuery");
			q.setInteger("size", transferredSize);
			q.setString("userId", userId);
			q.setString("serviceId", serviceId);
			q.setParameter("periodDay", Period.DAY);
			q.setCalendar("dateTimeDay", dateTimeDay);
			q.setParameter("periodMonth", Period.MONTH);
			q.setCalendar("dateTimeMonth", dateTimeMonth);
			q.setParameter("periodYear", Period.YEAR);
			q.setCalendar("dateTimeYear", dateTimeYear);
			int affectedRows = q.executeUpdate();
			do{
				if(affectedRows >= 3) break;
				session.save(new AccessState(
						userId, serviceId, dateTimeDay
						, Period.DAY, 1, transferredSize
						));
				if(affectedRows == 2) break;
				session.save(new AccessState(
						userId, serviceId, dateTimeMonth
						, Period.MONTH, 1, transferredSize
						));
				if(affectedRows == 1) break;
				session.save(new AccessState(
						userId, serviceId, dateTimeYear
						, Period.YEAR, 1, transferredSize
						));
			} while(false);
/*/
			// ストアドプロシージャを利用するバージョン。(内部でテーブルロック)
			session.createSQLQuery(
					"select \"AccessStat.increment\"(" +
					":userGridId, :userId, :serviceAndNodeGridId, :serviceId, :nodeId" +
					", :lastAccessDateTime" +
					", :baseDateTimeDay, :periodDay" +
					", :baseDateTimeMonth, :periodMonth" +
					", :baseDateTimeYear, :periodYear" +
					", :requestBytes, :responseBytes, :responseMillis)"
					)
					.setString("userGridId", userGridId)
					.setString("userId", userId)
					.setString("serviceAndNodeGridId", serviceAndNodeGridId)
					.setString("serviceId", serviceId)
					.setString("nodeId", nodeId)
					.setCalendar("lastAccessDateTime", accessDateTime)
					.setCalendar("baseDateTimeDay", dateTimeDay)
					.setInteger("periodDay", DAY.ordinal())
					.setCalendar("baseDateTimeMonth", dateTimeMonth)
					.setInteger("periodMonth", MONTH.ordinal())
					.setCalendar("baseDateTimeYear", dateTimeYear)
					.setInteger("periodYear", YEAR.ordinal())
					.setInteger("requestBytes", requestBytes)
					.setInteger("responseBytes", responseBytes)
					.setInteger("responseMillis", responseMillis)
					.list();
//*/
			// 
			// 
			getContext().fireUpdate(
					new AccessStatPK(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId, dateTimeDay, DAY)
					, new AccessStat(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId
							, dateTimeDay, DAY, 0, 0, 0, 0)
					, new String[]{"accessCount", "requestBytes", "responseBytes", "responseMillis"});
			getContext().fireUpdate(
					new AccessStatPK(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId, dateTimeMonth, MONTH)
					, new AccessStat(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId
							, dateTimeMonth, MONTH, 0, 0, 0, 0)
					, new String[]{"accessCount", "requestBytes", "responseBytes", "responseMillis"});
			getContext().fireUpdate(
					new AccessStatPK(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId, dateTimeYear, YEAR)
					, new AccessStat(userGridId, userId, serviceAndNodeGridId, serviceId, nodeId
							, dateTimeYear, YEAR, 0, 0, 0, 0)
					, new String[]{"accessCount", "requestBytes", "responseBytes", "responseMillis"});
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<AccessStat> listAccessStats(String serviceGridId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<AccessStat> r = session.createCriteria(AccessStat.class)
					.add(Property.forName("serviceAndNodeGridId").eq(serviceGridId))
					.list();
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<AccessStat> listAccessStatsNewerThanOrEqualsTo(
			String serviceGridId, Calendar dateTime)
	throws DaoException {
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		dateTime = CalendarUtil.toDefaultTimeZone(dateTime);

		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<AccessStat> result = (List<AccessStat>)session.createCriteria(
					AccessStat.class)
					.add(Property.forName("lastAccessDateTime").ge(dateTime))
					.add(Property.forName("serviceAndNodeGridId").eq(serviceGridId))
					.addOrder(org.hibernate.criterion.Order.asc("lastAccessDateTime"))
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

	public AccessStat getAccessStat(String userGridId, String userId
			, String serviceAndNodeGridId, String serviceId, String nodeId
			, Calendar baseDateTime, Period period)
	throws DaoException {
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		baseDateTime = CalendarUtil.toDefaultTimeZone(baseDateTime);
		baseDateTime = toBeginning(baseDateTime, period);

		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessStat ret = (AccessStat)session.get(
					AccessStat.class
					, new AccessStatPK(userGridId, userId
							, serviceAndNodeGridId, serviceId, nodeId, baseDateTime, period)
					);
			getContext().commitTransaction();
			if(ret != null){
				return ret;
			} else{
				return new AccessStat(
						userGridId, userId, serviceAndNodeGridId, serviceId, nodeId
						, baseDateTime, period, 0, 0, 0, 0);
			}
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<AccessStat> getAccessStats(
			String userGridId, String userId, String serviceGridId, String serviceId,
			Calendar baseDateTime)
	throws DaoException {
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		baseDateTime = CalendarUtil.toDefaultTimeZone(baseDateTime);

		Calendar baseDateTimeDay = toBeginning(baseDateTime, Period.DAY);
		Calendar baseDateTimeMonth = toBeginning(baseDateTime, Period.MONTH);
		Calendar baseDateTimeYear = toBeginning(baseDateTime, Period.YEAR);

		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<AccessStat> result = (List<AccessStat>)session.createQuery(
					SELECTACCESSSTATES)
					.setString("userGridId", userGridId)
					.setString("userId", userId)
					.setString("serviceAndNodeGridId", serviceGridId)
					.setString("serviceId", serviceId)
					.setCalendar("baseDateTimeDay", baseDateTimeDay)
					.setParameter("pd", Period.DAY)
					.setCalendar("baseDateTimeMonth", baseDateTimeMonth)
					.setParameter("pm", Period.MONTH)
					.setCalendar("baseDateTimeYear", baseDateTimeYear)
					.setParameter("py", Period.YEAR)
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

	public AccessRankingEntrySearchResult searchUserAccessRanking(int startIndex,
			int maxCount, String serviceGridId, String serviceId, String userGridId
			, Calendar startDateTime, Calendar endDateTime
			, Period period, Order[] orders)
	throws DaoException
	{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		startDateTime = CalendarUtil.toDefaultTimeZone(startDateTime);
		startDateTime = toBeginning(startDateTime, period);
		endDateTime = CalendarUtil.toDefaultTimeZone(endDateTime);
		endDateTime = toBeginning(endDateTime, period);
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<?> result = createUserRankingQuery(
					session, startIndex, maxCount
					, serviceGridId, serviceId, userGridId
					, startDateTime, endDateTime, period
					, orders
					).list();
			AccessRankingEntry[] elements = ArrayUtil.collect(
					result.toArray(), AccessRankingEntry.class
					, new Transformer<Object, AccessRankingEntry>(){
						public AccessRankingEntry transform(Object value)
								throws TransformationException {
							Object[] v = (Object[])value;
							return new AccessRankingEntry(
									v[0].toString()
									, v[1].toString()
									, v[2] != null ? v[2].toString() : ""
									, v[3].toString()
									, v[4].toString()
									, v[5] != null ? v[5].toString() : ""
									, ((Long)v[6]).intValue()
									, ((Long)v[7]).longValue()
									, ((Long)v[8]).longValue()
									, ((Long)v[9]).longValue()
									);
						}
			});
			int totalCount = 0;
			if(elements.length < maxCount){
				totalCount = elements.length + startIndex;
			} else{
				totalCount = ((Long)createUserRankingCountQuery(
						session, startIndex, maxCount
						, serviceGridId, serviceId
						, startDateTime, endDateTime, period
						).uniqueResult()).intValue();
			}
			getContext().commitTransaction();
			return new AccessRankingEntrySearchResult(
					elements, totalCount, true);
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

	public AccessRankingEntrySearchResult searchServiceAccessRanking(int startIndex,
			int maxCount, String userGridId, String userId
			, Calendar startDateTime, Calendar endDateTime
			, Period period, Order[] orders)
	throws DaoException
	{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		startDateTime = CalendarUtil.toDefaultTimeZone(startDateTime);
		startDateTime = toBeginning(startDateTime, period);
		endDateTime = CalendarUtil.toDefaultTimeZone(endDateTime);
		endDateTime = toBeginning(endDateTime, period);

		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<?> result = createServiceRankingQuery(
					session, startIndex, maxCount
					, userGridId, userId
					, startDateTime, endDateTime, period
					, orders
					).list();
			AccessRankingEntry[] elements = ArrayUtil.collect(
					result.toArray(), AccessRankingEntry.class
					, new Transformer<Object, AccessRankingEntry>(){
						public AccessRankingEntry transform(Object value)
								throws TransformationException {
							Object[] v = (Object[])value;
							return new AccessRankingEntry(
									v[0].toString()
									, v[1].toString()
									, v[2] != null ? v[2].toString() : ""
									, v[3].toString()
									, v[4].toString()
									, v[5] != null ? v[5].toString() : ""
									, ((Long)v[6]).intValue()
									, ((Long)v[7]).longValue()
									, ((Long)v[8]).longValue()
									, ((Long)v[9]).longValue()
									);
						}
			});
			int totalCount = 0;
			if(elements.length < maxCount){
				totalCount = elements.length + startIndex;
			} else{
				totalCount = ((Long)createServiceRankingCountQuery(
						session, startIndex, maxCount
						, userGridId, userId
						, startDateTime, endDateTime, period
						).uniqueResult()).intValue();
			}
			getContext().commitTransaction();
			return new AccessRankingEntrySearchResult(
					elements, totalCount, true);
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

	public void deleteAccessStatOfGrid(final String serviceGridId)
	throws DaoException {
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session, "where serviceAndNodeGridId=:serviceGridId")
					.setString("serviceGridId", serviceGridId)
					.executeUpdate();
			}
		});
	}

	public void deleteAccessStatOfService(final String serviceGridId, final String serviceId)
	throws DaoException {
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session
					, "where serviceAndNodeGridId=:serviceGridId and serviceId=:serviceId")
				.setString("serviceGridId", serviceGridId)
				.setString("serviceId", serviceId)
				.executeUpdate();
			}});
	}

	public void deleteAccessStat(
			final String userGridId, final String userId
			, final String serviceGridId, final String serviceId
			, final Calendar baseDateTime, final Period period)
	throws DaoException {
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session
					, "where userGridId=:userGridId and userId=:userId" +
							" and serviceAndNodeGridId=:serviceGridId and serviceId=:serviceId" +
							" and baseDateTime=:baseDateTime and period=:period")
						.setString("userGridId", userGridId)
						.setString("userId", userId)
						.setString("serviceGridId", serviceGridId)
						.setString("serviceId", serviceId)
						.setCalendar("baseDateTime", baseDateTime)
						.setParameter("period", period)
						.executeUpdate();
			}});
	}

	public void deleteAccessStatOfUser(
			final String userGridId, final String userId)
	throws DaoException{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session, "where userGridId=:userGridId and userId=:userId")
				.setString("userGridId", userGridId)
				.setString("userId", userId)
				.executeUpdate();
			}});
	}

	private static Calendar toBeginning(Calendar calendar, Period period){
		switch(period){
			case YEAR:
				return CalendarUtil.createBeginningOfYear(calendar);
			case MONTH:
				return CalendarUtil.createBeginningOfMonth(calendar);
			case DAY:
				return CalendarUtil.createBeginningOfDay(calendar);
			case SECOND:
				Calendar c = (Calendar)calendar.clone();
				c.set(Calendar.MILLISECOND, 0);
				return c;
			default:
				throw new IllegalArgumentException(
						"period must be one of [YEAR, MONTH, DAY]."
						);
		}
	}

	private static Query createUserRankingQuery(
			Session session, int startIndex, int maxCount
			, String serviceGridId, String serviceId, String userGridId
			, Calendar startDateTime, Calendar endDateTime
			, Period period, Order[] orders
			){
		return createRankingQuery(
				session, orders, USERRANKINGQUERY
				).setString("serviceGridId", serviceGridId)
				.setString("serviceId", serviceId)
				.setString("userGridId", userGridId)
				.setCalendar("startDateTime", startDateTime)
				.setCalendar("endDateTime", endDateTime)
				.setParameter("period", period)
				.setFirstResult(startIndex)
				.setMaxResults(maxCount);
	}

	private static Query createUserRankingCountQuery(
			Session session, int startIndex, int maxCount
			, String serviceGridId, String serviceId
			, Calendar startDateTime, Calendar endDateTime
			, Period period
			){
		return session.createQuery(USERRANKINGQUERY_FORCOUNT)
				.setString("serviceGridId", serviceGridId)
				.setString("serviceId", serviceId)
				.setCalendar("startDateTime", startDateTime)
				.setCalendar("endDateTime", endDateTime)
				.setParameter("period", period)
				;
	}

	private static Query createServiceRankingQuery(
			Session session, int startIndex, int maxCount
			, String userGridId, String userId
			, Calendar startDateTime, Calendar endDateTime
			, Period period, Order[] orders
			){
		return createRankingQuery(
				session, orders, SERVICERANKINGQUERY
				).setString("userGridId", userGridId)
				.setString("userId", userId)
				.setCalendar("startDateTime", startDateTime)
				.setCalendar("endDateTime", endDateTime)
				.setParameter("period", period)
				.setFirstResult(startIndex)
				.setMaxResults(maxCount);
	}

	private static Query createServiceRankingCountQuery(
			Session session, int startIndex, int maxCount
			, String serviceGridId, String serviceId
			, Calendar startDateTime, Calendar endDateTime
			, Period period
			){
		return session.createQuery(SERVICERANKINGQUERY_FORCOUNT)
				.setString("userGridId", serviceGridId)
				.setString("userId", serviceId)
				.setCalendar("startDateTime", startDateTime)
				.setCalendar("endDateTime", endDateTime)
				.setParameter("period", period)
				;
	}

	private static Query createRankingQuery(
			Session session, Order[] orders, String query
			){
		StringBuilder q = new StringBuilder(query);
		if(orders.length == 0){
			q.append(" order by sum(a.accessCount) desc, sum(a.responseBytes) desc");
		} else{
			appendOrders(q, orders);
		}
		return session.createQuery(q.toString());
	}

	private static void appendOrders(StringBuilder q, Order[] orders){
		boolean first = true;
		for(Order o : orders){
			String name = o.getFieldName();
			String orderField = fieldToOrderField.get(name.toUpperCase());
			if(orderField == null) continue;
			if(first){
				q.append(" order by ");
				first = false;
			} else{
				q.append(", ");
			}
			Boolean orderFieldIsString = fieldIsString.get(name.toUpperCase());
			if(orderFieldIsString != null && orderFieldIsString.booleanValue()){
				q.append("lower(");
				q.append(orderField);
				q.append(") ");
			} else{
				q.append(orderField);
			}
			switch(o.getDirection()){
				case ASCENDANT:
					q.append("asc");
					break;
				case DESCENDANT:
					q.append("desc");
					break;
			}
		}
	}

	private static final String USERRANKINGQUERY_FORCOUNT =
		"select count(distinct userId) "
		+ " from AccessStat"
		+ " where serviceAndNodeGridId=:serviceGridId"
		+ "   and serviceId=:serviceId"
		+ "   and baseDateTime>=:startDateTime"
		+ "   and baseDateTime<=:endDateTime"
		+ "   and period=:period"
		;
	private static final String USERRANKINGQUERY_SELECT_FROM =
		"select u.gridId, u.userId, u.organization"
		+ ", s.gridId, s.serviceId, s.serviceName"
		+ ", sum(a.accessCount), sum(a.requestBytes), sum(a.responseBytes), sum(a.responseMillis)"
		+ " from AccessStat as a, Service as s, User as u"
		;
	private static final String USERRANKINGQUERY_WHERE =
		" where s.gridId=:serviceGridId and s.serviceId=:serviceId"
		+ "   and a.baseDateTime>=:startDateTime"
		+ "   and a.baseDateTime<=:endDateTime"
		+ "   and a.period=:period"
		+ "   and a.userGridId=:userGridId"
		+ "   and a.userGridId=u.gridId and a.userId=u.userId"
		+ "   and a.serviceAndNodeGridId=s.gridId and a.serviceId=s.serviceId"
		;
	private static final String USERRANKINGQUERY_GROUPBY =
		" group by u.gridId, u.userId, u.organization, s.gridId, s.serviceId, s.serviceName"
		;
	private static final String USERRANKINGQUERY =
		USERRANKINGQUERY_SELECT_FROM
		+ USERRANKINGQUERY_WHERE
		+ USERRANKINGQUERY_GROUPBY
		;
	private static final String SERVICERANKINGQUERY =
		"select u.gridId, u.userId, u.organization"
		+ ", s.gridId, s.serviceId, s.serviceName"
		+ ", sum(a.accessCount), sum(a.requestBytes), sum(a.responseBytes), sum(a.responseMillis)"
		+ " from AccessStat as a, Service as s, User as u"
		+ " where u.gridId=:userGridId and u.userId=:userId"
		+ "   and a.baseDateTime>=:startDateTime"
		+ "   and a.baseDateTime<=:endDateTime"
		+ "   and a.period=:period"
		+ "   and a.userGridId=u.gridId and a.userId=u.userId"
		+ "   and a.serviceAndNodeGridId=s.gridId and a.serviceId=s.serviceId"
		+ " group by u.gridId, u.userId, u.organization, s.gridId, s.serviceId, s.serviceName"
		;
	private static final String SERVICERANKINGQUERY_FORCOUNT =
		"select count(distinct serviceId) "
		+ " from AccessStat"
		+ " where userGridId=:userGridId"
		+ "   and userId=:userId"
		+ "   and baseDateTime>=:startDateTime"
		+ "   and baseDateTime<=:endDateTime"
		+ "   and period=:period"
		;
	private static final String SELECTACCESSSTATES =
			"select new AccessStat(a.userGridId, a.userId" +
			"    , a.serviceAndNodeGridId, a.serviceId, '*'" +
			"    , a.baseDateTime, a.period, cast(sum(a.accessCount) as int)" +
			"    , sum(a.requestBytes), sum(a.responseBytes), sum(a.responseMillis)" +
			"    )" +
			"  from AccessStat as a" +
			"  where (" +
			"    (a.baseDateTime=:baseDateTimeDay and a.period=:pd)" +
			"    or (a.baseDateTime=:baseDateTimeMonth and a.period=:pm)" +
			"    or (a.baseDateTime=:baseDateTimeYear and a.period=:py)" +
			"    )" +
			"    and a.userGridId=:userGridId and a.userId=:userId" +
			"    and a.serviceAndNodeGridId=:serviceAndNodeGridId and a.serviceId=:serviceId" +
			"  group by userGridId, userId, serviceAndNodeGridId, serviceId, baseDateTime, period" +
			"  order by a.period"
			;
	private static Map<String, String> fieldToOrderField
			= new HashMap<String, String>();
	private static Map<String, Boolean> fieldIsString
			= new HashMap<String, Boolean>();
	static{
		fieldToOrderField.put("userGridId".toUpperCase(), "u.gridId");
		fieldToOrderField.put("userId".toUpperCase(), "u.userId");
		fieldToOrderField.put("userOrganization".toUpperCase(), "u.organization");
		fieldToOrderField.put("serviceGridId".toUpperCase(), "s.gridId");
		fieldToOrderField.put("serviceId".toUpperCase(), "s.serviceId");
		fieldToOrderField.put("serviceName".toUpperCase(), "s.serviceName");
		fieldToOrderField.put("accessCount".toUpperCase(), "sum(a.accessCount)");
		fieldToOrderField.put("requestBytes".toUpperCase(), "sum(a.requestBytes)");
		fieldToOrderField.put("responseBytes".toUpperCase(), "sum(a.responseBytes)");
		fieldToOrderField.put("responseMillis".toUpperCase(), "sum(a.responseMillis)");
		fieldIsString.put("userGridId".toUpperCase(), true);
		fieldIsString.put("userId".toUpperCase(), true);
		fieldIsString.put("userOrganization".toUpperCase(), true);
		fieldIsString.put("serviceGridId".toUpperCase(), true);
		fieldIsString.put("serviceId".toUpperCase(), true);
		fieldIsString.put("serviceName".toUpperCase(), true);
		fieldIsString.put("accessCount".toUpperCase(), false);
		fieldIsString.put("requestBytes".toUpperCase(), false);
		fieldIsString.put("responseBytes".toUpperCase(), false);
		fieldIsString.put("responseMillis".toUpperCase(), false);
	}
}
