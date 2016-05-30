/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
 * Copyright (C) 2015 The Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.service.component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.logger.ConsoleLogger;

public class LoggingComponentServiceFactory implements ComponentServiceFactory {
	public LoggingComponentServiceFactory() {
	}

	public LoggingComponentServiceFactory(ComponentServiceFactory original){
		this.original = original;
		this.logger = new ConsoleLogger();
	}

	public LoggingComponentServiceFactory(ComponentServiceFactory original, Logger logger){
		this.original = original;
		this.logger = logger;
	}

	public void setOriginalFactory(ComponentServiceFactory original) {
		this.original = original;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public <T> T getService(final String invocationName, final Class<T> interfaceClass) {
		final T service = original.getService(invocationName, interfaceClass);
		if(service == null) return service;
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{interfaceClass}
				, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						logger.log(String.format("service %s:%s invoked. %s.%s(%s)"
								, invocationName, interfaceClass.getName()
								, service.getClass().getName(), method.getName()
								, Arrays.toString(args)
								));
						try{
							Object ret = method.invoke(service, args);
							if(ret != null){
								logger.log(String.format("service %s:%s returned. %s.%s -> %s"
										, invocationName, interfaceClass.getName()
										, service.getClass().getName(), method.getName()
										, ret.getClass().isArray() ? (ret.getClass().getComponentType().isPrimitive() ? ret : (
												((Object[])ret).length + " elements:" + Arrays.toString((Object[])ret))
												): ret
										));
							} else{
								logger.log(String.format("service %s:%s returned. %s.%s -> null(or void)"
										, invocationName, interfaceClass.getName()
										, service.getClass().getName(), method.getName()
										));
							}
							return ret;
						} catch(InvocationTargetException e){
							logger.log(String.format("service %s:%s throwed exception. %s.%s -> %s"
									, invocationName, interfaceClass.getName()
									, service.getClass().getName(), method.getName()
									, ExceptionUtil.getMessageWithStackTrace(e.getCause())
									));
							throw e;
						} catch(Throwable t){
							logger.log(String.format("service %s:%s throwed exception. %s.%s -> %s"
									, invocationName, interfaceClass.getName()
									, service.getClass().getName(), method.getName()
									, ExceptionUtil.getMessageWithStackTrace(t)
									));
							throw t;
						}
					}
				}));
	}

	private ComponentServiceFactory original;
	private Logger logger;
}
