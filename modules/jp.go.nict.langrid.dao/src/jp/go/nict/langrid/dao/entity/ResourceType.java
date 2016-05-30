/*
 * $Id: ResourceType.java 207 2010-10-02 14:10:53Z t-nakaguchi $
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity(name="ResourceType")
@IdClass(ResourceTypePK.class)
public class ResourceType
extends UpdateManagedEntity
implements Serializable{
	/**
	 * 
	 * 
	 */
	public ResourceType(){
	}

	/**
	 * 
	 * 
	 */
	public ResourceType(
			String domainId, String resourceTypeID
			) {
		this.domainId = domainId;
		this.resourceTypeId = resourceTypeID;
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
	public String getResourceTypeId() {
		return resourceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceTypeId(String resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceTypeName() {
		return resourceTypeName;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
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
	public Map<String, ResourceMetaAttribute> getMetaAttributes() {
		return metaAttributes;
	}

	/**
	 * 
	 * 
	 */
	public void setMetaAttributes(Map<String, ResourceMetaAttribute> metaAttributes) {
		this.metaAttributes.clear();
		this.metaAttributes.putAll(metaAttributes);
	}

	/**
	 * 
	 * 
	 */
	public void setMetaAttributeCollection(Collection<ResourceMetaAttribute> metaAttributes) {
		this.metaAttributes.clear();
		for(ResourceMetaAttribute a : metaAttributes){
			this.metaAttributes.put(a.getAttributeId(), a);
		}
	}

	@Override
	protected HashCodeBuilder appendSpecialHashCode(HashCodeBuilder builder,
			Collection<String> appendedFields) {
		HashCodeBuilder b = super.appendSpecialHashCode(builder, appendedFields);
		appendedFields.add("metaAttributes");
		if(metaAttributes != null){
			b.append(metaAttributes.entrySet());
		}
		return b;
	}

	@Id
	private String domainId;
	@Id
	private String resourceTypeId;
	private String resourceTypeName;
	private String description;

	@ManyToMany
	@MapKey(name="attributeId")
	@JoinColumns({
		@JoinColumn(name="domainId")
		, @JoinColumn(name="attributeId")
	})
	private Map<String, ResourceMetaAttribute> metaAttributes = new HashMap<String, ResourceMetaAttribute>();

	private static final long serialVersionUID = -4736672475173715976L;
}
