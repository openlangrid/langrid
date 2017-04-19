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

import static jp.go.nict.langrid.commons.util.ArrayUtil.array;
import static jp.go.nict.langrid.dao.OrderDirection.ASCENDANT;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.lang.StringGenerator;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.AccessRightNotFoundException;
import jp.go.nict.langrid.dao.AccessRightSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.ServicePK;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateAccessRightDao
extends HibernateCRUDDao<AccessRight>
implements AccessRightDao
{
	/**
	 * 
	 * 
	 */
	public HibernateAccessRightDao(HibernateDaoContext context){
		super(context, AccessRight.class);
	}

	public void clear() throws DaoException{
		getContext().beginTransaction();
		try{
			createDeleteQuery(AccessRight.class).executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void clearExceptDefaults() throws DaoException {
		getContext().beginTransaction();
		try{
			createDeleteQuery(
					AccessRight.class
					, "where not (userId='*')"
					).executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<AccessRight> listAccessRights(String serviceGridId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<AccessRight> r = session
					.createCriteria(AccessRight.class)
					.add(Property.forName("serviceGridId").eq(serviceGridId))
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
	public Iterable<ServicePK> listAccessibleServices(String userGridId, String userId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		Set<ServicePK> services = new LinkedHashSet<ServicePK>();
		try{
			List<AccessRight> r = session
					.createQuery("from AccessRight" +
							" where (userGridId='*' or userGridId=:gridId)" +
							"   and (userId='*' or userId=:userId)" +
							" order by serviceId, userGridId, userId"
							)
					.setString("gridId", userGridId)
					.setString("userId", userId)
					.list();
			for(AccessRight ar : r){
				services.add(new ServicePK(ar.getServiceGridId(), ar.getServiceId()));
			}
			getContext().commitTransaction();
			return services;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public AccessRightSearchResult searchAccessRights(
			int startIndex, int maxCount
			, String userGridId, String userId
			, String serviceGridId, String[] serviceIds
			, Order[] orders
			) throws DaoException{
		if(orders.length == 0){
			orders = array(
					new Order("userId", ASCENDANT)
					, new Order("serviceId", ASCENDANT)
					);
		}

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Criteria c = session.createCriteria(AccessRight.class);
			addSearchAccessRightCriterion(c, userGridId, userId, serviceGridId, serviceIds);
			List<AccessRight> elements = (List<AccessRight>)CriteriaUtil.getList(
					c, startIndex, maxCount, orders);
			int totalCount = 0;
			if(elements.size() < maxCount){
				totalCount = elements.size() + startIndex;
			} else{
				Criteria cr = session.createCriteria(AccessRight.class);
				addSearchAccessRightCriterion(cr, userGridId, userId, serviceGridId, serviceIds);
				totalCount = CriteriaUtil.getCount(cr);
			}
			AccessRightSearchResult r = new AccessRightSearchResult(
					elements.toArray(new AccessRight[]{}), totalCount, true
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
	public AccessRightSearchResult searchAccessRightsAccordingToDefaultAndOwner(
			int startIndex, int maxCount
			, String userGridId, String userId
			, String serviceAndOwnerGridId, String[] serviceIds
			, String ownerUserId, Order[] orders
			) throws DaoException
	{
		if(orders.length > 0){
			orders = ArrayUtil.collect(orders, new Transformer<Order, Order>(){
				@Override
				public Order transform(Order value) throws TransformationException {
					String mapped = mappedFields.get(value.getFieldName());
					if(mapped != null){
						return new Order(mapped, value.getDirection());
					} else{
						return value;
					}
				}
			});
		}
		String orderby = "order by lower(l.sn), lower(l.on)";
		if(orders.length > 0){
			orderby = QueryUtil.buildOrderByQuery(capitalIgnoreFields, orders);
		}
		Session session = getSession();
		getContext().beginTransaction();
		try{
			if(getContext().getTransactionNestCount() > 1){
				session.flush();
			}
			String servicesParameters = null;
			if(serviceAndOwnerGridId.length() > 0){
				servicesParameters = " and s.gridId=:serviceGridId";
				servicesParameters += serviceIds.length > 0
					? " and s.serviceid in ("
							+ StringUtil.repeatedString(new StringGenerator() {
								public String generate() {
									return ":service" + i++;
								}
								private int i = 0;
							}, serviceIds.length, ", ")
							+ ")"
					: "";
			} else{
				servicesParameters = "";
			}
			String usersParameters = null;
			if(userGridId.length() > 0){
				usersParameters = " and (u.gridId=:userGridId";
				usersParameters += userId.length() > 0
						? " and u.userid=:userId)"
						: " and u.userid<>'*')";
			} else{
				usersParameters = " and u.userid<>'*'";
			}
			String ownerExcludes = (serviceAndOwnerGridId.length() > 0 && ownerUserId.length() > 0) ?
					" and (u.gridId<>:ownerUserGridId or u.userid<>:ownerUserId)"
					: "";
			Query q = session.createSQLQuery(String.format(
					selectJoinedEntities, servicesParameters
					, usersParameters, ownerExcludes, orderby
					));
			q.setFirstResult(startIndex);
			q.setMaxResults(maxCount);
			if(serviceAndOwnerGridId.length() > 0){
				q.setString("serviceGridId", serviceAndOwnerGridId);
			}
			for(int i = 0; i < serviceIds.length; i++){
				q.setString("service" + i, serviceIds[i]);
			}
			if(userGridId.length() > 0){
				q.setString("userGridId", userGridId);
			}
			if(userId.length() > 0){
				q.setString("userId", userId);
			}
			if(serviceAndOwnerGridId.length() > 0 && ownerUserId.length() > 0){
				q.setString("ownerUserGridId", serviceAndOwnerGridId);
				q.setString("ownerUserId", ownerUserId);
			}
			List<Object> list = q.list();
			List<AccessRight> elements = new ArrayList<AccessRight>();
			for(Object o : list){
				Object[] values = (Object[])o;
				String ugid = values[0].toString();
				String uid = values[1].toString();
				String sgid = values[2].toString();
				String sid = values[3].toString();
				Boolean permitted = (Boolean)values[4];
				Boolean gdPermitted = (Boolean)values[5];
				Boolean sdPermitted = (Boolean)values[6];
				boolean p = false;
				if(permitted != null){
					p = permitted;
				} else if(gdPermitted != null){
					p = gdPermitted;
				} else if(sdPermitted != null){
					p = sdPermitted;
				}
				AccessRight ar = new AccessRight(
						ugid, uid, sgid, sid, p
						);
				Timestamp ct = ((Timestamp)values[7]);
				if(ct != null){
					ar.setCreatedDateTime(CalendarUtil.createFromMillis(
							ct.getTime()));
				}
				Timestamp ut = ((Timestamp)values[8]);
				if(ut != null){
					ar.setCreatedDateTime(CalendarUtil.createFromMillis(
							ut.getTime()));
				}
				elements.add(ar);
			}
			long totalCount = 0;
			if(elements.size() < maxCount){
				totalCount = elements.size() + startIndex;
			} else{
				SQLQuery cq = session.createSQLQuery(String.format(
						countJoinedEntities, servicesParameters
						, usersParameters, ownerExcludes
						));
				if(serviceAndOwnerGridId.length() > 0){
					cq.setString("serviceGridId", serviceAndOwnerGridId);
				}
				for(int i = 0; i < serviceIds.length; i++){
					cq.setString("service" + i, serviceIds[i]);
				}
				if(userGridId.length() > 0){
					cq.setString("userGridId", userGridId);
				}
				if(userId.length() > 0){
					cq.setString("userId", userId);
				}
				if(serviceAndOwnerGridId.length() > 0 && ownerUserId.length() > 0){
					cq.setString("ownerUserGridId", serviceAndOwnerGridId);
					cq.setString("ownerUserId", ownerUserId);
				}
				totalCount = ((Number)cq.uniqueResult()).longValue();
			}

/*			Criteria c = session.createCriteria(AccessRight.class);
			addSearchAccessRightCriterionIgnoreDefault(c, userId, serviceIds);
			List<AccessRight> elements = (List<AccessRight>)CriteriaUtil.getList(
					c, startIndex, maxCount, orders);
			int totalCount = 0;
			if(elements.size() < maxCount){
				totalCount = elements.size() + startIndex;
			} else{
				Criteria cr = session.createCriteria(AccessRight.class);
				addSearchAccessRightCriterionIgnoreDefault(cr, userId, serviceIds);
				totalCount = CriteriaUtil.getCount(cr);
			}
*/			AccessRightSearchResult r = new AccessRightSearchResult(
					elements.toArray(new AccessRight[]{}), (int)totalCount, true
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

	public AccessRight getAccessRight(
			String userGridId, String userId, String serviceGridId, String serviceId
			) throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessRight r = doGetAccessRight(
					session, userGridId, userId, serviceGridId, serviceId);
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public AccessRight setAccessRight(
			String userGridId, String userId
			, String serviceGridId, String serviceId, boolean permitted)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessRight r = doGetAccessRight(
					session, userGridId, userId, serviceGridId, serviceId);
			Calendar now = Calendar.getInstance();
			if(r == null){
				session.save(new AccessRight(
					userGridId, userId, serviceGridId, serviceId
					, permitted, now, now
					));
			} else if(r.isPermitted() != permitted){
				r.setPermitted(permitted);
				r.setUpdatedDateTime(now);
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

	public void deleteAccessRight(
			String userGridId, String userId, String serviceGridId, String serviceId)
	throws AccessRightNotFoundException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessRight r = (AccessRight)session.createCriteria(AccessRight.class)
				.add(Property.forName("userGridId").eq(userGridId))
				.add(Property.forName("userId").eq(userId))
				.add(Property.forName("serviceGridId").eq(serviceGridId))
				.add(Property.forName("serviceId").eq(serviceId))
				.uniqueResult();
			if(r != null){
				session.delete(r);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public AccessRight getServiceDefaultAccessRight(
			String serviceGridId, String serviceId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessRight r = doGetAccessRight(
					session, "*", "*", serviceGridId, serviceId);
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public AccessRight setServiceDefaultAccessRight(
			String serviceGridId, String serviceId, boolean permitted
			) throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessRight r = doGetAccessRight(
					session, "*", "*", serviceGridId, serviceId);
			Calendar now = Calendar.getInstance();
			if(r == null){
				r = new AccessRight("*", "*", serviceGridId, serviceId, permitted);
				r.setCreatedDateTime(now);
				r.setUpdatedDateTime(now);
				session.save(r);
			} else{
				r.setPermitted(permitted);
				r.setUpdatedDateTime(now);
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

	public void adjustGridDefaultRights(
			final String serviceGridId, final String serviceId, final boolean permitted)
	throws DaoException{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				session.createSQLQuery(adjustGridDefaultInsertQuery)
						.setString("serviceGridId", serviceGridId)
						.setString("serviceId", serviceId)
						.setBoolean("permitted", !permitted)
						.executeUpdate();
			}
		});
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				session.createSQLQuery(adjustGridDefaultDeleteQuery)
						.setString("serviceGridId", serviceGridId)
						.setString("serviceId", serviceId)
						.setBoolean("permitted", permitted)
						.executeUpdate();
			}
		});
	}

	public AccessRight getGridDefaultAccessRight(
			String userGridId, String serviceGridId, String serviceId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessRight r = doGetAccessRight(
					session, userGridId, "*", serviceGridId, serviceId);
			getContext().commitTransaction();
			return r;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public AccessRight setGridDefaultAccessRight(
			String userGridId, String serviceGridId, String serviceId, boolean permitted
			) throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			AccessRight r = doGetAccessRight(
					session, userGridId, "*", serviceGridId, serviceId);
			Calendar now = Calendar.getInstance();
			if(r == null){
				r = new AccessRight(userGridId, "*", serviceGridId, serviceId, permitted);
				r.setCreatedDateTime(now);
				r.setUpdatedDateTime(now);
				session.save(r);
			} else{
				r.setPermitted(permitted);
				r.setUpdatedDateTime(now);
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

	public void deleteGridDefaultAccessRight(
			final String userGridId, final String serviceGridId, final String serviceId)
	throws AccessRightNotFoundException, DaoException{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				AccessRight r = (AccessRight)session.createCriteria(AccessRight.class)
					.add(Property.forName("userGridId").eq(userGridId))
					.add(Property.forName("userId").eq("*"))
					.add(Property.forName("serviceGridId").eq(serviceGridId))
					.add(Property.forName("serviceId").eq(serviceId))
					.uniqueResult();
				if(r != null){
					session.delete(r);
				}
			}
		});
	}

	public void deleteAccessRightsOfGrid(final String serviceGridId)
	throws DaoException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session, "where serviceGridId=:serviceGridId")
						.setString("serviceGridId", serviceGridId)
						.executeUpdate();
			}
		});
	}

	public void adjustUserRights(
			final String userGridId, final String serviceGridId, final String serviceId, final String ownerUserId
			, final boolean permitted)
	throws DaoException{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				session.createSQLQuery(adjustUserInsertQuery)
						.setString("userGridId", userGridId)
						.setString("serviceGridId", serviceGridId)
						.setString("serviceId", serviceId)
						.setString("ownerUserId", ownerUserId)
						.setBoolean("permitted", !permitted)
						.executeUpdate();
			}
		});
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				session.createSQLQuery(adjustUserDeleteQuery)
						.setString("userGridId", userGridId)
						.setString("serviceGridId", serviceGridId)
						.setString("serviceId", serviceId)
						.setBoolean("permitted", permitted)
						.executeUpdate();
			}
		});
	}

	public void deleteAccessRightsOfService(final String serviceGridId, final String serviceId)
	throws DaoException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session
						, "where serviceGridId=:serviceGridId and serviceId=:serviceId")
						.setString("serviceGridId", serviceGridId)
						.setString("serviceId", serviceId)
						.executeUpdate();
			}
		});
	}

	public void deleteAccessRightsOfUser(final String userGridId, final String userId)
	throws DaoException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session
						, "where userGridId=:userGridId and userId=:userId")
						.setString("userGridId", userGridId)
						.setString("userId", userId)
						.executeUpdate();
			}
		});
	}

	/**
	 * 
	 * 
	 */
	private static void addSearchAccessRightCriterion(
			Criteria c, String userGridId, String userId
			, String serviceGridId, String[] serviceIds)
	{
		if(userGridId.length() > 0 && userId.length() > 0){
			c.add(Property.forName("userGridId").eq(userGridId));
			c.add(Property.forName("userId").eq(userId));
		}
		if(serviceGridId.length() > 0 && serviceIds.length > 0){
			c.add(Property.forName("serviceGridId").eq(serviceGridId));
			Disjunction dj = Restrictions.disjunction();
			for(String id : serviceIds){
				dj.add(Property.forName("serviceId").eq(id));
			}
			c.add(dj);
		}
	}

	private AccessRight doGetAccessRight(
			Session session, String userGridId, String userId
			, String serviceGridId, String serviceId)
	throws HibernateException{
		return (AccessRight)session.createCriteria(AccessRight.class)
			.add(Property.forName("userGridId").eq(userGridId))
			.add(Property.forName("userId").eq(userId))
			.add(Property.forName("serviceGridId").eq(serviceGridId))
			.add(Property.forName("serviceId").eq(serviceId))
			.uniqueResult();
	}

	private static String selectJoinedEntities =
		"select l.ugid, l.uid, l.sgid, l.sid, ar2.permitted, ar.permitted as gdpermitted, l.sdpermitted\n" +
		"  , l.cd, l.ud\n" +
		"  from ((select u.gridid as ugid, u.userid as uid, u.organization as on\n" +
		"    , s.gridid as sgid, s.serviceid as sid, s.serviceName as sn\n" +
		"    , a.permitted as sdpermitted" +
		"    , a.createddatetime as cd, a.updateddatetime as ud\n" +
		"    from users u, service s, accessright a\n" +
		"    where s.gridid=a.servicegridid and s.serviceid=a.serviceid\n" +
		"      and a.usergridid='*' and a.userid='*'\n" +
		"      and u.userid not in (\n" +
		"        select userid from userroles\n" +
		"        where gridid=u.gridid and rolename='langridadmin')\n" +
		"      and u.userid in (\n" +
		"        select userid from userroles\n" +
		"        where gridid=u.gridid and rolename in ('langriduser', 'langridserviceuser'))\n" +
		"      %s %s %s\n" +
		"    ) as l\n" +
		"  left join accessright ar on l.ugid=ar.usergridid and ar.userid='*'\n" +
		"    and l.sgid=ar.servicegridid and l.sid=ar.serviceid" +
		"  )\n" +
		"  left join accessright ar2 on l.ugid=ar2.usergridid and l.uid=ar2.userid\n" +
		"    and l.sgid=ar2.servicegridid and l.sid=ar2.serviceid\n" +
		"  %s\n";
	private static String countJoinedEntities =
		"select count(u.userid)" +
		"  from users u, service s" +
		"  where u.userid not in (" +
		"      select userid from userroles" +
		"      where gridid=u.gridid and rolename='langridadmin')" +
		"    and u.userid in (" +
		"      select userid from userroles" +
		"      where gridid=u.gridid and rolename in ('langriduser', 'langridserviceuser'))" +
		"    %s %s %s";
	private static String adjustGridDefaultInsertQuery =
		"insert into accessright\n" +
		"  select :serviceGridId, :serviceId, g.gridid, '*', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :permitted\n" +
		"  from grid g\n" +
		"  left join accessright a\n" +
		"    on g.gridid=a.usergridid and a.userid='*'\n" +
		"    and a.servicegridid=:serviceGridId and a.serviceid=:serviceId\n" +
		"  where a.permitted is null";
	private static String adjustGridDefaultDeleteQuery =
		"delete from accessright\n" +
		"where\n" +
		"  servicegridid=:serviceGridId and serviceid=:serviceId\n" +
		"  and usergridid<>'*' and userid='*' and permitted=:permitted";
	private static String adjustUserInsertQuery =
		"insert into accessright\n" +
		"  select :serviceGridId, :serviceId, u.gridid, u.userid, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :permitted\n" +
		"  from users u\n" +
		"  left join accessright a\n" +
		"    on u.gridid=a.usergridid and u.userid=a.userid\n" +
		"    and a.servicegridid=:serviceGridId and a.serviceid=:serviceId\n" +
		"  where u.gridId=:userGridId and a.permitted is null\n" +
		"    and u.userid not in (\n" +
		"      select userid from userroles\n" +
		"      where gridid=u.gridid and rolename='langridadmin'\n" +
		"    )\n" +
		"    and u.userid in (\n" +
		"      select userid from userroles\n" +
		"      where gridid=u.gridid and rolename in ('langriduser', 'langridserviceuser')\n" +
		"    )" +
		"    and (u.gridId<>:serviceGridId or u.userid<>:ownerUserId) ";
	private static String adjustUserDeleteQuery =
		"delete from accessright\n" +
		"where\n" +
		"  servicegridid=:serviceGridId and serviceid=:serviceId\n" +
		"  and usergridid=:userGridId and userid<>'*'\n" +
		"  and permitted=:permitted";
	private static Map<String, String> mappedFields = new HashMap<String, String>();
	private static Set<String> capitalIgnoreFields = new HashSet<String>();
	static{
		mappedFields.put("userGridId", "l.ugid");
		mappedFields.put("userId", "l.uid");
		mappedFields.put("organization", "l.on");
		mappedFields.put("serviceGridId", "l.sgid");
		mappedFields.put("serviceId", "l.sid");
		mappedFields.put("serviceName", "l.sn");
		mappedFields.put("createdDateTime", "ar.createdDateTime");
		mappedFields.put("updatedDateTime", "ar.updatedDateTime");
		capitalIgnoreFields.add("l.ugid");
		capitalIgnoreFields.add("l.uid");
		capitalIgnoreFields.add("l.on");
		capitalIgnoreFields.add("l.sgid");
		capitalIgnoreFields.add("l.sid");
		capitalIgnoreFields.add("l.sn");
	}
}
