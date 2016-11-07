/*
 * $Id: AspectBase.java 1498 2015-02-13 03:50:47Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.cosee;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.cs.calltree.CallNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.rpc.TransportHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1498 $
 */
public class AspectBase{
	/**
	 * 
	 * 
	 */
	public AspectBase(EndpointRewriter[] rewriters){
		this.rewriters = rewriters;
	}

	protected void beginProcess(
			ServiceContext serviceContext, long processId)
	{
//		logger.info("process begin. tid: " + Thread.currentThread().getId()
//				+ "  pid: "+ processId);

		Map<String, Object> props = new HashMap<String, Object>();
		// 
		// 
		MimeHeaders mHeaders = serviceContext.getRequestMimeHeaders();
		extractHttpHeader(mHeaders, props, LangridConstants.HTTPHEADER_FROMADDRESS);
		extractHttpHeader(mHeaders, props, LangridConstants.HTTPHEADER_CORENODEURL);
		extractHttpHeader(mHeaders, props, LangridConstants.HTTPHEADER_CALLNEST);
		extractHttpHeader(mHeaders, props, LangridConstants.HTTPHEADER_TYPEOFUSE);
		extractHttpHeader(mHeaders, props, LangridConstants.HTTPHEADER_TYPEOFAPPPROVISION);
		extractHttpHeader(mHeaders, props, LangridConstants.HTTPHEADER_FEDERATEDCALL_BYPASSINGINVOCATION);
		// 呼出アドレスが無ければ足す。
		if(mHeaders.getHeader(LangridConstants.HTTPHEADER_FROMADDRESS) == null){
			props.put(
					LangridConstants.HTTPHEADER_FROMADDRESS
					, serviceContext.getRemoteAddress());
		}
		// SOAPヘッダ情報を抽出
		Iterable<RpcHeader> rHeaders = serviceContext.getRequestRpcHeaders();
		extractSoapHeader(rHeaders, props, LangridConstants.ACTOR_SERVICE_TREEBINDING);

		// calltree準備
		props.put("calltree", new ArrayList<CallNode>());
		// gridTracks準備
		props.put("gridTracks", new ArrayList<Pair<String, String>>());
		// additionalMimeHeaders準備
		props.put("additionalMimeHeaders", new ArrayList<TransportHeader>());
		// additionalRpcHeaders準備
		props.put("additionalRpcHeaders", new ArrayList<RpcHeader>());
		// responseHeaders準備
		props.put("responseHeaders", new ArrayList<RpcHeader>());

		// 各RewriterのextractPropertiesを呼び出す
		for(EndpointRewriter r : rewriters){
			r.extractProperties(
				serviceContext, props
				);
		}
		idToProperties.put(processId, props);
	}

	protected void initEndpointRewriters(
			ServiceContext serviceContext, long processId, EndpointRewriter[] rewriters){
		for(EndpointRewriter r : rewriters){
			r.extractProperties(
				serviceContext, idToProperties.get(processId)
				);
		}
	}

	protected Endpoint rewriteEndpoint(
			long processId, URI processUri
			, long invocationId, String partnerLinkName
			, URI serviceNamespace, Endpoint original
			){
		return rewriteEndpoint(processId, processUri
				, invocationId, partnerLinkName
				, serviceNamespace, original, rewriters);
	}

	protected Endpoint rewriteEndpoint(
			long processId, URI processUri
			, long invocationId, String partnerLinkName
			, URI serviceNamespace, Endpoint original
			, EndpointRewriter[] rewriters)
	{
		Map<String, Object> properties = idToProperties.get(processId);
		Endpoint ep = original;
		synchronized (properties) {
			if(properties == null){
				logger.severe(
						"[tid: "
						+ Thread.currentThread().getId()
						+ "] properteis for process:"
						+ processId
						+ " is null!! using original endpoint:"
						+ original.getAddress()
						+ " ["
						+ original.getUserName()
						+ ":********]"
						);
				return original;
			}

			for(EndpointRewriter r : rewriters){
				ep = r.rewrite(ep, properties, processUri
						, partnerLinkName, serviceNamespace);
			}
			if(ep.getServiceId() != null){
				properties.put("invocation#" + invocationId, ep.getServiceId());
			} else if(ep.getAddress() != null){
				properties.put("invocation#" + invocationId, ep.getAddress().toString());
			}
		}
		return ep;
	}

	protected void appendInvocationHeaders(
			long processId, long invocationId
			, String partnerLinkName
			, Map<String, Object> mimeHeaders
			, Collection<RpcHeader> rpcHeaders)
	{
		Map<String, Object> properties = idToProperties.get(processId);
		synchronized (properties) {
			if(properties == null){
				logger.severe("properties is null!");
				return;
			}

			Map<String, Object> tempProperties = new HashMap<String, Object>(
					properties
					);
			for(EndpointRewriter r : rewriters){
				r.adjustProperties(tempProperties, partnerLinkName);
			}

			// HTTPヘッダ情報をコピー
			copyHttpHeader(tempProperties, mimeHeaders, LangridConstants.HTTPHEADER_FROMADDRESS);
			copyHttpHeader(tempProperties, mimeHeaders, LangridConstants.HTTPHEADER_CALLNEST);
			copyHttpHeader(tempProperties, mimeHeaders, LangridConstants.HTTPHEADER_TYPEOFUSE);
			copyHttpHeader(tempProperties, mimeHeaders, LangridConstants.HTTPHEADER_TYPEOFAPPPROVISION);
			copyHttpHeader(tempProperties, mimeHeaders, LangridConstants.HTTPHEADER_FEDERATEDCALL_BYPASSINGINVOCATION);
			copyAdditionalMimeHeaders(tempProperties, mimeHeaders);

			// Rpcヘッダ情報をコピー
			copyRpcHeader(tempProperties, rpcHeaders, LangridConstants.ACTOR_SERVICE_TREEBINDING);
			copyAdditionalRpcHeaders(tempProperties, rpcHeaders);
		}
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void processInvocationResponseHeaders(
			long processId, long invocationId, String invocationName, long deltaTime
			, MimeHeaders mimeHeaders
			, Iterable<RpcHeader> rpcHeaders, RpcFault rpcFault)
	{
		Map<String, Object> properties = idToProperties.get(processId);
		if(properties == null){
			logger.severe("properties is null!");
			return;
		}
		Collection<CallNode> nodes = (Collection<CallNode>)properties.get(
				"calltree");
		String serviceId = properties.get("invocation#" + invocationId).toString();
		try{
			CallNode node = CallTreeUtil.createNode(mimeHeaders, rpcHeaders);
			node.setServiceId(serviceId);
			node.setResponseTimeMillis(deltaTime);
			node.setInvocationName(invocationName);
			if(rpcFault != null){
				node.setFaultCode(rpcFault.getFaultCode());
				node.setFaultString(rpcFault.getFaultString());
	//			logger.info("soap fault: " + fault.getFaultCode() + "[" + fault.getFaultString() + "]");
			}
	//		logger.info("receive invocation response.  tid: " + Thread.currentThread().getId()
	//				+ "  pid: " + processId + "  iid: " + invocationId);
			nodes.add(node);
		} catch(ParseException e){
			logger.log(Level.WARNING
					, "failed to add call node for service id \"" +
					serviceId + "\" because of invalid calltree text."
					, e);
		}
		Collection<Pair<String, String>> gridTracks = (Collection<Pair<String, String>>)properties.get("gridTracks");
		gridTracks.add(Pair.create(
				invocationName,
				MimeHeadersUtil.getJoinedValue(mimeHeaders, LangridConstants.HTTPHEADER_GRIDTRACK))
				);
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void appendResponseHeaders(long processId
			, MimeHeaders mimeHeaders, Collection<RpcHeader> rpcHeaders){
		Map<String, Object> properties = idToProperties.get(processId);
		if(properties == null){
			logger.severe("properties is null!");
			return;
		}
		Collection<CallNode> nodes = (Collection<CallNode>)properties.get(
				"calltree");
		if(nodes.size() > 0){
			rpcHeaders.add(new RpcHeader(
					LangridConstants.ACTOR_SERVICE_CALLTREE
					, "calltree", CallTreeUtil.encodeTree(nodes)
					));
		}

		Collection<Pair<String, String>> gridTracks = (Collection<Pair<String, String>>)
				properties.get("gridTracks");
		if(gridTracks.size() > 0){
			String v = "(" + StringUtil.join(
					gridTracks.toArray(new Pair[]{}),
					p -> p.getFirst() + ":" + p.getSecond(),
					",") + ")";
			mimeHeaders.addHeader(LangridConstants.HTTPHEADER_GRIDTRACK, v);
		}

		Collection<RpcHeader> headers = (Collection<RpcHeader>)properties.get(
				"responseHeaders");
		if(headers.size() > 0){
			for(RpcHeader h : headers){
				rpcHeaders.add(h);
			}
		}
	}

	protected void endProcess(long processId){
//		logger.info("process end. tid: " + Thread.currentThread().getId()
//				+ "  pid: "+ processId);
		idToProperties.remove(processId);
	}

	protected Map<String, Object> getProperties(long processId){
		return idToProperties.get(processId);
	}

	private void extractHttpHeader(
			MimeHeaders headers
			, Map<String, Object> properties
			, String key)
	{
		String[] values = headers.getHeader(key);
		if(values != null && values.length > 0){
			String v = MimeHeadersUtil.getJoinedValue(headers, key);
			if(v != null){
				properties.put(key, v);
			}
		}
	}

	private void extractSoapHeader(
			Iterable<RpcHeader> rpcHeaders
			, Map<String, Object> properties
			, String namespace)
	{
		List<RpcHeader> headers = new ArrayList<RpcHeader>();
		properties.put(namespace, headers);
		for(RpcHeader h : rpcHeaders){
			if(h.getNamespace().equals(namespace))
				headers.add(h.clone());
		}
	}

	private void copyHttpHeader(Map<String, Object> properties
			, Map<String, Object> mimeHeaders
			, String key){
		String value = (String)properties.get(key);
		if(value != null){
			mimeHeaders.put(key, value);
		}
	}

	private void copyAdditionalMimeHeaders(Map<String, Object> properties, Map<String, Object> mimeHeaders){
		@SuppressWarnings("unchecked")
		List<TransportHeader> additionalHeaders = (List<TransportHeader>)properties.get("additionalMimeHeaders");
		if(additionalHeaders == null) return;
		for(TransportHeader v : additionalHeaders){
			mimeHeaders.put(v.getName(), v.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private void copyRpcHeader(Map<String, Object> properties
			, Collection<RpcHeader> headers
			, String key){
		List<RpcHeader> hds = (List<RpcHeader>)properties.get(key);
		if(hds == null) return;
		for(RpcHeader h : hds){
			headers.add(h.clone());
		}
	}

	private void copyAdditionalRpcHeaders(Map<String, Object> properties, Collection<RpcHeader> rpcHeaders){
		@SuppressWarnings("unchecked")
		List<RpcHeader> additionalHeaders = (List<RpcHeader>)properties.get("additionalRpcHeaders");
		if(additionalHeaders == null) return;
		for(RpcHeader v : additionalHeaders){
			rpcHeaders.add(v);
		}
	}

	private EndpointRewriter[] rewriters;
	private Map<Long, Map<String, Object>> idToProperties
		= new ConcurrentHashMap<Long, Map<String, Object>>();
	private static Logger logger = LoggerFactory.create();
}
