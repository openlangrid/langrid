/*
 * $Id: AccessLog.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
import java.util.Calendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Access log information.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class AccessLog
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AccessLog(){}

	/**
	 * 
	 * Constructor.
	 * @param address Source address of the access
	 * @param host Hostname
	 * @param dateTime Date and time of access
	 * @param requestUri Destination URI
	 * @param responseCode Response code
	 * @param responseBytes Response size (Bytes)
	 * @param referer HTTP referer
	 * @param agent HTTP Agent
	 * @param faultCode SOAP failure code
	 * @param faultString SOAP failure string
	 * @param userId User ID
	 * @param serviceId Service ID
	 * @param nodeId Node ID(unused)
	 * 
	 */
	public AccessLog(String address, String host
			, Calendar dateTime, String requestUri
			, int responseCode, int responseBytes
			, String referer, String agent
			, String faultCode, String faultString
			, String userId, String serviceId, String nodeId)
	{
		this.address = address;
		this.host = host;
		this.dateTime = dateTime;
		this.requestUri = requestUri;
		this.responseCode = responseCode;
		this.responseBytes = responseBytes;
		this.referer = referer;
		this.agent = agent;
		this.faultCode = faultCode;
		this.faultString = faultString;
		this.userId = userId;
		this.serviceId = serviceId;
		this.nodeId = nodeId;
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
	 * Gets access source address.
	 * @return Access source address
	 * 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * Sets access source address.
	 * @param address Source address of the access
	 * 
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * Gets access source host.
	 * @return Access source hostname
	 * 
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * Sets access source host.
	 * @param host Hostname
	 * 
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * Gets date and time of access.
	 * @return Date and time of access
	 * 
	 */
	public Calendar getDateTime() {
		return dateTime;
	}

	/**
	 * 
	 * Sets date and time of access.
	 * @param dateTime Date and time of access
	 * 
	 */
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * 
	 * Gets access destination URI.
	 * @return Access destination URI
	 * 
	 */
	public String getRequestUri() {
		return requestUri;
	}

	/**
	 * 
	 * Sets access destination URI.
	 * @param requestUri Destination URI
	 * 
	 */
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	/**
	 * 
	 * Gets response code.
	 * @return Response code
	 * 
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * 
	 * Sets response code.
	 * @param responseCode Response code
	 * 
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * 
	 * Gets response size.
	 * @return Response size
	 * 
	 */
	public int getResponseBytes() {
		return responseBytes;
	}

	/**
	 * 
	 * Sets response size.
	 * @param responseBytes Response size (Bytes)
	 * 
	 */
	public void setResponseBytes(int responseBytes) {
		this.responseBytes = responseBytes;
	}

	/**
	 * 
	 * Gets HTTP referer.
	 * @return HTTP Referer
	 * 
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * 
	 * Sets HTTP referer.
	 * @param referer HTTP referer
	 * 
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	/**
	 * 
	 * Gets HTTP agent.
	 * @return HTTP Agent
	 * 
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * 
	 * Sets HTTP agent.
	 * @param agent HTTP Agent
	 * 
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * 
	 * Gets SOAP failure code.
	 * @return SOAP Failure code
	 * 
	 */
	public String getFaultCode(){
		return faultCode;
	}

	/**
	 * 
	 * Sets SOAP failure code.
	 * @param faultCode SOAP failure code
	 * 
	 */
	public void setFaultCode(String faultCode){
		this.faultCode = faultCode;
	}

	/**
	 * 
	 * Gets SOAP failure string.
	 * @return SOAP Failure string
	 * 
	 */
	public String getFaultString(){
		return faultString;
	}

	/**
	 * 
	 * Sets SOAP failure string.
	 * @param faultString SOAP failure string
	 * 
	 */
	public void setFaultString(String faultString){
		this.faultString = faultString;
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
	 * Gets user ID.
	 * @return User ID
	 * 
	 */
	public String getUserId() {
		return userId;
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
	 * Sets node ID.
	 * @param nodeId Node ID
	 * 
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 
	 * Gets node ID.
	 * @return Node ID
	 * 
	 */
	public String getNodeId() {
		return nodeId;
	}

	private String address;
	private String host;
	private Calendar dateTime;
	private String requestUri;
	private int responseCode;
	private int responseBytes;
	private String referer;
	private String agent;
	private String faultCode;
	private String faultString;
	private String userId;
	private String serviceId;
	private String nodeId;

	private static final long serialVersionUID = -7316630513618099744L;
}
