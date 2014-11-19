/*
 * $Id:AccessRight.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
@IdClass(AccessRightPK.class)
public class AccessRight
extends UpdateManagedEntity
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public AccessRight(){
	}

	/**
	 * 
	 * 
	 */
	public AccessRight(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			, boolean permitted
			)
	{
		this.userGridId = userGridId;
		this.userId = userId;
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
		this.permitted = permitted;
	}

	/**
	 * 
	 * 
	 */
	public AccessRight(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			, boolean permitted
			, Calendar createdDateTime, Calendar updatedDateTime
			)
	{
		super(createdDateTime, updatedDateTime);
		this.userGridId = userGridId;
		this.userId = userId;
		this.serviceGridId = serviceGridId;
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
	 * 
	 */
	public String getUserGridId() {
		return userGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setUserGridId(String userGridId) {
		this.userGridId = userGridId;
	}

	/**
	 * 
	 * 
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	public boolean isPermitted() {
		return permitted;
	}

	/**
	 * 
	 * 
	 */
	public void setPermitted(boolean permitted) {
		this.permitted = permitted;
	}

	@Id
	private String userGridId;
	@Id
	private String userId;
	@Id
	private String serviceGridId;
	@Id
	private String serviceId;
	private boolean permitted;

	private static final long serialVersionUID = 8872278782955845400L;
}
