/*
 * $Id: Domain.java 204 2010-10-02 13:16:26Z t-nakaguchi $
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

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
public class Domain
extends UpdateManagedEntity
implements Serializable{
	/**
	 * 
	 * 
	 */
	public Domain(){
	}

	/**
	 * 
	 * 
	 */
	public Domain(
			String domainId
			) {
		this.domainId = domainId;
	}

	/**
	 * 
	 * 
	 */
	public String getDomainId() {
		return domainId;
	}

	/**
	 * 
	 * 
	 */
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	/**
	 * 
	 * 
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * 
	 * 
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * 
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * 
	 */
	public String getOwnerUserGridId() {
		return ownerUserGridId;
	}

	/**
	 * 
	 * 
	 */
	public void setOwnerUserGridId(String ownerUserGridId) {
		this.ownerUserGridId = ownerUserGridId;
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

	@Id
	private String domainId;
	private String domainName;
	private String description;
	private String ownerUserGridId;
	private String ownerUserId;

	private static final long serialVersionUID = -2284656452601124978L;
}
