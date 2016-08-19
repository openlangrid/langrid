/*
 * $Id: P2PGridBasisServiceMetaAttributeDao.java 1514 2015-03-08 12:43:46Z t-nakaguchi $
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
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttributePK;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceMetaAttributeData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1514 $
 */
public class P2PGridBasisServiceMetaAttributeDao
extends AbstractP2PGridBasisDao<ServiceMetaAttribute>
implements DataDao, ServiceTypeDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisServiceMetaAttributeDao(DomainDao domainDao, ServiceTypeDao dao, DaoContext context) {
		super(context);
		this.domainDao = domainDao;
		this.dao = dao;
		setHandler(handler);
	}

	@Override
	synchronized public boolean updateData(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.debug("[ServiceMetaAttribute] : " + data.getId());
		if(data.getClass().equals(ServiceMetaAttributeData.class) == false) {
			throw new UnmatchedDataTypeException(ServiceMetaAttributeData.class.toString(), data.getClass().toString());
		}

		ServiceMetaAttribute entity = null;
		try {
			entity = ((ServiceMetaAttributeData)data).getServiceMetaAttribute();
			Domain domainInDb = domainDao.getDomain(entity.getDomainId());
			if(domainInDb.getOwnerUserGridId().equals(getSelfGridId())) return false;
			if(!isReachableTo(domainInDb.getOwnerUserGridId())) return false;
		} catch (DomainNotFoundException e) {
		} catch(Exception e) {
			throw new DataDaoException(e);
		}
		return handleData(data, entity);
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

	@Override
	public void mergeServiceType(ServiceType st) throws DaoException {
		dao.mergeServiceType(st);
	}

	private DomainDao domainDao;
	private ServiceTypeDao dao;
	private GenericHandler<ServiceMetaAttribute> handler = new GenericHandler<ServiceMetaAttribute>(){
		protected boolean onNotificationStart() {
			try{
				getDaoContext().beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				getController().publish(new ServiceMetaAttributeData(
						getDaoContext().loadEntity(ServiceMetaAttribute.class, id)
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
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisServiceMetaAttributeDao.class);
}
