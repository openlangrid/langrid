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

import java.lang.reflect.InvocationTargetException;

import jp.go.nict.langrid.servicecontainer.decorator.Decorator;
import jp.go.nict.langrid.servicecontainer.decorator.DecoratorChain;
import jp.go.nict.langrid.servicecontainer.decorator.Request;

public class CacheDecoratorJCS implements Decorator{
	@Override
	public Object doDecorate(Request request, DecoratorChain chain) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
/*		if(!request.getMethod().getReturnType().equals(void.class)){
			String serviceId = request.getServiceId();
			String methodName = request.getMethod().getName();
			String key = methodName + ":" + JSON.encode(request.getArgs());
			try{
				JCS cache = JCS.getInstance(serviceId);
				Object ret = cache.get(key);
				if(ret != null){
					if(ret == nullObject){
						return null;
					}
					return ret;
				}
				ret = chain.doDecorate(request);
				if(ret != null){
					if(ret instanceof Serializable){
						cache.put(key, ret);
					} else{
						logger.info("cache for " + serviceId + "#" + methodName + "(..) will not work because result not implement Serialiable.");
					}
				} else{
					cache.put(key, nullObject);
				}
				Object value = cache.get(key);
				if(value == null){
					System.out.println("cache not work.");
				}
				return ret;
			} catch(CacheException e){
				logger.log(Level.SEVERE, "cache exception occurred.", e);
			}
		}
*/		return chain.next(request);
	}
/*
	private static class NullObject implements Serializable{
		private static final long serialVersionUID = -2462581557702951129L;

	}
	private static final Object nullObject = new NullObject();
	private static Logger logger = Logger.getLogger(CacheDecoratorJCS.class.getName());
*/
}
