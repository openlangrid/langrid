/*
 * $Id:ServiceDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 388 $
 */
@Entity
@IdClass(ResourcePK.class)
public class Resource
extends UpdateManagedEntity
implements AttributedElement<ResourceAttribute>, Serializable{
	/**
	 * 
	 * 
	 */
	public Resource(){
	}

	/**
	 * 
	 * 
	 */
	public Resource(String gridId, String resourceId){
		this.gridId = gridId;
		this.resourceId = resourceId;
	}

	/**
	 * 
	 * 
	 */
	public Resource(String gridId, String resourceId, String resourceName
			, String resourceTypeDomainId, String resourceTypeId)
	{
		this.gridId = gridId;
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceTypeDomainId = resourceTypeDomainId;
		this.resourceTypeId = resourceTypeId;
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

	public ResourceAttribute getAttribute(String name){
		return attributes.get(name);
	}

	public Collection<ResourceAttribute> getAttributes(){
		return attributes.values();
	}

	public void setAttributes(Collection<ResourceAttribute> attributes){
		this.attributes.clear();
		for(ResourceAttribute a : attributes){
			this.attributes.put(a.getName(), a);
		}
	}

	public String getAttributeValue(String attributeName){
		Attribute a = attributes.get(attributeName);
		if(a == null) return null;
		return a.getValue();
	}

	/**
	 * 
	 * 
	 */
	public String getCopyrightInfo() {
		return copyrightInfo;
	}

	/**
	 * 
	 * 
	 */
	public String getCpuInfo() {
		return cpuInfo;
	}

	/**
	 * 
	 * 
	 */
	public String getLicenseInfo() {
		return licenseInfo;
	}

	/**
	 * 
	 * 
	 */
	public String getMemoryInfo() {
		return memoryInfo;
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
	public String getResourceDescription() {
		return resourceDescription;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * 
	 * 
	 */
	public String getSpecialNoteInfo() {
		return specialNoteInfo;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceTypeDomainId() {
		return resourceTypeDomainId;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceTypeDomainId(String resourceTypeDomainId) {
		this.resourceTypeDomainId = resourceTypeDomainId;
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

	public String getOsInfo() {
		return osInfo;
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
	public boolean isVisible(){
		return visible;
	}

	public void removeAttribute(String attributeName){
		if(attributes.containsKey(attributeName)){
			attributes.remove(attributeName);
		}
	}

	/**
	 * 
	 * 
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public void setAttribute(ResourceAttribute attribute){
		attributes.put(attribute.getName(), attribute);
	}

	public void setAttributeValue(String attributeName, String attributeValue){
		ResourceAttribute a = getAttribute(attributeName);
		if(a == null){
			attributes.put(attributeName
					, new ResourceAttribute(gridId, resourceId
							, attributeName, attributeValue));
		} else{
			a.setValue(attributeValue);
			a.touchUpdatedDateTime();
		}
	}

	/**
	 * 
	 * 
	 */
	public void setCopyrightInfo(String copyrightInfo) {
		this.copyrightInfo = copyrightInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setLicenseInfo(String licenseInfo) {
		this.licenseInfo = licenseInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setMemoryInfo(String memoryInfo) {
		this.memoryInfo = memoryInfo;
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
	public void setResourceDescription(String serviceDescription) {
		this.resourceDescription = serviceDescription;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceId(String serviceId){
		this.resourceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceName(String serviceName){
		this.resourceName = serviceName;
	}

	/**
	 * 
	 * 
	 */
	public void setSpecialNoteInfo(String specialNoteInfo) {
		this.specialNoteInfo = specialNoteInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setVisible(boolean visible){
		this.visible = visible;
	}

	public void setOsInfo(String osInfo) {
		this.osInfo = osInfo;
	}

	/**
	 * 
	 * 
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * 
	 * 
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	protected EqualsBuilder appendSpecialEquals(EqualsBuilder builder,
			Object value, Collection<String> appendedFields, boolean ignoreDates) {
		Resource s = (Resource)value;
		EqualsBuilder b = super.appendSpecialEquals(
				builder, value, appendedFields, ignoreDates);
		appendedFields.add("attributes");
		if(!ignoreDates){
			EqualsBuilderUtil.appendAsSet(
					b, getAttributes(), s.getAttributes()
					);
			return b;
		}
		try{
			EqualsBuilderUtil.appendAsSet(
					b, getAttributes(), s.getAttributes()
					, ResourceAttribute.class, "equalsIgnoreDates"
					);
			return b;
		} catch(IllegalAccessException e){
			throw new RuntimeException(e);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e);
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	protected HashCodeBuilder appendSpecialHashCode(HashCodeBuilder builder,
			Collection<String> appendedFields) {
		HashCodeBuilder b = super.appendSpecialHashCode(builder, appendedFields);
		appendedFields.add("attributes");
		return b.append(attributes.entrySet());
	}

	@Id
	private String gridId;
	@Id
	private String resourceId;

	private String resourceTypeDomainId;
	private String resourceTypeId;

	private String resourceName;
	@Type(type="text")
	private String resourceDescription;
	@Type(type="text")
	private String specialNoteInfo;
	@Type(type="text")
	private String copyrightInfo;
	@Type(type="text")
	private String licenseInfo;
	private String cpuInfo;
	private String memoryInfo;
	private String osInfo;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="resourceId")
	@JoinColumns({
		@JoinColumn(name="gridId")
		, @JoinColumn(name="resourceId")
	})
	@MapKey(name="name")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Map<String, ResourceAttribute> attributes = new HashMap<String, ResourceAttribute>();

	private String ownerUserId;

	private boolean active;
	private boolean approved = false;
	private boolean visible = true;

	private static final long serialVersionUID = -6358768494321883034L;
}
