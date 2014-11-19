/*
 * $Id: AbstractEndpointRewriter.java 1187 2014-04-10 14:25:28Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.cosee;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.parameter.ParameterLoader;
import jp.go.nict.langrid.commons.parameter.ParameterRequiredException;
import jp.go.nict.langrid.commons.parameter.annotation.Parameter;
import jp.go.nict.langrid.commons.parameter.annotation.ParameterConfig;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.BasicAuthUtil;
import jp.go.nict.langrid.commons.ws.Constants;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.param.ServiceContextParameterContext;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1187 $
 */
public abstract class AbstractEndpointRewriter implements EndpointRewriter{
	/**
	 * 
	 * 
	 * @author Takao Nakaguchi
	 * @author $Author: t-nakaguchi $
	 * @version $Revision: 1187 $
	 */
	@ParameterConfig(prefix="langrid.")
	public static class Parameters{
		/**
		 * 
		 * 
		 */
		@Parameter(defaultValue="http:/")
		public String coreNodeUrl;

		/**
		 * 
		 * 
		 */
		@Parameter(name="targetNamespaceCacheSize", defaultValue="200")
		public int tnsCapacity;

		/**
		 * 
		 * 
		 */
		@Parameter(name="targetNamespaceCacheTtlSec", defaultValue="600")
		public int tnsTtlSeconds;

		/**
		 * 
		 * 
		 */
		@Parameter(name="serviceIdCacheSize", defaultValue="200")
		public int sidCapacity;

		/**
		 * 
		 * 
		 */
		@Parameter(name="serviceIdCacheTtlSec", defaultValue="600")
		public int sidTtlSeconds;
	}

	public void extractProperties(
			ServiceContext serviceContext
			, Map<String, Object> properties)
	{
		if(serviceContext.getRequestMimeHeaders() != null){
			String header = MimeHeadersUtil.getJoinedValue(
					serviceContext.getRequestMimeHeaders()
					, Constants.HEADER_AUTHORIZATION);
			Pair<String, String> p = BasicAuthUtil.decode(header);
			if(p != null){
				properties.put("userName", p.getFirst());
				properties.put("password", p.getSecond());
			}
			properties.put("coreNodeUrl"
					, MimeHeadersUtil.getJoinedValue(
							serviceContext.getRequestMimeHeaders()
							, LangridConstants.HTTPHEADER_CORENODEURL));
		}
		prepare(serviceContext);
	}

	public void adjustProperties(
			Map<String, Object> properties
			, String partnerLinkName){
	}

	protected Endpoint makeEndpoint(
			Endpoint original, String serviceId
			, Map<String, Object> properties
			)
	{
		URI url = null;
		String query = "";
		if(serviceId.startsWith("http://") || serviceId.startsWith("https://")){
			try{
				url = new URI(serviceId);
				return new Endpoint(url);
			} catch(URISyntaxException e){
			}
		} else{
			String[] urls = serviceId.split("\\?", 1);
			serviceId = urls[0];
			if(urls.length == 2){
				query = "?" + urls[1];
			}
		}
		try{
			String core = (String)properties.get("coreNodeUrl");
			if(core == null){
				core = coreNodeUrl;
			}
			url = new URI(
					coreNodeUrl
					+ (coreNodeUrl.endsWith("/") ? "" : "/")
					+ "invoker/" + serviceId + query
					);
		} catch(URISyntaxException e){
			logger.log(Level.SEVERE, "invalid serviceId: " + serviceId, e);
			return original;
		}
		return new Endpoint(
				serviceId, url
				, (String)properties.get("userName")
				, (String)properties.get("password")
				);
	}

	protected Endpoint makeNoAccessPermissionEndpoint(String userId){
		String exceptionUrl = coreNodeUrl + "exception/";
		try{
			URI url = new URI(
					exceptionUrl + "NoAccessPermissionException?userId=" + userId);
			return new Endpoint(null, url, "anonymous", "");
		} catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}

	private static synchronized void prepare(ServiceContext context){
		if(initialized) return;

		Parameters p = new Parameters();
		try{
			new ParameterLoader().load(
					p
					, new ServiceContextParameterContext(context)
					);
		} catch(ParameterRequiredException e){
			throw new RuntimeException(e.getParameterName() + " is not defined.");
		}

		coreNodeUrl = p.coreNodeUrl;
		initialized = true;
	}

	private static boolean initialized;
	private static String coreNodeUrl = "http:/";
	private static Logger logger = LoggerFactory.create();
}
