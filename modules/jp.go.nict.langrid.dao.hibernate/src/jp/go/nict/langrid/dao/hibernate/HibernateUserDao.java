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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.UserSearchResult;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserPK;
import jp.go.nict.langrid.dao.entity.UserRole;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateUserDao
extends HibernateCRUDDao<User>
implements UserDao
{
	/**
	 * 
	 * 
	 */
	public HibernateUserDao(HibernateDaoContext context){
		super(context, User.class);
	}

	public void clear() throws DaoException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				session.createQuery("delete from UserAttribute").executeUpdate();
				session.createQuery("delete from UserRole").executeUpdate();
				HibernateUserDao.super.clear();
			}
		});
	}

	public void clearExceptAdmins() throws DaoException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				Set<String> adminUsers = new HashSet<String>();
				for(Object o : session.createCriteria(UserRole.class)
						.add(Property.forName("roleName").in(
								new String[]{"langridadmin", "manager"}
								)).list()){
					UserRole r = (UserRole)o;
					adminUsers.add(r.getUserId());
				}
				for(Object o : session.createCriteria(UserRole.class).list()){
					UserRole r = (UserRole)o;
					if(!adminUsers.contains(r.getUserId())){
						session.delete(o);
					}
				}
				for(Object o : session.createCriteria(User.class).list()){
					User u = (User)o;
					if(!adminUsers.contains(u.getUserId())){
						session.delete(o);
					}
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<User> dumpAllUsers(String userGridId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<User> list = session
					.createCriteria(User.class)
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

	@SuppressWarnings("unchecked")
	public List<User> listAllUsers(String userGridId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<User> list = session
					.createCriteria(User.class)
					.add(Property.forName("gridId").eq(userGridId))
					.add(Property.forName("visible").eq(true))
					.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public UserSearchResult searchUsers(
			int startIndex, int maxCount, String userGridId
			, MatchingCondition[] conditions, Order[] orders)
	throws DaoException{
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"updatedDateTime", OrderDirection.DESCENDANT
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

	public boolean isUserExist(String userGridId, String userId)
	throws DaoException{
		return super.exists(new UserPK(userGridId, userId));
	}

	public void addUser(final User user, final String... userRoles)
	throws DaoException, UserAlreadyExistsException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				String gid = user.getGridId();
				String uid = user.getUserId();
				if(findUser(session, gid, uid) != null){
					throw new UserAlreadyExistsException(gid, uid);
				} else{
					for(String r : userRoles){
						user.getRoles().add(new UserRole(user.getGridId(), user.getUserId(), r));
					}
					session.save(user);
				}
			}
		});
	}

	public void deleteUsersOfGrid(final String userGridId)
	throws DaoException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				for(User u : listAllUsers(userGridId)){
					session.delete(u);
				}
			}
		});
	}

	public void deleteUser(final String userGridId, final String userId)
	throws DaoException, UserNotFoundException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				User u = findUser(session, userGridId, userId);
				if(u == null){
					throw new UserNotFoundException(userGridId, userId);
				}
				session.delete(u);
			}
		});
	}

	public User getUser(String userGridId, String userId)
	throws DaoException, UserNotFoundException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			User u = findUser(session, userGridId, userId);
			getContext().commitTransaction();
			if(u == null){
				throw new UserNotFoundException(userGridId, userId);
			}
			return u;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public List<User> getUsersByEmail(String userGridId, String email)
	throws DaoException, UserNotFoundException{
		return transact(new DaoBlockR<List<User>>(){
			@SuppressWarnings("unchecked")
			@Override
			public List<User> execute(Session session) throws DaoException {
				return (List<User>)CriteriaUtil.getList(
						session.createCriteria(User.class)
								.add(Property.forName("gridId").eq(userGridId))
								.add(Property.forName("emailAddress").eq(email))
						, 0, 1, new Order[]{}
						);
			}
		});
	}

	public boolean hasUserRole(final String userGridId, final String userId, final String role)
	throws DaoException{
		return transact(new DaoBlockR<Boolean>(){
			@Override
			public Boolean execute(Session session) throws DaoException {
				int c = (Integer)session.createCriteria(UserRole.class)
						.setProjection(Projections.count("userId"))
						.add(Property.forName("gridId").eq(userGridId))
						.add(Property.forName("userId").eq(userId))
						.add(Property.forName("roleName").eq(role))
						.uniqueResult();
				return c > 0;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public UserSearchResult searchUsersShouldChangePassword(
			final int startIndex, final int maxCount, final String userGridId
			, Calendar dateTime, Order[] orders) throws DaoException
	{
		//## hibernate-postgresql対策。タイムゾーンをシステムデフォルトに合わせる
		final Calendar dt = CalendarUtil.toDefaultTimeZone(dateTime);
		final Order[] o = orders;

		return transact(new DaoBlockR<UserSearchResult>(){
			@Override
			public UserSearchResult execute(Session session) throws DaoException {
				List<User> users = session
						.createQuery(
							selectClause + fromAndWhereClause
							+ QueryUtil.buildOrderByQuery(User.class, o)
						)
						.setString("gridId", userGridId)
						.setCalendar("dateTime", dt)
						.setFirstResult(startIndex)
						.setMaxResults(maxCount)
						.list();
				long totalCount = 0;
				if(users.size() < maxCount){
					totalCount = users.size() + startIndex;
				} else{
					totalCount = (Long)session.createQuery(
							countClause + fromAndWhereClause
							)
							.setString("gridId", userGridId)
							.setCalendar("dateTime", dt)
							.uniqueResult();
				}
				return new UserSearchResult(
						users.toArray(new User[]{})
						, (int)totalCount
						, true
						);
			}
		});
	}

	@Override
	public List<Pair<String, Calendar>> listAllUserIdAndUpdates(String userGridId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			@SuppressWarnings("unchecked")
			List<Object[]> list = session
					.createCriteria(User.class)
					.setProjection(Projections.projectionList().add(
							Projections.property("userId")).add(
							Projections.property("updatedDateTime")))
					.add(Property.forName("gridId").eq(userGridId))
					.add(Property.forName("visible").eq(true))
					.addOrder(Property.forName("updatedDateTime").asc())
					.list();
			getContext().commitTransaction();
			List<Pair<String, Calendar>> ret = new ArrayList<>();
			for(Object[] r : list){
				ret.add(Pair.create((String)r[0], (Calendar)r[1]));
			}
			return ret;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	private User findUser(Session session, String userGridId, String userId){
		return (User)session.get(User.class
				, new UserPK(userGridId, userId));
	}

	@SuppressWarnings("unchecked")
	private UserSearchResult listUsers(
			final int startIndex, final int maxCount
			, final String userGridId, final Order[] orders)
	throws DaoException{
		return transact(new DaoBlockR<UserSearchResult>(){
			@Override
			public UserSearchResult execute(Session session)
					throws DaoException {
				List<User> users = (List<User>)CriteriaUtil.getList(
						session.createCriteria(User.class)
								.add(Property.forName("gridId").eq(userGridId))
								.add(Property.forName("visible").eq(true))
						, startIndex, maxCount, orders
						);
				int totalCount = 0;
				if(users.size() < maxCount){
					totalCount = users.size() + startIndex;
				} else{
					totalCount = CriteriaUtil.getCount(
							session.createCriteria(User.class)
							.add(Property.forName("gridId").eq(userGridId))
							.add(Property.forName("visible").eq(true))
							);
				}
				return new UserSearchResult(
						users.toArray(new User[]{})
						, totalCount
						, true
						);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private UserSearchResult searchUsers(
			Session session, int startIndex, int maxCount
			, String userGridId, MatchingCondition[] conditions
			, Order[] orders)
	throws DaoException, HibernateException
	{
		assert(conditions.length > 0);
		conditions = ArrayUtil.append(
				conditions, ArrayUtil.array(
						new MatchingCondition("gridId", userGridId)
						, new MatchingCondition("visible", true)
						));

		getContext().beginTransaction();
		try{
			Query query = QueryUtil.buildSearchQuery(
					session, User.class, userFields
					, conditions, orders);
			List<User> users = (List<User>)query
				.setFirstResult(startIndex)
				.setMaxResults(maxCount)
				.list();
			long totalCount = 0;
			if(users.size() < maxCount){
				totalCount = users.size() + startIndex;
			} else{
				totalCount = (Long)QueryUtil.buildRowCountQuery(
						session, User.class, userFields, conditions
						).uniqueResult();
			}
			getContext().commitTransaction();
			return new UserSearchResult(
					users.toArray(new User[]{})
					, (int)totalCount
					, true
					);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw e;
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	private static final String selectClause = "select this_";
	private static final String countClause = "select count(this_)";
	private static final String fromAndWhereClause =
		" from User as this_ where this_.gridId=:gridId" +
		" and this_.passwordChangedDate < :dateTime";

	private static Map<String, Class<?>> userFields
			= new HashMap<String, Class<?>>();
	static{
		for(Field f : User.class.getDeclaredFields()){
			userFields.put(f.getName(), f.getType());
		}
	}
}
