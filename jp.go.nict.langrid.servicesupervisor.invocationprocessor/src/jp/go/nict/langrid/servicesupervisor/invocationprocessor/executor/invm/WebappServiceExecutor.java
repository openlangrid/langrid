/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010-2011 NICT Language Grid Project.
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

import static jp.go.nict.langrid.dao.entity.ServiceContainerType.ATOMIC;
import static jp.go.nict.langrid.dao.entity.ServiceContainerType.COMPOSITE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.util.Holder;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.param.HttpServletRequestParameterContext;
import jp.go.nict.langrid.commons.ws.servlet.InputStreamServletInputStream;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.NoValidEndpointsException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ProcessFailedException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ServiceExecutor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.TooManyCallNestException;

/**
 * Service executor for webapp (services deployed to same web application)
 * @author Takao Nakaguchi
 */
public class WebappServiceExecutor implements ServiceExecutor{
	public WebappServiceExecutor(
			boolean enableTimeout, int atomicReadTimeout, int compositeReadTimeout
			) {
		this.timeoutEnabled = enableTimeout;
		this.atomicReadTimeout    = atomicReadTimeout;
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

	@Override
	public void invokeRequest(
			ServletContext servletContext
			, HttpServletRequest request, HttpServletResponse response 
			, ServiceContext serviceContext, DaoContext daoContext
			, Service service, List<ServiceEndpoint> endpoints
			, String additionalUrlPart, Map<String, String> headers, byte[] input
			)
	throws DaoException, TooManyCallNestException, NoValidEndpointsException, ProcessFailedException, IOException{
		String serviceId = service.getServiceId();
		StringBuilder faults = new StringBuilder();
		URL url;
		ServiceEndpoint endpoint = endpoints.get(0);
		
		if(additionalUrlPart != null){
			try{
				url = URLUtil.mergePath(endpoint.getUrl(), additionalUrlPart);
			} catch(MalformedURLException e){
				logger.warning("failed to create url: " + endpoint.getUrl().toString()
						+ "  param: " + additionalUrlPart);
				url = endpoint.getUrl();
			}
		} else{
			url = endpoint.getUrl();
		}

		int timeoutMillis = new HttpServletRequestParameterContext(request).getInteger(
				"langrid.timeout", 0);
		if(timeoutMillis == 0){
			timeoutMillis = service.getTimeoutMillis();
			if(timeoutMillis == 0){
				ServiceContainerType ct = service.getContainerType();
				if(ct == null || ct.equals(ATOMIC)){
					timeoutMillis = atomicReadTimeout;
				} else if(ct.equals(COMPOSITE)){
					timeoutMillis = compositeReadTimeout;
				}
			}
		}

		if(timeoutEnabled && timeoutMillis > 0){
			final ServletRequest wrequest = createServletRequest(request, headers, input);
			final RequestDispatcher rd = servletContext.getRequestDispatcher(url.getPath());
			final HttpServletResponse res = response;
			final Holder<Exception> uncaughtException = new Holder<Exception>();
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						rd.forward(wrequest, res);
					} catch (ServletException e) {
						uncaughtException.set(e);
					} catch (IOException e) {
						uncaughtException.set(e);
					}
				}
			});
			thread.start();
			try{
				thread.join(timeoutMillis);
			} catch(InterruptedException e){
			}
			if(thread.isAlive()){
				//thread.stop();
				response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
			} else{
				Exception e = uncaughtException.get();
				if ( e != null ) {
					logger.log(Level.WARNING
							, "invocation failed. sid: " + serviceId
							+ ", url: " + url
							, e);
					faults.append("invocation failed with exception: ");
					faults.append(e.toString());
					throw new ProcessFailedException(faults.toString());
				}
			}
		}  else {
			try{
				servletContext.getRequestDispatcher(url.getPath()).forward(
						createServletRequest(request, headers, input)
						, response);
				return;
			} catch(ServletException e){
				logger.log(Level.WARNING
						, "invocation failed. sid: " + serviceId
						+ ", url: " + url
						, e);
				faults.append("invocation failed with exception: ");
				faults.append(e.toString());
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
	}

	private ServletRequest createServletRequest(
			HttpServletRequest request
			, final Map<String, String> headers
			, final byte[] input){
		return new HttpServletRequestWrapper(request){
			@Override
			public String getHeader(String name) {
				String v = super.getHeader(name);
				if(v != null){
					return v;
				} else{
					return headers.get(name);
				}
			}
			@Override
			public ServletInputStream getInputStream() throws IOException {
				return new InputStreamServletInputStream(new ByteArrayInputStream(input));
			}
		};
	}

	private boolean timeoutEnabled;
	private int atomicReadTimeout;
	private int compositeReadTimeout;
	private static Logger logger = Logger.getLogger(WebappServiceExecutor.class.getName());
}
