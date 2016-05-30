/*
 * $Id: ServiceDeployment.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ServiceDeployment
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public ServiceDeployment(){
	}

	/**
	 * 
	 * 
	 */
	public ServiceDeployment(String serviceId, String nodeId,
			String servicePath, boolean enabled) {
		this.serviceId = serviceId;
		this.nodeId = nodeId;
		this.servicePath = servicePath;
		this.enabled = enabled;
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
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * 
	 * 
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * 
	 * 
	 */
	public String getNodeId() {
		return nodeId;
	}
	/**
	 * 
	 * 
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * 
	 * 
	 */
	public String getServicePath() {
		return servicePath;
	}
	/**
	 * 
	 * 
	 */
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	/**
	 * 
	 * 
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 
	 * 
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private String serviceId;
	private String nodeId;
	private String servicePath;
	private boolean enabled;

	private static final long serialVersionUID = -3714831039240171255L;
}
