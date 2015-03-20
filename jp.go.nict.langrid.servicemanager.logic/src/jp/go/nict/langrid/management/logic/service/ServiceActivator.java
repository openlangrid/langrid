/*
 * $Id:ServiceActivator.java 5259 2007-09-06 10:10:27Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.management.logic.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.bpel.ProcessAnalysisException;
import jp.go.nict.langrid.bpel.deploy.DeploymentException;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.util.InvalidLangridUriException;
import jp.go.nict.langrid.commons.ws.util.LangridUriUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceDeployment;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.util.BPELServiceInstanceReader;
import jp.go.nict.langrid.dao.util.BPRBPELServiceInstanceReader;
import jp.go.nict.langrid.dao.util.ExternalServiceInstanceReader;
import jp.go.nict.langrid.management.logic.ServiceNotActivatableException;
import jp.go.nict.langrid.management.logic.ServiceNotDeactivatableException;

import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:5259 $
 */
public class ServiceActivator {
	/**
	 * 
	 * 
	 */
	public static void activate(
			ServiceDao dao, Service service, InputStream instance
			, ProcessDeployer deployer, String activeBpelDeployBinding
			)
		throws DaoException, ServiceNotActivatableException
	{
		if(service.getClass().equals(ExternalService.class)){
			activateExternalService(
					(ExternalService)service
					, new ExternalServiceInstanceReader(instance)
					);
		} else if(service.getClass().equals(BPELService.class)){
			activateBPELService(
					dao, (BPELService)service
					, new BPRBPELServiceInstanceReader(instance)
					, deployer, activeBpelDeployBinding
					);
		} else{
			throw new ServiceNotActivatableException(
					service.getGridId(), service.getServiceId()
					, "Unknown Service type"
					);
		}
	}

	/**
	 * 
	 * 
	 */
	public static int activateParents(
			ServiceDao dao, String childServiceGridId, String childServiceId
			, ProcessDeployer deployer, String activeBpelDeployBinding
			)
	throws DaoException{
		int c = 0;
		for(BPELService s : dao.listParentServicesOf(childServiceGridId, childServiceId)){
			try{
				InputStream is = dao.getServiceInstance(s.getGridId(), s.getServiceId());
				s = (BPELService)dao.getService(s.getGridId(), s.getServiceId());
				if(is != null){
					activate(dao, s, is
							, deployer, activeBpelDeployBinding
							);
				}
				c++;
			} catch(ServiceNotActivatableException e){
				logger.log(
						Level.WARNING
						, "failed to activate parent service: " + s.getServiceId()
						+ ", child: " + childServiceId, e);
			}
		}
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static void deactivate(Service service
			, ProcessDeployer deployer)
		throws ServiceNotDeactivatableException
	{
		if(service.getClass().equals(ExternalService.class)){
			deactivateExternalService((ExternalService)service);
		} else if(service.getClass().equals(BPELService.class)){
			Exception exception = null;
			try{
				deactivateBPELService((BPELService)service
						, deployer);
			} catch (RemoteException e) {
				exception = e;
			} catch (IOException e) {
				exception = e;
			} catch (ServiceException e) {
				exception = e;
			}
			if(exception != null){
				logger.log(Level.WARNING, "failed to undeploy BPEL", exception);
				throw new ServiceNotDeactivatableException(
						service.getServiceId(), exception);
			}
		} else{
			throw new ServiceNotDeactivatableException(
					service.getServiceId()
					, "Unknown Service type"
					);
		}
		service.setActive(false);
	}

	private static void activateExternalService(
			ExternalService service
			, ExternalServiceInstanceReader instance
			)
		throws ServiceNotActivatableException
	{
		boolean found = false;

		for(ServiceEndpoint e : service.getServiceEndpoints()){
			if(e.isEnabled()){
				found = true;
				break;
			}
		}
		for(ServiceDeployment d : service.getServiceDeployments()){
			if(d.isEnabled()){
				found = true;
				break;
			}
		}
		if(!found){
			throw new ServiceNotActivatableException(
					service.getGridId(), service.getServiceId()
					, "no valid endpoint found"
					);
		}
	}

	private static void activateBPELService(
			ServiceDao dao, BPELService service, BPELServiceInstanceReader instance
			, ProcessDeployer deployer, String activeBpelDeployBinding
			)
		throws DaoException, ServiceNotActivatableException
	{
		Exception exception = null;
		try {
			String binding = service.getAttributeValue("bpel.binding");
			if(binding == null){
				binding = activeBpelDeployBinding;
			}
			Map<URI, String> handlers = new HashMap<URI, String>();
			for(Invocation i : service.getInvocations()){
				try{
					Service s = dao.getService(i.getServiceGridId(), i.getServiceId());
					String handler = s.getAttributeValue("bpel.invokeHandler");
					if(handler != null){
						handlers.put(
								LangridUriUtil.createServiceUri(s.getGridId(), s.getServiceId())
								, handler);
					}
				} catch(ServiceNotFoundException e){
				}
			}
			for(String u : service.getPartnerServiceNamespaceURIs()){
				try{
					URI uri = new URI(u);
					Pair<String, String> ids = LangridUriUtil.extractServiceIds(uri);
					Service s = dao.getService(ids.getFirst(), ids.getSecond());
					String handler = s.getAttributeValue("bpel.invokeHandler");
					if(handler != null){
						handlers.put(uri, handler);
					}
				} catch(InvalidLangridUriException e){
				} catch(ServiceNotFoundException e){
				}
			}
			deployer.deploy(service, instance, binding, handlers);
		} catch (RemoteException e) {
			exception = e;
		} catch (ClassNotFoundException e) {
			exception = e;
		} catch (DeploymentException e) {
			exception = e;
		} catch (SQLException e) {
			exception = e;
		} catch (IOException e) {
			exception = e;
		} catch (ProcessAnalysisException e) {
			exception = e;
		} catch (SAXException e) {
			exception = e;
		} catch (ServiceException e) {
			exception = e;
		} catch (URISyntaxException e) {
			exception = e;
		}
		if(exception != null){
			logger.warning("failed to deploy BPEL");
			throw new ServiceNotActivatableException(
					service.getGridId(), service.getServiceId()
					, exception);
		}
	}

	private static void deactivateExternalService(ExternalService service){

	}

	private static void deactivateBPELService(
			BPELService service, ProcessDeployer deployer)
	throws IOException, ServiceException{
		deployer.undeploy(service);
	}

	private static Logger logger = Logger.getLogger(
			ServiceActivator.class.getName());
}
