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

import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLimitNotFoundException;
import jp.go.nict.langrid.dao.AccessLimitSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessLimitPK;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateAccessLimitDao
extends HibernateCRUDDao<AccessLimit>
implements AccessLimitDao
{
	/**
	 * 
	 * 
	 */
	public HibernateAccessLimitDao(HibernateDaoContext context){
		super(context, AccessLimit.class);
	}

	@Override
	public void clear() throws DaoException{
		super.clear();
	}

	@Override
	public void clearExceptDefaults() throws DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(
						AccessLimit.class
						, "where not (userId='*')"
						).executeUpdate();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<AccessLimit> listAccessLimits(final String serviceGridId)
	throws DaoException{
		return transact(new DaoBlockR<List<AccessLimit>>() {
			public List<AccessLimit> execute(Session session)
			throws DaoException {
				return createCriteria(session)
						.add(Property.forName("serviceGridId").eq(serviceGridId))
						.list();
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public AccessLimitSearchResult searchAccessLimits(
			int startIndex, int maxCount
			, String userGridId, String userId
			, String serviceGridId, String[] serviceIds
			, Order[] orders
			) throws DaoException
	{
		if(orders.length == 0){
			orders = new Order[]{
					new Order("userId", OrderDirection.ASCENDANT)
					, new Order("serviceId", OrderDirection.ASCENDANT)
					, new Order("period", OrderDirection.ASCENDANT)
					, new Order("limitType", OrderDirection.ASCENDANT)
					};
		}

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(AccessLimit.class);
			addSearchAccessLimitsCriterion(c, userGridId, userId, serviceGridId, serviceIds);
			List<AccessLimit> elements = (List<AccessLimit>)CriteriaUtil.getList(
					c, startIndex, maxCount, orders);
			int totalCount = 0;
			if(elements.size() < maxCount){
				totalCount = elements.size() + startIndex;
			} else{
				Criteria cr = session.createCriteria(AccessLimit.class);
				addSearchAccessLimitsCriterion(cr, userGridId, userId, serviceGridId, serviceIds);
				totalCount = CriteriaUtil.getCount(cr);
			}
			AccessLimitSearchResult r = new AccessLimitSearchResult(
					elements.toArray(new AccessLimit[]{}), totalCount
					, true
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
	public AccessLimit getAccessLimit(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			, Period period, LimitType limitType
			) throws AccessLimitNotFoundException, DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessLimitPK pk = new AccessLimitPK(
					userGridId, userId, serviceGridId, serviceId
					, period, limitType
					);
			AccessLimit ac = (AccessLimit)session.get(
					AccessLimit.class, pk
					);
			getContext().commitTransaction();
			if(ac == null){
				throw new AccessLimitNotFoundException(
						userGridId, userId, serviceGridId, serviceId, period, limitType
						);
			}
			return ac;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AccessLimit> getAccessLimits(
			String userGridId, String userId, String serviceGridId, String serviceId)
	throws DaoException {
		if(userId == null || userId.length() == 0){
			throw new DaoException("userId must not be an empty.");
		}
		if(serviceId == null || serviceId.length() == 0){
			throw new DaoException("serviceId must not be an empty.");
		}
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(AccessLimit.class);
			addSearchAccessLimitsCriterion(c, userGridId, userId, serviceGridId, new String[]{serviceId});
			c.addOrder(org.hibernate.criterion.Order.asc("period"));
			c.addOrder(org.hibernate.criterion.Order.asc("limitType"));
			List<AccessLimit> ret = (List<AccessLimit>)c.list();
			getContext().commitTransaction();
			return ret;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public AccessLimit getServiceDefaultAccessLimit(
			String userGridId, String serviceGridId, String serviceId
			, Period period, LimitType limitType)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(AccessLimit.class);
			c.add(Property.forName("userGrudId").eq(userGridId));
			c.add(Property.forName("userId").eq("*"));
			c.add(Property.forName("serviceGridId").eq(serviceGridId));
			c.add(Property.forName("serviceId").eq(serviceId));
			c.add(Property.forName("period").eq(period));
			c.add(Property.forName("limitType").eq(limitType));
			AccessLimit r = (AccessLimit)c.uniqueResult();
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AccessLimit> getServiceDefaultAccessLimits(
			String userGridId, String serviceGridId, String serviceId)
	throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(AccessLimit.class);
			c.add(Property.forName("userGridId").eq(userGridId));
			c.add(Property.forName("userId").eq("*"));
			c.add(Property.forName("serviceGridId").eq(serviceGridId));
			c.add(Property.forName("serviceId").eq(serviceId));
			List<AccessLimit> r = c.list();
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public void setServiceDefaultAccessLimit(
			String userGridId, String serviceGridId, String serviceId
			, Period period, LimitType limitType, int limit)
	throws DaoException
	{
		setAccessLimit(
				userGridId, "*", serviceGridId, serviceId, period
				,limitType, limit
				);
	}

	@Override
	public AccessLimit setAccessLimit(
			String userGridId, String userId, String serviceGridId, String serviceId
			, Period period, LimitType limitType, int limit)
	throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(AccessLimit.class);
			addSearchAccessLimitsCriterion(
					c, userGridId, userId, serviceGridId, new String[]{serviceId});
			c.add(Property.forName("period").eq(period));
			c.add(Property.forName("limitType").eq(limitType));
			AccessLimit r = (AccessLimit)c.uniqueResult();
			if(r == null){
				r = new AccessLimit(
						userGridId, userId, serviceGridId, serviceId
						, period, limitType, limit
						);
				session.save(r);
			} else{
				r.setUserId(userId);
				r.setServiceId(serviceId);
				r.setPeriod(period);
				r.setLimitCount(limit);
				r.setLimitType(limitType);
				r.touchUpdatedDateTime();
				session.update(r);
			}
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteAccessLimit(String userGridId, String userId
			, String serviceGridId, String serviceId
			, Period period, LimitType limitType)
		throws AccessLimitNotFoundException, DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessLimit r = (AccessLimit)session.get(
					AccessLimit.class, new AccessLimitPK(
							userGridId, userId, serviceGridId, serviceId
							, period, limitType
							));
			if(r == null){
				getContext().commitTransaction();
				throw new AccessLimitNotFoundException(
						userGridId, userId, serviceGridId, serviceId, period, limitType);
			}
			session.delete(r);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} 
	}

	@Override
	public void deleteAccessLimits(
			String userGridId, String userId, String serviceGridId, String serviceId)
		throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			session.createQuery(
					"delete from AccessLimit" +
					" where userGridId=:userGridId and userId=:userId" +
					" and serviceGridId=:serviceGridId and serviceId=:serviceId"
					)
					.setString("userGridId", userGridId)
					.setString("userId", userId)
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

	@Override
	public void deleteAccessLimitsOfGrid(final String gridId)
			throws DaoException
	{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				session.createQuery("delete from AccessLimit" +
					" where serviceGridId=:gridId")
					.setString("gridId", gridId)
					.executeUpdate();
			}
		});
	}

	@Override
	public void deleteAccessLimitsOfService(final String serviceGridId, final String serviceId)
	throws DaoException{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				session.createQuery("delete from AccessLimit" +
						" where serviceGridId=:serviceGridId and serviceId=:serviceId")
						.setString("serviceGridId", serviceGridId)
						.setString("serviceId", serviceId)
						.executeUpdate();
			}
		});
	}

	@Override
	public void deleteAccessLimitsOfUser(final String userGridId, final String userId)
	throws DaoException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				session.createQuery("delete from AccessLimit where userId=:userId")
						.setString("userId", userId).executeUpdate();
			}
		});
	}

	/**
	 * 
	 * 
	 */
	private static void addSearchAccessLimitsCriterion(
			Criteria c
			, String userGridId, String userId
			, String serviceGridId, String[] serviceIds
			){
		if(userGridId.length() > 0){
			c.add(Property.forName("userGridId").eq(userGridId));
			if(userId.length() > 0){
				c.add(Property.forName("userId").eq(userId));
			}
		}
		if(serviceGridId.length() > 0){
			c.add(Property.forName("serviceGridId").eq(serviceGridId));
			if(serviceIds.length > 0){
				Disjunction dj = Restrictions.disjunction();
				for(String id : serviceIds){
					dj.add(Property.forName("serviceId").eq(id));
				}
				c.add(dj);
			}
		}
	}
}
