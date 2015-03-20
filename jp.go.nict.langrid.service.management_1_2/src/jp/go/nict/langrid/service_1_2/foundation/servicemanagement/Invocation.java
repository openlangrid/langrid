/*
 * $Id: Invocation.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores external service call data.
 * If the information for the default service responding to the call cannot be acquired,
 * store null in defaultServiceId and serviceType.
 * The serviceType is, {@link jp.go.nict.langrid.service_1_2.typed.ServiceType ServiceType}'s
 * enumerated value, cast as a string.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class Invocation
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public Invocation(){
	}

	/**
	 * 
	 * Constructor.
	 * @param invocationName Invocation name
	 * @param serviceId Default service ID
	 * @param serviceName Default service name
	 * @param serviceType Default service type
	 * 
	 */
	public Invocation(String invocationName
			, String serviceId
			, String serviceName
			, String serviceType) {
		this.invocationName = invocationName;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceType = serviceType;
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
	 * Returns external call name.
	 * @return External call name
	 * 
	 */
	public String getInvocationName() {
		return invocationName;
	}

	/**
	 * 
	 * Sets external call name.
	 * @param invocationName Invocation name
	 * 
	 */
	public void setInvocationName(String invocationName) {
		this.invocationName = invocationName;
	}

	/**
	 * 
	 * Returns default service ID.
	 * @return Default user ID
	 * 
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * Sets default service ID.
	 * @param serviceId Default service ID
	 * 
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * Return service name.
	 * @return Service name
	 * 
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * Set service name.
	 * @param serviceName Service name
	 * 
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * Returns service type.
	 * @return Service type
	 * 
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * 
	 * Sets service type.
	 * @param serviceType Service type
	 * 
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	private String invocationName;
	private String serviceId;
	private String serviceName;
	private String serviceType;

	private static final long serialVersionUID = -8411323252529378889L;
}
