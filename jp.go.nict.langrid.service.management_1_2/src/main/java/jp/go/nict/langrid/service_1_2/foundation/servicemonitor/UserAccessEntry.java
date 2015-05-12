/*
 * $Id: UserAccessEntry.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.servicemonitor;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores ranking data.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class UserAccessEntry
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public UserAccessEntry(){
	}

	/**
	 * 
	 * Constructor.
	 * @param userId User ID
	 * @param userOrganization The user's organization
	 * @param serviceId Service ID
	 * @param serviceName Service name
	 * @param accessCount Access count
	 * @param transferredSize Size of data transfer
	 * 
	 */
	public UserAccessEntry(
			String userId, String userOrganization
			, String serviceId, String serviceName
			, int accessCount, long transferredSize)
	{
		this.userId = userId;
		this.userOrganization = userOrganization;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.accessCount = accessCount;
		this.transferredSize = transferredSize;
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
	 * Returns userId.
	 * 
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * Sets userId.
	 * 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * Returns userOrganization.
	 * 
	 */
	public String getUserOrganization() {
		return userOrganization;
	}

	/**
	 * 
	 * Sets userOrganization.
	 * 
	 */
	public void setUserOrganization(String userOrganization) {
		this.userOrganization = userOrganization;
	}

	/**
	 * 
	 * Returns serviceId.
	 * 
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * Sets serviceId.
	 * 
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * Returns serviceName.
	 * 
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * Sets serviceName.
	 * 
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * Returns access count.
	 * @return Number of times accessed
	 * 
	 */
	public int getAccessCount() {
		return accessCount;
	}

	/**
	 * 
	 * Sets access count.
	 * @param accessCount Access count
	 * 
	 */
	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	/**
	 * 
	 * Returns data transfer size.
	 * @return Size of data transfer
	 * 
	 */
	public long getTransferredSize() {
		return transferredSize;
	}

	/**
	 * 
	 * Sets data transfer size.
	 * @param transferredSize Size of data transfer
	 * 
	 */
	public void setTransferredSize(long transferredSize) {
		this.transferredSize = transferredSize;
	}

	private String userId;
	private String userOrganization;
	private String serviceId;
	private String serviceName;
	private int accessCount;
	private long transferredSize;

	private static final long serialVersionUID = 1518084083305571921L;
}
