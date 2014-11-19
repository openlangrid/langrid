/*
 * $Id: AccessRight.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores access privilege.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class AccessRight
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AccessRight(){
	}

	/**
	 * 
	 * Constructor.
	 * @param userId User ID
	 * @param serviceId Service ID
	 * @param permitted Whether permitted or not
	 * 
	 */
	public AccessRight(String userId
			, String serviceId, boolean permitted)
	{
		this.userId = userId;
		this.serviceId = serviceId;
		this.permitted = permitted;
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
	 * Gets user ID.
	 * @return User ID
	 * 
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * Sets user ID.
	 * @param userId User ID
	 * 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * Gets service ID.
	 * @return Service ID
	 * 
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * Sets service ID.
	 * @param serviceId Service ID
	 * 
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * Gets permission flag.
	 * @return Permission flag
	 * 
	 */
	public boolean isPermitted() {
		return permitted;
	}

	/**
	 * 
	 * Sets permission flag.
	 * @param permitted Permission flag
	 * 
	 */
	public void setPermitted(boolean permitted) {
		this.permitted = permitted;
	}

	private String userId;
	private String serviceId;
	private boolean permitted;

	private static final long serialVersionUID = -3584041131539017741L;
}
