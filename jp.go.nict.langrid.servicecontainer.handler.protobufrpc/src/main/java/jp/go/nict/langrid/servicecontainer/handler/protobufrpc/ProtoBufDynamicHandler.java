/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.servicecontainer.handler.protobufrpc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufParser;
import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufWriter;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;
import jp.go.nict.langrid.servicecontainer.executor.StreamingNotifier;
import jp.go.nict.langrid.servicecontainer.executor.StreamingReceiver;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.handler.ServiceFactory;
import jp.go.nict.langrid.servicecontainer.handler.ServiceLoader;
import jp.go.nict.langrid.servicecontainer.handler.protobufrpc.servlet.PBServiceServiceContext;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ProtoBufDynamicHandler
extends AbstractProtoBufHandler
implements ProtoBufHandler
{
	/**
	 * 
	 * 
	 */
	@Override
	public void handle(
			String serviceName, String methodName
			, HttpServletRequest request, final HttpServletResponse response
			, CodedInputStream is, final CodedOutputStream os
			){
		try{
			ServiceContext sc = new PBServiceServiceContext(request, new ArrayList<Header>());
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			List<RpcHeader> resHeaders = new ArrayList<RpcHeader>();
			Class<?> clazz = null;
			Object result = null;
			try{
				ServiceLoader loader = new ServiceLoader(sc);
				ServiceFactory f = loader.loadServiceFactory(cl, serviceName);
				Collection<Class<?>> interfaceClasses = f.getInterfaces();
				if(interfaceClasses.isEmpty()){
					throw new RuntimeException("method \"" + methodName
							+ "\" not found in service \"" + serviceName + "\".");
				}
				Method method = null;
				for(Class<?> clz : interfaceClasses){
					method = ClassUtil.findMethod(clz, methodName);
					if(method == null) continue;
					clazz = clz;
					break;
				}
				if(method == null){
					throw new RuntimeException("method \"" + methodName
							+ "\" not found in service \"" + serviceName + "\".");
				}
				Pair<Collection<RpcHeader>, Object[]> req
					= ProtobufParser.parseRpcRequest(is, method.getParameterTypes());
				sc = new ServletServiceContext(request, req.getFirst());
				RIProcessor.start(sc);
				try{
					Object service = f.createService(cl, sc, clazz);
					if(service instanceof StreamingNotifier){
						@SuppressWarnings("unchecked")
						StreamingNotifier<Object> notif = (StreamingNotifier<Object>)service;
						notif.setReceiver(new StreamingReceiver<Object>() {
							@Override
							public boolean receive(Object result) {
								try {
									ProtobufWriter.writeObject(os, 3, result);
									os.flush();
									response.getOutputStream().flush();
								} catch (Exception e) {
									return false;
								}
								return true;
							}
						});
						try{
							result = method.invoke(service, req.getSecond());
						} finally{
							notif.setReceiver(null);
						}
					} else{
						result = method.invoke(service, req.getSecond());
					}
				} finally{
					MimeHeaders resMimeHeaders = new MimeHeaders();
					RIProcessor.finish(resMimeHeaders, resHeaders);
					MimeHeadersUtil.setToHttpServletResponse(resMimeHeaders, response);
				}
				ProtobufWriter.writeSuccessRpcResponse(
						os, resHeaders, result
						);
			} catch(InvocationTargetException e){
				Throwable t = e.getTargetException();
				logger.log(Level.SEVERE, "failed to handle request for " + serviceName
						+ ":" + clazz.getName() + "#" + methodName
						, t);
		    	response.setStatus(500);
		    	ProtobufWriter.writeFaultRpcResponse(
		    			os, resHeaders, createRpcFault(t)
		    			);
			} catch(Exception e){
				logger.log(Level.SEVERE, "failed to handle request for " + serviceName
						+ ":" + clazz.getName() + "#" + methodName
						, e);
		    	response.setStatus(500);
		    	ProtobufWriter.writeFaultRpcResponse(
		    			os, resHeaders, createRpcFault(e)
		    			);
			}
		} catch(IOException e){
            throw new RuntimeException(e);
        }
    }

	private static Logger logger = Logger.getLogger(ProtoBufDynamicHandler.class.getName());
}
