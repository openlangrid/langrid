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

import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.OverUseLimitDao;
import jp.go.nict.langrid.dao.OverUseLimitNotFoundException;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.OverUseLimit;
import jp.go.nict.langrid.dao.entity.Period;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateOverUseLimitDao
extends HibernateCRUDDao<OverUseLimit>
implements OverUseLimitDao
{
	/**
	 * 
	 * 
	 */
	public HibernateOverUseLimitDao(HibernateDaoContext context){
		super(context, OverUseLimit.class);
	}

	public void clear() throws DaoException{
		getContext().beginTransaction();
		try{
			createDeleteQuery(OverUseLimit.class).executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<OverUseLimit> listOverUseLimits(String gridId, Order[] orders)
	throws DaoException
	{
		if(orders.length == 0){
			orders = new Order[]{
					new Order("period", OrderDirection.ASCENDANT)
					};
		}

		getContext().beginTransaction();
		try{
			Criteria c = getSession()
					.createCriteria(OverUseLimit.class)
					.add(Property.forName("gridId").eq(gridId));
			CriteriaUtil.addOrders(c, orders);
			List<OverUseLimit> list = (List<OverUseLimit>)c.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void setOverUseLimit(
			String gridId, Period period
			, LimitType limitType, int limit
			)
	throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(OverUseLimit.class)
					.add(Property.forName("gridId").eq(gridId))
					.add(Property.forName("period").eq(period))
					.add(Property.forName("limitType").eq(limitType));
			OverUseLimit r = (OverUseLimit)c.uniqueResult();
			if(r == null){
				session.save(new OverUseLimit(gridId, period, limitType, limit));
			} else{
				r.setGridId(gridId);
				r.setPeriod(period);
				r.setLimitCount(limit);
				r.setLimitType(limitType);
				r.touchUpdatedDateTime();
				session.update(r);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public OverUseLimit getOverUseLimit(String gridId
			, Period period, LimitType limitType)
		throws OverUseLimitNotFoundException, DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			OverUseLimit l = (OverUseLimit)session
					.createCriteria(OverUseLimit.class)
					.add(Property.forName("gridId").eq(gridId))
					.add(Property.forName("period").eq(period))
					.add(Property.forName("limitType").eq(limitType))
					.uniqueResult();
			if(l == null){
				getContext().commitTransaction();
				throw new OverUseLimitNotFoundException(
						gridId, period, limitType
						);
			}
			getContext().commitTransaction();
			return l;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} 
	}

	public void deleteOverUseLimit(
			String gridId, Period period
			, LimitType limitType)
		throws OverUseLimitNotFoundException, DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			session.delete(getOverUseLimit(gridId, period, limitType));
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} 
	}

	public void deleteOverUseLimitsOfGrid(final String gridId)
	throws DaoException{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session, "where gridId=:gridId")
						.setString("gridId", gridId)
						.executeUpdate();
			}
		});
	}
}
