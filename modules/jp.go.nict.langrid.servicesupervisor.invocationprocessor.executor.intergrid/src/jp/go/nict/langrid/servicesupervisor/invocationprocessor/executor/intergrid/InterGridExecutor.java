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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intergrid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.management.logic.FederationLogic;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.AbstractExecutor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.Executor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ExecutorParams;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.NoValidEndpointsException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ProcessFailedException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ServiceInvoker;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.TooManyCallNestException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class InterGridExecutor extends AbstractExecutor implements Executor {
	public InterGridExecutor(DaoFactory daoFactory, ExecutorParams params)
	throws DaoException{
		this.gridDao = daoFactory.createGridDao();
		this.federationLogic = new FederationLogic();
		this.serviceDao = daoFactory.createServiceDao();
		this.connectionTimeout = params.interGridCallConnectionTimeout;
		this.readTimeout = params.interGridCallReadTimeout;
		this.maxCallNest = params.maxCallNest;
	}

	@Override
	public void execute(
			ServletContext servletContext
			, HttpServletRequest request, HttpServletResponse response
			, ServiceContext serviceContext, DaoContext daoContext
			, String serviceGridId, String serviceId
			, Map<String, String> headers
			, String additionalUrlPart, String protocol, byte[] input
			)
	throws DaoException, TooManyCallNestException, NoValidEndpointsException, ProcessFailedException, IOException{
		Service service = null;
		URL url = null;
		String authId = null;
		String authPasswd = null;
		daoContext.beginTransaction();
		try{
			Federation f = null;
			List<Federation> path = federationLogic.getShortestPath(serviceContext.getSelfGridId(), serviceGridId);
			if(path.size() > 0){
				if(serviceContext.getRequestMimeHeaders().getHeader(
						LangridConstants.HTTPHEADER_FEDERATEDCALL_BYPASSINGINVOCATION) != null){
					// get farthest
					f = path.get(path.size() - 1);
				} else{
					// get nearest
					f = path.get(0);
				}
			}
			if(f == null){
				throw new ProcessFailedException("no route to target grid: " + serviceGridId);
			}
			if(serviceGridId.equals(serviceContext.getSelfGridId())){
				service = serviceDao.getService(serviceContext.getSelfGridId(), serviceId);
				if(service == null){
					throw new ProcessFailedException("no service: " + serviceId +
							" exists at grid: " + serviceGridId);
				}
			}
			Grid g = gridDao.getGrid(f.getTargetGridId());
			String gurl = g.getUrl();
			if(!gurl.endsWith("/")) gurl += "/";
			url = new URL(gurl + "invoker/" + serviceGridId + ":" + serviceId
					+ ((additionalUrlPart != null) ? additionalUrlPart : ""));
			authId = f.getTargetGridUserId();
			authPasswd = f.getTargetGridAccessToken();
		} catch(MalformedURLException e){
			throw new ProcessFailedException(e);
		} finally{
			daoContext.commitTransaction();
		}
		adjustHeaders(serviceContext, service, headers);

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

		ServiceInvoker.invoke(
				url, authId, authPasswd, headers
				, new ByteArrayInputStream(input), response, response.getOutputStream()
				, connectionTimeout, readTimeout
				);
	}

	protected void adjustHeaders(ServiceContext context, Service service, Map<String, String> headers){
		if(service != null) super.adjustHeaders(service, headers);
		headers.put(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_SOURCEGRIDID
				, context.getSelfGridId()
				);
		headers.put(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_CALLERUSER
				, context.getAuthUserGridId() + ":" + context.getAuthUser()
				);

	}
	
	private FederationLogic federationLogic;
	private GridDao gridDao;
	private ServiceDao serviceDao;
	private int connectionTimeout;
	private int readTimeout;
	private int maxCallNest;
	
	//private static Logger logger = Logger.getLogger(IntraGridExecutor.class.getName());
}
