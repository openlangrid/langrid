/*
 * $Id: CompositeServiceEntry.java 225 2010-10-03 00:23:14Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.service_1_2.foundation.servicemanagement;

import java.io.Serializable;
import java.util.Calendar;

import jp.go.nict.langrid.service_1_2.LanguagePath;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class CompositeServiceEntry
extends ServiceEntry
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public CompositeServiceEntry(){
	}

	/**
	 * 
	 * 
	 */
	public CompositeServiceEntry(String serviceId, String serviceName
			, String serviceDescription, 
			String serviceTypeDomain, String serviceType
			, String instanceType, LanguagePath[] supportedLanguages
			, String endpointUrl, String ownerUserId
			, Calendar registeredDate, Calendar updatedDate
			, boolean active
			, ServiceEntry[] servicesInUse
			)
	{
		super(serviceId, serviceName
			, serviceDescription,
			serviceTypeDomain, serviceType
			, instanceType, supportedLanguages
			, endpointUrl, ownerUserId
			, registeredDate, updatedDate
			, active);
		this.servicesInUse = servicesInUse;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 
	 * 
	 */
	public ServiceEntry[] getServicesInUse() {
		return servicesInUse;
	}

	/**
	 * 
	 * 
	 */
	public void setServicesInUse(ServiceEntry[] servicesInUse) {
		this.servicesInUse = servicesInUse;
	}

	private ServiceEntry[] servicesInUse;

	private static final long serialVersionUID = 2478635194524021806L;
}
