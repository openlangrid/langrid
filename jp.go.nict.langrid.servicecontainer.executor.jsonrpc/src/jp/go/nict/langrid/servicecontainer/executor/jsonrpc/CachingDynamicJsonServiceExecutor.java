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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MimeHeaders;

import com.google.common.cache.Cache;

import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcFaultUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.rpc.json.JsonRpcResponse;
import jp.go.nict.langrid.commons.rpc.json.JsonRpcUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONException;
import jp.go.nict.langrid.servicecontainer.executor.StreamingNotifier;

/**
 * 
 * 
 */
public class CachingDynamicJsonServiceExecutor
extends AbstractJsonRpcServiceExecutor
implements InvocationHandler{
	public static <T> T create(String invocationName, long invocationId
			, Endpoint endpoint, Class<T> interfaceClass
			, Cache<String, Object> cache){
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class[]{interfaceClass, StreamingNotifier.class}
				, new CachingDynamicJsonServiceExecutor(
						invocationName, invocationId, endpoint, cache)
				));
	}

	public CachingDynamicJsonServiceExecutor(String invocationName){
		super(invocationName);
	}

	public CachingDynamicJsonServiceExecutor(String invocationName, long invocationId, Endpoint endpoint
			, Cache<String, Object> cache){
		super(invocationName, invocationId, endpoint);
		this.cache = cache;
	}

	protected Pair<Endpoint, Long> preprocessJsonRpc(Map<String, Object> mimeHeaders,
			Collection<RpcHeader> rpcHeaders, Method method, Object[] args){
		return preprocess(mimeHeaders, rpcHeaders, method, args);
	}

	@Override
	public Object invoke(Object proxy, final Method method, final Object[] args)
	throws Throwable {
		Map<String, Object> mimeHeaders = new HashMap<String, Object>();
		final List<RpcHeader> rpcHeaders = new ArrayList<RpcHeader>();
		Pair<Endpoint, Long> r = preprocessJsonRpc(mimeHeaders, rpcHeaders, method, args);
		long s = System.currentTimeMillis();
		URL url = r.getFirst().getAddress().toURL();
		final String key = url.getProtocol() + "://" + url.getAuthority() + url.getPath()
				+ "##" + method.getName() + "(" + JSON.encode(args) + ")";
		Object ret = cache.getIfPresent(key);
		if(ret != null){
			postprocess(r.getSecond(), System.currentTimeMillis() - s, new MimeHeaders()
				, new ArrayList<RpcHeader>(), null);
			return ret;
		} else{
			HttpURLConnection con = null;
			JsonRpcResponse result = null;
			RpcFault fault = null;
			try{
				con = (HttpURLConnection)r.getFirst().getAddress().toURL().openConnection();
				con.setUseCaches(false);
				con.setDoOutput(true);
				con.setConnectTimeout(3000);
				con.setReadTimeout(10000);
				con.setRequestProperty("Accept", "application/json-rpc");
				con.setRequestProperty("Content-type", "application/json-rpc");
				con.setRequestProperty(LangridConstants.HTTPHEADER_PROTOCOL, Protocols.JSON_RPC);
				for(Map.Entry<String, Object> entry : mimeHeaders.entrySet()){
					con.addRequestProperty(entry.getKey(), entry.getValue().toString());
				}
				OutputStream os = con.getOutputStream();
				JSON.encode(JsonRpcUtil.createRequest(rpcHeaders, method, args), os);
				os.flush();
				InputStream is = null;
				try{
					is = con.getInputStream();
				} catch(IOException e){
					is = con.getErrorStream();
				} finally{
					if(is != null){
						try{
							result = JSON.decode(is, JsonRpcResponse.class);
						} catch(JSONException e){
							fault = new RpcFault("Server.userException", e.toString()
									, ExceptionUtil.getMessageWithStackTrace(e));
						}
					} else{
						throw new RuntimeException("failed to open response stream.");
					}
				}
				if(result.getError() != null){
					fault = result.getError();
					throw RpcFaultUtil.rpcFaultToThrowable(result.getError());
				}
				ret = converter.convert(result.getResult(), method.getReturnType());
				cache.put(key, ret);
				return result;
			} finally{
				long dt = System.currentTimeMillis( ) - s;
				List<RpcHeader> resHeaders = null;
				if(result != null && result.getHeaders() != null){
					resHeaders = Arrays.asList(result.getHeaders());
				}
				postprocessJsonRpc(r.getSecond(), dt, con, resHeaders, fault);
				if(con != null) con.disconnect();
			}
		}
	}

	private Cache<String, Object> cache;
	private static Converter converter = new Converter();
}
