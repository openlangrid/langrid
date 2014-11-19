/*
 * $Id: AppAuthEndpointRewriter.java 1187 2014-04-10 14:25:28Z t-nakaguchi $
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
public class AppAuthEndpointRewriter extends AbstractEndpointRewriter{
	@Override
	public void extractProperties(
			ServiceContext serviceContext
			, Map<String, Object> properties){
		super.extractProperties(serviceContext, properties);
		properties.put(prefix + ".key", serviceContext.getInitParameter("langrid.appAuthKey"));
		properties.put(prefix + ".userId", serviceContext.getAuthUser());
	}

	@Override
	public Endpoint rewrite(
			Endpoint original, Map<String, Object> properties
			, URI processNamespace, String partnerLinkName, URI serviceNamespace
			)
	{
		String key = (String)properties.get(prefix + ".key");
		String userId = (String)properties.get(prefix + ".userId");
		if(key == null || userId == null) return original;
		Endpoint ep = new Endpoint(
				original.getServiceId()
				, original.getAddress(), key, userId
				);
		ep.setProtocol(original.getProtocol());
		return ep;
	}

	private static String prefix = AppAuthEndpointRewriter.class.getName();
}
