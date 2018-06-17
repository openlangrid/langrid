/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.io.FileFilters;
import jp.go.nict.langrid.commons.io.FileNameUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.transformer.ClassToInstanceTransformer;
import jp.go.nict.langrid.commons.transformer.StringToClassTransformer;
import jp.go.nict.langrid.commons.util.function.Filters;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.io.ServiceContextFileResolver;
import jp.go.nict.langrid.commons.ws.param.ServiceContextParameterContext;
import jp.go.nict.langrid.servicecontainer.handler.loader.FileServiceFactoryLoader;
import jp.go.nict.langrid.servicecontainer.handler.loader.ServiceFactoryLoader;
import jp.go.nict.langrid.servicecontainer.service.AbstractService;

/**
 * Service loader.
 * @author Shingo Furukido
 * @author Takao Nakaguchi
 */
public class ServiceLoader {
	public ServiceLoader(ServiceContext serviceContext){
		this(serviceContext, new ServiceFactoryLoader[]{});
	}

	public ServiceLoader(ServiceContext serviceContext, ServiceFactoryLoader[] loaders){
		this.serviceContext = serviceContext;
		this.serviceFactoryLoaders = new ArrayList<ServiceFactoryLoader>(Arrays.asList(loaders));
		ParameterContext pc = new ServiceContextParameterContext(serviceContext);
		Arrays.stream(pc.getStrings("serviceFactoryLoaders", new String[]{}))
			.map(s -> new StringToClassTransformer<ServiceFactoryLoader>(true).transform(s))
			.filter(Filters.<Class<ServiceFactoryLoader>>nonNull())
			.map(s -> new ClassToInstanceTransformer<ServiceFactoryLoader>().transform(s))
			.forEach(v -> serviceFactoryLoaders.add(v));
			;
		this.servicesPath = new ServiceContextFileResolver(serviceContext).resolve(
			pc.getString("servicesPath", "WEB-INF/services"));
		if(servicesPath != null && servicesPath.exists() && servicesPath.isDirectory()){
			for(String mapping : pc.getStrings("fileServiceFactoryLoaders", new String[]{"xml=jp.go.nict.langrid.servicecontainer.handler.loader.XmlServiceFactoryLoader"})){
				String[] m = mapping.split("=");
				if(m.length != 2){
					logger.warning("ignore invalid definition: " + mapping);
					continue;
				}
				try {
					Class<?> clz = Class.forName(m[1]);
					for(String ext : m[0].split(":")){
						fileServiceFactoryLoaders.put(ext, (FileServiceFactoryLoader)clz.newInstance());
					}
				} catch (ClassNotFoundException e) {
					logger.log(Level.WARNING, "failed to load ExtServiceLoader.", e);
				} catch (InstantiationException e) {
					logger.log(Level.WARNING, "failed to load ExtServiceLoader.", e);
				} catch (IllegalAccessException e) {
					logger.log(Level.WARNING, "failed to load ExtServiceLoader.", e);
				} catch (IllegalArgumentException e) {
					logger.log(Level.WARNING, "failed to load ExtServiceLoader.", e);
				} catch (SecurityException e) {
					logger.log(Level.WARNING, "failed to load ExtServiceLoader.", e);
				}
			}
		}
	}

	public void setServiceFactoryLoaders(
			List<ServiceFactoryLoader> serviceFactoryLoaders) {
		this.serviceFactoryLoaders = serviceFactoryLoaders;
	}

	public File getServicesPath() {
		return servicesPath;
	}

	public Iterable<String> listServiceNames()
	throws IOException{
		Collection<String> services = new ArrayList<String>();
		for(ServiceFactoryLoader l : serviceFactoryLoaders){
			services.addAll(Arrays.asList(l.listServiceNames()));
		}
		if(fileServiceFactoryLoaders.size() == 0){
			return services;
		}
		if(!servicesPath.exists()){
			throw new IOException("path \"" + servicesPath + "\" is not exist.");
		}
		if(!servicesPath.isDirectory()){
			throw new IOException("path \"" + servicesPath + "\" is not a directory.");
		}
		for(File f : servicesPath.listFiles(FileFilters.isFile())){
			String name = f.getName();
			String ext = FileNameUtil.getExt(f);
			if(ext.equals("")) continue;
			if(!fileServiceFactoryLoaders.containsKey(ext.toLowerCase())) continue;
			services.add(name.substring(0, name.length() - 1 - ext.length()));
		}
		return services;
	}

	/**
	 * Load service.
	 * @param <T> the type of service interface.
	 * @param serviceName the service name.
	 * @param serviceInterface the service interface.
	 * @return the service loaded and configured by the service loader.
	 */
	public <T> T load(String serviceName, Class<T> serviceInterface){
		return load(
				Thread.currentThread().getContextClassLoader()
				, serviceName, serviceInterface
				);
	}

	/**
	 * Load service.
	 * @param <T> the type of service interface.
	 * @param classLoader the class loader used for loading service class.
	 * @param serviceName the service name.
	 * @param serviceInterface the service interface.
	 * @return the service loaded and configured by the service loader.
	 */
	public <T> T load(
			ClassLoader classLoader, String serviceName, Class<T> serviceInterface
			){
		Object service = getServiceFactory(classLoader, serviceName).createService(
				classLoader, serviceContext, serviceInterface);
		if(service instanceof AbstractService){
			((AbstractService)service).setServiceName(serviceName);
		}
		return serviceInterface.cast(service);
	}

	/**
	 * Load service factory.
	 * @param classLoader the class loader used for loading service factory.
	 * @param serviceName the service name.
	 * @return the service factory configured by the service loader.
	 */
	public ServiceFactory loadServiceFactory(
			ClassLoader classLoader, String serviceName
			){
		return getServiceFactory(classLoader, serviceName);
	}

	private ServiceFactory getServiceFactory(
			ClassLoader classLoader, String serviceName){
		ServiceContextUtil.setCurrentServiceContextForAbstractService(serviceContext);
		for(ServiceFactoryLoader l : serviceFactoryLoaders){
			if(l.hasServiceFactoryFor(serviceName)){
				return l.getServiceFactory(classLoader, serviceContext, serviceName);
			}
		}
		if(servicesPath != null){
			File dir = servicesPath;
			String[] s = serviceName.split(":", 2);
			if(s.length == 2){
				dir = new File(dir, s[0]);
				serviceName = s[1];
			}
			File[] files = dir.listFiles();
			if(files != null) for(File f : files){
				String fname = f.getName();
				int pi = fname.lastIndexOf('.');
				if(pi == -1) continue;
				if(fname.substring(0, pi).equals(serviceName)){
					String ext = fname.substring(pi + 1);
					FileServiceFactoryLoader l = fileServiceFactoryLoaders.get(ext);
					if(l != null) return l.getServiceFactory(
							classLoader, serviceContext, serviceName, f);
				}
			}
		}
		return null;
	}

	private ServiceContext serviceContext;
	private File servicesPath;
	private List<ServiceFactoryLoader> serviceFactoryLoaders = new ArrayList<ServiceFactoryLoader>();
	private Map<String, FileServiceFactoryLoader> fileServiceFactoryLoaders = new ConcurrentHashMap<String, FileServiceFactoryLoader>();

	private static Logger logger = Logger.getLogger(ServiceLoader.class.getName());
}
