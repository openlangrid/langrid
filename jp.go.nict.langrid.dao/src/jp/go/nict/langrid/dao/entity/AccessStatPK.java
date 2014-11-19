/*
 * $Id:HibernateAccessRightDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 388 $
 */
@Embeddable
public class AccessStatPK implements Serializable{
	/**
	 * 
	 * 
	 */
	public AccessStatPK(){
		recalcHashCode();
	}

	/**
	 * 
	 * 
	 */
	public AccessStatPK(
			String userGridId, String userId
			, String serviceAndNodeGridId, String serviceId, String nodeId
			, Calendar baseDateTime, Period period) {
		this.userGridId = userGridId;
		this.userId = userId;
		this.serviceAndNodeGridId = serviceAndNodeGridId;
		this.serviceId = serviceId;
		this.nodeId = nodeId;
		this.baseDateTime = baseDateTime;
		this.period = period;
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
	public String getUserGridId() {
		return userGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setUserGridId(String userGridId) {
		this.userGridId = userGridId;
		recalcHashCode();
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
		recalcHashCode();
	}

	/**
	 * 
	 * 
	 */
	public String getServiceAndNodeGridId() {
		return serviceAndNodeGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceAndNodeGridId(String serviceAndNodeGridId) {
		this.serviceAndNodeGridId = serviceAndNodeGridId;
		recalcHashCode();
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
		recalcHashCode();
	}

	/**
	 * 
	 * 
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
		recalcHashCode();
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
		recalcHashCode();
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
		recalcHashCode();
	}

	private void recalcHashCode(){
		hashCode = new HashCodeBuilder()
			.append(userGridId)
			.append(userId)
			.append(serviceAndNodeGridId)
			.append(serviceId)
			.append(nodeId)
			.append(baseDateTime)
			.append(period)
			.hashCode();
	}

	private String userGridId;
	private String userId;
	private String serviceAndNodeGridId;
	private String serviceId;
	private String nodeId;
	private Calendar baseDateTime;
	private Period period;
	private transient int hashCode; 

	private static final long serialVersionUID = -8861537416682432484L;
}
