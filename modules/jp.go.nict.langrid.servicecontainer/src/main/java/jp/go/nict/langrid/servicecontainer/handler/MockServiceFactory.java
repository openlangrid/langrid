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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

/**
 * 
 */
public class MockServiceFactory
extends AbstractServiceFactory
implements ServiceFactory{
	public void init(ServiceContext serviceContext, String serviceName){
		super.init(serviceContext, serviceName);
		if(!initialized && generateDummyData){
			try{
				checkDummyData(serviceContext);
			} catch(Exception e){
			}
		}
		initialized = true;
	}

	@Override
	public Object getService(){
		return Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, getInterfaces().toArray(new Class[]{})
				, new MockServiceHandler());
	}

	public boolean isGenerateDummyData() {
		return generateDummyData;
	}

	public void setGenerateDummyData(boolean generateDummyData) {
		this.generateDummyData = generateDummyData;
	}

	@Override
	public void afterLoad() {
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Set<Class<?>> guessInterfaceClasses() {
		return Collections.EMPTY_SET;
	}
	
	private void checkDummyData(ServiceContext serviceContext)
	throws SecurityException, ClassNotFoundException{
		if(!generateDummyData) return;
		JSON j = new JSON();
		j.setPrettyPrint(true);
		for(Class<?> clazz : getInterfaces()){
			if(!clazz.isInterface()) continue;
			for(Method m : clazz.getMethods()){
				File f = getFile(serviceContext, m);
				if(f.exists()) continue;
				if(!f.getParentFile().exists()){
					f.getParentFile().mkdirs();
				}
				Logger.getAnonymousLogger().info("generating " + f);
				try {
					Object ret = ClassUtil.newDummyInstance(m.getReturnType());
					OutputStream os = new FileOutputStream(f);
					try{
						j.format(ret, os);
					} finally{
						os.close();
					}
				} catch (InstantiationException e1) {
				} catch (IllegalAccessException e1) {
				} catch(IOException e){
				}
			}
		}
	}


	class MockServiceHandler implements InvocationHandler{
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			return getDummyData(method, args);
		}

		@SuppressWarnings("unchecked")
		protected <T> T getDummyData(Method method, Object[] args){
			try{
				File f = getFile(RIProcessor.getCurrentServiceContext(), method, args);
				if(f.exists()){
					InputStream is = new FileInputStream(f);
					try{
						return (T)JSON.decode(is, method.getReturnType());
					} finally{
						is.close();
					}
				} else{
					return null;
				}
			} catch(IOException e){
				throw new RuntimeException(e);
			}
		}
	}

	private File getFile(ServiceContext sc, Method method, Object... args){
		String configPathBase = sc.getInitParameter("servicesPath");
		if(configPathBase == null) configPathBase = "WEB-INF/services";
		String base = sc.getRealPath(configPathBase);

		String methodName = method.getName();
		String dir = String.format("%s/%s_mock", base, getServiceName());
		StringBuilder argpart = new StringBuilder();
		for(Object arg : args){
			argpart.append("_").append(arg);
		}
		int n = method.getParameterTypes().length - args.length;
		for(int i = 0; i < n; i++){
			argpart.append("_null");
		}
		File ret = new File(dir + "/" + methodName + argpart + ".json");
		if(ret.exists()) return ret;
		return new File(dir + "/" + methodName + ".json");
	}

	private boolean initialized;
	private boolean generateDummyData;
}
