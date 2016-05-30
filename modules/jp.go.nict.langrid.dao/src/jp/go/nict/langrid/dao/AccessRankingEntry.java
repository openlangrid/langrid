/* $Id:AccessLogDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 388 $
 */
public class AccessRankingEntry {
	/**
	 * 
	 * 
	 */
	public AccessRankingEntry(
			String userGridId, String userId, String userOrganization
			, String serviceGridId, String serviceId, String serviceName
			, int accessCount, long requestBytes, long responseBytes, long responseMillis) {
		this.userGridId = userGridId;
		this.userId = userId;
		this.userOrganization = userOrganization;
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.accessCount = accessCount;
		this.requestBytes = requestBytes;
		this.responseBytes = responseBytes;
		this.responseMillis = responseMillis;
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
	public String getUserOrganization() {
		return userOrganization;
	}

	/**
	 * 
	 * 
	 */
	public void setUserOrganization(String userOrganization) {
		this.userOrganization = userOrganization;
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
	public int getAccessCount() {
		return accessCount;
	}

	/**
	 * 
	 * 
	 */
	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	public long getRequestBytes() {
		return requestBytes;
	}

	public void setRequestBytes(long requestBytes) {
		this.requestBytes = requestBytes;
	}
	
	public long getResponseBytes() {
		return responseBytes;
	}
	
	public void setResponseBytes(long responseBytes) {
		this.responseBytes = responseBytes;
	}
	
	public long getResponseMillis() {
		return responseMillis;
	}
	
	public void setResponseMillis(long responseMillis) {
		this.responseMillis = responseMillis;
	}

	private String userGridId;
	private String userId;
	private String userOrganization;
	private String serviceGridId;
	private String serviceId;
	private String serviceName;
	private int accessCount;
	private long requestBytes;
	private long responseBytes;
	private long responseMillis;
}
