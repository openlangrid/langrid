/*
 * $Id:ServiceTypePK.java 4384 2007-04-03 08:56:48Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 207 $
 */
public class ServiceTypePK implements Serializable{
	/**
	 * 
	 * 
	 */
	public ServiceTypePK(){
		recalcHashCode();
	}

	/**
	 * 
	 * 
	 */
	public ServiceTypePK(String domainId, String serviceTypeId){
		this.domainId = domainId;
		this.serviceTypeId = serviceTypeId;
		recalcHashCode();
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return hashCode;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 
	 * 
	 */
	public String getDomainId() {
		return domainId;
	}

	/**
	 * 
	 * 
	 */
	public void setDomainId(String domainId) {
		this.domainId = domainId;
		recalcHashCode();
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
	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
		recalcHashCode();
	}

	private void recalcHashCode(){
		hashCode = new HashCodeBuilder()
			.append(domainId)
			.append(serviceTypeId)
			.toHashCode();
	}

	private String domainId;
	private String serviceTypeId;
	private transient int hashCode;

	private static final long serialVersionUID = -5201980701391014617L;
}
