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
package jp.go.nict.langrid.servicecontainer.service.component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import jp.go.nict.langrid.cosee.AppAuthEndpointRewriter;
import jp.go.nict.langrid.cosee.DynamicBindingRewriter;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.cosee.EndpointRewriter;
import jp.go.nict.langrid.cosee.UserAuthEndpointRewriter;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessorContext;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public abstract class AbstractComponentServiceFactory
implements ComponentServiceFactory{
	public AbstractComponentServiceFactory(){
		rewriters = new EndpointRewriter[]{
			new DynamicBindingRewriter()
			, new AppAuthEndpointRewriter()
			, new UserAuthEndpointRewriter()
			};
	}

	public AbstractComponentServiceFactory(EndpointRewriter[] rewriters){
		this.rewriters = rewriters;
	}

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
		if(cacheEnabled){
			if(cache == null){
				cache = CacheBuilder.newBuilder().build();
			}
		} else{
			cache = null;
		}
	}

	public Cache<String, Object> getCache() {
		return cache;
	}

	public int getCacheTtlSec() {
		return cacheTtlSec;
	}

	public void setCacheTtlSec(int cacheTtlSec){
		this.cacheTtlSec = cacheTtlSec;
		if(cacheEnabled) initCache();
	}

	public int getCacheCapacity() {
		return cacheCapacity;
	}

	public void setCacheCapacity(int capacity){
		this.cacheCapacity = capacity;
		if(cacheEnabled) initCache();
	}

	@Override
	public <T> T getService(final String invocationName, final Class<T> interfaceClass) {
		final long iid = RIProcessor.newInvocationId();
		String name = getClass().getName() + "#" + hashCode() + ".rewritersInitialized";
		RIProcessorContext pc = RIProcessor.getCurrentProcessorContext();
		Object initialized = pc.getProperty(name);
		if(initialized == null){
			RIProcessor.initEndpointRewriters(rewriters);
			pc.setProperty(name, true);
		}
		return interfaceClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class<?>[]{interfaceClass},
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Endpoint endpoint = RIProcessor.rewriteEndpoint(
								iid, invocationName, rewriters,
								method, args);
						return method.invoke(getService(invocationName, iid, endpoint, interfaceClass),
								args);
					}
				}));
	}

	public abstract <T> T getService(
			String invocationName, long invocationId, Endpoint endpoint
			, Class<T> interfaceClass);

	private void initCache(){
		cache = CacheBuilder.newBuilder()
				.expireAfterAccess(cacheTtlSec, TimeUnit.SECONDS)
				.maximumSize(cacheCapacity)
				.build();
	}

	private EndpointRewriter[] rewriters;
	private boolean cacheEnabled;
	private Cache<String, Object> cache;
	private int cacheTtlSec = 600;
	private int cacheCapacity = 100;
}
