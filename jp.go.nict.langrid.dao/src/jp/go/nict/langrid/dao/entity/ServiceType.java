/*
 * $Id: ServiceType.java 207 2010-10-02 14:10:53Z t-nakaguchi $
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
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity(name="ServiceType")
@IdClass(ServiceTypePK.class)
public class ServiceType
extends UpdateManagedEntity
implements Serializable{
	/**
	 * 
	 * 
	 */
	public ServiceType(){
	}

	/**
	 * 
	 * 
	 */
	public ServiceType(
			String domainId, String serviceTypeID
			) {
		this.domainId = domainId;
		this.serviceTypeId = serviceTypeID;
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
	public String getServiceTypeId() {
		return serviceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceTypeName() {
		return serviceTypeName;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
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
	public Map<String, ServiceMetaAttribute> getMetaAttributes() {
		return metaAttributes;
	}

	/**
	 * 
	 * 
	 */
	public void setMetaAttributes(Map<String, ServiceMetaAttribute> metaAttributes) {
		this.metaAttributes.clear();
		this.metaAttributes.putAll(metaAttributes);
	}

	/**
	 * 
	 * 
	 */
	public void setMetaAttributeCollection(Collection<ServiceMetaAttribute> metaAttributes) {
		this.metaAttributes.clear();
		for(ServiceMetaAttribute a : metaAttributes){
			this.metaAttributes.put(a.getAttributeId(), a);
		}
	}

	/**
	 * 
	 * 
	 */
	public Map<String, ServiceInterfaceDefinition> getInterfaceDefinitions(){
		return interfaceDefinitions;
	}

	/**
	 * 
	 * 
	 */
	public void setInterfaceDefinitions(Map<String, ServiceInterfaceDefinition> interfaceDefinitions){
		this.interfaceDefinitions.clear();
		this.interfaceDefinitions.putAll(interfaceDefinitions);
	}

	/**
	 * 
	 * 
	 */
	public void setInterfaceDefinitionCollection(Collection<ServiceInterfaceDefinition> interfaceDefinitions) {
		this.interfaceDefinitions.clear();
		for(ServiceInterfaceDefinition i : interfaceDefinitions){
			this.interfaceDefinitions.put(i.getProtocolId(), i);
		}
	}

	@Override
	protected EqualsBuilder appendSpecialEquals(EqualsBuilder builder,
			Object value, Collection<String> appendedFields, boolean ignoreDates) {
		ServiceType s = (ServiceType)value;
		EqualsBuilder b = super.appendSpecialEquals(
				builder, value, appendedFields, ignoreDates);
		appendedFields.add("interfaceDefinitions");
		if(!ignoreDates){
			EqualsBuilderUtil.appendAsSet(
					b, interfaceDefinitions.values(), s.interfaceDefinitions.values()
					);
			return b;
		}
		try{
			EqualsBuilderUtil.appendAsSet(
					b, interfaceDefinitions.values(), s.interfaceDefinitions.values()
					, ServiceInterfaceDefinition.class, "equalsIgnoreDates"
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
		appendedFields.add("interfaceDefinitions");
		return b
			.append(interfaceDefinitions.entrySet())
			;
	}

	@Id
	private String domainId;
	@Id
	private String serviceTypeId;
	private String serviceTypeName;
	@Type(type="text")
	private String description;

	@ManyToMany
	@MapKey(name="attributeId")
	@JoinColumns({
		@JoinColumn(name="domainId")
		, @JoinColumn(name="attributeId")
	})
	private Map<String, ServiceMetaAttribute> metaAttributes
			= new HashMap<String, ServiceMetaAttribute>();

	@OneToMany(cascade=CascadeType.ALL, mappedBy="serviceType")
	@MapKey(name="protocolId")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Map<String, ServiceInterfaceDefinition> interfaceDefinitions
			= new HashMap<String, ServiceInterfaceDefinition>();

	private static final long serialVersionUID = 5088891377062146038L;
}
