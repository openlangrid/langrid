/*
 * $Id: AccessLimitNotFoundException.java 214 2010-10-02 14:32:38Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public class AccessLimitNotFoundException extends DaoException {
	/**
	 * 
	 * @param userId User ID
	 * @param serviceId Service ID
	 * @param period Units of period
	 * @param limitType Restriction type
	 * 
	 */
	public AccessLimitNotFoundException(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			, Period period, LimitType limitType){
		super("AccessLimit(userGridId: " + userGridId
				+ ",userId: " + userId
				+ ",serviceGridId:" + serviceGridId
				+ ",serviceId:" + serviceId
				+ ",period:" + period
				+ ",limitType:" + limitType
				+ ") not found.");
		this.userGridId = userGridId;
		this.userId = userId;
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
		this.period = period;
		this.limitType = limitType;
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
	public Period getPeriod() {
		return period;
	}

	/**
	 * 
	 * Sets units of period.
	 * @param period Units of period
	 * 
	 */
	public void setPeriod(Period period) {
		this.period = period;
	}

	/**
	 * 
	 * Gets ristriction type.
	 * @return restriction type
	 * 
	 */
	public LimitType getLimitType() {
		return limitType;
	}

	/**
	 * 
	 * Sets ristriction type.
	 * @param limitType restriction type.
	 * 
	 */
	public void setLimitType(LimitType limitType) {
		this.limitType = limitType;
	}

	private String userGridId;
	private String userId;
	private String serviceGridId;
	private String serviceId;
	private Period period;
	private LimitType limitType;
	private static final long serialVersionUID = 8477992092624543733L;
}
