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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jp.go.nict.langrid.client.protobufrpc.PbClientFactory;
import jp.go.nict.langrid.commons.io.CloseIgnoringFilterOutputStream;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.servicecontainer.executor.ClientFactoryServiceExecutor;
import jp.go.nict.langrid.servicecontainer.executor.factory.AbstractClientFactoryComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

/**
 * 
 * 
 */
public class PbComponentServiceFactory
extends AbstractClientFactoryComponentServiceFactory
implements ComponentServiceFactory{
	public PbComponentServiceFactory() {
		super(new PbClientFactory());
	}
	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(String invocationName, long invocationId
			, Endpoint endpoint, Class<T> interfaceClass){
		try{
			Constructor<?> ctor = getExecutor(interfaceClass);
			if(ctor != null){
				return (T)ctor.newInstance(
						invocationName, invocationId, endpoint);
			}
			return super.getService(invocationName, invocationId, endpoint, interfaceClass);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void setExecutors(Map<Class<?>, Class<?>> executors) {
		for(Map.Entry<Class<?>, Class<?>> entry : executors.entrySet()){
			try {
				executorConstructors.put(
						entry.getKey(),
						entry.getValue().getConstructor(String.class, long.class, Endpoint.class));
			} catch (NoSuchMethodException e) {
				logger.warning("failed to load executor for " + entry.getKey().getName() +
						" because " + e);
			} catch (SecurityException e) {
				logger.warning("failed to load executor for " + entry.getKey().getName() +
						" because " + e);
			}
		}
	}

	public void setLogExecution(boolean logExecution){
		this.logExecution = logExecution;
	}

	public Constructor<?> getExecutor(Class<?> interfaceClass){
		return executorConstructors.get(interfaceClass);
	}

	private Map<Class<?>, Constructor<?>> executorConstructors = new HashMap<Class<?>, Constructor<?>>();
	private boolean logExecution;
	private static Logger logger = Logger.getLogger(PbComponentServiceFactory.class.getName());
}
