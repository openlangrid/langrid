/*
 * $Id: ServiceTypeAlreadyExistsException.java 214 2010-10-02 14:32:38Z t-nakaguchi $
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
public class ServiceTypeAlreadyExistsException extends DaoException {
	/**
	 * 
	 * 
	 */
	public ServiceTypeAlreadyExistsException(String domainId, String serviceTypeId) {
		super(toMessage(domainId, serviceTypeId));
		this.domainId = domainId;
		this.serviceTypeId = serviceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public String getDomainId(){
		return domainId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceTypeId(){
		return serviceTypeId;
	}

	private String domainId;
	private String serviceTypeId;

	private static String toMessage(String domainId, String serviceTypeId){
		return "serviceType \"" + domainId + ":" + serviceTypeId + "\" is already exists.";
	}

	private static final long serialVersionUID = -6818982119511271395L;
}
