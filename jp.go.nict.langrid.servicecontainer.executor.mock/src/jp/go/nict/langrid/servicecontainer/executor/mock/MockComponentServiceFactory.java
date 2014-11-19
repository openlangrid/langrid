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
package jp.go.nict.langrid.servicecontainer.executor.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.AbstractComponentServiceFactory;
import net.arnx.jsonic.JSON;

/**
 * 
 * 
 */
public class MockComponentServiceFactory
extends AbstractComponentServiceFactory
implements ComponentServiceFactory{
	@Override
	public <T> T getService(final String invocationName, long invocationId
			, Endpoint endpoint, Class<T> interfaceClass){
		try{
			return interfaceClass.cast(Proxy.newProxyInstance(
					Thread.currentThread().getContextClassLoader(),
					new Class<?>[]{interfaceClass},
					new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
							String name = invocationName + "_" + method.getName();
							do{
								Class<?> rt = method.getReturnType();
								if(rt.equals(void.class)) break;
								File f = new File(RIProcessor.getCurrentServiceContext().getRealPath(
										basePath + "/" + name + ".json"
										));
								if(!f.exists()){
									break;
								}
								InputStream is = new FileInputStream(f);
								try{
									return JSON.decode(is, rt);
								} finally{
									is.close();
								}
							} while(false);
							logger.warning("file not found for: " + name);
							return null;
						}
					}));
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	private String basePath;
	private static Logger logger = Logger.getLogger(MockComponentServiceFactory.class.getName());
}
