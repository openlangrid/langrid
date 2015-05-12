/*
 * $Id: NodeEntry.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.nodemanagement;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class NodeEntry
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public NodeEntry(){
	}

	/**
	 * 
	 * 
	 */
	public NodeEntry(String nodeId, String nodeName, String nodeType
			, String url, String ownerUserId, String ownerUserOrganization
			, boolean active, Calendar registeredDate, Calendar updatedDate) {
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		this.nodeType = nodeType;
		this.url = url;
		this.ownerUserId = ownerUserId;
		this.ownerUserOrganization = ownerUserOrganization;
		this.active = active;
		this.registeredDate = registeredDate;
		this.updatedDate = updatedDate;
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
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 
	 * 
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * 
	 * 
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * 
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserId() {
		return ownerUserId;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserOrganization() {
		return ownerUserOrganization;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerUserOrganization(String ownerUserOrganization) {
		this.ownerUserOrganization = ownerUserOrganization;
	}

	/**
	 * 
	 * 
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * 
	 * 
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getRegisteredDate() {
		return registeredDate;
	}

	/**
	 * 
	 * 
	 */
	public void setRegisteredDate(Calendar registeredDate) {
		this.registeredDate = registeredDate;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * 
	 * 
	 */
	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}

	private String nodeId;
	private String nodeName;
	private String nodeType;
	private String url;
	private String ownerUserId;
	private String ownerUserOrganization;
	private boolean active;
	private Calendar registeredDate;
	private Calendar updatedDate;

	private static final long serialVersionUID = -3243375664563934021L;
}
