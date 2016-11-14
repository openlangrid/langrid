/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.handler.axis;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.WSDDEngineConfiguration;
import org.apache.axis.deployment.wsdd.WSDDBeanMapping;
import org.apache.axis.deployment.wsdd.WSDDConstants;
import org.apache.axis.deployment.wsdd.WSDDDeployment;
import org.apache.axis.deployment.wsdd.WSDDService;
import org.apache.axis.transport.http.AxisServlet;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.transformer.ClassToClassNameTransformer;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServletConfigServiceContext;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.param.ServletConfigParameterContext;
import jp.go.nict.langrid.servicecontainer.handler.ServiceFactory;
import jp.go.nict.langrid.servicecontainer.handler.ServiceLoader;
import jp.go.nict.langrid.servicecontainer.handler.annotation.ServicesUtil;
import jp.go.nict.langrid.servicecontainer.handler.loader.ServiceFactoryLoader;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class SGAxisServlet extends AxisServlet {
	/**
	 * 
	 * 
	 */
	public static ServiceContext getCurrentServletServiceContext(){
		return currentServletConfig.get();
	}

	public static ServiceLoader getCurrentServletServiceLoader() {
		return currentServiceLoader.get();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		ParameterContext pc = new ServletConfigParameterContext(getServletConfig(), true);
		String mappings = pc.getString("wsddBeanNamespaceMappings", "");
		initNamespaceMappings(mappings);
		this.defaultLoaders = ServicesUtil.getServiceFactoryLoaders(getClass());
		updateServiceDeployment();
	}

	protected ServiceFactoryLoader[] getDefaultServiceFactoryLoaders(){
		return defaultLoaders;
	}


	private synchronized void updateServiceDeployment() throws ServletException{
		lastUpdate = System.currentTimeMillis();
		try{
			EngineConfiguration config = getEngine(this).getConfig();
			if(!(config instanceof WSDDEngineConfiguration)) return;
			loadServicesFromServicesPath(
					((WSDDEngineConfiguration)config).getDeployment()
					, new ServiceLoader(new ServletConfigServiceContext(getServletConfig()), getDefaultServiceFactoryLoaders())
					);
		} catch(AxisFault e){
		} catch(IOException e){
			throw new ServletException(e);
		}
	}

	private void loadServicesFromServicesPath(WSDDDeployment deployment, ServiceLoader loader)
	throws IOException{
		for(String name: loader.listServiceNames()){
			if(deployment.getWSDDService(new QName(name)) != null) continue;

			ServiceFactory factory = null;
			try{
				factory = loader.loadServiceFactory(
						Thread.currentThread().getContextClassLoader()
						, name);
			} catch(RuntimeException e){
				if(e.getClass().getName().equals("BeanCreationException")){
					logger.log(
							Level.WARNING
							, "ignore service \"" + name + "\" because interface class not determined."
							, e);
					continue;
				}
				throw e;
			}
			Set<Class<?>> interfaceClasses = factory.getInterfaces();
			if(interfaceClasses.isEmpty()){
				logger.warning("ignore service \"" + name + "\" because interface class not determined.");
				continue;
			}

			WSDDService s = new WSDDService();
			s.setName(name);
			s.getServiceDesc().setName(name);
			s.setProviderQName(new QName(
					"http://xml.apache.org/axis/wsdd/providers/java"
					, "SGRPC"));
			s.setParameter("className", interfaceClasses.iterator().next().getName());
			s.setParameter("x_sg_interfaces"
					, StringUtil.join(interfaceClasses.toArray(new Class<?>[]{}), new ClassToClassNameTransformer(), ",")
					);
			s.setParameter("x_sg_managed", "true");
			for(Class<?> c : interfaceClasses){
				addBeanMapping(s, c.getName());
			}
			deployment.deployService(s);
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ServiceContext sc = new ServletServiceContext(req, new ArrayList<RpcHeader>());
		currentServletConfig.set(sc);
		currentServiceLoader.set(new ServiceLoader(sc, getDefaultServiceFactoryLoaders()));
		try{
			if((60 * 1000) <= (System.currentTimeMillis() - lastUpdate)){
				updateServiceDeployment();
			} else{
				String serviceName = SGJavaProvider.getServiceName(sc);
				if(serviceName != null && getEngine(this).getService(serviceName) == null){
					updateServiceDeployment();
				}
			}
			// First execution must be completed before other executions start in order
			// to prevent ConcurrentModificationException caused in axis library.
			boolean done = false;
			synchronized(SGAxisServlet.class){
				if(first){
					super.service(req, resp);
					first = false;
					done = true;
				}
			}
			if(!done){
				super.service(req, resp);
			}
		} finally{
			currentServiceLoader.remove();
			currentServletConfig.remove();
		}
	}
	private static boolean first = true;

	private void initNamespaceMappings(String mappings){
		for(String m : mappings.split("\\n")){
			String[] values = m.trim().split(", *");
			if(values.length < 2 || values[0].length() == 0) continue;
			namespaceMappings.add(Pair.create(values[0], values[1]));
		}
	}

	private void addBeanMapping(WSDDService service, String interfaceClass){
		try {
			Set<Class<?>> visited = new HashSet<Class<?>>();
			Deque<Class<?>> classes = new LinkedList<Class<?>>();
			Class<?> clazz = Class.forName(interfaceClass);
			Set<Class<?>> mappedClasses = new HashSet<Class<?>>();
			classes.addLast(clazz);
			visited.add(clazz);
			while(clazz != null && !clazz.equals(Object.class)){
				for(Method m : clazz.getDeclaredMethods()){
					addBeanMapping(service, m.getReturnType(), mappedClasses);
					for(Class<?> a : m.getParameterTypes()){
						addBeanMapping(service, a, mappedClasses);
					}
					for(Class<?> e : m.getExceptionTypes()){
						addBeanMapping(service, e, mappedClasses);
					}
				}
				if(clazz.isInterface()){
					for(Class<?> clz : clazz.getInterfaces()){
						if(!visited.contains(clz)){
							classes.add(clz);
							visited.add(clz);
						}
					}
				} else{
					Class<?> clz = clazz.getSuperclass();
					if(!visited.contains(clz)){
						classes.add(clz);
						visited.add(clz);
					}
				}
				clazz = classes.pollFirst();
			}
		} catch (ClassNotFoundException e) {
		}
	}

	private void addBeanMapping(WSDDService service, Class<?> clazz, Collection<Class<?>> mappedClasses){
		while(clazz != null){
			while(clazz.isArray()){
				clazz = clazz.getComponentType();
			}
			if(!needsMapping(clazz)) return;
			if(mappedClasses.contains(clazz)) return;
			mappedClasses.add(clazz);

			for(Method m : clazz.getDeclaredMethods()){
				if(!m.isSynthetic()
						&& m.getName().startsWith("get")
						&& m.getParameterTypes().length == 0
						&& (m.getModifiers() & Modifier.PUBLIC) != 0){
					addBeanMapping(service, m.getReturnType(), mappedClasses);
				}
			}

			WSDDBeanMapping m = new WSDDBeanMapping();
			m.setLanguageSpecificType(clazz);
			Package p = clazz.getPackage();
			if(p == null){
				// arrayちゃんと処理できてる?
				// System.out.println(clazz);
			}
			String packageName = p != null ? p.getName() : "";
			String simpleName = clazz.getSimpleName();
			boolean qnameSet = false;
			for(Pair<String, String> n : namespaceMappings){
				if(packageName.startsWith(n.getFirst())){
					String rest = packageName.substring(n.getFirst().length()).replace('.', '/');
					String nsPrefix = n.getSecond();
					if(rest.length() > 0){
						rest = rest + "/";
						if(nsPrefix.endsWith("/") && rest.charAt(0) == '/'){
							rest = rest.substring(1);
						}
					}
					m.setQName(new QName(n.getSecond() + rest, simpleName));
					qnameSet = true;
					break;
				}
			}
			if(!qnameSet){
				m.setQName(new QName("uri:" + packageName + "/"	, simpleName));
			}
	        m.setSerializer(WSDDConstants.BEAN_SERIALIZER_FACTORY);
	        m.setDeserializer(WSDDConstants.BEAN_DESERIALIZER_FACTORY);
			service.addTypeMapping(m);

			clazz = clazz.getSuperclass();
		}
	}

	private boolean needsMapping(Class<?> clazz){
		if(clazz.isPrimitive()) return false;
		if(clazz.equals(Object.class)) return false;
		if(clazz.equals(Class.class)) return false;
		return !ignoreClasses.contains(clazz);
	}

	private List<Pair<String, String>> namespaceMappings = new ArrayList<Pair<String,String>>();
	private long lastUpdate;
	private ServiceFactoryLoader[] defaultLoaders;
	private static final long serialVersionUID = 4086806608108580827L;
	private static Set<Class<?>> ignoreClasses = new HashSet<Class<?>>();
	private static ThreadLocal<ServiceContext> currentServletConfig = new ThreadLocal<ServiceContext>();
	private static ThreadLocal<ServiceLoader> currentServiceLoader = new ThreadLocal<ServiceLoader>();
	private static Logger logger = Logger.getLogger(SGAxisServlet.class.getName());

	static{
		ignoreClasses.add(Object.class);
		ignoreClasses.add(Exception.class);
		ignoreClasses.add(String.class);
		ignoreClasses.add(Void.class);
		ignoreClasses.add(Byte.class);
		ignoreClasses.add(Character.class);
		ignoreClasses.add(Short.class);
		ignoreClasses.add(Integer.class);
		ignoreClasses.add(Long.class);
		ignoreClasses.add(Float.class);
		ignoreClasses.add(Double.class);
		ignoreClasses.add(Calendar.class);
	}
}
