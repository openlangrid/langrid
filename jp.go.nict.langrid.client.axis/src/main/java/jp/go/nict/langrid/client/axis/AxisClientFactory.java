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
package jp.go.nict.langrid.client.axis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.beanutils.DynamicInvocationHandler;
import jp.go.nict.langrid.commons.util.Pair;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Stub;
import org.apache.axis.transport.http.HTTPConstants;

/**
 * ClientFactory using Axis Stubs.
 * NOTE: The AxisClientFactory doesn't support content-encodings other than identity.
 * @author Takao Nakaguchi
 */
public class AxisClientFactory implements ClientFactory {
	static class AxisDynamicInvocationHandler extends DynamicInvocationHandler<Stub>{
		public AxisDynamicInvocationHandler(Stub stub, Converter converter){
			super(stub, converter);
			reqAttr = new AxisStubRequestAttributes(stub);
			resAttr = new AxisStubResponseAttributes(stub);
		}
		protected void preInvocation() {
			reqAttr.setUpStub();
		}
		protected void postInvocation() {
			resAttr.reload();
		}
		public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {
			Class<?> clz = method.getDeclaringClass();
			if(clz.equals(RequestAttributes.class)){
				return method.invoke(reqAttr, args);
			} else if(clz.equals(ResponseAttributes.class)){
				return method.invoke(resAttr, args);
			} else{
				return super.invoke(proxy, method, args);
			}
		}
		private AxisStubRequestAttributes reqAttr;
		private AxisStubResponseAttributes resAttr;
	}
	public AxisClientFactory() {
	}
	public AxisClientFactory(EngineConfiguration config){
		this.config = config;
	}

	public <T> T create(Class<T> interfaceClass, URL url){
		Stub stub = createStub(interfaceClass);
		if(stub == null) return null;
		AxisStubUtil.setUrl(stub, url);
		return create(interfaceClass, stub);
	}

	public <T> T create(Class<T> interfaceClass, URL url, String userId, String password){
		Stub stub = createStub(interfaceClass);
		if(stub == null) return null;
		AxisStubUtil.setUrl(stub, url);
		AxisStubUtil.setUserName(stub, userId);
		AxisStubUtil.setPassword(stub, password);
		return create(interfaceClass, stub);
	}

	private <T> T create(Class<T> interfaceClass, Stub stub){
		stub._setProperty("axis.transport.version", HTTPConstants.HEADER_PROTOCOL_V11);
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{interfaceClass, RequestAttributes.class, ResponseAttributes.class}
				, new AxisDynamicInvocationHandler(stub, converter)
				));
	}

	public boolean hasStub(Class<?> intf){
		return stubs.containsKey(intf);
	}

	public void registerStub(Class<?> intf, Class<? extends org.apache.axis.client.Service> locatorClass, String createMethodName){
		try {
			Method m = locatorClass.getMethod(createMethodName);
			Object loc = config != null
					? locatorClass.getConstructor(EngineConfiguration.class).newInstance(config)
					: locatorClass.newInstance();
			stubs.put(intf, Pair.create(loc, m));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void unregisterStub(Class<?> intf){
		stubs.remove(intf);
	}

	public Converter getConverter(){
		return converter;
	}

	private Stub createStub(Class<?> interfaceClass){
		Pair<Object, Method> s = stubs.get(interfaceClass);
		if(s == null) return null;
		try{
			return (Stub)s.getSecond().invoke(s.getFirst());
		} catch(IllegalAccessException e){
			throw new RuntimeException(e);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e);
		}
	}

	private EngineConfiguration config;

	private Map<Class<?>, Pair<Object, Method>> stubs
			= new HashMap<Class<?>, Pair<Object, Method>>();
	private Converter converter = new Converter();
}
