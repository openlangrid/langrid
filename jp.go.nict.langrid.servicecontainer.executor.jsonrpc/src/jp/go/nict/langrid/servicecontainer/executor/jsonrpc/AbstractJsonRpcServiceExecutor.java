/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
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
package jp.go.nict.langrid.servicecontainer.executor.jsonrpc;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.servicecontainer.service.component.AbstractServiceExecutor;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public abstract class AbstractJsonRpcServiceExecutor
extends AbstractServiceExecutor{
	public AbstractJsonRpcServiceExecutor(String invocationName){
		super(invocationName);
	}

	public AbstractJsonRpcServiceExecutor(String invocationName, long invocationId, Endpoint endpoint){
		super(invocationName, invocationId, endpoint);
	}

	/**
	 * 
	 * 
	 */
	protected Pair<Endpoint, Long> preprocessJsonRpc(Map<String, Object> mimeHeaders, Collection<RpcHeader> rpcHeaders,
			Method method, Object[] args){
		return preprocess(mimeHeaders, rpcHeaders, method, args);
	}

	/**
	 * 
	 * 
	 */
	protected void postprocessJsonRpc(
			long iid, long deltaTime
			, HttpURLConnection con, Iterable<RpcHeader> responseHeaders, RpcFault fault){
		RpcFault rpcFault = null;
		try{
			if(fault != null){
				throw new RuntimeException(fault.getDetail());
			}
		} finally{
			MimeHeaders mh = null;
			if(con != null){
				final Map<String, List<String>> mheaders = con.getHeaderFields();
				mh = new MimeHeaders(){
					@Override
					public String[] getHeader(String name){
						List<String> v = mheaders.get(name);
						if(v == null) return null;
						return v.toArray(new String[]{});
					}
				};
			} else{
				mh = new MimeHeaders();
			}
			postprocess(iid, deltaTime, mh, responseHeaders, rpcFault);
		}
    }
}
