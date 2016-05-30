/*
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
package jp.go.nict.langrid.foundation.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.parameter.ParameterLoader;
import jp.go.nict.langrid.commons.parameter.ParameterRequiredException;
import jp.go.nict.langrid.commons.parameter.annotation.Parameter;
import jp.go.nict.langrid.commons.parameter.annotation.ParameterConfig;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.foundation.AbstractLangridServlet;
import jp.go.nict.langrid.management.logic.ServiceLogic;
import jp.go.nict.langrid.management.logic.ServiceLogicException;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class WSDLGeneratorServlet
extends AbstractLangridServlet
{
	/**
	 * 
	 * 
	 * @author Takao Nakaguchi
	 * @author $Author: t-nakaguchi $
	 * @version $Revision: 497 $
	 */
	@ParameterConfig(prefix="langrid.")
	public static class Parameters{
		/**
		 * 
		 * 
		 */
		@Parameter(name="wsdlCacheCapacity", defaultValue="16")
		public int cacheCapacity;

		/**
		 * 
		 * 
		 */
		@Parameter(name="wsdlCacheTtlSec", defaultValue="600")
		public int cacheTtlSec;

		/**
		 * 
		 * 
		 */
		@Parameter(name="wsdlCacheMaxWsdlSize", defaultValue="65536")
		public int cacheMaxWsdlSize;
	}

	public void init() throws ServletException{
		super.init();
		try{
			new ParameterLoader().load(params, getParameterContext());
			coreNodeUrl = new URL(getParameterContext().getValue("langrid.node.url"));
			if(coreNodeUrl == null){
				throw new ServletException("Parameter langrid.node.url must not be null.");
			}
		} catch(MalformedURLException e){
			throw new ServletException(e);
		} catch(ParameterRequiredException e){
			throw new ServletException(e);
		}
		wsdlCacheForService.setCapacity(params.cacheCapacity);
	}

	protected void doPrepare(DaoFactory daoFactory)
	throws DaoException{
	}

	protected void doProcess(
			HttpServletRequest request, HttpServletResponse response
			, ServiceContext serviceContext, DaoContext daoContext
			, String userId, String serviceGridId, String serviceId
			)
			throws IOException, MalformedURLException, ServletException
			, ServiceNotFoundException, DaoException
	{
		boolean forServiceType = request.getServletPath().endsWith("/st");

		String key = (forServiceType ? "st:" : "") + serviceGridId + ":" + serviceId;
		Cache cache = forServiceType ? wsdlCacheForServiceType : wsdlCacheForService;
		byte[] wsdl = null;
		try{
			wsdl = (byte[])cache.getFromCache(key, params.cacheTtlSec);
		} catch(NeedsRefreshException e){
			try{
				wsdl = forServiceType ?
						generateWsdlForServiceType(daoContext, serviceGridId, serviceId) :
						generateWsdlForService(daoContext, serviceGridId, serviceId);
			} catch(ServiceLogicException ex){
				throw new ServletException(ex);
			} finally{
				if(wsdl != null && wsdl.length < params.cacheMaxWsdlSize){
					cache.putInCache(key, wsdl);
				} else{
					cache.cancelUpdate(key);
				}
			}
		}
		if(wsdl == null){
			throw new ServletException("failed to generate wsdl.");
		}
		String ua = request.getHeader("User-Agent");
		if(ua != null && ua.indexOf("AppleWebKit") == -1){
			response.setHeader("Content-type", "text/xml");
		} else{
			response.setHeader("Content-type", "text/plain");
		}
		OutputStream os = response.getOutputStream();
		os.write(wsdl);
		os.flush();
	}

	private byte[] generateWsdlForService(DaoContext daoContext, String gridId, String serviceId)
	throws DaoException, IOException, ServiceLogicException{
		return new ServiceLogic("").getServiceWsdl(coreNodeUrl.toString(), gridId, serviceId);
	}

	private byte[] generateWsdlForServiceType(DaoContext daoContext, String gridId, String serviceId)
	throws DaoException, IOException, ServiceLogicException{
		return new ServiceLogic("").getServiceTypeWsdl(coreNodeUrl.toString(), gridId, serviceId
				, getGridId());
	}

	private Parameters params = new Parameters();
	private URL coreNodeUrl;
	private static Cache wsdlCacheForService = new Cache(true, false, false);
	private static Cache wsdlCacheForServiceType = new Cache(true, false, false);

	private static final long serialVersionUID = -1714896927544856288L;
}
