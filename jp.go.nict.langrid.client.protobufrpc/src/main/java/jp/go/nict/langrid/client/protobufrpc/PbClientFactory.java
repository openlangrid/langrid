/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.client.protobufrpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.Collection;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.RpcRequestAttributes;
import jp.go.nict.langrid.client.RpcResponseAttributes;
import jp.go.nict.langrid.commons.io.DuplicatingInputStream;
import jp.go.nict.langrid.commons.io.EmptyInputStream;
import jp.go.nict.langrid.commons.io.LimitedFilteredOutputStream;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufParser;
import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufWriter;
import jp.go.nict.langrid.commons.rpc.ArrayElementsNotifier;
import jp.go.nict.langrid.commons.rpc.ArrayElementsReceiver;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcFaultUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.Protocols;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.InvalidProtocolBufferException;

public class PbClientFactory implements ClientFactory{
	class ProtobufInvocationHandler implements InvocationHandler, ArrayElementsNotifier{
		public ProtobufInvocationHandler(URL url){
			this.url = url;
		}
		@Override
		@SuppressWarnings("unchecked")
		public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable{
			Class<?> clz = method.getDeclaringClass();
			if(clz.equals(RequestAttributes.class)){
				return method.invoke(reqAttrs, args);
			} else if(clz.equals(ResponseAttributes.class)){
				return method.invoke(resAttrs, args);
			} else if(clz.equals(ArrayElementsNotifier.class)){
				return method.invoke(this, args);
			} else{
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				try{
					con.setDoOutput(true);
					con.setConnectTimeout(10 * 1000);
					con.setReadTimeout(10 * 60 * 1000);
					con.setRequestProperty(LangridConstants.HTTPHEADER_PROTOCOL, Protocols.PROTOBUF_RPC);
					reqAttrs.setUpConnection(con);
					CodedOutputStream cos = CodedOutputStream.newInstance(
							con.getOutputStream());
					ProtobufWriter.writeRpcRequest(
							cos, reqAttrs.getAllRpcHeaders(), method, args
							);
					cos.flush();
					InputStream is = null;
					try{
						is = con.getInputStream();
					} catch(IOException e){
						is = con.getErrorStream();
					}
					if(is == null){
						is = new EmptyInputStream();
					}
					if(con.getResponseCode() == 404){
						throw new RuntimeException(StreamUtil.readAsString(is, "UTF-8"));
					}
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					is = new DuplicatingInputStream(is, new LimitedFilteredOutputStream(baos, 2048));
					if(dumpStream != null){
						is = new DuplicatingInputStream(is, dumpStream);
					}
					CodedInputStream cis = CodedInputStream.newInstance(is);
					Trio<Collection<RpcHeader>, RpcFault, Object> ret = null;
					Class<?> rt = method.getReturnType();
					try{
						if(rt.isArray() && receiver != null){
							Pair<Collection<RpcHeader>, RpcFault> r = ProtobufParser.parseRpcArrayResponse(cis, rt, receiver);
							ret = Trio.create(r.getFirst(), r.getSecond(), Array.newInstance(rt.getComponentType(), 0));
						} else{
							ret = (Trio<Collection<RpcHeader>, RpcFault, Object>)ProtobufParser.parseRpcResponse(cis, rt);
						}
					} catch(InvalidProtocolBufferException e){
						byte[] b = baos.toByteArray();
						StringBuilder sb = new StringBuilder();
						sb.append(StreamUtil.readAsString(new ByteArrayInputStream(b),
								Charset.forName("UTF-8").newDecoder()
								.onMalformedInput(CodingErrorAction.REPLACE)
								.onUnmappableCharacter(CodingErrorAction.REPLACE)));
						if(b.length == 500){
							sb.append("\n------snip------\n");
						}
						sb.append(StreamUtil.readAsString(is,
								Charset.forName("UTF-8").newDecoder()
								.onMalformedInput(CodingErrorAction.REPLACE)
								.onUnmappableCharacter(CodingErrorAction.REPLACE)));
						throw new RuntimeException(sb.toString(), e);
					} catch(Exception e){
						throw new RuntimeException(e);
					}
					resAttrs.loadAttributes(con, ret.getFirst());
					if(ret.getSecond() != null){
						RpcFault f = ret.getSecond();
						throw RpcFaultUtil.rpcFaultToThrowable(f);
					}
					return ret.getThird();
				} finally{
					con.disconnect();
				}
			}
		}

		@Override
		public <T> void setReceiver(ArrayElementsReceiver<T> receiver) {
			this.receiver = receiver;
		}

		private URL url;
		private RpcRequestAttributes reqAttrs = new RpcRequestAttributes();
		private RpcResponseAttributes resAttrs = new RpcResponseAttributes();
		private ArrayElementsReceiver<?> receiver;
	}

	@Override
	public <T> T create(Class<T> interfaceClass, URL url) {
		ProtobufInvocationHandler h = new ProtobufInvocationHandler(url);
		return create(interfaceClass, h);
	}

	@Override
	public <T> T create(Class<T> interfaceClass, URL url, String userId, String password) {
		ProtobufInvocationHandler h = new ProtobufInvocationHandler(url);
		h.reqAttrs.setUserId(userId);
		h.reqAttrs.setPassword(password);
		return create(interfaceClass, h);
	}

	private <T> T create(Class<T> interfaceClass, ProtobufInvocationHandler handler) {
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{
					interfaceClass, RequestAttributes.class, ResponseAttributes.class
					, ArrayElementsNotifier.class }
				, handler
				));
	}

	public void setDumpStream(OutputStream dumpStream){
		this.dumpStream = dumpStream;
	}

	private OutputStream dumpStream;
}
