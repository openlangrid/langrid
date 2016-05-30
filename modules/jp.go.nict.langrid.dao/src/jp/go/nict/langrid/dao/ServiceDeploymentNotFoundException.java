/*
 * $Id: ServiceDeploymentNotFoundException.java 214 2010-10-02 14:32:38Z t-nakaguchi $
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
package jp.go.nict.langrid.dao;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 214 $
 */
public class ServiceDeploymentNotFoundException extends DaoException{
	/**
	 * 
	 * 
	 */
	public ServiceDeploymentNotFoundException(
			String serviceAndNodeGridId, String serviceId, String nodeId) {
		super("gid: " + serviceAndNodeGridId + ", sid: " + serviceId + ", nid: " + nodeId);
		this.serviceAndNodeGridId = serviceAndNodeGridId;
		this.serviceId = serviceId;
		this.nodeId = nodeId;
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
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * 
	 */
	public String getNodeId(){
		return nodeId;
	}

	private String serviceAndNodeGridId;
	private String serviceId;
	private String nodeId;

	private static final long serialVersionUID = -329126290507733121L;
}
