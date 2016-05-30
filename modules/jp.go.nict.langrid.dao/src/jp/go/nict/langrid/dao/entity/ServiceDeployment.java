/*
 * $Id:Endpoint.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 207 $
 */
@Entity
@IdClass(ServiceDeploymentPK.class)
public class ServiceDeployment
extends UpdateManagedEntity
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public ServiceDeployment(){
	}

	/**
	 * 
	 * 
	 */
	public ServiceDeployment(String gridId, String serviceId, String nodeId,
			String servicePath, boolean enabled) {
		this.gridId = gridId;
		this.serviceId = serviceId;
		this.nodeId = nodeId;
		this.servicePath = servicePath;
		this.enabled = enabled;
	}

	/**
	 * 
	 * 
	 */
	public String getGridId() {
		return gridId;
	}

	/**
	 * 
	 * 
	 */
	public void setGridId(String gridId) {
		this.gridId = gridId;
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
	public String getServicePath() {
		return servicePath;
	}

	/**
	 * 
	 * 
	 */
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getDeployedDateTime() {
		return deployedDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setDeployedDateTime(Calendar deployedDateTime) {
		this.deployedDateTime = deployedDateTime;
	}

	/**
	 * 
	 * 
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 
	 * 
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getDisabledByErrorDate(){
		if(disabledByErrorDate == null) return null;
		else return (Calendar)disabledByErrorDate.clone();
	}

	/**
	 * 
	 * 
	 */
	public void setDisabledByErrorDate(Calendar date){
		disabledByErrorDate = date;
	}

	@Id
	private String gridId;
	@Id
	private String serviceId;
	@Id
	private String nodeId;
	private String servicePath;
	private Calendar deployedDateTime;
	private boolean enabled;
	private Calendar disabledByErrorDate;

	private static final long serialVersionUID = 7504702979480916007L;
}
