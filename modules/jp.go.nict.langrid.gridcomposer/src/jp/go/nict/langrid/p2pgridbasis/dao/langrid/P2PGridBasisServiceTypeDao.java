/*
 * $Id: P2PGridBasisServiceTypeDao.java 1522 2015-03-11 02:20:42Z t-nakaguchi $
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
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.DomainNotFoundException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.ServiceTypeNotFoundException;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.ServiceTypePK;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceTypeData;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1522 $
 */
public class P2PGridBasisServiceTypeDao implements DataDao, ServiceTypeDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisServiceTypeDao(DomainDao domainDao, ServiceTypeDao dao, DaoContext context) {
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
		logger.debug("### ServiceType : setEntityListener ###");
		daoContext.addEntityListener(ServiceType.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### ServiceType : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(ServiceType.class, handler);
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
		logger.debug("[ServiceType] : " + data.getId());
		if(data.getClass().equals(ServiceTypeData.class) == false) {
			throw new UnmatchedDataTypeException(ServiceTypeData.class.toString(), data.getClass().toString());
		}

		ServiceTypeData serviceTypeData = (ServiceTypeData) data;
		ServiceType serviceType = null;
		try{
			serviceType = serviceTypeData.getServiceType();
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		}
		try{
			String did = serviceType.getDomainId();
			Domain d = domainDao.getDomain(did);
			if(d == null || d.getOwnerUserGridId().equals(getController().getSelfGridId())){
				return false;
			}
		} catch(ControllerException e){
			return false;
		} catch(DomainNotFoundException e){
			return false;
		} catch (DaoException e) {
			throw new DataDaoException(e);
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			try {
				logger.info("Delete");
				removeEntityListener();
				dao.deleteServiceType(serviceType.getDomainId(), serviceType.getServiceTypeId());
				updated = true;
				setEntityListener();
				getController().baseSummaryAdd(data);
			} catch (ServiceTypeNotFoundException e) {
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
		} else{
			try {
				logger.debug("New or UpDate");
				daoContext.beginTransaction();
				removeEntityListener();
				try{
					for(ServiceMetaAttribute a : serviceType.getMetaAttributes().values()){
						if(!dao.isServiceMetaAttributeExist(a.getDomainId(), a.getAttributeId())){
							dao.addServiceMetaAttribute(a);
						}
					}
					dao.mergeServiceType(serviceType);
					daoContext.commitTransaction();
				} catch(DaoException e){
					daoContext.rollbackTransaction();
					throw e;
				} finally{
					setEntityListener();
				}
				getController().baseSummaryAdd(data);
				return true;
			} catch (DaoException e) {
				throw new DataDaoException(e);
			} catch (ControllerException e) {
				throw new DataDaoException(e);
			}
		}
	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
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
	public void deleteServiceMetaAttribute(String domainId, String attributeName)
			throws DaoException {
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
		return dao.isServiceMetaAttributeExist(domainId, serviceMetaAttributeId);
	}
	
	@Override
	public void deleteServiceMetaAttribute(String domainId) throws DaoException {
		dao.deleteServiceMetaAttribute(domainId);
	}
	
	@Override
	public void deleteServiceType(String domainId) throws DaoException {
		dao.deleteServiceType(domainId);
	}
	
	@Override
	public void mergeServiceType(ServiceType st) throws DaoException {
		dao.mergeServiceType(st);
	}

	private DomainDao domainDao;
	private ServiceTypeDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<ServiceType> handler = new GenericHandler<ServiceType>(){
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
				getController().publish(new ServiceTypeData(
						daoContext.loadEntity(ServiceType.class, id)
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
				ServiceTypePK pk = (ServiceTypePK)id;
				getController().revoke(ServiceTypeData.getDataID(null, pk));
				logger.info("revoked[ServiceType(id=" + id + ")]");
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

	static private Logger logger = Logger.getLogger(P2PGridBasisServiceTypeDao.class);
}
