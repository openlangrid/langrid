/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
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
package jp.go.nict.langrid.servicecontainer.handler.loader;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.param.ServiceContextParameterContext;
import jp.go.nict.langrid.servicecontainer.handler.ServiceFactory;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author Takao Nakaguchi
 */
public class XmlServiceFactoryLoader implements FileServiceFactoryLoader{
	public ServiceFactory getServiceFactory(
			ClassLoader classLoader, ServiceContext serviceContext, String serviceName, File configFile){
		try{
			ServiceFactory factory = getServiceFactory(
					serviceContext, configFile, classLoader
					);
			factory.init(serviceContext, serviceName);
			return factory;
		} catch(BeanDefinitionStoreException e){
			logger.log(Level.WARNING, "failed to load service.", e);
			throw e;
		}
	}

	private static synchronized ServiceFactory getServiceFactory(
			ServiceContext serviceContext, File configPath, final ClassLoader classLoader){
		final String path = configPath.getAbsoluteFile().toURI().toString();
		try {
			Cache<String, ServiceFactory> c = contextCache.get();
			if(c == null){
				c = CacheBuilder.newBuilder()
						.expireAfterWrite(
								new ServiceContextParameterContext(serviceContext).getInteger(
										"jp.go.nict.langrid.servicecontainer.handler.loader.XmlServiceFactoryLoader.factoryChacheSeconds",
										3600),
								TimeUnit.SECONDS)
						.build();
				contextCache.set(c);
			}
			return c.get(path, new Callable<ServiceFactory>(){
				public ServiceFactory call() throws Exception {
					return loadFrom(classLoader, path);
				};
			});
		} catch (ExecutionException e) {
			logger.log(Level.SEVERE, "failed to load service factory for " + configPath, e);
			return null;
		}
	}

	private static ServiceFactory loadFrom(ClassLoader classLoader, String path){
		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(new String[]{path},false,null);
		ctx.setClassLoader(classLoader);
		ctx.setValidating(false);
		ctx.refresh();
		try{
			ServiceFactory f = ctx.getBean("target", ServiceFactory.class);
			f.afterLoad();
			return f;
		} finally{
			ctx.close();
		}
	}

	private static ThreadLocal<Cache<String, ServiceFactory>> contextCache =
			new ThreadLocal<Cache<String, ServiceFactory>>();

	private static Logger logger = Logger.getLogger(XmlServiceFactoryLoader.class.getName());
}
