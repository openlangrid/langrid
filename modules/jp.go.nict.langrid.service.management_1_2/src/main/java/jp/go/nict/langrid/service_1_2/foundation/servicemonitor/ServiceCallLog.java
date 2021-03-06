/*
 * $Id:AccessLog.java 4384 2007-04-03 08:56:48Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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

import java.util.Calendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class ServiceCallLog extends AccessLog{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceCallLog(){}

	/**
	 * 
	 * 
	 */
	public ServiceCallLog(String address, String host
			, Calendar dateTime, String requestUri
			, int responseCode, int responseBytes
			, String referer, String agent
			, String faultCode, String faultString
			, String userId, String serviceId, String nodeId
			, int callNest, String callTree)
	{
		super(address, host, dateTime, requestUri, responseCode
				, responseBytes, referer, agent, faultCode, faultString
				, userId, serviceId, nodeId
				);
		this.callNest = callNest;
		this.callTree = callTree;
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
	public int getCallNest() {
		return callNest;
	}

	/**
	 * 
	 * 
	 */
	public void setCallNest(int callNest) {
		this.callNest = callNest;
	}

	/**
	 * 
	 * 
	 */
	public String getCallTree() {
		return callTree;
	}

	/**
	 * 
	 * 
	 */
	public void setCallTree(String callTree) {
		this.callTree = callTree;
	}

	private int callNest;
	private String callTree;

	private static final long serialVersionUID = -1691561912977085779L;
}
