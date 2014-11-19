/*
 * $Id: P2PGridBasisServiceMetaAttributeDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.ServiceMetaAttributeNotFoundException;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttributePK;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceMetaAttributeData;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisServiceMetaAttributeDao implements DataDao, ServiceTypeDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisServiceMetaAttributeDao(ServiceTypeDao dao, DaoContext context) {
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
		logger.debug("### ServiceMetaAttribute : setEntityListener ###");
		daoContext.addEntityListener(ServiceMetaAttribute.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### ServiceMetaAttribute : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(ServiceMetaAttribute.class, handler);
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
		logger.debug("[ServiceMetaAttribute] : " + data.getId());
		if(data.getClass().equals(ServiceMetaAttributeData.class) == false) {
			throw new UnmatchedDataTypeException(ServiceMetaAttributeData.class.toString(), data.getClass().toString());
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			try {
				logger.info("Delete");
				ServiceMetaAttributeData serviceMetaAttributeData = (ServiceMetaAttributeData) data;
				ServiceMetaAttribute serviceMetaAttribute = serviceMetaAttributeData.getServiceMetaAttribute();
				removeEntityListener();
				dao.deleteServiceMetaAttribute(serviceMetaAttribute.getDomainId(), serviceMetaAttribute.getAttributeId());
				updated = true;
				setEntityListener();
				getController().baseSummaryAdd(data);
			} catch (DataConvertException e) {
				throw new DataDaoException(e);
			} catch (ServiceMetaAttributeNotFoundException e) {
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

		ServiceMetaAttribute serviceMetaAttribute = null;
		try {
			ServiceMetaAttributeData serviceMetaAttributeData = (ServiceMetaAttributeData)data;
			serviceMetaAttribute = serviceMetaAttributeData.getServiceMetaAttribute();

			logger.debug("New or UpDate");
			removeEntityListener();
			daoContext.beginTransaction();
			daoContext.mergeEntity(serviceMetaAttribute);
			daoContext.commitTransaction();
			setEntityListener();
			getController().baseSummaryAdd(data);
			return true;
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
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
	public List<ServiceType> listAllServiceTypes() throws DaoException {
		return dao.listAllServiceTypes();
	}

	@Override
	public List<ServiceType> listAllServiceTypes(String domainId)
			throws DaoException {
		return dao.listAllServiceTypes(domainId);
	}

	@Override
	public void addServiceType(ServiceType serviceType) throws DaoException {
		dao.addServiceType(serviceType);
	}

	@Override
	public void deleteServiceType(String domainId, String serviceTypeId)
			throws DaoException {
		dao.deleteServiceType(domainId, serviceTypeId);
	}

	@Override
	public ServiceType getServiceType(String domainId, String serviceTypeId)
			throws DaoException {
		return dao.getServiceType(domainId, serviceTypeId);
	}

	@Override
	public boolean isServiceTypeExist(String domainId, String serviceTypeId)
			throws DaoException {
		return dao.isServiceTypeExist(domainId, serviceTypeId);
	}

	@Override
	public List<ServiceMetaAttribute> listAllServiceMetaAttributes()
			throws DaoException {
		return dao.listAllServiceMetaAttributes();
	}

	@Override
	public List<ServiceMetaAttribute> listAllServiceMetaAttributes(
			String domainId) throws DaoException {
		return dao.listAllServiceMetaAttributes(domainId);
	}

	@Override
	public void addServiceMetaAttribute(ServiceMetaAttribute metaAttribute)
			throws DaoException {
		dao.addServiceMetaAttribute(metaAttribute);
	}

	@Override
	public void deleteServiceMetaAttribute(String domainId,
			String attributeName) throws DaoException {
		dao.deleteServiceMetaAttribute(domainId, attributeName);
	}

	@Override
	public ServiceMetaAttribute getServiceMetaAttribute(String domainId,
			String attributeName) throws DaoException {
		return dao.getServiceMetaAttribute(domainId, attributeName);
	}

	@Override
	public boolean isServiceMetaAttributeExist(String domainId,
			String serviceMetaAttributeId) throws DaoException {
		return isServiceMetaAttributeExist(domainId, serviceMetaAttributeId);
	}
	
	@Override
	public void deleteServiceMetaAttribute(String domainId) throws DaoException {
		dao.deleteServiceMetaAttribute(domainId);
	}
	
	@Override
	public void deleteServiceType(String domainId) throws DaoException {
		dao.deleteServiceType(domainId);
	}

	private ServiceTypeDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<ServiceMetaAttribute> handler = new GenericHandler<ServiceMetaAttribute>(){
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
				getController().publish(new ServiceMetaAttributeData(
						daoContext.loadEntity(ServiceMetaAttribute.class, id)
						));
				logger.info("published[ServiceType(id=" + id + ")]");
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
				ServiceMetaAttributePK pk = (ServiceMetaAttributePK)id;
				getController().revoke(ServiceMetaAttributeData.getDataID(null, pk));
				logger.info("revoked[ServiceMetaAttribute(id=" + id + ")]");
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

	static private Logger logger = Logger.getLogger(P2PGridBasisServiceMetaAttributeDao.class);
}
