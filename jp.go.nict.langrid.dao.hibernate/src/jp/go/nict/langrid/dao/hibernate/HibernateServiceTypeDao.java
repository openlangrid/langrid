/*
 * $Id:HibernateServiceTypeDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import jp.go.nict.langrid.dao.ServiceMetaAttributeAlreadyExistsException;
import jp.go.nict.langrid.dao.ServiceMetaAttributeNotFoundException;
import jp.go.nict.langrid.dao.ServiceTypeAlreadyExistsException;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.ServiceTypeNotFoundException;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttributePK;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.ServiceTypePK;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1239 $
 */
public class HibernateServiceTypeDao
extends HibernateCRUDDao<ServiceType>
implements ServiceTypeDao{
	/**
	 * 
	 * 
	 */
	public HibernateServiceTypeDao(HibernateDaoContext context){
		super(context, ServiceType.class);
	}

	@Override
	public void clear() throws DaoException {
		deleteEntities(ServiceType.class);
		deleteEntities(ServiceMetaAttribute.class);
	}

	@Override
	public List<ServiceType> listAllServiceTypes() throws DaoException {
		return list();
	}

	@Override
	public List<ServiceType> listAllServiceTypes(final String domainId)
			throws DaoException {
		return transact(new DaoBlockR<List<ServiceType>>(){
			@Override
			@SuppressWarnings("unchecked")
			public List<ServiceType> execute(Session session)
					throws DaoException {
				return (List<ServiceType>)session.createCriteria(ServiceType.class)
				.add(Property.forName("domainId").eq(domainId))
				.addOrder(Order.asc("serviceTypeName"))
				.list();
			}
		});
	}

	public void addServiceType(final ServiceType serviceType)
	throws ServiceTypeAlreadyExistsException, DaoException{
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				String dId = serviceType.getDomainId();
				String stId = serviceType.getServiceTypeId();
				ServiceType current = (ServiceType)session.get(ServiceType.class, new ServiceTypePK(dId, stId));
				if(current != null){
					throw new ServiceTypeAlreadyExistsException(dId, stId);
				} else{
					session.save(serviceType);
				}
			}
		});
	}

	public void deleteServiceType(final String domainId, final String serviceTypeId)
	throws ServiceTypeNotFoundException, DaoException{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
/*				session.createQuery("delete from ServiceInterfaceDefinition s where s.serviceType.domainId=:domainId"
						+ " and s.serviceType.serviceTypeId=:serviceTypeId")
						.setString("domainId", domainId)
						.setString("serviceTypeId", serviceTypeId)
						.executeUpdate();
*/				ServiceType current = (ServiceType)session.get(
						ServiceType.class, new ServiceTypePK(domainId, serviceTypeId));
				if(current == null){
					throw new ServiceTypeNotFoundException(domainId, serviceTypeId);
				}
				session.delete(current);
			}
		});
	}

	public void deleteServiceType(final String domainId)
	throws ServiceTypeNotFoundException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try {
			for(ServiceType ob : listAllServiceTypes(domainId)) {
				session.delete(ob);
			}
			for(ServiceMetaAttribute ob : listAllServiceMetaAttributes(domainId)){
				session.delete(ob);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public ServiceType getServiceType(final String domainId, final String serviceTypeId)
	throws ServiceTypeNotFoundException, DaoException {
		return transact(new DaoBlockR<ServiceType>(){
			@Override
			public ServiceType execute(Session session) throws DaoException {
				ServiceType current = (ServiceType)session.get(
						ServiceType.class, new ServiceTypePK(domainId, serviceTypeId));
				if(current == null){
					throw new ServiceTypeNotFoundException(domainId, serviceTypeId);
				}
				return current;
			}
		});
	}

	@Override
	public boolean isServiceTypeExist(String domainId, String resourceTypeId) throws DaoException {
		return exists(new ServiceTypePK(domainId, resourceTypeId));
	}

	@Override
	public List<ServiceMetaAttribute> listAllServiceMetaAttributes()
	throws DaoException {
		return transact(new DaoBlockR<List<ServiceMetaAttribute>>(){
			@Override
			@SuppressWarnings("unchecked")
			public List<ServiceMetaAttribute> execute(Session session)
			throws DaoException {
				return (List<ServiceMetaAttribute>)session.createCriteria(ServiceMetaAttribute.class)
						.list();
			}
		});
	}

	@Override
	public List<ServiceMetaAttribute> listAllServiceMetaAttributes(final String domainId)
	throws DaoException {
		return transact(new DaoBlockR<List<ServiceMetaAttribute>>(){
			@Override
			@SuppressWarnings("unchecked")
			public List<ServiceMetaAttribute> execute(Session session)
			throws DaoException {
				return (List<ServiceMetaAttribute>)session.createCriteria(ServiceMetaAttribute.class)
						.add(Property.forName("domainId").eq(domainId))
						.list();
			}
		});
	}

	@Override
	public void addServiceMetaAttribute(final ServiceMetaAttribute metaAttribute)
	throws ServiceMetaAttributeAlreadyExistsException, DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				String dId = metaAttribute.getDomainId();
				String atn = metaAttribute.getAttributeName();
				ServiceMetaAttribute current = (ServiceMetaAttribute)session.get(
						ServiceMetaAttribute.class, new ServiceMetaAttributePK(dId, atn));
				if(current != null){
					throw new ServiceMetaAttributeAlreadyExistsException(dId, atn);
				} else{
					session.save(metaAttribute);
				}
			}
		});
	}

	@Override
	public void deleteServiceMetaAttribute(final String domainId, final String attributeName)
	throws ServiceMetaAttributeNotFoundException, DaoException{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				ServiceMetaAttribute current = (ServiceMetaAttribute)session.get(
						ServiceMetaAttribute.class, new ServiceMetaAttributePK(domainId, attributeName));
				if(current == null){
					throw new ServiceMetaAttributeNotFoundException(domainId, attributeName);
				}
				session.delete(current);
			}
		});
	}

	@Override
	public void deleteServiceMetaAttribute(final String domainId)
	throws ServiceMetaAttributeNotFoundException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try {
			for(ServiceMetaAttribute ob : listAllServiceMetaAttributes(domainId)) {
				session.delete(ob);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public ServiceMetaAttribute getServiceMetaAttribute(final String domainId, final String attributeName)
	throws ServiceMetaAttributeNotFoundException, DaoException {
		return transact(new DaoBlockR<ServiceMetaAttribute>(){
			@Override
			public ServiceMetaAttribute execute(Session session) throws DaoException {
				ServiceMetaAttribute current = (ServiceMetaAttribute)session.get(
						ServiceMetaAttribute.class, new ServiceMetaAttributePK(domainId, attributeName));
				if(current == null){
					throw new ServiceMetaAttributeNotFoundException(domainId, attributeName);
				}
				return current;
			}
		});
	}

	@Override
	public boolean isServiceMetaAttributeExist(String domainId, String serviceMetaAttributeId)
	throws DaoException {
		try{
			return getSession().get(
					ServiceMetaAttribute.class
					, new ServiceMetaAttributePK(domainId, serviceMetaAttributeId)
					) != null;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			throw e;
		}
	}
}
