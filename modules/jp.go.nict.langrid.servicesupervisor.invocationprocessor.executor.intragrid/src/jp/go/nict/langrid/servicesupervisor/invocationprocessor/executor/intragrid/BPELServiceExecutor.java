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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.param.HttpServletRequestParameterContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.NoValidEndpointsException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ProcessFailedException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ServiceExecutor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ServiceInvoker;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.TooManyCallNestException;

public class BPELServiceExecutor implements ServiceExecutor{
	public BPELServiceExecutor(
			String servicesUrl, String defaultAppAuthKey
			, int connectionTimeout, int readTimeout){
		this.servicesUrl = servicesUrl;
		this.defaultAppAuthKey = defaultAppAuthKey;
		this.connectionTimeout = connectionTimeout;
		this.readTimeout = readTimeout;
	}

	public List<ServiceEndpoint> getEndpoints(
			Service service, String protocol)
	throws DaoException{
		BPELService bs = (BPELService)service;
		String userName = defaultAppAuthKey;
		try{
			URL url = new URL(servicesUrl + "/" + bs.getDeployedId());
			return Arrays.asList(new ServiceEndpoint(
					service.getGridId(), service.getServiceId(), protocol
					, url, bs.getAppAuthKey(), userName, true
					));
		} catch(MalformedURLException e){
			throw new DaoException(e);
		}
	}

	public void invokeRequest(
			ServletContext servletContext
			, HttpServletRequest request, HttpServletResponse response 
			, ServiceContext serviceContext, DaoContext daoContext
			, Service service, List<ServiceEndpoint> endpoint
			, String additionalUrlPart, Map<String, String> headers
			, byte[] input
			)
	throws DaoException, TooManyCallNestException, NoValidEndpointsException
	, ProcessFailedException, IOException{
		String appAuthKey = null;
		if(service.getAppAuthKey() != null){
			appAuthKey = service.getAppAuthKey();
		} else{
			appAuthKey = defaultAppAuthKey;
		}

		int readTimeoutMillis = new HttpServletRequestParameterContext(request).getInteger(
				"langrid.timeout", 0);
		if(readTimeoutMillis == 0){
			readTimeoutMillis = service.getTimeoutMillis();
			if(readTimeoutMillis == 0){
				readTimeoutMillis = readTimeout;
			}
		}

		doInvoke(
				serviceContext, service.getServiceId()
				, additionalUrlPart, appAuthKey, endpoint.get(0), headers
				, input, response
				, connectionTimeout, readTimeoutMillis
				, service.getInstanceType()
				);
	}

	private static void doInvoke(
			ServiceContext context, String serviceId
			, String additionalUrlPart, String serviceAppAuthKey, ServiceEndpoint endpoint
			, Map<String, String> headers
			, byte[] input, HttpServletResponse output
			, int conTimeoutMillis, int readTimeoutMillis, InstanceType instanceType
			)
	throws IOException, NoValidEndpointsException, ProcessFailedException{
		StringBuilder faults = new StringBuilder();
		ServiceEndpoint ep = endpoint;
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
			userName = serviceAppAuthKey;
			password = context.getAuthUserGridId() + ":" + context.getAuthUser();
		}
		try{
			ServiceInvoker.invoke(
					context.getSelfGridId(), url, userName, password, headers
					, new ByteArrayInputStream(input), output, output.getOutputStream()
					, conTimeoutMillis, readTimeoutMillis
					);
			return;
		} catch(SocketTimeoutException e){
			logger.warning(
					"invocation timed out. sid: " + serviceId
					+ ", url: " + url
					+ ", timeoutMillis: " + readTimeoutMillis
					);
			faults.append("invocation timed out. timeoutMillis: ");
			faults.append(readTimeoutMillis);
		} catch(IOException e){
			logger.log(Level.WARNING
					, "invocation failed. sid: " + serviceId
					+ ", url: " + url
					, e);
			faults.append("invocation failed with exception: ");
			faults.append(e.toString());
		}
		throw new ProcessFailedException(faults.toString());
	}

	private String servicesUrl;
	private String defaultAppAuthKey;
	private int connectionTimeout;
	private int readTimeout;

	private static Logger logger = Logger.getLogger(BPELServiceExecutor.class.getName());
}
