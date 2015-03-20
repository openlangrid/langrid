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

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * 
 */
public class TargetServiceFactory
extends AbstractServiceFactory
implements ServiceFactory{
	@Override
	public Object getService(){
		return service;
	}

	/**
	 * 
	 * 
	 */
	public void setService(Object service){
		if(this.service != null){
			throw new RuntimeException("The instance of service is already set.");
		}
		this.service = service;
	}

	@Override
	public void afterLoad() {
		try {
			service.getClass().getMethod("afterLoad").invoke(service);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private Object service;
}
