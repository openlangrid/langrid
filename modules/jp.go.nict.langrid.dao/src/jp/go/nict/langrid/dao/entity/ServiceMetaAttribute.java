/*
 * $Id: ServiceMetaAttribute.java 207 2010-10-02 14:10:53Z t-nakaguchi $
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
import javax.persistence.IdClass;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
@IdClass(ServiceMetaAttributePK.class)
public class ServiceMetaAttribute
extends UpdateManagedEntity
implements Serializable{
	/**
	 * 
	 * 
	 */
	public ServiceMetaAttribute(){
	}

	/**
	 * 
	 * 
	 */
	public ServiceMetaAttribute(
			String domainId, String attributeId
			) {
		this.domainId = domainId;
		this.attributeId = attributeId;
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
	public String getAttributeId() {
		return attributeId;
	}

	/**
	 * 
	 * 
	 */
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * 
	 * 
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * 
	 * 
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
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

	@Id
	private String domainId;
	@Id
	private String attributeId;
	private String attributeName;
	private String description;

	private static final long serialVersionUID = -1659936715200093224L;
}
