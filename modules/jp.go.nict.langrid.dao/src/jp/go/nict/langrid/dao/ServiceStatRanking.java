/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
 * @author Takao Nakaguchi
 */
public class ServiceStatRanking{
	public ServiceStatRanking() {
	}
	public ServiceStatRanking(String gridId, String serviceId, String ownerUserId,
			long accessCount, long requstBytes, long responseBytes,
			long responseMillis, double responseMillisAve) {
		super();
		this.gridId = gridId;
		this.serviceId = serviceId;
		this.ownerUserId = ownerUserId;
		this.accessCount = accessCount;
		this.requstBytes = requstBytes;
		this.responseBytes = responseBytes;
		this.responseMillis = responseMillis;
		this.responseMillisAve = responseMillisAve;
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

	public long getAccessCount() {
		return accessCount;
	}
	public void setAccessCount(long accessCount) {
		this.accessCount = accessCount;
	}
	public String getGridId() {
		return gridId;
	}
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}
	public String getOwnerUserId() {
		return ownerUserId;
	}
	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	public long getRequstBytes() {
		return requstBytes;
	}
	public void setRequstBytes(long requstBytes) {
		this.requstBytes = requstBytes;
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
	public double getResponseMillisAve() {
		return responseMillisAve;
	}
	public void setResponseMillisAve(double responseMillisAve) {
		this.responseMillisAve = responseMillisAve;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	private String gridId;
	private String serviceId;
	private String ownerUserId;
	private long accessCount;
	private long requstBytes;
	private long responseBytes;
	private long responseMillis;
	private double responseMillisAve;
}
