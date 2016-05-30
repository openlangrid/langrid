/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.decorator.cache;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.servicecontainer.decorator.Decorator;
import jp.go.nict.langrid.servicecontainer.decorator.DecoratorChain;
import jp.go.nict.langrid.servicecontainer.decorator.Request;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.Config;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.base.persistence.PersistenceListener;

public class CacheDecorator implements Decorator{
	public CacheDecorator(){
	}

	public void setCacheTtlSec(int cacheTtlSec) {
		this.cacheTtlSec = cacheTtlSec;
	}

	@Override
	public Object doDecorate(Request request, DecoratorChain chain) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
		initCache(request.getServiceContext());
		if(!request.getMethod().getReturnType().equals(void.class)){
			String serviceId = request.getServiceId();
			String methodName = request.getMethod().getName();
			String key = serviceId + "#" + methodName + ":" + JSON.encode(request.getArgs());
			try{
				return cache.getFromCache(key, cacheTtlSec);
			} catch(NeedsRefreshException e){
				boolean put = false;
				try{
					Object ret = chain.next(request);
					cache.putInCache(key, ret);
					put = true;
					return ret;
				} finally{
					if(!put) cache.cancelUpdate(key);
				}
			}
		}
		return chain.next(request);
	}

	private int cacheTtlSec = 600;
	private static Cache cache;
	private static synchronized void initCache(ServiceContext context){
		if(cache != null) return;
		String base = CacheDecorator.class.getName();
		int cap = 100;
		String capacity = context.getInitParameter(base + ".cacheCapacity");
		if(capacity != null){
			cap = Integer.parseInt(capacity);
		}
		cache = new Cache(true, false, false
				, true, "com.opensymphony.oscache.base.algorithm.LRUCache", cap);

		String cachePath = context.getInitParameter(base + ".cachePath");
		if(cachePath != null){
			Config cfg = new Config(new Properties());
			cfg.set("cache.path", context.getRealPath("WEB-INF" + File.separator + cachePath));
			PersistenceListener listener = new com.opensymphony.oscache.plugins.diskpersistence.HashDiskPersistenceListener();
	        listener = listener.configure(cfg);
			cache.setPersistenceListener(listener);
		}
	}
}
