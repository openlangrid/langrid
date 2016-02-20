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
package jp.go.nict.langrid.cosee;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.binding.DynamicBindingUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.rpc.TransportHeader;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.cosee.binding.TreeBindings;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class DynamicBindingRewriter extends AbstractEndpointRewriter{
	@SuppressWarnings("unchecked")
	public void extractProperties(
			ServiceContext serviceContext
			, Map<String, Object> properties)
	{
		super.extractProperties(
				serviceContext, properties);

		try{
			TreeBindings treeBindings = (TreeBindings)properties.get("treeBindings");
			if(treeBindings == null){
				treeBindings = new TreeBindings();
			}
			treeBindings.merge(new TreeBindings(
					(List<RpcHeader>)properties.get(
							LangridConstants.ACTOR_SERVICE_TREEBINDING
							)
					));
			properties.put("treeBindings", treeBindings);
			properties.put("treeBindings.authKey", serviceContext.getInitParameter("langrid.appAuthKey"));
			properties.put("treeBindings.authUserId", serviceContext.getAuthUser());
		} catch(ParseException e){
			logger.log(Level.WARNING
					, "failed to parse binding information."
					, e);
			properties.put("treeBindings", new TreeBindings());
		}
	}

	public Endpoint rewrite(
			Endpoint original, Map<String, Object> properties
			, URI processNamespace, String partnerLinkName, URI serviceNamespace,
			String methodName, String[] paramNames, Object[] args
			)
	{
		TreeBindings tb = (TreeBindings)properties.get("treeBindings");
		String serviceId = null;
		BindingNode node = tb.getBindingNodeFor(partnerLinkName, methodName, paramNames, args);
		if(node != null){
			serviceId = node.getServiceId();
			if(serviceId != null && node.getGridId() != null){
				serviceId = node.getGridId() + ":" + serviceId;
			}
		}
		if(serviceId == null || serviceId.trim().length() == 0){
			return original;
		}

		Endpoint ep = makeEndpoint(
				original, serviceId, properties
				);
		if(!serviceId.contains("://")){
			String key = (String)properties.get("treeBindings.authKey");
			String userId = (String)properties.get("treeBindings.authUserId");
			if(key != null && userId != null){
				ep.setUserName(key);
				ep.setPassword(userId);
			}
		}
		return ep;
	}

	@Override
	public void adjustProperties(
			Map<String, Object> properties
			, String partnerLinkName, String methodName, String[] paramNames, Object[] args)
	{
		super.adjustProperties(properties, partnerLinkName, methodName, paramNames, args);
		TreeBindings tb = (TreeBindings)properties.get("treeBindings");
		BindingNode node = tb.getBindingNodeFor(partnerLinkName, methodName, paramNames, args);
		if(node == null) return;

		{
			@SuppressWarnings("unchecked")
			List<TransportHeader> additionalHeaders = (List<TransportHeader>)properties.get("additionalMimeHeaders");
			for(TransportHeader th : node.getTransportHeaders()){
				additionalHeaders.add(th);
			}
		}
		{
			@SuppressWarnings("unchecked")
			List<RpcHeader> additionalHeaders = (List<RpcHeader>)properties.get("additionalRpcHeaders");
			for(RpcHeader th : node.getRpcHeaders()){
				additionalHeaders.add(th);
			}
		}

		Collection<BindingNode> children = node.getChildren();
		if(children != null && children.size() > 0){
			List<RpcHeader> headers = new ArrayList<RpcHeader>();
			headers.add(new RpcHeader(
					LangridConstants.ACTOR_SERVICE_TREEBINDING
					, "binding"
					, DynamicBindingUtil.encodeTree(children)
					));
			properties.put(
					LangridConstants.ACTOR_SERVICE_TREEBINDING
					, headers);
		} else{
			properties.remove(
					LangridConstants.ACTOR_SERVICE_TREEBINDING
					);
		}
	}

	private static Logger logger = LoggerFactory.create();
}
