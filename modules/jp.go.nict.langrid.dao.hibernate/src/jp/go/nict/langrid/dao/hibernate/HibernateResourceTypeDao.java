/*
 * $Id:HibernateResourceTypeDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import jp.go.nict.langrid.dao.ResourceMetaAttributeAlreadyExistsException;
import jp.go.nict.langrid.dao.ResourceMetaAttributeNotFoundException;
import jp.go.nict.langrid.dao.ResourceTypeAlreadyExistsException;
import jp.go.nict.langrid.dao.ResourceTypeDao;
import jp.go.nict.langrid.dao.ResourceTypeNotFoundException;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttributePK;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ResourceTypePK;

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
public class HibernateResourceTypeDao
extends HibernateCRUDDao<ResourceType>
implements ResourceTypeDao{
	/**
	 * 
	 * 
	 */
	public HibernateResourceTypeDao(HibernateDaoContext context){
		super(context, ResourceType.class);
	}

	@Override
	public void clear() throws DaoException {
		deleteEntities(ResourceType.class);
		deleteEntities(ResourceMetaAttribute.class);
	}

	@Override
	public List<ResourceType> listAllResourceTypes() throws DaoException {
		return list();
	}

	@Override
	public List<ResourceType> listAllResourceTypes(final String domainId) throws DaoException {
		return transact(new DaoBlockR<List<ResourceType>>(){
			@Override
			@SuppressWarnings("unchecked")
			public List<ResourceType> execute(Session session)
					throws DaoException {
				return (List<ResourceType>)session.createCriteria(ResourceType.class)
				.add(Property.forName("domainId").eq(domainId))
				.addOrder(Order.asc("resourceTypeName"))
				.list();
			}
		});
	}

	public void addResourceType(ResourceType resourceType)
	throws ResourceTypeAlreadyExistsException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			String dId = resourceType.getDomainId();
			String rtId = resourceType.getResourceTypeId();
			ResourceType current = (ResourceType)session.get(ResourceType.class, new ResourceTypePK(dId, rtId));
			if(current != null){
				getContext().commitTransaction();
				throw new ResourceTypeAlreadyExistsException(rtId);
			} else{
				session.save(resourceType);
				getContext().commitTransaction();
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

	public void deleteResourceType(String domainId, String resourceTypeId)
	throws ResourceTypeNotFoundException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			ResourceType current = (ResourceType)session.get(ResourceType.class, new ResourceTypePK(domainId, resourceTypeId));
			if(current == null){
				getContext().commitTransaction();
				throw new ResourceTypeNotFoundException(resourceTypeId);
			}
			session.delete(current);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteResourceType(String domainId)
	throws ResourceTypeNotFoundException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try {
			for(ResourceType ob : listAllResourceTypes(domainId)) {
				session.delete(ob);
			}
			for(ResourceMetaAttribute ob : listAllResourceMetaAttributes(domainId)){
				session.delete(ob);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public ResourceType getResourceType(String domainId, String resourceTypeId)
	throws ResourceTypeNotFoundException, DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			ResourceType current = (ResourceType)session.get(ResourceType.class, new ResourceTypePK(domainId, resourceTypeId));
			getContext().commitTransaction();
			if(current == null){
				throw new ResourceTypeNotFoundException(resourceTypeId);
			}
			return current;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isResourceTypeExist(String domainId, String resourceTypeId) throws DaoException {
		return exists(new ResourceTypePK(domainId, resourceTypeId));
	}

	@Override
	public List<ResourceMetaAttribute> listAllResourceMetaAttributes()
	throws DaoException {
		return transact(new DaoBlockR<List<ResourceMetaAttribute>>(){
			@Override
			@SuppressWarnings("unchecked")
			public List<ResourceMetaAttribute> execute(Session session)
			throws DaoException {
				return (List<ResourceMetaAttribute>)session.createCriteria(ResourceMetaAttribute.class)
						.list();
			}
		});
	}

	@Override
	public List<ResourceMetaAttribute> listAllResourceMetaAttributes(final String domainId)
	throws DaoException {
		return transact(new DaoBlockR<List<ResourceMetaAttribute>>(){
			@Override
			@SuppressWarnings("unchecked")
			public List<ResourceMetaAttribute> execute(Session session)
			throws DaoException {
				return (List<ResourceMetaAttribute>)session.createCriteria(ResourceMetaAttribute.class)
						.add(Property.forName("domainId").eq(domainId))
						.list();
			}
		});
	}

	@Override
	public void addResourceMetaAttribute(final ResourceMetaAttribute metaAttribute)
	throws ResourceMetaAttributeAlreadyExistsException, DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				String dId = metaAttribute.getDomainId();
				String atn = metaAttribute.getAttributeName();
				ResourceMetaAttribute current = (ResourceMetaAttribute)session.get(
						ResourceMetaAttribute.class, new ResourceMetaAttributePK(dId, atn));
				if(current != null){
					throw new ResourceMetaAttributeAlreadyExistsException(dId, atn);
				} else{
					session.save(metaAttribute);
				}
			}
		});
	}

	@Override
	public void deleteResourceMetaAttribute(final String domainId, final String attributeName)
	throws ResourceMetaAttributeNotFoundException, DaoException{
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				ResourceMetaAttribute current = (ResourceMetaAttribute)session.get(
						ResourceMetaAttribute.class, new ResourceMetaAttributePK(domainId, attributeName));
				if(current == null){
					throw new ResourceMetaAttributeNotFoundException(domainId, attributeName);
				}
				session.delete(current);
			}
		});
	}
	
	@Override
	public void deleteResourceMetaAttribute(final String domainId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try {
			for(ResourceMetaAttribute ob : listAllResourceMetaAttributes(domainId)) {
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
	public ResourceMetaAttribute getResourceMetaAttribute(final String domainId, final String attributeName)
	throws ResourceMetaAttributeNotFoundException, DaoException {
		return transact(new DaoBlockR<ResourceMetaAttribute>(){
			@Override
			public ResourceMetaAttribute execute(Session session) throws DaoException {
				ResourceMetaAttribute current = (ResourceMetaAttribute)session.get(
						ResourceMetaAttribute.class, new ResourceMetaAttributePK(domainId, attributeName));
				if(current == null){
					throw new ResourceMetaAttributeNotFoundException(domainId, attributeName);
				}
				return current;
			}
		});
	}

	@Override
	public boolean isResourceMetaAttributeExist(String domainId, String resourceMetaAttributeId) throws DaoException {
		try{
			return getSession().get(
					ResourceMetaAttribute.class
					, new ResourceMetaAttributePK(domainId, resourceMetaAttributeId)
					) != null;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			throw e;
		}
	}
}
