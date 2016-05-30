/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.servicecontainer.decorator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.go.nict.langrid.servicecontainer.executor.DynamicService;

/**
 * 
 * 
 */
public class ServiceInvoker{
	/**
	 * 
	 * 
	 */
	public Object invoke(Request request)
	throws InvocationTargetException, IllegalArgumentException, IllegalAccessException{
		Object service = request.getService();
		Method m = request.getMethod();
		if(m.getDeclaringClass().isAssignableFrom(service.getClass())){
			return m.invoke(request.getService(), request.getArgs());
		} else if(service instanceof DynamicService){
			try{
				return ((DynamicService)service).invoke(m, request.getArgs());
			} catch(Throwable t){
				throw new InvocationTargetException(t);
			}
		} else{
			return m.invoke(service, request.getArgs());
		}
	}
}
