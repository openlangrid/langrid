/*
 * $Id: P2PGridBasisResourceMetaAttributeDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.dao.langrid;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.DomainNotFoundException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.ResourceMetaAttributeNotFoundException;
import jp.go.nict.langrid.dao.ResourceTypeDao;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttributePK;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceMetaAttributeData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisResourceMetaAttributeDao implements DataDao, ResourceTypeDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisResourceMetaAttributeDao(DomainDao domainDao, ResourceTypeDao dao, DaoContext context) {
		this.domainDao = domainDao;
		this.dao = dao;
		this.daoContext = context;
	}

	private P2PGridController getController() throws ControllerException{
		if (controller == null) {
			controller = JXTAController.getInstance();
		}

		return controller;
	}

	public void setEntityListener() {
		logger.debug("### ResourceMetaAttribute : setEntityListener ###");
		daoContext.addEntityListener(ResourceMetaAttribute.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### ResourceMetaAttribute : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(ResourceMetaAttribute.class, handler);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.dao#updateDataSource(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public boolean updateDataSource(Data data) throws DataDaoException, UnmatchedDataTypeException {
		return updateDataTarget(data);
	}
	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.dao#updateData(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public boolean updateDataTarget(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.debug("[ResourceMetaAttribute] : " + data.getId());
		if(data.getClass().equals(ResourceMetaAttributeData.class) == false) {
			throw new UnmatchedDataTypeException(ResourceMetaAttributeData.class.toString(), data.getClass().toString());
		}

		ResourceMetaAttributeData resourceMetaAttributeData = (ResourceMetaAttributeData) data;
		ResourceMetaAttribute resourceMetaAttribute = null;
		try {
			resourceMetaAttribute = resourceMetaAttributeData.getResourceMetaAttribute();
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		}
		try {
			Domain domainInDb = domainDao.getDomain(resourceMetaAttribute.getDomainId());
			if(domainInDb.getOwnerUserGridId().equals(this.controller.getSelfGridId())){
				return false;
			}
		} catch (DomainNotFoundException e) {
		} catch (DaoException e) {
			e.printStackTrace();
			return false;
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			try {
				logger.info("Delete");
				removeEntityListener();
				dao.deleteResourceMetaAttribute(resourceMetaAttribute.getDomainId(), resourceMetaAttribute.getAttributeId());
				updated = true;
				setEntityListener();
				getController().baseSummaryAdd(data);
			} catch (ResourceMetaAttributeNotFoundException e) {
				// 
				// 
				try {
					getController().baseSummaryAdd(data);
				} catch (ControllerException e1) {
					e1.printStackTrace();
				}
			} catch (DaoException e) {
				throw new DataDaoException(e);
			} catch (ControllerException e) {
				throw new DataDaoException(e);
			}
			return updated;
		}

		try {
			logger.debug("New or UpDate");
			removeEntityListener();
			daoContext.beginTransaction();
			daoContext.mergeEntity(resourceMetaAttribute);
			daoContext.commitTransaction();
			setEntityListener();
			getController().baseSummaryAdd(data);
			return true;
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
	}


	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public List<ResourceType> listAllResourceTypes() throws DaoException {
		return dao.listAllResourceTypes();
	}

	@Override
	public List<ResourceType> listAllResourceTypes(String domainId)
			throws DaoException {
		return dao.listAllResourceTypes(domainId);
	}

	@Override
	public void addResourceType(ResourceType resourceType) throws DaoException {
		dao.addResourceType(resourceType);
	}

	@Override
	public void deleteResourceType(String domainId, String resourceTypeId)
			throws DaoException {
		dao.deleteResourceType(domainId, resourceTypeId);
	}

	@Override
	public ResourceType getResourceType(String domainId, String resourceTypeId)
			throws DaoException {
		return dao.getResourceType(domainId, resourceTypeId);
	}

	@Override
	public boolean isResourceTypeExist(String domainId, String resourceTypeId)
			throws DaoException {
		return dao.isResourceTypeExist(domainId, resourceTypeId);
	}

	@Override
	public List<ResourceMetaAttribute> listAllResourceMetaAttributes()
			throws DaoException {
		return dao.listAllResourceMetaAttributes();
	}

	@Override
	public List<ResourceMetaAttribute> listAllResourceMetaAttributes(
			String domainId) throws DaoException {
		return dao.listAllResourceMetaAttributes(domainId);
	}

	@Override
	public void addResourceMetaAttribute(ResourceMetaAttribute metaAttribute)
			throws DaoException {
		dao.addResourceMetaAttribute(metaAttribute);
	}

	@Override
	public void deleteResourceMetaAttribute(String domainId,
			String attributeName) throws DaoException {
		dao.deleteResourceMetaAttribute(domainId, attributeName);
	}

	@Override
	public ResourceMetaAttribute getResourceMetaAttribute(String domainId,
			String attributeName) throws DaoException {
		return dao.getResourceMetaAttribute(domainId, attributeName);
	}

	@Override
	public boolean isResourceMetaAttributeExist(String domainId,
			String resourceMetaAttributeId) throws DaoException {
		return dao.isResourceMetaAttributeExist(domainId, resourceMetaAttributeId);
	}
	
	@Override
	public void deleteResourceMetaAttribute(String domainId) throws DaoException {
		dao.deleteResourceMetaAttribute(domainId);
	}
	
	@Override
	public void deleteResourceType(String domainId) throws DaoException {
		dao.deleteResourceType(domainId);
	}

	private DomainDao domainDao;
	private ResourceTypeDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<ResourceMetaAttribute> handler = new GenericHandler<ResourceMetaAttribute>(){
		protected boolean onNotificationStart() {
			try{
				daoContext.beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				getController().publish(new ResourceMetaAttributeData(
						daoContext.loadEntity(ResourceMetaAttribute.class, id)
						));
				logger.info("published[ResourceType(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to publish instance.", e);
			} catch(DaoException e){
				logger.error("failed to access dao.", e);
			} catch(DataConvertException e){
				logger.error("failed to convert data.", e);
			}
		}

		protected void doRemove(Serializable id){
			try{
				ResourceMetaAttributePK pk = (ResourceMetaAttributePK)id;
				getController().revoke(ResourceMetaAttributeData.getDataID(null, pk));
				logger.info("revoked[ResourceMetaAttribute(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to revoke instance.", e);
			} catch(DataNotFoundException e){
				logger.error("failed to find data.", e);
			}
		}

		protected void onNotificationEnd(){
			try{
				daoContext.commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisResourceMetaAttributeDao.class);
}
