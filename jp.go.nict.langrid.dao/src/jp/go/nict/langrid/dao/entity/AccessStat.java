/* $Id:AccessLogDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@NamedQueries({
	@NamedQuery(
			name="AccessStat.incrementQuery"
			, query="update AccessStat set " +
			" accesscount = accesscount + 1" +
			" , transferredsize = transferredsize + :size " +
			"where userId=:userId " +
			" and serviceId=:serviceId" +
			" and nodeId=:nodeId" +
			" and (" +
			"   (period=:periodDay and baseDateTime=:dateTimeDay)" +
			"   or" +
			"   (period=:periodMonth and baseDateTime=:dateTimeMonth)" +
			"   or" +
			"   (period=:periodYear and baseDateTime=:dateTimeYear)" +
			"   )"
	)
})
@IdClass(AccessStatPK.class)
public class AccessStat
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public AccessStat(){
	}

	/**
	 * 
	 * 
	 */
	public AccessStat(
			String userGridId, String userId
			, String serviceAndNodeGridId, String serviceId, String nodeId
			, Calendar baseDateTime, Period period
			, int accessCount, long requestBytes, long responseBytes, long responseMillis) {
		this.userGridId = userGridId;
		this.userId = userId;
		this.serviceAndNodeGridId = serviceAndNodeGridId;
		this.serviceId = serviceId;
		this.nodeId = nodeId;
		this.baseDateTime = baseDateTime;
		this.period = period;
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
	public String getServiceAndNodeGridId() {
		return serviceAndNodeGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceAndNodeGridId(String serviceAndNodeGridId) {
		this.serviceAndNodeGridId = serviceAndNodeGridId;
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
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
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

	/**
	 * 
	 * 
	 */
	public long getRequestBytes() {
		return requestBytes;
	}

	/**
	 * 
	 * 
	 */
	public void setRequestBytes(long requestBytes) {
		this.requestBytes = requestBytes;
	}

	/**
	 * 
	 * 
	 */
	public long getResponseBytes() {
		return responseBytes;
	}

	/**
	 * 
	 * 
	 */
	public void setResponseBytes(long responseBytes) {
		this.responseBytes = responseBytes;
	}

	/**
	 * 
	 * 
	 */
	public long getResponseMillis() {
		return responseMillis;
	}

	/**
	 * 
	 * 
	 */
	public void setResponseMillis(long responseMillis) {
		this.responseMillis = responseMillis;
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

	@Id
	private String userGridId;
	@Id
	private String userId;
	@Id
	private String serviceAndNodeGridId;
	@Id
	private String serviceId;
	@Id
	private String nodeId;
	@Id
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar baseDateTime;
	@Id
	private Period period;
	private int accessCount;
	private long requestBytes;
	private long responseBytes;
	private long responseMillis;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar lastAccessDateTime;

	private static final long serialVersionUID = 1038829527212625585L;
}
