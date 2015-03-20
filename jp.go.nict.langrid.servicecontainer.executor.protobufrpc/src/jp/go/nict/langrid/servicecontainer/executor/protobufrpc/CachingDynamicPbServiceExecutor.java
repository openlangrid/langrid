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
package jp.go.nict.langrid.servicecontainer.executor.protobufrpc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Fault;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.commons.io.CascadingIOException;
import jp.go.nict.langrid.commons.lang.block.BlockPE;
import jp.go.nict.langrid.commons.lang.block.BlockPPE;
import jp.go.nict.langrid.commons.protobufrpc.DefaultRpcController;
import jp.go.nict.langrid.commons.protobufrpc.URLRpcChannel;
import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufParser;
import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufWriter;
import jp.go.nict.langrid.commons.rpc.ArrayElementsReceiver;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Holder;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.servicecontainer.executor.StreamingNotifier;
import jp.go.nict.langrid.servicecontainer.executor.StreamingReceiver;

import com.google.common.cache.Cache;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * 
 * 
 */
public class CachingDynamicPbServiceExecutor
extends AbstractPbServiceExecutor
implements InvocationHandler{
	public static <T> T create(String invocationName, long invocationId
			, Endpoint endpoint, Class<T> interfaceClass
			, Cache<String, Object> cache){
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class[]{interfaceClass, StreamingNotifier.class}
				, new CachingDynamicPbServiceExecutor(
						invocationName, invocationId, endpoint, cache)
				));
	}

	public CachingDynamicPbServiceExecutor(String invocationName){
		super(invocationName);
	}

	public CachingDynamicPbServiceExecutor(String invocationName, long invocationId, Endpoint endpoint
			, Cache<String, Object> cache){
		super(invocationName, invocationId, endpoint);
		this.cache = cache;
	}

	protected Pair<URLRpcChannel, Long> preprocessPb(List<RpcHeader> rpcHeaders){
		Map<String, Object> mimeHeaders = new HashMap<String, Object>();
		Pair<Endpoint, Long> r = preprocess(mimeHeaders, rpcHeaders);
		try{
			Endpoint ep = r.getFirst();
			URLRpcChannel channel = new URLRpcChannel(ep.getAddress().toURL(), ep.getUserName(), ep.getPassword());
			for(Map.Entry<String, Object> entry : mimeHeaders.entrySet()){
				channel.getRequestProperties().put(
						entry.getKey()
						, entry.getValue().toString()
						);
			}
			return Pair.create(channel, r.getSecond());
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, final Method method, final Object[] args)
			throws Throwable {
		if(method.getClass().equals(StreamingNotifier.class)){
			if(method.getName().equals("setReceiver")){
				receiver = (StreamingReceiver<Object>)args[0];
			}
			return null;
		}
		final List<RpcHeader> reqHeaders = new ArrayList<RpcHeader>();
		Pair<URLRpcChannel, Long> r = preprocessPb(reqHeaders);

		long s = System.currentTimeMillis();
		URL url = r.getFirst().getUrl();
		final String key = url.getProtocol() + "://" + url.getAuthority() + url.getPath()
				+ "##" + method.getName() + "(" + JSON.encode(args) + ")";
		Object ret = cache.getIfPresent(key);
		if(ret != null){
			postprocess(r.getSecond(), System.currentTimeMillis() - s, new MimeHeaders()
				, new ArrayList<RpcHeader>(), null);
			return ret;
		}
		DefaultRpcController c = new DefaultRpcController();
		final List<Header> resHeaders = new ArrayList<Header>();
		final Holder<Object> resultHolder = new Holder<Object>();
		final Holder<RpcFault> faultHolder = new Holder<RpcFault>();
		try{
			r.getFirst().call(
					new BlockPE<OutputStream, IOException>() {
						@Override
						public void execute(OutputStream p1) throws IOException {
							CodedOutputStream cos = CodedOutputStream.newInstance(p1);
							try{
								ProtobufWriter.writeRpcRequest(cos, reqHeaders, method, args);
							} catch(IOException e){
								throw e;
							} catch(Exception e){
								throw new CascadingIOException(e);
							}
						}
					}, new BlockPE<InputStream, IOException>(){
						@Override
						public void execute(InputStream p1) throws IOException {
							CodedInputStream cis = CodedInputStream.newInstance(p1);
							try{
								if(receiver != null && method.getReturnType().isArray()){
									final List<Object> result = new ArrayList<Object>();
									Pair<Collection<RpcHeader>, RpcFault> ret
										= ProtobufParser.parseRpcArrayResponse(
												cis, method.getReturnType(), new ArrayElementsReceiver<Object>() {
													@Override
													public void receive(Object element) {
														if(!receiver.receive(element)){
															result.add(element);
														}
													}
												});
									for(RpcHeader rh : ret.getFirst()){
										resHeaders.add(Header.newBuilder()
												.setName(rh.getNamespace())
												.setValue(rh.getValue())
												.build());
									}
									faultHolder.set(ret.getSecond());
									resultHolder.set(result.toArray((Object[])Array.newInstance(
											method.getReturnType().getComponentType(), 0)));
								} else{
									Trio<Collection<RpcHeader>, RpcFault, ?> ret
										= ProtobufParser.parseRpcResponse(cis, method.getReturnType());
									for(RpcHeader rh : ret.getFirst()){
										resHeaders.add(Header.newBuilder()
												.setName(rh.getNamespace())
												.setValue(rh.getValue())
												.build());
									}
									faultHolder.set(ret.getSecond());
									resultHolder.set(ret.getThird());
								}
								cache.put(key, resultHolder.get());
							} catch(IOException e){
								throw e;
							} catch(Exception e){
								throw new CascadingIOException(e);
							}
						}
					}, new BlockPPE<InputStream, IOException, IOException>(){
						@Override
						public void execute(InputStream p1, IOException p2)
						throws IOException{
							CodedInputStream cis = CodedInputStream.newInstance(p1);
							try{
								Trio<Collection<RpcHeader>, RpcFault, ?> ret
									= ProtobufParser.parseRpcResponse(cis, method.getReturnType());
								for(RpcHeader rh : ret.getFirst()){
									resHeaders.add(Header.newBuilder()
											.setName(rh.getNamespace())
											.setValue(rh.getValue())
											.build());
								}
								faultHolder.set(ret.getSecond());
								resultHolder.set(ret.getThird());
							} catch(IOException e){
								throw e;
							} catch(Exception e){
								throw new CascadingIOException(e);
							}
						}
					}
					);
		} finally{
			Fault f = null;
			RpcFault rf = faultHolder.get();
			if(rf != null){
				f = Fault.newBuilder()
						.setFaultCode(rf.getFaultCode())
						.setFaultString(rf.getFaultString())
						.setFaultDetail(rf.getDetail())
						.build();
			}
			postprocessPb(
					r.getSecond(), System.currentTimeMillis( ) - s
					, c, r.getFirst()
					, resHeaders, f
					);
		}
		return resultHolder.get();
	}

	private StreamingReceiver<Object> receiver;
	private Cache<String, Object> cache;
}
