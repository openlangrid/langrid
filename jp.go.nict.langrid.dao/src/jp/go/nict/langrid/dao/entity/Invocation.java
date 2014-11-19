/*
 * $Id: Invocation.java 484 2012-05-24 02:35:49Z t-nakaguchi $
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
package jp.go.nict.langrid.dao.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 484 $
 */
@Entity
@IdClass(InvocationPK.class)
public class Invocation
extends UpdateManagedEntity
implements Serializable{
	/**
	 * 
	 * 
	 */
	public Invocation() {
	}

	/**
	 * 
	 * 
	 */
	public Invocation(
			String ownerServiceGridId, String ownerServiceId
			, String invocationName
			, String serviceGridId, String serviceId
			, String serviceName) {
		this.ownerServiceGridId = ownerServiceGridId;
		this.ownerServiceId = ownerServiceId;
		this.invocationName = invocationName;
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerServiceGridId() {
		return ownerServiceGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerServiceGridId(String ownerServiceGridId) {
		this.ownerServiceGridId = ownerServiceGridId;
	}

	/**
	 * 
	 * @return the ownerServiceId
	 * 
	 */
	public String getOwnerServiceId() {
		return ownerServiceId;
	}

	/**
	 * 
	 * @param ownerServiceId the ownerServiceId to set
	 * 
	 */
	public void setOwnerServiceId(String ownerServiceId) {
		this.ownerServiceId = ownerServiceId;
	}

	/**
	 * 
	 * 
	 */
	public String getInvocationName() {
		return invocationName;
	}

	/**
	 * 
	 * 
	 */
	public void setInvocationName(String invocationName) {
		this.invocationName = invocationName;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceGridId() {
		return serviceGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceGridId(String serviceGridId) {
		this.serviceGridId = serviceGridId;
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
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	
	/**
	 * 
	 * 
	 */
	public String getServiceTypeId() {
		return serviceTypeId;
	}
	
	/**
	 * 
	 * 
	 */
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	/**
	 * 
	 * 
	 */
	public String getDomainId() {
		return domainId;
	}
	
	@Id
	private String ownerServiceGridId;
	@Id
	private String ownerServiceId;
	@Id
	private String invocationName;

	private String serviceGridId;
	private String serviceId;
	private String serviceName;
	private String serviceTypeId;
	private String domainId;

	private static final long serialVersionUID = -5861766303202946876L;
}
