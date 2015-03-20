/*
 * $Id:HibernateUserDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.ServiceActionScheduleDao;
import jp.go.nict.langrid.dao.ServiceActionScheduleNotFoundException;
import jp.go.nict.langrid.dao.ServiceActionScheduleSearchResult;
import jp.go.nict.langrid.dao.entity.ServiceActionSchedule;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 216 $
 */
public class HibernateServiceActionScheduleDao
extends HibernateDao
implements ServiceActionScheduleDao
{
	/**
	 * 
	 * 
	 */
	public HibernateServiceActionScheduleDao(HibernateDaoContext context){
		super(context);
	}

	public void clear() throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			session.createQuery("delete from ServiceActionSchedule").executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ServiceActionSchedule> listServiceActionSchedule(String bookingGridId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<ServiceActionSchedule> list = (List<ServiceActionSchedule>)
			session.createCriteria(ServiceActionSchedule.class)
				.add(Property.forName("gridId").eq(bookingGridId))
				.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public ServiceActionScheduleSearchResult searchServiceActionSchedule(
			int startIndex, int maxCount, MatchingCondition[] conditions, Order[] orders)
	throws DaoException
	{
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"updatedDateTime", OrderDirection.DESCENDANT
					)};
		}

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(ServiceActionSchedule.class);
			CriteriaUtil.addMatchingConditions(c, conditions);
			List<ServiceActionSchedule> elements = (List<ServiceActionSchedule>)CriteriaUtil.getList(
					c, startIndex, maxCount, orders);
			int totalCount = 0;
			if(elements.size() < maxCount){
				totalCount = elements.size() + startIndex;
			} else{
				Criteria cr = session.createCriteria(ServiceActionSchedule.class);
				totalCount = CriteriaUtil.getCount(cr);
			}
			ServiceActionScheduleSearchResult r = new ServiceActionScheduleSearchResult(
					elements.toArray(new ServiceActionSchedule[]{}), totalCount, true
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

	public void addServiceActionSchedule(ServiceActionSchedule booking) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			session.save(booking);
			booking.setNodeLocalId(booking.getId());
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteServiceActionSchedule(String bookingGridId, int bookingId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(ServiceActionSchedule ob : listServiceActionSchedule(bookingGridId)) {
				if(ob.getId() == bookingId){
					session.delete(ob);
					getContext().commitTransaction();
					return;
				}
			}
			throw new ServiceActionScheduleNotFoundException(bookingGridId, bookingId);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteAllServiceActionSchedule(String bookingGridId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(ServiceActionSchedule ob : listServiceActionSchedule(bookingGridId)) {
				session.delete(ob);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}
}
