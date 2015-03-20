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

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.TemporaryUserDao;
import jp.go.nict.langrid.dao.TemporaryUserSearchResult;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.TemporaryUser;
import jp.go.nict.langrid.dao.entity.TemporaryUserPK;
import jp.go.nict.langrid.dao.hibernate.searchsupport.SearchSupport;
import jp.go.nict.langrid.dao.hibernate.searchsupport.StringSearchSupport;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateTemporaryUserDao
extends HibernateCRUDDao<TemporaryUser>
implements TemporaryUserDao
{
	/**
	 * 
	 * 
	 */
	public HibernateTemporaryUserDao(HibernateDaoContext context){
		super(context, TemporaryUser.class);
		this.context = context;
	}

	public void clear() throws DaoException{
		Session session = getSession();
		context.beginTransaction();
		try{
			session.createQuery("delete from TemporaryUser").executeUpdate();
			context.commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void clearExpiredUsers() throws DaoException{
		Session session = getSession();
		context.beginTransaction();
		try{
			session.createQuery(
					"delete from TemporaryUser where endAvailableDateTime < current_timestamp()"
					).executeUpdate();
			context.commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<TemporaryUser> listAllUsers(String userGridId)
		throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<TemporaryUser> list = session
					.createCriteria(TemporaryUser.class)
					.add(Property.forName("gridId").eq(userGridId))
					.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public TemporaryUserSearchResult searchUsers(
			int startIndex, int maxCount, String userGridId, String parentUserId
			, MatchingCondition[] conditions, Order[] orders)
		throws DaoException
	{
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"beginAvailableDateTime", OrderDirection.ASCENDANT
					)};
		}

		conditions = ArrayUtil.append(conditions, new MatchingCondition(
				"parentUserId", parentUserId, MatchingMethod.COMPLETE
				));
		return searchUsers(
				getSession(), startIndex, maxCount
				, userGridId, conditions, orders
				);
	}

	public TemporaryUserSearchResult searchUsers(
			int startIndex, int maxCount, String userGridId
			, MatchingCondition[] conditions, Order[] orders)
		throws DaoException
	{
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"beginAvailableDateTime", OrderDirection.ASCENDANT
					)};
		}

		if(conditions.length == 0){
			return listUsers(startIndex, maxCount, userGridId, orders);
		} else{
			return searchUsers(
					getSession(), startIndex, maxCount
					, userGridId, conditions, orders
					);
		}
	}

	public boolean isUserExists(String userGridId, String userId)
	throws DaoException
	{
		Session session = getSession();
		context.beginTransaction();
		try{
			TemporaryUser u = findUser(session, userGridId, userId);
			context.commitTransaction();
			return u != null;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public String getParentUserIdIfUserAvailable(
			String userGridId, String userId, String password)
	throws DaoException
	{
		Session session = getSession();
		context.beginTransaction();
		try{
			//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
			Calendar now = CalendarUtil.toDefaultTimeZone(Calendar.getInstance());
			String parentUserId = (String)session.createCriteria(TemporaryUser.class)
				.setProjection(Projections.property("parentUserId"))
				.add(Property.forName("gridId").eq(userGridId))
				.add(Property.forName("userId").eq(userId))
				.add(Property.forName("password").eq(password))
				.add(Property.forName("beginAvailableDateTime").le(now))
				.add(Property.forName("endAvailableDateTime").gt(now))
				.uniqueResult();
			context.commitTransaction();
			return parentUserId;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public boolean isParent(String userGridId, String parentUserId, String userId)
		throws DaoException
	{
		Session session = getSession();
		context.beginTransaction();
		try{
			Long result = (Long)session.getNamedQuery("TemporaryUser.isParent")
					.setString("gridId", userGridId)
					.setString("parentUserId", parentUserId)
					.setString("userId", userId)
					.uniqueResult();
			context.commitTransaction();
			return result > 0;
		} catch(MappingException e){
			throw new DaoException(e);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			return false;
		}
	}

	public void addUser(TemporaryUser user)
		throws DaoException, UserAlreadyExistsException
	{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		if(user.getBeginAvailableDateTime() != null)
			user.setBeginAvailableDateTime(CalendarUtil.toDefaultTimeZone(user.getBeginAvailableDateTime()));
		if(user.getEndAvailableDateTime() != null)
			user.setEndAvailableDateTime(CalendarUtil.toDefaultTimeZone(user.getEndAvailableDateTime()));

		Session session = getSession();
		context.beginTransaction();
		try{
			String gid = user.getGridId();
			String uid = user.getUserId();
			if(findUser(session, gid, uid) != null){
				context.commitTransaction();
				throw new UserAlreadyExistsException(gid, uid);
			} else{
				session.save(user);
				context.commitTransaction();
			}
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void setAvailableDateTime(
			TemporaryUser user, Calendar beginAvailableDateTime
			, Calendar endAvailableDateTime)
	throws DaoException
	{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		beginAvailableDateTime = CalendarUtil.toDefaultTimeZone(beginAvailableDateTime);
		endAvailableDateTime = CalendarUtil.toDefaultTimeZone(endAvailableDateTime);
		user.setBeginAvailableDateTime(beginAvailableDateTime);
		user.setEndAvailableDateTime(endAvailableDateTime);
		user.touchUpdatedDateTime();
	}

	public void deleteUser(String userGridId, String userId)
		throws DaoException, UserNotFoundException
	{
		Session session = getSession();
		context.beginTransaction();
		try{
			TemporaryUser u = findUser(session, userGridId, userId);
			if(u == null){
				context.commitTransaction();
				throw new UserNotFoundException(userGridId, userId);
			}
			session.delete(u);
			session.createQuery(
					"delete from UserRole where gridId=:gridId and userId=:userId"
					)
					.setString("gridId", userGridId)
					.setString("userId", userId)
					.executeUpdate();
			context.commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			throw e;
		}
	}

	@Override
	public void deleteUsersOfGrid(final String userGridId) throws DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				createDeleteQuery(session, "where gridId=:gridId")
						.setString("gridId", userGridId)
						.executeUpdate();
			}
		});
	}

	public TemporaryUser getUser(String userGridId, String userId)
		throws DaoException, UserNotFoundException
	{
		Session session = getSession();
		context.beginTransaction();
		try{
			TemporaryUser u = findUser(session, userGridId, userId);
			context.commitTransaction();
			if(u == null){
				throw new UserNotFoundException(userGridId, userId);
			}
			return u;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			throw new DaoException(e);
		}
	}

	private TemporaryUser findUser(Session session, String userGridId, String userId){
		return (TemporaryUser)session.get(TemporaryUser.class
				, new TemporaryUserPK(userGridId, userId));
	}

	@SuppressWarnings("unchecked")
	private TemporaryUserSearchResult listUsers(
			int startIndex, int maxCount, String userGridId, Order[] orders)
	throws DaoException{
		Session session = getSession();
		context.beginTransaction();
		try{
			List<TemporaryUser> users = (List<TemporaryUser>)CriteriaUtil.getList(
					session.createCriteria(TemporaryUser.class)
							.add(Property.forName("gridId").eq(userGridId))
					, startIndex, maxCount, orders
					);
			int totalCount = 0;
			if(users.size() < maxCount){
				totalCount = users.size() + startIndex;
			} else{
				totalCount = CriteriaUtil.getCount(
						session.createCriteria(TemporaryUser.class)
							.add(Property.forName("gridId").eq(userGridId))
						);
			}
			context.commitTransaction();
			return new TemporaryUserSearchResult(
					users.toArray(new TemporaryUser[]{})
					, totalCount
					, true
					);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private TemporaryUserSearchResult searchUsers(
			Session session, int startIndex, int maxCount
			, String userGridId, MatchingCondition[] conditions, Order[] orders)
	throws DaoException, HibernateException{
		assert(conditions.length > 0);

		context.beginTransaction();
		try{
			Query query = QueryUtil.buildSearchQueryWithSearchSupports(
					session, TemporaryUser.class
					, new HashMap<String, SearchSupport<TemporaryUser>>()
					, new StringSearchSupport<TemporaryUser>()
					, conditions, orders);
			List<TemporaryUser> users = (List<TemporaryUser>)query
				.setFirstResult(startIndex)
				.setMaxResults(maxCount)
				.list();
			long totalCount = 0;
			if(users.size() < maxCount){
				totalCount = users.size() + startIndex;
			} else{
				totalCount = (Long)QueryUtil.buildRowCountQueryWithSearchSupports(
						session, TemporaryUser.class
						, searchSupports, defaultSearchSupport
						, userGridId, false, conditions
						).uniqueResult();
			}
			context.commitTransaction();
			return new TemporaryUserSearchResult(
					users.toArray(new TemporaryUser[]{})
					, (int)totalCount
					, true
					);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			context.rollbackTransaction();
			throw e;
		} catch(RuntimeException e){
			context.rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			context.rollbackTransaction();
			throw new DaoException(e);
		}
	}

	private DaoContext context;

	private static final Map<String, SearchSupport<TemporaryUser>> searchSupports
		= new HashMap<String, SearchSupport<TemporaryUser>>();
	private static final SearchSupport<TemporaryUser> defaultSearchSupport
		= new StringSearchSupport<TemporaryUser>();


	private static Map<String, Class<?>> userFields
			= new HashMap<String, Class<?>>();
	static{
		for(Field f : TemporaryUser.class.getDeclaredFields()){
			userFields.put(f.getName(), f.getType());
		}
	}
}
