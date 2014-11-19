/*
 * $Id: AccessLimit.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores access restriction data.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class AccessLimit
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AccessLimit(){
	}

	/**
	 * 
	 * Constructor.
	 * @param userId User ID
	 * @param serviceId Service ID
	 * @param period Units of period
	 * @param limitType Restriction value type
	 * @param limitCount Restriction value
	 * 
	 */
	public AccessLimit(String userId, String serviceId,
			String period, String limitType, int limitCount) {
		this.userId = userId;
		this.serviceId = serviceId;
		this.period = period;
		this.limitType = limitType;
		this.limitCount = limitCount;
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
	 * Returns the type of period.
	 * @return Type of period
	 * 
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * 
	 * Sets the type of period.
	 * @param period Type of period
	 * 
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * 
	 * Returns limitType.
	 * 
	 */
	public String getLimitType() {
		return limitType;
	}

	/**
	 * 
	 * Sets limitType.
	 * 
	 */
	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	/**
	 * 
	 * Returns restriction value.
	 * @return Restriction value
	 * 
	 */
	public int getLimitCount() {
		return limitCount;
	}

	/**
	 * 
	 * Sets restriction value.
	 * @param limitCount Restriction value
	 * 
	 */
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	private String userId;
	private String serviceId;
	private String period;
	private String limitType;
	private int limitCount;

	private static final long serialVersionUID = -7173871252065794785L;
}
