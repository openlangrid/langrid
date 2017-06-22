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
package jp.go.nict.langrid.servicecontainer.executor.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.servicecontainer.executor.AbstractServiceExecutor;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;

public class JavaServiceExecutor<T>
extends AbstractServiceExecutor
implements InvocationHandler{
	public JavaServiceExecutor(
			String invocationName, long invocationId, Endpoint endpoint, Class<T> interfaceClass){
		super(invocationName, invocationId, endpoint);
		this.interfaceClass = interfaceClass;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
	throws Throwable {
		Trio<ServiceContext, String, Long> r = preprocessJava(method, args);
		long s = System.currentTimeMillis();
		try{
			return method.invoke(RIProcessor.getCurrentProcessorContext().getServiceLoader().load(
					getClass().getClassLoader()
					, r.getSecond()
					, interfaceClass
					), args);
		} catch(InvocationTargetException e){
			throw e.getCause();
		} finally{
			postprocessJava(r.getThird(), System.currentTimeMillis() - s);
		}
	}

	private Trio<ServiceContext, String, Long> preprocessJava(Method method, Object[] args){
		Map<String, Object> mimeHeaders = new HashMap<String, Object>();
		List<RpcHeader> headers = new ArrayList<RpcHeader>();
		Pair<Endpoint, Long> r = preprocess(mimeHeaders, headers, method, args);

		String query = r.getFirst().getServiceId();
		if( query == null ) {
			throw new RuntimeException("failed to determine service");
		}
		String[] values = query.split("=");
		String serviceName = values[0];
		if(values.length > 1){
			serviceName = values[1];
		}

		ServiceContext sc = new JavaServiceContext(
				RIProcessor.getCurrentServiceContext(), mimeHeaders, headers
				);
		RIProcessor.start(sc);
		return Trio.create(sc, serviceName, r.getSecond());
	}

	private void postprocessJava(long iid, long deltaTime){
		MimeHeaders resMimeHeaders = new MimeHeaders();
		List<RpcHeader> resRpcHeaders = new ArrayList<RpcHeader>();
		RIProcessor.finish(resMimeHeaders, resRpcHeaders);
		postprocess(iid, deltaTime, resMimeHeaders, resRpcHeaders, null);
	}

	private Class<?> interfaceClass;
}
