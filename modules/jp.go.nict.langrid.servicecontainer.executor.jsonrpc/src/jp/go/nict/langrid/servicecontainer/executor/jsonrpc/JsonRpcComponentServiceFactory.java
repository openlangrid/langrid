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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.servicecontainer.executor.CachingClientFactoryServiceExecutor;
import jp.go.nict.langrid.servicecontainer.executor.ClientFactoryServiceExecutor;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.AbstractComponentServiceFactory;

/**
 * 
 * 
 */
public class JsonRpcComponentServiceFactory
extends AbstractComponentServiceFactory
implements ComponentServiceFactory{
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(String invocationName, long invocationId
			, Endpoint endpoint, Class<T> interfaceClass){
		try{
			Constructor<?> ctor = factories.get(interfaceClass);
			if(ctor == null){
				ClientFactory f = new JsonRpcClientFactory();
				return (T)Proxy.newProxyInstance(
						Thread.currentThread().getContextClassLoader()
						, new Class<?>[]{interfaceClass}
						, isCacheEnabled() ?
						new CachingClientFactoryServiceExecutor(
								invocationName, invocationId, endpoint, interfaceClass,
								f, getCache()
								) :
						new ClientFactoryServiceExecutor(
								invocationName, invocationId, endpoint, interfaceClass,
								f));
			}
			return (T)ctor.newInstance(
					invocationName, invocationId, endpoint);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e.getTargetException());
		} catch(RuntimeException e){
			throw e;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	private static Map<Class<?>, Constructor<?>> factories
		= new HashMap<Class<?>, Constructor<?>>( ) ;
}
