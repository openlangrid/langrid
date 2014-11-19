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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.servicecontainer.decorator.Decorator;
import jp.go.nict.langrid.servicecontainer.decorator.FirstDecoratorChain;
import jp.go.nict.langrid.servicecontainer.decorator.ServiceInvoker;
import jp.go.nict.langrid.servicecontainer.executor.DynamicService;
import jp.go.nict.langrid.servicecontainer.executor.StreamingNotifier;
import jp.go.nict.langrid.servicecontainer.service.AbstractService;

/**
 * 
 * 
 */
public abstract class AbstractServiceFactory implements ServiceFactory{
	@Override
	public void init(ServiceContext serviceContext, String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	@Override
	public <T> T createService(
			ClassLoader classLoader, ServiceContext context
			, Class<T> interfaceClass)
	{
		Object service = getService();

		Set<Class<?>> interfaces = getInterfaces();
		if(service instanceof StreamingNotifier){
			interfaces.add(StreamingNotifier.class);
		}
		if(service instanceof AbstractService){
			((AbstractService) service).setServiceName(serviceName);
		}
		return interfaceClass.cast(Proxy.newProxyInstance(
				classLoader, interfaces.toArray(new Class<?>[]{})
				, new ServiceInvocationHandler(context, serviceName
						, new FirstDecoratorChain(decorators, new ServiceInvoker())
						, service)
				));
	}

	public Object getMockService(){
		Set<Class<?>> interfaces = getInterfaces();
		return Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(), interfaces.toArray(new Class<?>[]{})
				, new InvocationHandler() {
					@Override
					public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
						return null;
					}
				});
	}

	public String getInterfaceClass(){
		return interfaceClassName;
	}

	public void setInterfaceClass(String interfaceClassName) {
		this.interfaceClassName = interfaceClassName;
	}

	public List<String> getInterfaceClasses() {
		return interfaceClassNames;
	}

	public void setInterfaceClasses(List<String> interfaceClassNames) {
		this.interfaceClassNames = interfaceClassNames;
	}

	protected void setInterfaceClasses(Class<?>[] interfaceClasses){
		this.interfaceClasses = interfaceClasses;
	}

	@Override
	public Set<Class<?>> getInterfaces(){
		Set<Class<?>> ret = new TreeSet<Class<?>>(new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		ret.addAll(Arrays.asList(interfaceClasses));
		if(interfaceClassName != null){
			try{
				ret.add(Class.forName(interfaceClassName));
			} catch(ClassNotFoundException e){
				logger.warning("failed to load class: " + interfaceClassName);
			}
		}
		for(String c : interfaceClassNames){
			try{
				ret.add(Class.forName(c));
			} catch(ClassNotFoundException e){
				logger.warning("failed to load class: " + c);
			}
		}
		if(ret.isEmpty()) for(Class<?> c : guessInterfaceClasses()){
			ret.add(c);
		}
		return ret;
	}

	protected Set<Class<?>> guessInterfaceClasses(){
		Set<Class<?>> ret = new LinkedHashSet<Class<?>>();
		Object service = getService();
		if(service == null) return ret;
		Class<?> clazz = service.getClass();
		while(clazz != null && !clazz.equals(Object.class)){
			for(Class<?> c : clazz.getInterfaces()){
				String name = c.getName();
				if(name.startsWith("java.") || name.startsWith("javax.")) continue;
				if(c.equals(DynamicService.class)) continue;
				ret.add(c);
			}
			clazz = clazz.getSuperclass();
		}
		return ret;
	}

	/**
	 * 
	 * 
	 */
	public List<Decorator> getDecorators(){
		return decorators;
	}

	/**
	 * 
	 * 
	 */
	public void setDecorators(List<Decorator> decorators){
		this.decorators = decorators;
	}

	private String serviceName;
	private String interfaceClassName;
	private Class<?>[] interfaceClasses = {};
	private List<String> interfaceClassNames = new ArrayList<String>();
	private List<Decorator> decorators = new ArrayList<Decorator>();
	private static Logger logger = Logger.getLogger(AbstractServiceFactory.class.getName());
}
