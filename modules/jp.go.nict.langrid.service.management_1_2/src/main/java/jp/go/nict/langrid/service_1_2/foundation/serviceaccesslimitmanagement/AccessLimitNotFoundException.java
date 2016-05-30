/*
 * $Id:AccessConstraint.java 4090 2007-02-02 05:55:39Z nakaguchi $
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

import jp.go.nict.langrid.service_1_2.LangridException;

/**
 * 
 * Exception thrown when the specified access restriction does not exist.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class AccessLimitNotFoundException extends LangridException{
	/**
	 * 
	 * Constructor.
	 * @param userId User ID
	 * @param serviceId Service ID
	 * @param period Units of period
	 * @param limitType Restriction type
	 * 
	 */
	public AccessLimitNotFoundException(String userId, String serviceId,
			String period, String limitType){
		super("access limit for userId: "
				+ userId
				+ ",serviceId:" + serviceId
				+ ",period:" + period
				+ ",limitType:" + limitType
				+ " is not found.");
		this.userId = userId;
		this.serviceId = serviceId;
		this.period = period;
		this.limitType = limitType;
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
	 * Sets units of period.
	 * @return Units of period
	 * 
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * 
	 * Sets units of period.
	 * @param period Units of period
	 * 
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * 
	 * Gets ristriction type.
	 * @return restriction type
	 * 
	 */
	public String getLimitType() {
		return limitType;
	}

	/**
	 * 
	 * Sets ristriction type.
	 * @param limitType restriction type.
	 * 
	 */
	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	private String userId;
	private String serviceId;
	private String period;
	private String limitType;

	private static final long serialVersionUID = -5769677783656790374L;
}
