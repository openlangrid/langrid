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

import java.lang.reflect.Method;

import jp.go.nict.langrid.commons.ws.ServiceContext;

/**
 * 
 * 
 * @author Shingo Furukido
 * @author Takao Nakaguchi
 */
public class Request{
	public Request(ServiceContext serviceContext
			, String authUserId, String serviceId
			, Object service, Class<?> serviceClass, Method method, Object[] args){
		this.serviceContext = serviceContext;
		this.authUserId = authUserId;
		this.serviceId = serviceId;
		this.service = service;
		this.serviceClass = serviceClass;
		this.method = method;
		this.args = args;
	}

	public ServiceContext getServiceContext(){
		return serviceContext;
	}

	public String getAuthUserId(){
		return authUserId;
	}

	public String getServiceId(){
		return serviceId;
	}

	public Class<?> getServiceClass( ){
		return serviceClass;
	}

	public Object getService() {
		return service;
	}

	public Method getMethod(){
		return method;
	}

	public Object[] getArgs(){
		return args;
	}

	public void setArgs( Object[] args ){
		this.args = args;
	}

	private ServiceContext serviceContext;
	private String authUserId;
	private String serviceId;
	private Object service;
	private Class<?> serviceClass;
	private Method method;
	private Object[] args;
}
