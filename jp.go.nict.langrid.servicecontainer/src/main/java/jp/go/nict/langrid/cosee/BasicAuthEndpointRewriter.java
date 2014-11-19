/*
 * $Id: BasicAuthEndpointRewriter.java 1187 2014-04-10 14:25:28Z t-nakaguchi $
 *
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
package jp.go.nict.langrid.cosee;

import java.net.URI;
import java.util.Map;

import jp.go.nict.langrid.commons.ws.ServiceContext;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1187 $
 */
public class BasicAuthEndpointRewriter extends AbstractEndpointRewriter{
	@Override
	public void extractProperties(
			ServiceContext serviceContext
			, Map<String, Object> properties){
		super.extractProperties(serviceContext, properties);
		String userName = serviceContext.getInitParameter("langrid.basicAuthUserName");
		if(userName != null){
			properties.put(prefix + ".userName", userName);
			String password = serviceContext.getInitParameter("langrid.basicAuthPassword");
			properties.put(prefix + ".password", password != null ? password : "");
		}
	}

	@Override
	public Endpoint rewrite(
			Endpoint original, Map<String, Object> properties
			, URI processNamespace, String partnerLinkName, URI serviceNamespace
			)
	{
		String userName = (String)properties.get(prefix + ".userName");
		String password = (String)properties.get(prefix + ".password");
		if(userName != null){
			Endpoint ep = new Endpoint(
					original.getServiceId()
					, original.getAddress(), userName, password
					);
			ep.setProtocol(original.getProtocol());
			return ep;
		} else{
			return original;
		}
	}

	private static String prefix = BasicAuthEndpointRewriter.class.getName();
}
