/*
 * $Id:AccessLog.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
public class AccessLog
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public AccessLog(){}

	/**
	 * 
	 * 
	 */
	public AccessLog(String address, String host, Calendar dateTime
			, String requestUri, int requestBytes
			, int responseCode, int responseBytes
			, String referer, String agent
			, long responseMillis, String protocolId
			, String faultCode, String faultString
			, String userGridId, String userId
			, String serviceAndNodeGridId
			, String serviceId, String nodeId
			, int callNest, String callTree
			)
	{
		this.address = address;
		this.host = host;
		this.dateTime = dateTime;
		this.requestUri = requestUri;
		this.requestBytes = requestBytes;
		this.responseCode = responseCode;
		this.responseBytes = responseBytes;
		this.referer = referer;
		this.agent = agent;
		this.responseMillis = responseMillis;
		this.protocolId = protocolId;
		this.faultCode = faultCode;
		this.faultString = faultString;
		this.userGridId = userGridId;
		this.userId = userId;
		this.serviceAndNodeGridId = serviceAndNodeGridId;
		this.serviceId = serviceId;
		this.nodeId = nodeId;
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
	public int getId(){
		return id;
	}

	/**
	 * 
	 * 
	 */
	public void setId(int id){
		this.id = id;
	}

	/**
	 * 
	 * 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * 
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * 
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * 
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getDateTime() {
		return dateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * 
	 * 
	 */
	public String getRequestUri() {
		return requestUri;
	}

	/**
	 * 
	 * 
	 */
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	/**
	 * 
	 * 
	 */
	public int getRequestBytes() {
		return requestBytes;
	}

	/**
	 * 
	 * 
	 */
	public void setRequestBytes(int requestBytes) {
		this.requestBytes = requestBytes;
	}

	/**
	 * 
	 * 
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * 
	 * 
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * 
	 * 
	 */
	public int getResponseBytes() {
		return responseBytes;
	}

	/**
	 * 
	 * 
	 */
	public void setResponseBytes(int responseBytes) {
		this.responseBytes = responseBytes;
	}

	/**
	 * 
	 * 
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * 
	 * 
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	/**
	 * 
	 * 
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * 
	 * 
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * 
	 * @return the responseMillis
	 * 
	 */
	public long getResponseMillis() {
		return responseMillis;
	}

	/**
	 * 
	 * @param responseMillis the responseMillis to set
	 * 
	 */
	public void setResponseMillis(long responseMillis) {
		this.responseMillis = responseMillis;
	}

	/**
	 * 
	 * 
	 */
	public String getProtocolId() {
		return protocolId;
	}

	/**
	 * 
	 * 
	 */
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}

	/**
	 * 
	 * 
	 */
	public String getFaultCode(){
		return faultCode;
	}

	/**
	 * 
	 * 
	 */
	public void setFaultCode(String faultCode){
		this.faultCode = faultCode;
	}

	/**
	 * 
	 * 
	 */
	public String getFaultString(){
		return faultString;
	}

	/**
	 * 
	 * 
	 */
	public void setFaultString(String faultString){
		this.faultString = faultString;
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
	public int getNodeLocalId() {
		return nodeLocalId;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeLocalId(int nodeLocalId) {
		this.nodeLocalId = nodeLocalId;
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

	@Id
	private int id;
	private String address;
	private String host;
	private Calendar dateTime;
	private String requestUri;
	private int requestBytes;
	private int responseCode;
	private int responseBytes;
	private String referer;
	private String agent;
	private long responseMillis;
	private String protocolId;
	private String faultCode;
	@Type(type="text")
	private String faultString;
	private String userGridId;
	private String userId;
	private String serviceAndNodeGridId;
	private String serviceId;
	private String nodeId;
	private int nodeLocalId;
	private int callNest;
	@Type(type="text")
	private String callTree;

	private static final long serialVersionUID = -5130713163805744837L;
}
