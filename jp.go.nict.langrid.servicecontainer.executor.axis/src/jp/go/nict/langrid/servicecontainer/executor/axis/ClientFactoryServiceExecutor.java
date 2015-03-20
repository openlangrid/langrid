/*
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.executor.axis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.servicecontainer.executor.StreamingNotifier;
import jp.go.nict.langrid.servicecontainer.executor.StreamingReceiver;
import jp.go.nict.langrid.servicecontainer.service.component.AbstractServiceExecutor;

/**
 * 
 * 
 */
public class ClientFactoryServiceExecutor
extends AbstractServiceExecutor
implements InvocationHandler{
	public ClientFactoryServiceExecutor(
			String invocationName, long invocationId, Endpoint endpoint
			, Class<?> interfaceClass, ClientFactory clientFactory){
		super(invocationName, invocationId, endpoint);
		this.clientFactory = clientFactory;
		this.interfaceClass = interfaceClass;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if(StreamingNotifier.class.isAssignableFrom(method.getDeclaringClass())){
			if(method.getName().equals("setReceiver") && method.getParameterTypes().length == 1
					&& method.getParameterTypes()[0].equals(StreamingReceiver.class)){
				// Axis Service Executor not support StreamingNotifier
				return null;
			}
		}
		Map<String, Object> httpHeaders = new Hashtable<String, Object>();
		List<RpcHeader> headers = new ArrayList<RpcHeader>();
		Pair<Endpoint, Long> r = preprocess(httpHeaders, headers);

		Endpoint endpoint = r.getFirst();
		long iid = r.getSecond();

		Object client = clientFactory.create(interfaceClass, endpoint.getAddress().toURL());
		RequestAttributes reqAttrs = (RequestAttributes)client;
		reqAttrs.setUserId(endpoint.getUserName());
		reqAttrs.setPassword(endpoint.getPassword());
		reqAttrs.addRequestMimeHeaders(httpHeaders);
		for(RpcHeader h : headers){
			reqAttrs.addRequestRpcHeader(h.getNamespace(), h.getName(), h.getValue());
		}
		long s = System.currentTimeMillis();
		try{
			return method.invoke(client,  args);
		} finally{
			ResponseAttributes resAttrs = (ResponseAttributes)client;
			postprocess(iid, System.currentTimeMillis() - s
					, MimeHeadersUtil.fromStringObjectMap(resAttrs.getResponseMimeHeaders())
					, resAttrs.getResponseRpcHeaders()
					, resAttrs.getResponseRpcFault()
					);
		}
	}

	private ClientFactory clientFactory;
	private Class<?> interfaceClass;
}
