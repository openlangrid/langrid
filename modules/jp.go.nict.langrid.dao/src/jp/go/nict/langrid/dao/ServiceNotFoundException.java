/*
 * $Id: ServiceNotFoundException.java 214 2010-10-02 14:32:38Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public class ServiceNotFoundException extends DaoException{
	/**
	 * 
	 * 
	 */
	public ServiceNotFoundException(String serviceGridId, String serviceId) {
		super(toMessage(serviceGridId, serviceId));
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public ServiceNotFoundException(String serviceGridId, String serviceId, Throwable cause){
		super(toMessage(serviceGridId, serviceId));
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceGridId(){
		return serviceGridId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceId(){
		return serviceId;
	}

	private String serviceGridId;
	private String serviceId;

	private static String toMessage(String serviceGridId, String serviceId){
		return "service (id:" + serviceId + ",gridId:" + serviceGridId + ") is not found in repository.";
	}

	private static final long serialVersionUID = -4752460924017516130L;
}
