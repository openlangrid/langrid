/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
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
package jp.go.nict.langrid.servicecontainer.executor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.MimeHeaders;
import jp.go.nict.langrid.cosee.DynamicBindingRewriter;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.cosee.EndpointRewriter;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;

/**
 * Abstract class for Service Executors.
 * @author Takao Nakaguchi
 */
public abstract class AbstractServiceExecutor {
	/**
	 * Constructor.
	 * @param invocationName invocation name
	 */
	public AbstractServiceExecutor(String invocationName){
		this.invocationName = invocationName;
	}

	/**
	 * Constructor.
	 * @param invocationName invocation name.
	 * @param invocationId invocation id.
	 * @param endpoint endpoint of the service.
	 */
	public AbstractServiceExecutor(String invocationName, long invocationId, Endpoint endpoint){
		this.invocationName = invocationName;
		this.invocationId = invocationId;
		this.endpoint = endpoint;
	}

	public String getInvocationName(){
		return invocationName;
	}

	protected Pair<Endpoint, Long> preprocess(
			Map<String, Object> mimeHeaders, Collection<RpcHeader> rpcHeaders,
			Method method, Object[] args
			){
		if(invocationId == -1){
			invocationId = RIProcessor.newInvocationId();
		}
		if(endpoint == null){
			endpoint = RIProcessor.rewriteEndpoint(invocationId, invocationName, rewriters,
					method, args);
		}
		RIProcessor.appendInvocationHeaders(
				invocationId, invocationName, method, args, mimeHeaders, rpcHeaders
				);
		return Pair.create(endpoint, invocationId);
	}

	protected void postprocess(long iid, long deltaTime
			, MimeHeaders resMimeHeaders, Iterable<RpcHeader> resRpcHeaders, RpcFault resRpcFault){
		RIProcessor.processInvocationResponseHeaders(
				iid, invocationName, deltaTime
				, resMimeHeaders, resRpcHeaders, resRpcFault
				);
	}

	private String invocationName;
	private long invocationId;
	private Endpoint endpoint;
	private static EndpointRewriter[] rewriters = {new DynamicBindingRewriter()};
}
