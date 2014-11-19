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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Embeddable;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.ResourceAlreadyExistsException;
import jp.go.nict.langrid.dao.ResourceDao;
import jp.go.nict.langrid.dao.ResourceNotFoundException;
import jp.go.nict.langrid.dao.ResourceSearchResult;
import jp.go.nict.langrid.dao.entity.OldResourceType;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourcePK;
import jp.go.nict.langrid.dao.hibernate.searchsupport.AttributeSearchSupport;
import jp.go.nict.langrid.dao.hibernate.searchsupport.EnumSearchSupport;
import jp.go.nict.langrid.dao.hibernate.searchsupport.ResourceBooleanSearchSupport;
import jp.go.nict.langrid.dao.hibernate.searchsupport.SearchSupport;
import jp.go.nict.langrid.dao.hibernate.searchsupport.StringSearchSupport;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 389 $
 */
public class HibernateResourceDao
extends HibernateCRUDDao<Resource>
implements ResourceDao
{
	/**
	 * 
	 * 
	 */
	public HibernateResourceDao(HibernateDaoContext context){
		super(context, Resource.class);
	}

	public void clear() throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(Object o : session.createCriteria(Resource.class).list()){
				session.delete(o);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Resource> listAllResources(String resourceGridId)
		throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<Resource> list = session
					.createCriteria(Resource.class)
					.add(Property.forName("gridId").eq(resourceGridId))
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
	public List<Resource> listResourcesOfUser(String resourceGridId, String userId)
		throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<Resource> list = (List<Resource>)
					session.createCriteria(Resource.class)
					.add(Property.forName("gridId").eq(resourceGridId))
					.add(Property.forName("ownerUserId").eq(userId))
					.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public ResourceSearchResult searchResources(
			int startIndex, int maxCount, String resourceGridId
			, MatchingCondition[] conditions, Order[] orders)
		throws DaoException
	{
		return doSearchResources(
				startIndex, maxCount, resourceGridId, conditions, orders
				, "visible=true"
				);
	}

	public void addResource(Resource resource)
	throws DaoException, ResourceAlreadyExistsException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			String gid = resource.getGridId();
			String rid = resource.getResourceId();
			Resource current = (Resource)session.get(Resource.class
					, new ResourcePK(gid, rid));
			if(current != null){
				getContext().commitTransaction();
				throw new ResourceAlreadyExistsException(gid, rid);
			} else{
				session.save(resource);
				getContext().commitTransaction();
			}
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteResource(String resourceGridId, String resourceId)
	throws ResourceNotFoundException, DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Resource current = (Resource)session.get(Resource.class
					, new ResourcePK(resourceGridId, resourceId));
			if(current == null){
				getContext().commitTransaction();
				throw new ResourceNotFoundException(resourceGridId, resourceId);
			}
			session.delete(current);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteResourcesOfGrid(final String gridId)
	throws DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				session.createQuery(
						"delete from ResourceAttribute where gridId=:gridId")
						.setString("gridId", gridId)
						.executeUpdate();
				createDeleteQuery(session, "where gridId=:gridId")
						.setString("gridId", gridId)
						.executeUpdate();
			}
		});
	}

	public void deleteResourcesOfUser(String userGridId, String userId)
	throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(Resource s : listResourcesOfUser(userGridId, userId)){
				session.delete(s);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public Resource getResource(String resourceGridId, String resourceId)
	throws ResourceNotFoundException, DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Resource current = (Resource)session.get(Resource.class
					, new ResourcePK(resourceGridId, resourceId));
			getContext().commitTransaction();
			if(current == null){
				throw new ResourceNotFoundException(resourceGridId, resourceId);
			}
			return current;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public boolean isResourceExist(String resourceGridId, String resourceId)
	throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			boolean b = session.get(Resource.class
					, new ResourcePK(resourceGridId, resourceId)) != null;
			getContext().commitTransaction();
			return b;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private ResourceSearchResult doSearchResources(
			int startIndex, int maxCount, String resourceGridId
			, MatchingCondition[] conditions, Order[] orders
			, String... additionalConditions)
		throws DaoException
	{
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"updatedDateTime", OrderDirection.DESCENDANT
					)};
		}

		// filterの準備
		List<Pair<MatchingCondition, SearchSupport>> filters
				= new ArrayList<Pair<MatchingCondition, SearchSupport>>();
		for(MatchingCondition c : conditions){
			SearchSupport b = conditionBuilders.get(c.getFieldName());
			if(b != null && b.isFileteringNeeded()){
				filters.add(Pair.create(c, b));
			}
		}

		Session session = getSession();
		getContext().beginTransaction();
		try{
			Query query = QueryUtil.buildSearchQueryWithSearchSupports(
					session, Resource.class
					, conditionBuilders, defaultConditionBuilder
					, conditions, orders
					, additionalConditions
					);
			if(filters.size() > 0){
				// クエリで検索してフィルタで絞り込み
				List<Resource> resources = (List<Resource>)query.list();
	
				for(Pair<MatchingCondition, SearchSupport> f : filters){
					f.getSecond().filterResults(f.getFirst(), resources);
				}

				int totalCount = resources.size();
				boolean totalCountFixed = false;
				if(resources.size() <= (maxCount + startIndex)){
					totalCountFixed = true;
				}

				getContext().commitTransaction();
				return new ResourceSearchResult(
						resources.subList(startIndex, Math.min(startIndex + maxCount, totalCount))
						.toArray(new Resource[]{})
						, totalCount, totalCountFixed
						);
			} else{
				// クエリ検索のみ
				List<Resource> resources = (List<Resource>)query
					.setFirstResult(startIndex)
					.setMaxResults(maxCount)
					.list();

				long totalCount = 0;
				if(resources.size() < maxCount){
					totalCount = resources.size() + startIndex;
				} else{
					totalCount = (Long)QueryUtil.buildRowCountQueryWithSearchSupports(
							session, Resource.class
							, conditionBuilders, defaultConditionBuilder
							, resourceGridId, false, conditions, additionalConditions
							).uniqueResult();
				}

				getContext().commitTransaction();
				return new ResourceSearchResult(
						resources.toArray(new Resource[]{})
						, (int)totalCount, true
						);
			}
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

	private static Map<String, Class<?>> resourceFields
		= new HashMap<String, Class<?>>();
	private static Map<String, SearchSupport<Resource>> conditionBuilders
		= new HashMap<String, SearchSupport<Resource>>();
	private static SearchSupport<Resource> defaultConditionBuilder
		= new AttributeSearchSupport<Resource>();

	@SuppressWarnings("unchecked")
	private static EnumSearchSupport<Resource, ? extends Enum<?>> create(
			Field f){
		return new EnumSearchSupport<Resource, OldResourceType>(
				(Class<OldResourceType>)f.getType()
				);
	}
	static{
		for(Field f : Resource.class.getDeclaredFields()){
			resourceFields.put(f.getName(), f.getType());
		}
		for(Field f : Resource.class.getDeclaredFields()){
			if(Collection.class.isAssignableFrom(f.getType())) continue;
			if(Enum.class.isAssignableFrom(f.getType())){
				conditionBuilders.put(f.getName(), create(f));
				continue;
			}
			if(f.getType().equals(boolean.class)){
				conditionBuilders.put(
						f.getName()
						, new ResourceBooleanSearchSupport<Resource>());
				continue;
			}
			boolean needStringValueSuffix
				= f.getType().getAnnotation(Embeddable.class) != null;
			conditionBuilders.put(
					f.getName()
					, new StringSearchSupport<Resource>(
							needStringValueSuffix ? ".stringValue" : ""
							)
					);
		}
	}
}
