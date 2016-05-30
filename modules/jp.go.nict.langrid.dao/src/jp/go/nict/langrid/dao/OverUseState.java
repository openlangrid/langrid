/*
 * $Id:AccessConstraintSearchResult.java 4090 2007-02-02 05:55:39Z nakaguchi $
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

import java.io.Serializable;
import java.util.Calendar;

import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public class OverUseState
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public OverUseState(){
	}

	/**
	 * 
	 * 
	 */
	public OverUseState(
			String userGridId, String userId
			, String serviceGridId, String serviceId
			, Calendar baseDateTime, Period period
			, LimitType limitType, int limitCount
			, long currentCount, Calendar lastAccessDateTime
			) {
		this.userGridId = userGridId;
		this.userId = userId;
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
		this.baseDateTime = baseDateTime;
		this.period = period;
		this.limitType = limitType;
		this.limitCount = limitCount;
		this.currentCount = currentCount;
		this.lastAccessDateTime = lastAccessDateTime;
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
	public Calendar getBaseDateTime() {
		return baseDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setBaseDateTime(Calendar baseDateTime) {
		this.baseDateTime = baseDateTime;
	}

	/**
	 * 
	 * 
	 */
	public Period getPeriod() {
		return period;
	}

	/**
	 * 
	 * 
	 */
	public void setPeriod(Period period) {
		this.period = period;
	}

	/**
	 * 
	 * 
	 */
	public LimitType getLimitType() {
		return limitType;
	}

	/**
	 * 
	 * 
	 */
	public void setLimitType(LimitType limitType) {
		this.limitType = limitType;
	}

	/**
	 * 
	 * 
	 */
	public int getLimitCount() {
		return limitCount;
	}

	/**
	 * 
	 * 
	 */
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	/**
	 * 
	 * 
	 */
	public long getCurrentCount() {
		return currentCount;
	}

	/**
	 * 
	 * 
	 */
	public void setCurrentCount(long currentCount) {
		this.currentCount = currentCount;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getLastAccessDateTime() {
		return lastAccessDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setLastAccessDateTime(Calendar lastAccessDateTime) {
		this.lastAccessDateTime = lastAccessDateTime;
	}

	private String userGridId;
	private String userId;
	private String serviceGridId;
	private String serviceId;
	private Calendar baseDateTime;
	private Period period;
	private LimitType limitType;
	private int limitCount;
	private long currentCount;
	private Calendar lastAccessDateTime;

	private static final long serialVersionUID = 968603532507050041L;
}
