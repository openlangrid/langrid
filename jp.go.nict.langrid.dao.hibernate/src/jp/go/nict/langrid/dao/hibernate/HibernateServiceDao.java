/*
 * $Id: HibernateServiceDao.java 389 2011-08-24 01:48:28Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.ServiceAlreadyExistsException;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.ServiceSearchResult;
import jp.go.nict.langrid.dao.ServiceStatRanking;
import jp.go.nict.langrid.dao.ServiceStatRankingSearchResult;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServicePK;
import jp.go.nict.langrid.dao.hibernate.searchsupport.AttributeSearchSupport;
import jp.go.nict.langrid.dao.hibernate.searchsupport.SearchSupport;
import jp.go.nict.langrid.dao.hibernate.searchsupport.SearchSupports;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 389 $
 */
public class HibernateServiceDao
extends HibernateCRUDDao<Service>
implements ServiceDao
{
	/**
	 * 
	 * 
	 */
	public HibernateServiceDao(HibernateDaoContext context){
		super(context, Service.class);
	}

	public void clear() throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(Object o : session.createCriteria(BPELService.class).list()){
				session.delete(o);
			}
			for(Object o : session.createCriteria(ExternalService.class).list()){
				session.delete(o);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void clearDetachedInvocations() throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			session
				.createQuery("delete from Invocation where serviceid is null")
				.executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Service> listAllServices(String serviceGridId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<Service> list = session
					.createCriteria(Service.class)
					.add(Property.forName("gridId").eq(serviceGridId))
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
	public List<Service> listServicesOfUser(String userGridId, String userId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			List<Service> list = (List<Service>)
					session.createCriteria(Service.class)
					.add(Property.forName("gridId").eq(userGridId))
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

	@SuppressWarnings("unchecked")
	@Override
	public List<BPELService> listParentServicesOf(String gridId, String serviceId)
	throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Query q = session.createQuery(
					"select distinct parent from BPELService parent, Service child" +
					"  where exists(" +
					"    select i from parent.invocations i" +
					"      where i.serviceGridId=child.gridId and i.serviceId=child.serviceId" +
					"    )" +
					"    and child.gridId=:gridId and child.serviceId=:serviceId");
			q.setString("gridId", gridId);
			q.setString("serviceId", serviceId);
			List<BPELService> list = (List<BPELService>)q.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public ServiceSearchResult searchServices(
			int startIndex, int maxCount
			, String serviceGridId, boolean acrossGrids
			, MatchingCondition[] conditions, Order[] orders)
		throws DaoException
	{
		return doSearchServices(
				startIndex, maxCount, serviceGridId, acrossGrids
				, conditions, orders, "visible=true"
				);
	}

	public ServiceStatRankingSearchResult searchServiceStatRanking(
			int startIndex, int maxCount
			, String serviceGridId, String nodeId, boolean acrossGrids
			, MatchingCondition[] conditions, int sinceDays, Order[] orders)
		throws DaoException
	{
		return doSearchServiceStatRanking(
				startIndex, maxCount, serviceGridId, nodeId, acrossGrids
				, conditions, orders, sinceDays, "visible=true"
				);
	}

	public void addService(Service service)
	throws DaoException, ServiceAlreadyExistsException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			String gid = service.getGridId();
			String sid = service.getServiceId();
			Service current = (Service)session.get(Service.class
					, new ServicePK(gid, sid));
			if(current != null){
				getContext().commitTransaction();
				throw new ServiceAlreadyExistsException(gid, sid);
			} else{
				session.save(service);
				getContext().commitTransaction();
			}
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteService(String serviceGridId, String serviceId)
	throws ServiceNotFoundException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Service current = (Service)session.get(Service.class
					, new ServicePK(serviceGridId, serviceId));
			if(current == null){
				getContext().commitTransaction();
				throw new ServiceNotFoundException(serviceGridId, serviceId);
			}
			session.delete(current);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteServicesOfGrid(final String gridId) throws DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				for(Service s : listAllServices(gridId)){
					session.delete(s);
				}
			}
		});
	}

	public void deleteServicesOfUser(String userGridId, String userId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			for(Service s : listServicesOfUser(userGridId, userId)){
				session.delete(s);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public Service getService(String serviceGridId, String serviceId)
	throws ServiceNotFoundException, DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Service current = (Service)session.get(Service.class
					, new ServicePK(serviceGridId, serviceId));
			getContext().commitTransaction();
			if(current == null){
				throw new ServiceNotFoundException(serviceGridId, serviceId);
			}
			return current;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public boolean isServiceExist(String serviceGridId, String serviceId)
	throws DaoException
	{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			boolean b = session.get(Service.class
					, new ServicePK(serviceGridId, serviceId)) != null;
			getContext().commitTransaction();
			return b;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			return false;
		}
	}

	public InputStream getServiceInstance(String serviceGridId, String serviceId)
		throws DaoException, ServiceNotFoundException
	{
		Session session = getSession();
		try{
			Service current = (Service)session.get(Service.class
					, new ServicePK(serviceGridId, serviceId));
			if(current == null){
				throw new ServiceNotFoundException(serviceGridId, serviceId);
			}
			evictService(current);
			Blob s = current.getInstance();
			if(s != null) return s.getBinaryStream();
			else return null;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			throw new DaoException(e);
		} catch(SQLException e){
			throw new DaoException(e);
		}
	}

	public InputStream getServiceWsdl(String serviceGridId, String serviceId)
		throws DaoException, ServiceNotFoundException
	{
		Session session = getSession();
		try{
			Service current = (Service)session.get(Service.class
					, new ServicePK(serviceGridId, serviceId));
			if(current == null){
				throw new ServiceNotFoundException(serviceGridId, serviceId);
			}
			evictService(current);
			Blob wsdl = current.getWsdl();
			if(wsdl != null){
				return wsdl.getBinaryStream();
			} else{
				return null;
			}
		} catch(HibernateException e){
			logAdditionalInfo(e);
			throw new DaoException(e);
		} catch(SQLException e){
			throw new DaoException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public void evictService(Service service)
		throws DaoException
	{
		Session session = getSession();
		try{
			session.evict(service);
		} catch(HibernateException e){
			logAdditionalInfo(e);
			throw new DaoException(e);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private ServiceSearchResult doSearchServices(
			int startIndex, int maxCount
			, String serviceGridId, boolean acrossGrids
			, MatchingCondition[] conditions, Order[] orders
			, String... additionalConditions)
		throws DaoException
	{
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"updatedDateTime", OrderDirection.DESCENDANT
					)};
		}

		
		Map<String, SearchSupport<Service>> conditionBuilders = 
			SearchSupports.getServiceSearchSupports();
		SearchSupport<Service> defaultConditionBuilder = new AttributeSearchSupport<Service>();
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
					session
					, conditionBuilders, defaultConditionBuilder
					, serviceGridId, acrossGrids
					, conditions, orders
					, additionalConditions
					);
			if(filters.size() > 0){
				// クエリで検索してフィルタで絞り込み
				List<Service> services = (List<Service>)query.list();
	
				for(Pair<MatchingCondition, SearchSupport> f : filters){
					f.getSecond().filterResults(f.getFirst(), services);
				}

				int totalCount = services.size();
				boolean totalCountFixed = false;
				if(services.size() <= (maxCount + startIndex)){
					totalCountFixed = true;
				}

				getContext().commitTransaction();
				return new ServiceSearchResult(
						services.subList(startIndex, Math.min(startIndex + maxCount, totalCount))
						.toArray(new Service[]{})
						, totalCount, totalCountFixed
						);
			} else{
				// query search only
				List<Service> services = (List<Service>)query
					.setFirstResult(startIndex)
					.setMaxResults(maxCount)
					.list();

				long totalCount = 0;
				if(services.size() < maxCount){
					totalCount = services.size() + startIndex;
				} else{
					totalCount = (Long)QueryUtil.buildRowCountQueryWithSearchSupports(
							session, Service.class
							, conditionBuilders, defaultConditionBuilder
							, serviceGridId, acrossGrids, conditions, additionalConditions
							).uniqueResult();
				}

				getContext().commitTransaction();
				return new ServiceSearchResult(
						services.toArray(new Service[]{})
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

	@SuppressWarnings({"unchecked", "rawtypes"})
	private ServiceStatRankingSearchResult doSearchServiceStatRanking(
			int startIndex, int maxCount
			, String serviceGridId, String nodeId, boolean acrossGrids
			, MatchingCondition[] conditions, Order[] orders
			, int sinceDays, String... additionalConditions)
		throws DaoException
	{
		if(orders.length == 0){
			orders = new Order[]{new Order(
					"updatedDateTime", OrderDirection.DESCENDANT
					)};
		}

		Map<String, SearchSupport<Service>> conditionBuilders = 
			SearchSupports.getServiceSearchSupports();
		SearchSupport<Service> defaultConditionBuilder = new AttributeSearchSupport<Service>();
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
			Query query = QueryUtil.buildServiceRankingSearchQueryWithSearchSupports(
					session, conditionBuilders, defaultConditionBuilder
					, serviceGridId, nodeId, acrossGrids
					, conditions, orders, sinceDays
					, additionalConditions
					);
			if(filters.size() > 0){
				// apply filters
				final List<Object[]> result = (List<Object[]>)query.list();

				// adapter
				List<Service> services = new ArrayList<Service>(){
					private static final long serialVersionUID = 1L;
					@Override
					public Iterator<Service> iterator() {
						final ListIterator orig = result.listIterator();
						return new Iterator<Service>(){
							@Override
							public boolean hasNext() {
								return orig.hasNext();
							}
							@Override
							public Service next() {
								Object[] r = (Object[])orig.next();
								try{
									return getService((String)r[0], (String)r[1]);
								} catch(DaoException e){
									throw new RuntimeException(e);
								}
							}
							@Override
							public void remove() {
								orig.remove();
							}
						};
					}
				};
				for(Pair<MatchingCondition, SearchSupport> f : filters){
					f.getSecond().filterResults(f.getFirst(), services);
				}

				int totalCount = result.size();
				boolean totalCountFixed = false;
				if(totalCount <= (maxCount + startIndex)){
					totalCountFixed = true;
				}

				getContext().commitTransaction();
				return new ServiceStatRankingSearchResult(ArrayUtil.collect(
						result.subList(startIndex, Math.min(startIndex + maxCount, totalCount)).toArray()
						, ServiceStatRanking.class
						, new Transformer<Object, ServiceStatRanking>() {
							public ServiceStatRanking transform(Object value) throws TransformationException {
								Object[] v = (Object[])value;
								return new ServiceStatRanking(
										(String)v[0], (String)v[1], (String)v[2]
										, (Long)v[3], (Long)v[4], (Long)v[5]
										, (Long)v[6], (Double)v[7]);
							};
						})
						, totalCount, totalCountFixed
						);
			} else{
				// query search only
				List<Service> services = (List<Service>)query
					.setFirstResult(startIndex)
					.setMaxResults(maxCount)
					.list();

				long totalCount = 0;
				if(services.size() < maxCount){
					totalCount = services.size() + startIndex;
				} else{
					totalCount = (Long)QueryUtil.buildRowCountQueryWithSearchSupports(
							session, Service.class, SearchSupports.getServiceSearchSupports()
							, new AttributeSearchSupport<Service>()
							, serviceGridId, acrossGrids, conditions, additionalConditions
							).uniqueResult();
				}

				getContext().commitTransaction();
				return new ServiceStatRankingSearchResult(ArrayUtil.collect(
						services.toArray()
						, ServiceStatRanking.class
						, new Transformer<Object, ServiceStatRanking>() {
							public ServiceStatRanking transform(Object value) throws TransformationException {
								Object[] v = (Object[])value;
								return new ServiceStatRanking(
										(String)v[0], (String)v[1], (String)v[2]
										, (Long)v[3], (Long)v[4], (Long)v[5]
										, (Long)v[6], (Double)v[7]);
							};
						})
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
}
