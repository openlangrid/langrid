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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.OverUseState;
import jp.go.nict.langrid.dao.OverUseStateDao;
import jp.go.nict.langrid.dao.OverUseStateSearchResult;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateOverUseStateDao
	extends HibernateDao implements OverUseStateDao
{
	/**
	 * 
	 * 
	 */
	public HibernateOverUseStateDao(HibernateDaoContext context){
		super(context);
	}
	
	@Override
	public OverUseStateSearchResult searchOverUseWithPeriod(int startIndex, int maxCount,
		String gridId, Calendar startDateTime, Calendar endDateTime, Order[] orders,
		Period period)
	throws DaoException
	{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		startDateTime = CalendarUtil.toDefaultTimeZone(startDateTime);
		endDateTime = CalendarUtil.toDefaultTimeZone(endDateTime);
		
		if(orders.length == 0){
			orders = new Order[]{
				new Order("lastAccessDateTime", OrderDirection.DESCENDANT)
			};
		}
		for(Order o : orders){
			if(o.getFieldName().equals("period")){
				o.setFieldName("ol.period");
			}
		}

		Session session = getSession();
		getContext().beginTransaction();
		try{
			// AccessState検索
			Query qState = session.createQuery(countClouse + fromAndWhereClouseStateWithPeriod);
			qState.setString("gridId", gridId);
			qState.setCalendar("startDateTime", startDateTime);
			qState.setCalendar("endDateTime", endDateTime);
			qState.setInteger("period", period.ordinal());
			long countState = (Long)qState.uniqueResult();
			qState = session.createQuery(
				selectClouseState + fromAndWhereClouseStateWithPeriod
				+ QueryUtil.buildOrderByQuery(Object.class, "ac", orders));
			qState.setString("gridId", gridId);
			qState.setCalendar("startDateTime", startDateTime);
			qState.setCalendar("endDateTime", endDateTime);
			qState.setInteger("period", period.ordinal());

			List<OverUseState> result = new ArrayList<OverUseState>();
			ScrollableResults iState = qState.scroll(ScrollMode.FORWARD_ONLY);
			for(int i = 0; i < (startIndex + maxCount); i++){
				OverUseState currentState = getNextOverUseState(iState);
				if(currentState == null){
					break;
				}
				if(startIndex <= i) result.add(currentState);
			}
			OverUseStateSearchResult r = new OverUseStateSearchResult(
				result.toArray(new OverUseState[]{}), (int)(countState), true
			);
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			getContext().rollbackTransaction();
			logAdditionalInfo(e);
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
	public OverUseStateSearchResult searchOverUse(
			int startIndex, int maxCount, String gridId
			, Calendar startDateTime, Calendar endDateTime
			, Order[] orders)
		throws DaoException
	{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		startDateTime = CalendarUtil.toDefaultTimeZone(startDateTime);
		endDateTime = CalendarUtil.toDefaultTimeZone(endDateTime);

		if(orders.length == 0){
			orders = new Order[]{
					new Order("lastAccessDateTime", OrderDirection.DESCENDANT)
					};
		}
		for(Order o : orders){
			if(o.getFieldName().equals("period")){
				o.setFieldName("ol.period");
			}
		}
		Order[] ordersForLog = ArrayUtil.collect(orders, Order.class
				, new Transformer<Order, Order>(){
					public Order transform(Order value)
					throws TransformationException {
						Order o = new Order(
								value.getFieldName()
								, value.getDirection()
								);
						if(o.getFieldName().equals("lastAccessDateTime")){
							o.setFieldName("dateTime");
						} else if(o.getFieldName().equals("transferredSize")){
							o.setFieldName("responseBytes");
						}
						return o;
					}
			});

		Session session = getSession();
		getContext().beginTransaction();
		try{
			// AccessState検索
			Query qState = session.createQuery(countClouse + fromAndWhereClouseState);
			qState.setString("gridId", gridId);
			qState.setCalendar("startDateTime", startDateTime);
			qState.setCalendar("endDateTime", endDateTime);
			long countState = (Long)qState.uniqueResult();
			qState = session.createQuery(
					selectClouseState + fromAndWhereClouseState
					 + QueryUtil.buildOrderByQuery(Object.class, "ac", orders));
			qState.setString("gridId", gridId);
			qState.setCalendar("startDateTime", startDateTime);
			qState.setCalendar("endDateTime", endDateTime);

			// AccessLog検索
			Query qLog = session.createQuery(countClouse + fromAndWhereClouseLog);
			qLog.setString("gridId", gridId);
			qLog.setCalendar("startDateTime", startDateTime);
			qLog.setCalendar("endDateTime", endDateTime);
			long countLog = (Long)qLog.uniqueResult();
			qLog = session.createQuery(
					selectClouseLog + fromAndWhereClouseLog
					 + QueryUtil.buildOrderByQuery(Object.class, "al", ordersForLog));
			qLog.setString("gridId", gridId);
			qLog.setCalendar("startDateTime", startDateTime);
			qLog.setCalendar("endDateTime", endDateTime);

			// ソートを考慮しながらマージ
			List<OverUseState> result = new ArrayList<OverUseState>();
			Iterator<Object> iState = qState.list().iterator();
			Iterator<Object> iLog = qLog.list().iterator();
			OverUseState currentState = null;
			OverUseState currentLog = null;
			for(int i = 0; i < (startIndex + maxCount); i++){
				if(currentState == null){
					currentState = getNextOverUseState(iState);
				}
				if(currentLog == null){
					currentLog = getNextOverUseState(iLog);
				}
				if(currentState == null && currentLog == null){
					break;
				}
				if(currentState == null && currentLog != null){
					if(startIndex <= i) result.add(currentLog);
					currentLog = null;
					continue;
				}
				if(currentState != null && currentLog == null){
					if(startIndex <= i) result.add(currentState);
					currentState = null;
					continue;
				}
				int c = compare(currentState, currentLog, orders);
				if(c <= 0){
					if(startIndex <= i) result.add(currentState);
					currentState = null;
					continue;
				}
				if(c > 0){
					if(startIndex <= i) result.add(currentLog);
					currentLog = null;
					continue;
				}
			}

			OverUseStateSearchResult r = new OverUseStateSearchResult(
					result.toArray(new OverUseState[]{})
					, (int)(countState + countLog), true
					);
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			getContext().rollbackTransaction();
			logAdditionalInfo(e);
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	private OverUseState getNextOverUseState(ScrollableResults iLog){
		if(!iLog.next()) return null;
		Object[] row = iLog.get();
		LimitType t = (LimitType)iLog.get(6);
		long currentValue = 0;
		if(t.equals(LimitType.FREQUENCY)){
			currentValue = ((Number)iLog.get(8)).longValue();
		} else{
			currentValue = ((Number)iLog.get(9)).longValue();
		}
		return new OverUseState(
				(String)iLog.get(0), (String)iLog.get(1)
				, (String)iLog.get(2), (String)iLog.get(3)
				, (Calendar)iLog.get(4), (Period)iLog.get(5)
				, t, (Integer)iLog.get(7)
				, currentValue, (Calendar)iLog.get(10)
				);
	}

	private OverUseState getNextOverUseState(Iterator<Object> iLog){
		if(!iLog.hasNext()) return null;
		Object[] row = (Object[])iLog.next();
		LimitType t = (LimitType)row[6];
		long currentValue = 0;
		if(t.equals(LimitType.FREQUENCY)){
			currentValue = ((Number)row[8]).longValue();
		} else{
			currentValue = ((Number)row[9]).longValue();
		}
		return new OverUseState(
			(String)row[0], (String)row[1]
			                            , (String)row[2], (String)row[3]
			                            , (Calendar)row[4], (Period)row[5]
			                            , t, (Integer)row[7]
			                            , currentValue, (Calendar)row[10]
		);
	}

	@SuppressWarnings("unchecked")
	private int compare(OverUseState state1, OverUseState state2
			, Order[] orders)
	throws DaoException
	{
		try{
			for(Order o : orders){
				int s = o.getDirection().equals(OrderDirection.ASCENDANT)
						? 1 : -1;
				Method getter = getters.get(o.getFieldName());
				if(getter == null) throw new DaoException(String.format(
						"unknown field name: \"%s\" for OverUseState"
						, o.getFieldName()));
				Object value1 = getter.invoke(state1);
				Object value2 = getter.invoke(state2);
				if(value1 instanceof Comparable){
					int c = ((Comparable)value1).compareTo(value2);
					if(c != 0){
						return s * c;
					}
				}
			}
		} catch(IllegalAccessException e){
			throw new DaoException(e);
		} catch(InvocationTargetException e){
			throw new DaoException(e);
		}
		return 0;
	}

	private static final String countClouse =
		"select count(*) "
		;

	private static final String selectClouseState =
		"select"
		+ "  ac.userGridId, ac.userId"
		+ "  , ac.serviceAndNodeGridId, ac.serviceId"
		+ "  , ac.baseDateTime, ac.period"
		+ "  , ol.limitType, ol.limitCount"
		+ "  , ac.accessCount"
		+ "  , ac.responseBytes"
		+ "  , ac.lastAccessDateTime "
		;

	private static final String fromAndWhereClouseState =
		" from AccessStat ac, OverUseLimit ol"
		+ " where"
		+ "  ac.lastAccessDateTime < :endDateTime"
		+ "  and ac.lastAccessDateTime > :startDateTime"
		+ "  and ac.serviceAndNodeGridId=:gridId"
		+ "  and ac.serviceAndNodeGridId=ol.gridId"
		+ "  and ac.period=ol.period"
		+ "  and ol.gridId=:gridId"
		+ "  and ("
		+ "   (ol.limitType = 0"
		+ "    and ac.accessCount > ol.limitCount"
		+ "   )"
		+ "   or"
		+ "   (ol.limitType = 1"
		+ "    and ac.responseBytes > ol.limitCount"
		+ "   )"
		+ "  )";

	private static final String fromAndWhereClouseStateWithPeriod =
		" from AccessStat ac, OverUseLimit ol"
		+ " where"
		+ "  ac.lastAccessDateTime < :endDateTime"
		+ "  and ac.lastAccessDateTime > :startDateTime"
		+ "  and ac.serviceAndNodeGridId=:gridId"
		+ "  and ac.serviceAndNodeGridId=ol.gridId"
		+ "  and ol.period=:period"
		+ "  and ac.period=ol.period"
		+ "  and ol.gridId=:gridId"
		+ "  and ("
		+ "   (ol.limitType = 0"
		+ "    and ac.accessCount > ol.limitCount"
		+ "   )"
		+ "   or"
		+ "   (ol.limitType = 1"
		+ "    and ac.responseBytes > ol.limitCount"
		+ "   )"
		+ "  )";

	private static final String selectClouseLog =
		"select"
		+ "  al.userGridId, al.userId, al.serviceAndNodeGridId, al.serviceId"
		+ "  , al.dateTime as baseDateTime, ol.period"
		+ "  , ol.limitType, ol.limitCount"
		+ "  , 1 as accessCount"
		+ "  , al.responseBytes as transferredSize"
		+ "  , al.dateTime as lastAccessDateTime "
		;

	private static final String fromAndWhereClouseLog =
		" from AccessLog al, OverUseLimit ol"
		+ " where"
		+ "  al.dateTime < :endDateTime"
		+ "  and al.dateTime > :startDateTime"
		+ "  and al.serviceAndNodeGridId=:gridId"
		+ "  and al.serviceAndNodeGridId=ol.gridId"
		+ "  and al.responseBytes > ol.limitCount"
		+ "  and al.faultCode is null"
		+ "  and ol.gridId=:gridId"
		+ "  and ol.limitType=1"
		+ "  and ol.period=0"
		;

	private static final String fromAndWhereClouseLogWithPeriod =
		" from AccessLog al, OverUseLimit ol"
		+ " where"
		+ "  al.dateTime < :endDateTime"
		+ "  and al.dateTime > :startDateTime"
		+ "  and al.serviceAndNodeGridId=:gridId"
		+ "  and al.serviceAndNodeGridId=ol.gridId"
		+ "  and al.responseBytes > ol.limitCount"
		+ "  and al.faultCode is null"
		+ "  and ol.gridId=:gridId"
		+ "  and ol.limitType=1"
		+ "  and ol.period=:period"
		;

	private static Map<String, Method> getters
		= new HashMap<String, Method>();
	static{
		for(Method m : OverUseState.class.getDeclaredMethods()){
			String name = m.getName();
			if((name.startsWith("get")) && (name.length() >= 4)){
				String propName = 
					name.substring(3, 4).toLowerCase()
					+ name.substring(4);
				if(propName.equals("period"))
					propName = "ol.period";
				getters.put(propName, m);
			}
		}
	}
}
