/*
 * $Id: P2PGridBasisServiceDao.java 487 2012-05-24 02:42:03Z t-nakaguchi $
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.apache.axis.encoding.Base64;
import org.apache.log4j.Logger;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.axis.AxisServiceContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceAlreadyExistsException;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.ServiceSearchResult;
import jp.go.nict.langrid.dao.ServiceStatRankingSearchResult;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServicePK;
import jp.go.nict.langrid.dao.entity.WebappService;
import jp.go.nict.langrid.management.logic.ServiceNotActivatableException;
import jp.go.nict.langrid.management.logic.service.ProcessDeployer;
import jp.go.nict.langrid.management.logic.service.ServiceActivator;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 487 $
 */
public class P2PGridBasisServiceDao
extends AbstractP2PGridBasisUpdateManagedEntityDao<Service>
implements DataDao, ServiceDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisServiceDao(
			ServiceDao dao, DaoContext context
			, String activeBpelServicesUrl, String activeBpelDeployBinding) {
		super(context);
		this.dao = dao;
		this.activeBpelServicesUrl = activeBpelServicesUrl;
		this.activeBpelDeployBinding = activeBpelDeployBinding;
	}

	public void setEntityListener() {
		logger.debug("### Service : setEntityListener ###");
		getDaoContext().addEntityListener(ExternalService.class, esHandler);
		getDaoContext().addTransactionListener(esHandler);
		getDaoContext().addEntityListener(BPELService.class, bsHandler);
		getDaoContext().addTransactionListener(bsHandler);
		getDaoContext().addEntityListener(WebappService.class, wsHandler);
		getDaoContext().addTransactionListener(wsHandler);
	}

	public void removeEntityListener() {
		logger.debug("### Service : removeEntityListener ###");
		getDaoContext().removeTransactionListener(esHandler);
		getDaoContext().removeEntityListener(ExternalService.class, esHandler);
		getDaoContext().removeTransactionListener(bsHandler);
		getDaoContext().removeEntityListener(BPELService.class, bsHandler);
		getDaoContext().removeTransactionListener(wsHandler);
		getDaoContext().removeEntityListener(WebappService.class, wsHandler);
	}

	@Override
	synchronized public boolean updateData(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.debug("[Service] : " + data.getId());
		if(data.getClass().equals(ServiceData.class) == false) {
			throw new UnmatchedDataTypeException(ServiceData.class.toString(), data.getClass().toString());
		}

		ServiceData serviceData = (ServiceData) data;
		if(!isReachableTo(serviceData.getGridId())){
			return false;
		}

		Service service = null;
		try {
			if(data.getAttributes().getValue("instanceType").equals("BPEL")){
				service = serviceData.getBPELService();
			}else if(data.getAttributes().getValue("instanceType").equals("EXTERNAL")){
				service = serviceData.getExternalService();
			}else if(data.getAttributes().getValue("instanceType").equals("Java")){
				service = serviceData.getExternalService();
			}else if(data.getAttributes().getValue("instanceType").equals("WEBAPP")){
				service = serviceData.getWebappService();
			}else{
				logger.error("instanceType : " + data.getAttributes().getValue("instanceType"));
				throw new UnmatchedDataTypeException(ServiceData.class.toString(), data.getClass().toString());
			}
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		}
		try{
			if(service.getGridId().equals(getController().getSelfGridId())){
				// we must consider mirror mode. when mirror mode, we have to 
				// deploy services sent from the node which belongs to same grid.
				return false;
			}
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			try {
				logger.info("Delete");
				removeEntityListener();
				dao.deleteService(service.getGridId(), service.getServiceId());
				updated = true;
				setEntityListener();
				getController().baseSummaryAdd(data);
			} catch (ServiceNotFoundException e) {
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
			if(service.getServiceDescription() == null){
				service.setServiceDescription("");
			}
			if(!service.getGridId().equals(getController().getSelfGridId())){
				service.getServiceEndpoints().clear();
				service.getServiceDeployments().clear();
			}

			removeEntityListener();
			try{
				if(dao.isServiceExist(service.getGridId(), service.getServiceId())){
					logger.debug("UpDate");
					getDaoContext().updateEntity(service);
				}else{
					logger.debug("New");
					dao.addService(service);
				}
			} catch(DaoException e){
				throw new DataDaoException(e);
			} finally{
				setEntityListener();
			}

			if(service.getGridId().equals(getController().getSelfGridId())){
				getDaoContext().beginTransaction();
				removeEntityListener();
				try{
					if(service.isActive()){
						// activate
						ServiceContext sc = new AxisServiceContext();
						try{
							String instance = data.getAttributes().getValue("instance");
							if(instance != null){
								ProcessDeployer deployer = new ProcessDeployer(
										activeBpelServicesUrl
										, sc.getAuthUser()
										, sc.getAuthPass()
										);
								ServiceActivator.activate(
										dao, service, new ByteArrayInputStream(Base64.decode(instance))
										, deployer, activeBpelDeployBinding
										);
							}
						} catch(ServiceNotActivatableException e){
							logger.fatal("failed to activate service", e);
						}
					}
					getDaoContext().commitTransaction();
				} catch(DaoException e){
					getDaoContext().rollbackTransaction();
					throw new DataDaoException(e);
				} finally{
					setEntityListener();
				}
	
				getDaoContext().beginTransaction();
				removeEntityListener();
				try{
					if(service instanceof ExternalService){
						// activate parents
						// 
						// 

						ServiceContext sc = new AxisServiceContext();
						ProcessDeployer deployer = new ProcessDeployer(
								activeBpelServicesUrl
								, sc.getAuthUser()
								, sc.getAuthPass()
								);
						ServiceActivator.activateParents(
								dao, service.getGridId(), service.getServiceId()
								, deployer, activeBpelDeployBinding);
					}
					getDaoContext().commitTransaction();
				} catch(DaoException e){
					getDaoContext().rollbackTransaction();
					throw new DataDaoException(e);
				} finally{
					setEntityListener();
				}
			}

			getController().baseSummaryAdd(data);
			return true;
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
	}

	@Override
	public void addService(Service service) throws DaoException,
			ServiceAlreadyExistsException {
		dao.addService(service);
	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void clearDetachedInvocations() throws DaoException {
		dao.clearDetachedInvocations();
	}

	@Override
	public void deleteService(String serviceGridId, String serviceId)
			throws ServiceNotFoundException, DaoException {
		dao.deleteService(serviceGridId, serviceId);
	}

	@Override
	public void deleteServicesOfGrid(String gridId) throws DaoException {
		dao.deleteServicesOfGrid(gridId);
	}

	@Override
	public void deleteServicesOfUser(String userGridId, String userId)
			throws DaoException {
		dao.deleteServicesOfUser(userGridId, userId);
	}

	@Override
	public Service getService(String serviceGridId, String serviceId)
			throws ServiceNotFoundException, DaoException {
		return dao.getService(serviceGridId, serviceId);
	}

	@Override
	public InputStream getServiceInstance(String serviceGridId, String serviceId)
			throws DaoException, ServiceNotFoundException {
		return dao.getServiceInstance(serviceGridId, serviceId);
	}

	@Override
	public InputStream getServiceWsdl(String serviceGridId, String serviceId)
			throws DaoException, ServiceNotFoundException {
		return dao.getServiceWsdl(serviceGridId, serviceId);
	}

	@Override
	public boolean isServiceExist(String serviceGridId, String serviceId)
			throws DaoException {
		return dao.isServiceExist(serviceGridId, serviceId);
	}

	public List<BPELService> listParentServicesOf(String gridId, String serviceId)
	throws DaoException {
		return dao.listParentServicesOf(gridId, serviceId);
	}

	@Override
	public List<Service> listAllServices(String serviceGridId)
			throws DaoException {
		return dao.listAllServices(serviceGridId);
	}

	@Override
	public List<Service> listServicesOfUser(String userGridId, String userId)
			throws DaoException {
		return dao.listServicesOfUser(userGridId, userId);
	}

	@Override
	public ServiceSearchResult searchServices(int startIndex, int maxCount,
			String serviceGridId, boolean acrossGrids, MatchingCondition[] conditions, Order[] orders)
			throws DaoException {
		return dao.searchServices(startIndex, maxCount, serviceGridId, acrossGrids, conditions, orders);
	}

	@Override
	public ServiceStatRankingSearchResult searchServiceStatRanking(
			int startIndex, int maxCount, String serviceGridId, String nodeId,
			boolean acrossGrids, MatchingCondition[] conditions, int sinceDays,
			Order[] orders) throws DaoException {
		return dao.searchServiceStatRanking(
				startIndex, maxCount, serviceGridId, nodeId, acrossGrids, conditions
				, sinceDays, orders);
	}

	private ServiceDao dao;
	private class ServiceHandler<T extends Service> extends GenericHandler<T>{
		public ServiceHandler(Class<T> clazz){
			this.clazz = clazz;
		}
		private Class<T> clazz;

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
				Service service = getDaoContext().loadEntity(clazz, id);
				ServiceData data = new ServiceData(service);
				Blob blob = null;

				//Instance
				blob = service.getInstance();
				if(blob != null){
					data.getAttributes().setAttribute(
							"instance"
							, Base64.encode(StreamUtil.readAsBytes(blob.getBinaryStream()))
							);
				}

				//Wsdl
				blob = service.getWsdl();
				if(blob != null){
					data.getAttributes().setAttribute(
							"wsdl"
							, Base64.encode(StreamUtil.readAsBytes(blob.getBinaryStream())));
				}

				getController().publish(data);
				logger.info("published[" + clazz.getSimpleName() + "(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to publish instance.", e);
			} catch(DaoException e){
				logger.error("failed to access dao.", e);
			} catch(DataConvertException e){
				logger.error("failed to convert data.", e);
			} catch(IOException e){
				logger.error("failed to read data", e);
			} catch (SQLException e) {
				logger.error("failed to SQLException", e);
			}
		}

		public void doRemove(Serializable id){
			try {
				ServicePK pk = (ServicePK)id;
				getController().revoke(ServiceData.getDataID(null, pk));
				logger.info("revoked[ExternalService(id=" + id + ")]");
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
	}
	private GenericHandler<ExternalService> esHandler = new ServiceHandler<ExternalService>(
			ExternalService.class);
	private GenericHandler<BPELService> bsHandler = new ServiceHandler<BPELService>(
			BPELService.class);
	private GenericHandler<WebappService> wsHandler = new ServiceHandler<WebappService>(
			WebappService.class);
/*
	private GenericHandler<ExternalService> esHandler = new GenericHandler<ExternalService>() {
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
			try {
				ExternalService service = daoContext.loadEntity(ExternalService.class, id);
				ServiceData data = new ServiceData(service);
				Blob blob = null;

				//Instance
				blob = service.getInstance();
				if(blob != null){
					data.getAttributes().setAttribute(
							"instance"
							, Base64.encode(StreamUtil.readAsBytes(blob.getBinaryStream()))
							);
				}

				//Wsdl
				blob = service.getWsdl();
				if(blob != null){
					data.getAttributes().setAttribute(
							"wsdl"
							, Base64.encode(StreamUtil.readAsBytes(blob.getBinaryStream())));
				}

				getController().publish(data);
				logger.info("published[ExternalService(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to publish instance.", e);
			} catch(DaoException e){
				logger.error("failed to access dao.", e);
			} catch(DataConvertException e){
				logger.error("failed to convert data.", e);
			} catch(IOException e){
				logger.error("failed to read data", e);
			} catch (SQLException e) {
				logger.error("failed to SQLException", e);
			}
		}

		public void doRemove(Serializable id){
			try {
				ServicePK pk = (ServicePK)id;
				getController().revoke(ServiceData.getDataID(null, pk));
				logger.info("revoked[ExternalService(id=" + id + ")]");
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
	private GenericHandler<BPELService> bsHandler = new GenericHandler<BPELService>(){
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
				BPELService service = daoContext.loadEntity(BPELService.class, id);
				ServiceData data = new ServiceData(service);
				Blob blob = null;

				//Instance
				blob = service.getInstance();
				if(blob != null){
					data.getAttributes().setAttribute(
							"instance"
							, Base64.encode(StreamUtil.readAsBytes(blob.getBinaryStream()))
							);
				}

				//Wsdl
				blob = service.getWsdl();
				if(blob != null){
					data.getAttributes().setAttribute(
							"wsdl"
							, Base64.encode(StreamUtil.readAsBytes(blob.getBinaryStream()))
							);
				}

				getController().publish(data);
				logger.info("published[BPELService(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to publish instance.", e);
			} catch(DaoException e){
				logger.error("failed to access dao.", e);
			} catch(DataConvertException e){
				logger.error("failed to convert data.", e);
			} catch(IOException e){
				logger.error("failed to read data", e);
			} catch (SQLException e) {
				logger.error("failed to SQLException", e);
			}
		}

		public void doRemove(Serializable id){
			try {
				ServicePK pk = (ServicePK)id;
				getController().revoke(ServiceData.getDataID(null, pk));
				logger.info("revoked[BPELService(id=" + id + ")]");
			} catch (ControllerException e) {
				logger.error(e.getMessage());
			} catch (DataNotFoundException e) {
				logger.error(e.getMessage());
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
*/
	private String activeBpelServicesUrl;
	private String activeBpelDeployBinding;
	static private Logger logger = Logger.getLogger(P2PGridBasisServiceDao.class);
}
