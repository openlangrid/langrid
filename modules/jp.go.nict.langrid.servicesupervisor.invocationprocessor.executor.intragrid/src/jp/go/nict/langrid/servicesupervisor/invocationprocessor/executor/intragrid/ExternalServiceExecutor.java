/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intragrid;

import static jp.go.nict.langrid.dao.entity.ServiceContainerType.ATOMIC;
import static jp.go.nict.langrid.dao.entity.ServiceContainerType.COMPOSITE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.param.HttpServletRequestParameterContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.NoValidEndpointsException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ProcessFailedException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ServiceExecutor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ServiceInvoker;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.TooManyCallNestException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intragrid.balancer.Balancer;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intragrid.balancer.MinLatencyBalancer;

public class ExternalServiceExecutor implements ServiceExecutor{
	public ExternalServiceExecutor(
			String defaultAppAuthKey
			, int atomicConnectionTimeout, int atomicReadTimeout
			, int compositeConnectionTimeout, int compositeReadTimeout){
		this.defaultAppAuthKey = defaultAppAuthKey;
		this.atomicConnectionTimeout = atomicConnectionTimeout;
		this.atomicReadTimeout = atomicReadTimeout;
		this.compositeConnectionTimeout = compositeConnectionTimeout;
		this.compositeReadTimeout = compositeReadTimeout;
	}

	public List<ServiceEndpoint> getEndpoints(
			Service service, String protocol)
	throws DaoException{
		List<ServiceEndpoint> eps = new ArrayList<ServiceEndpoint>();
		for(ServiceEndpoint e : service.getServiceEndpoints()){
			String p = e.getProtocolId();
			if(p != null){
				if(e.getProtocolId().equals(protocol)){
					eps.add(e);
				}
			} else{
				if(protocol.equals(Protocols.DEFAULT)){
					eps.add(e);
				}
			}
		}
		return eps;
	}

	public void invokeRequest(
			ServletContext servletContext
			, HttpServletRequest request, HttpServletResponse response 
			, ServiceContext serviceContext, DaoContext daoContext
			, Service service, List<ServiceEndpoint> endpoints
			, String additionalUrlPart, Map<String, String> headers
			, byte[] input
			)
	throws DaoException, TooManyCallNestException, NoValidEndpointsException
	, ProcessFailedException, IOException{
		String appAuthKey = null;
		if(service.getContainerType().equals(COMPOSITE)){
			if(service.getAppAuthKey() != null){
				appAuthKey = service.getAppAuthKey();
			} else{
				appAuthKey = defaultAppAuthKey;
			}
		}

		ServiceContainerType ct = service.getContainerType();
		int conTimeoutMillis = atomicConnectionTimeout;
		if(ct != null && ct.equals(COMPOSITE)){
			conTimeoutMillis = compositeConnectionTimeout;
		}
		int readTimeoutMillis = new HttpServletRequestParameterContext(request).getInteger(
				"langrid.timeout", 0);
		if(readTimeoutMillis == 0){
			readTimeoutMillis = service.getTimeoutMillis();
			if(readTimeoutMillis == 0){
				if((ct == null || ct.equals(ATOMIC))){
					readTimeoutMillis = compositeReadTimeout;
				} else if(ct.equals(COMPOSITE)){
					readTimeoutMillis = atomicReadTimeout;
				}
			}
		}

		// System.out.println("atomic timeoutMillis = " + timeoutMillis);
		Set<ServiceEndpoint> modified = new LinkedHashSet<ServiceEndpoint>();
		Trio<Integer, Long, ServiceEndpoint> ret = doInvoke(
				serviceContext, service.getServiceId()
				, additionalUrlPart, appAuthKey, endpoints, headers
				, input, response
				, conTimeoutMillis, readTimeoutMillis
				, service.getInstanceType()
				, modified
				);

		if(service.getInstanceType().equals(InstanceType.EXTERNAL)){
			daoContext.beginTransaction();
			boolean committed = false;
			try{
				// update aveResponseMillis and experience count
				ServiceEndpoint invoked = ret.getThird();
				if(ret.getFirst() == 200 && ret.getSecond() != -1 && invoked != null){
					long d = ret.getSecond();
					long m = invoked.getAveResponseMillis();
					if(m == 0){
						invoked.setAveResponseMillis(d);
					} else{
						invoked.setAveResponseMillis(Math.round(d * 0.1 + m * 0.9));
					}
					invoked.setExperience(invoked.getExperience() + 1);
					modified.add(invoked);
				}
				mergeEndpoints(daoContext, modified);
				daoContext.commitTransaction();
				committed = true;
			} finally{
				if(!committed){
					daoContext.rollbackTransaction();
				}
			}
		}
	}

	private static Trio<Integer, Long, ServiceEndpoint> doInvoke(
			ServiceContext context, String serviceId
			, String additionalUrlPart, String serviceAppAuthKey, List<ServiceEndpoint> endpoints
			, Map<String, String> headers
			, byte[] input, HttpServletResponse output
			, int connectionTimeoutMillis, int readTimeoutMillis, InstanceType instanceType
			, Set<ServiceEndpoint> modifiedEndpoints
			)
	throws IOException, NoValidEndpointsException, ProcessFailedException{
		if(endpoints == null){
			throw new NoValidEndpointsException(
					"service " + serviceId + " has no endpoint."
					);
		}

		StringBuilder faults = new StringBuilder();
		List<ServiceEndpoint> activeEndpoints = new ArrayList<ServiceEndpoint>();
		for(ServiceEndpoint e : endpoints){
			if(!e.isEnabled()){
				// 
				// 
				Calendar dd = e.getDisabledByErrorDate();
				if(dd == null){
					// 
					// 
					continue;
				}
				dd.add(Calendar.DATE, 3);
				if(Calendar.getInstance().before(dd)){
					// 
					// 
					continue;
				}
				e.setEnabled(true);
				e.touchUpdatedDateTime();
				modifiedEndpoints.add(e);
			}
			activeEndpoints.add(e);
		}
		ServiceEndpoint ep = null;
		while(true){
			if(activeEndpoints.size() == 1){
				ep = activeEndpoints.get(0);
				activeEndpoints.clear();
			} else{
				ep = balancer.get().balance(serviceId, activeEndpoints);
				if(ep == null){
					throw new ProcessFailedException("failed to balance endpoints.");
				}
			}
			URL url;
			if(additionalUrlPart != null){
				try{
					url = URLUtil.mergePath(ep.getUrl(), additionalUrlPart);
				} catch(MalformedURLException e){
					logger.warning("failed to create url: " + ep.getUrl().toString()
							+ "  param: " + additionalUrlPart);
					url = ep.getUrl();
				}
			} else{
				url = ep.getUrl();
			}
			String userName = ep.getAuthUserName();
			String password = ep.getAuthPassword();
			if(serviceAppAuthKey != null){
				// application authentication
				userName = serviceAppAuthKey;
				password = context.getAuthUserGridId() + ":" + context.getAuthUser();
			}
			
			Exception exception = null;
			ByteArrayOutputStream errorOut = new ByteArrayOutputStream();
			int resCode = -1;
			long d = -1;
			try{
				long s = System.currentTimeMillis(); 
				resCode = ServiceInvoker.invoke(
						context.getSelfGridId(), url, userName, password, headers
						, new ByteArrayInputStream(input), output, errorOut
						, connectionTimeoutMillis
						, readTimeoutMillis // != 0 ? timeoutMillis : readTimeout
						);
				d = System.currentTimeMillis() - s;
				balancer.get().succeeded(ep, d, resCode);
				if(resCode == 200){
					modifiedEndpoints.add(ep);
					return Trio.create(resCode, d, ep);
				}
			} catch(SocketTimeoutException e){
				logger.warning(
						"invocation timed out. sid: " + serviceId
						+ ", url: " + url
						+ ", timeoutMillis: " + readTimeoutMillis // readTimeout
						);
				faults.append("invocation timed out. timeoutMillis: ");
				faults.append(readTimeoutMillis);  //readTimeout);
				exception = e;
			} catch(IOException e){
				logger.log(Level.WARNING
						, "invocation failed. sid: " + serviceId
						+ ", url: " + url
						, e);
				faults.append("invocation failed with exception: ");
				faults.append(e.toString());
				exception = e;
			}
			if(exception != null){
				balancer.get().failed(ep, d, resCode, exception);
			}

			// service invocation failed.
			if(activeEndpoints.size() > 1 &&
					resCode != 500 && resCode != 400 &&
					resCode != 405 && resCode != 415){
				// turn ep disabled if errors, except that are caused by SOAP Fault, occurred.
				ep.setEnabled(false);
				ep.setDisabledByErrorDate(Calendar.getInstance());
				if(exception != null){
					ep.setDisableReason(exception.toString());
				} else{
					String detail = "";
					try{
						detail = ": " + new String(errorOut.toByteArray(), "UTF-8");
					} catch(IOException e){
					}
					ep.setDisableReason("HTTP " + resCode + detail);
				}
				ep.touchUpdatedDateTime();
			}

			Iterator<ServiceEndpoint> i = activeEndpoints.iterator();
			boolean removed = false;
			while(i.hasNext()){
				ServiceEndpoint e = i.next();
				if(e.getUrl().equals(ep.getUrl())
						&& e.getServiceId().equals(ep.getServiceId())){
					i.remove();
					removed = true;
					break;
				}
			}
			modifiedEndpoints.add(ep);
			if(!removed){
				StreamUtil.transfer(
						new ByteArrayInputStream(errorOut.toByteArray())
						, output.getOutputStream()
						);
				return Trio.create(resCode, -1L, null);
			}
			if(activeEndpoints.size() == 0){
				// revive all endpoints since no endpoint alive.
				for(ServiceEndpoint e : endpoints){
					e.setEnabled(true);
					modifiedEndpoints.add(e);
				}
				// transfer latest response
				StreamUtil.transfer(
						new ByteArrayInputStream(errorOut.toByteArray())
						, output.getOutputStream()
						);
				return Trio.create(resCode, -1L, null);
			}
		}
	}

	private static void mergeEndpoints(DaoContext context, Collection<ServiceEndpoint> endpoints)
	throws DaoException{
		Map<Pair<String, URL>, ServiceEndpoint> eps
				= new HashMap<Pair<String, URL>, ServiceEndpoint>();
		for(ServiceEndpoint ep : endpoints){
			if(!ep.getClass().equals(ServiceEndpoint.class)) continue;
			eps.put(Pair.create(ep.getServiceId(), ep.getUrl()), ep);
		}
		for(ServiceEndpoint ep : eps.values()){
			context.mergeEntity(ep);
		}
	}

	private String defaultAppAuthKey;
	private int atomicReadTimeout = 0;
	private int atomicConnectionTimeout = 0;
	private int compositeReadTimeout = 0;
	private int compositeConnectionTimeout = 0;

	private static ThreadLocal<Balancer> balancer = new ThreadLocal<Balancer>(){
		protected Balancer initialValue() {
			BeanFactory f = new XmlBeanFactory(new ClassPathResource("/balancer.xml"));
			try{
				return (Balancer)f.getBean("balancer");
			} catch(NoSuchBeanDefinitionException e){
				logger.log(Level.WARNING, "failed to load balancer.", e);
				return new MinLatencyBalancer();
			}
		};
	};
	private static Logger logger = Logger.getLogger(ExternalServiceExecutor.class.getName());
}
