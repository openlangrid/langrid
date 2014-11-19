/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
package jp.go.nict.langrid.client.jsonrpc;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jp.go.nict.langrid.client.AsyncInvocationHandler;
import jp.go.nict.langrid.client.AuthMethod;
import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.RpcRequestAttributes;
import jp.go.nict.langrid.client.RpcResponseAttributes;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.io.DuplicatingInputStream;
import jp.go.nict.langrid.commons.io.DuplicatingOutputStream;
import jp.go.nict.langrid.commons.net.HttpURLConnectionUtil;
import jp.go.nict.langrid.commons.rpc.RpcFaultUtil;
import jp.go.nict.langrid.commons.rpc.json.JsonRpcResponse;
import jp.go.nict.langrid.commons.rpc.json.JsonRpcUtil;
import jp.go.nict.langrid.commons.util.concurrent.DaemonThreadFactory;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONException;

public class JsonRpcClientFactory implements ClientFactory{
	class JsonRpcInvocationHandler implements InvocationHandler{
		public JsonRpcInvocationHandler(URL url){
			this.url = url;
		}
		public JsonRpcInvocationHandler(URL url, int connectionTimeout, int readTimeout){
			this.url = url;
		}
		public URL getUrl() {
			return url;
		}
		public RpcRequestAttributes getReqAttrs() {
			return reqAttrs;
		}
		public RpcResponseAttributes getResAttrs() {
			return resAttrs;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable{
			Class<?> clz = method.getDeclaringClass();
			if(clz.equals(RequestAttributes.class)){
				return method.invoke(reqAttrs, args);
			} else if(clz.equals(ResponseAttributes.class)){
				return method.invoke(resAttrs, args);
			} else{
				HttpURLConnection con = openConnection();
				try{
					con.setUseCaches(false);
					con.setDoOutput(true);
					writeRequest(con, method, args);
					int sts = con.getResponseCode();
					if(sts == 404) throw new FileNotFoundException(url.toString());
					return readResponse(con, method.getReturnType());
				} finally{
					closeConnection(con);
				}
			}
		}
		private int nc = 1;
		protected HttpURLConnection openConnection() throws IOException{
			AuthMethod am = reqAttrs.getAuthMethod();
			if(am != null && am.equals(AuthMethod.DIGEST) &&
					reqAttrs.getUserId() != null){
				Map<String, String> param = null;
				HttpURLConnection con1 = (HttpURLConnection)url.openConnection();
				try{
					param = HttpURLConnectionUtil.parseWwwAuthenticateHeader(
							con1.getHeaderField("WWW-Authenticate"));
				} finally{
					con1.disconnect();
				}
				HttpURLConnection con2 = (HttpURLConnection)url.openConnection();
				con2.setRequestProperty(
						"Authorization",
						HttpURLConnectionUtil.createDigestAuthValue(
								param, "POST", url.getPath(),
								reqAttrs.getUserId(), reqAttrs.getPassword(), nc++)
						);
				return con2;
			} else{
				return (HttpURLConnection)url.openConnection();
			}
		}
		protected void closeConnection(HttpURLConnection con){
			con.disconnect();
		}
		protected void writeRequest(HttpURLConnection con, Method method, Object[] args) throws IOException{
			con.setRequestProperty("Accept", "application/json-rpc");
			con.setRequestProperty("Content-type", "application/json-rpc");
			con.setRequestProperty(LangridConstants.HTTPHEADER_PROTOCOL, Protocols.JSON_RPC);
			reqAttrs.setUpConnection(con);
			OutputStream os = con.getOutputStream();
			if(requestDumpStream != null){
				os = new DuplicatingOutputStream(os, requestDumpStream);
			}
			JSON.encode(JsonRpcUtil.createRequest(reqAttrs.getAllRpcHeaders(), method, args), os);
			os.flush();
		}
		protected Object readResponse(HttpURLConnection con, Class<?> returnType) throws IOException, ParseException, Exception{
			InputStream is = null;
			try{
				is = con.getInputStream();
			} catch(IOException e){
				is = con.getErrorStream();
			}
			if(responseDumpStream != null){
				is = new DuplicatingInputStream(is, responseDumpStream);
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			is = new DuplicatingInputStream(is, baos);
			JsonRpcResponse ret = null;
			try{
				ret = JSON.decode(is, JsonRpcResponse.class);
			} catch(JSONException e){
				throw new RuntimeException("Parse error for URL:[" + con.getURL() + "], JSON:" + baos.toString("UTF-8"), e);
			}
			if(ret.getHeaders() != null){
				resAttrs.loadAttributes(con, Arrays.asList(ret.getHeaders()));
			}
			if(ret.getError() != null){
				throw RpcFaultUtil.rpcFaultToThrowable(ret.getError());
			}
			return converter.convert(ret.getResult(), returnType);
		}
		private URL url;
		private RpcRequestAttributes reqAttrs = new RpcRequestAttributes();
		private RpcResponseAttributes resAttrs = new RpcResponseAttributes();
	}

	class JsonRpcAsyncInvocationHandler extends JsonRpcInvocationHandler implements AsyncInvocationHandler{
		public JsonRpcAsyncInvocationHandler(URL url){
			super(url);
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> Future<T> getLastFuture() {
			return lastFuture;
		}
		@Override
		@SuppressWarnings({"rawtypes", "unchecked"})
		public Object invoke(Object proxy, final Method method, final Object[] args)
		throws Throwable{
			Class<?> clz = method.getDeclaringClass();
			if(clz.equals(RequestAttributes.class)){
				return method.invoke(getReqAttrs(), args);
			} else if(clz.equals(ResponseAttributes.class)){
				return method.invoke(getResAttrs(), args);
			} else if(clz.equals(AsyncInvocationHandler.class)){
				return method.invoke(this, args);
			} else{
				lastFuture = getExecutorService().submit(new Callable() {
					@Override
					public Object call() throws Exception {
						HttpURLConnection con = openConnection();
						try{
							writeRequest(con, method, args);
							int sts = con.getResponseCode();
							if(sts == 404) throw new FileNotFoundException(getUrl().toString());
							return readResponse(con, method.getReturnType());
						} finally{
							closeConnection(con);
						}
					}
				});
				return null;
			}
		}

		@SuppressWarnings("rawtypes")
		private Future lastFuture;
	}

	@Override
	public <T> T create(Class<T> interfaceClass, URL url) {
		return create(interfaceClass, new JsonRpcInvocationHandler(url));
	}

	@Override
	public <T> T create(Class<T> interfaceClass, URL url, String userId, String password) {
		JsonRpcInvocationHandler h = new JsonRpcInvocationHandler(url);
		h.reqAttrs.setUserId(userId);
		h.reqAttrs.setPassword(password);
		return create(interfaceClass, h);
	}

	/**
	 * Create Async Client. That client always returns null for any method invocation.
	 * In spite of that the client implements AsyncInvocationHandler interface and
	 * returns Future of last call throw getLastFuture method.
	 * @param interfaceClass interface that define the interface of Service class.
	 * @param url url to invoke JSON-RPC method.
	 * @return acync client
	 */
	public <T> T createAsync(Class<T> interfaceClass, URL url) {
		return createAsync(interfaceClass, new JsonRpcAsyncInvocationHandler(url));
	}

	public <T> T createAsync(Class<T> interfaceClass, URL url, String userId, String password) {
		JsonRpcAsyncInvocationHandler h = new JsonRpcAsyncInvocationHandler(url);
		h.getReqAttrs().setUserId(userId);
		h.getReqAttrs().setPassword(password);
		return createAsync(interfaceClass, h);
	}

	private <T> T create(Class<T> interfaceClass, InvocationHandler handler){
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{interfaceClass, RequestAttributes.class, ResponseAttributes.class}
				, handler
				));
	}

	private <T> T createAsync(Class<T> interfaceClass, AsyncInvocationHandler handler){
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{interfaceClass, RequestAttributes.class, ResponseAttributes.class,
					AsyncInvocationHandler.class}
				, handler
				));
	}

	public void setRequestDumpStream(OutputStream stream){
		this.requestDumpStream = stream;
	}

	public void setResponseDumpStream(OutputStream stream){
		this.responseDumpStream = stream;
	}

	private synchronized ExecutorService getExecutorService(){
		if(es == null){
			es = Executors.newFixedThreadPool(
					Runtime.getRuntime().availableProcessors(),
					new DaemonThreadFactory()
					);
		}
		return es;
	}
	private ExecutorService es;
	private OutputStream requestDumpStream;
	private OutputStream responseDumpStream;
	private static Converter converter = new Converter();
}
