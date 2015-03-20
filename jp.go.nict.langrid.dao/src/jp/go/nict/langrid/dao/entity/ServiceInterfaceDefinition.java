/*
 * $Id: ServiceInterfaceDefinition.java 207 2010-10-02 14:10:53Z t-nakaguchi $
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
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
public class ServiceInterfaceDefinition
implements Serializable{
	/**
	 * 
	 * 
	 */
	public ServiceInterfaceDefinition(){
	}

	/**
	 * 
	 * 
	 */
	public ServiceInterfaceDefinition(
			ServiceType serviceType, String protocolId
			) {
		this.serviceType = serviceType;
		this.protocolId = protocolId;
	}

	/**
	 * 
	 * 
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
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
	public Blob getDefinition() {
		return definition;
	}

	/**
	 * 
	 * 
	 */
	public void setDefinition(Blob value) {
		this.definition = value;
	}

	@Id
	@GeneratedValue
	int id;

	@ManyToOne
	private ServiceType serviceType;
	private String protocolId;
	private Blob definition;

	private static final long serialVersionUID = 7324345885136052790L;
}
