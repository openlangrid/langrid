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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.invm;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.entity.WebappService;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.AbstractExecutor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.Executor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ExecutorParams;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.NoValidEndpointsException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ProcessFailedException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.TooManyCallNestException;

public class InVmExecutor
extends AbstractExecutor
implements Executor {
	public InVmExecutor(DaoFactory daoFactory, ExecutorParams params)
	throws DaoException{
		this.serviceDao = daoFactory.createServiceDao();
		this.maxCallNest = params.maxCallNest;
		this.webappTimeoutEnabled = params.webappTimeoutEnabled;
		this.atomicServiceReadTimeout = params.atomicServiceReadTimeout;
		this.compositeServiceReadTimeout = params.compositeServiceReadTimeout;
	}

	public void execute(
			ServletContext servletContext
			, HttpServletRequest request, HttpServletResponse response
			, ServiceContext serviceContext, DaoContext daoContext
			, String serviceGridId, String serviceId
			, Map<String, String> headers
			, String additionalUrlPart, String protocol,byte[] input
			)
	throws DaoException, TooManyCallNestException, NoValidEndpointsException, ProcessFailedException
	, IOException{
		Service service = null;
		List<ServiceEndpoint> endpoints = null;

		WebappServiceExecutor executor = new WebappServiceExecutor(
				webappTimeoutEnabled, atomicServiceReadTimeout, compositeServiceReadTimeout);
		daoContext.beginTransaction();
		try{
			service = serviceDao.getService(serviceGridId, serviceId);
			endpoints = executor.getEndpoints(service, protocol);
		} finally{
			daoContext.commitTransaction();
		}
		if(endpoints.size() == 0){
			throw new NoValidEndpointsException(
					"No valid endpoints exist for service: " + serviceGridId + ":" + serviceId);
		}
		adjustHeaders(service, headers);

		String nestCountString = headers.get(LangridConstants.HTTPHEADER_CALLNEST);
		if(nestCountString != null){
			int nest = Integer.parseInt(nestCountString) + 1;
			if(nest > maxCallNest){
				throw new TooManyCallNestException(maxCallNest, nest);
			} else{
				headers.put(
						LangridConstants.HTTPHEADER_CALLNEST
						, Integer.toString(nest));
			}
		}

		if(service instanceof WebappService){
			executor.invokeRequest(
						servletContext, request, response
						, serviceContext, daoContext
						, service, endpoints
						, additionalUrlPart, headers, input
						);
		} else{
			throw new ProcessFailedException(
					"no suitable executor for service: " + service.getServiceId());
		}
	}

	private ServiceDao serviceDao;
	private int maxCallNest;
	private boolean webappTimeoutEnabled;
	private int atomicServiceReadTimeout;
	private int compositeServiceReadTimeout;
}
