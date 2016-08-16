/*
 * $Id:Service.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import java.net.URL;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Type;

import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONHint;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
@IdClass(ServicePK.class)
public abstract class Service
extends UpdateManagedEntity
implements AttributedElement<ServiceAttribute>, Serializable{   
   /**
	 * 
	 * 
	 */
	public Service(){
	}

	/**
	 * 
	 * 
	 */
	public Service(String gridId, String serviceId){
		this.gridId = gridId;
		this.serviceId = serviceId;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
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
	 * 
	 */
	public void setServiceId(String serviceId){
		if(serviceId != null && !serviceId.equals(this.serviceId)){
			for(Map.Entry<String, ServiceAttribute> e : attributes.entrySet()){
				e.getValue().setServiceId(serviceId);
			}
		}
		this.serviceId = serviceId;
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
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceName(String serviceName){
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
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
	public String getCopyrightInfo() {
		return copyrightInfo;
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
	public String getLicenseInfo() {
		return licenseInfo;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceTypeDomainId() {
		return serviceTypeDomainId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceTypeDomainId(String serviceTypeDomainId){
		this.serviceTypeDomainId = serviceTypeDomainId;
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
	public void setServiceTypeId(String serviceTypeId){
		this.serviceTypeId = serviceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public InstanceType getInstanceType() {
		return instanceType;
	}

	/**
	 * 
	 * 
	 */
	public void setInstanceType(InstanceType instanceType){
		this.instanceType = instanceType;
	}

	/**
	 * 
	 * 
	 */
	public int getInstanceSize() {
		return instanceSize;
	}

	/**
	 * 
	 * 
	 */
	public void setInstanceSize(int instanceSize){
		this.instanceSize = instanceSize;
	}

	/**
	 * 
	 * 
	 */
	public Blob getInstance(){
		return instance;
	}

	/**
	 * 
	 * 
	 */
	public void setInstance(Blob instance){
		this.instance = instance;
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
	public String getOwnerUserId() {
		return ownerUserId;
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
	public boolean isActive() {
		return active;
	}

	/**
	 * 
	 * 
	 */
	public ServiceContainerType getContainerType() {
		return containerType;
	}

	/**
	 * 
	 * 
	 */
	public void setContainerType(ServiceContainerType containerType) {
		this.containerType = containerType;
	}

	/**
	 * 
	 * 
	 */
	public String getAppAuthKey() {
		return appAuthKey;
	}

	/**
	 * 
	 * 
	 */
	public void setAppAuthKey(String appAuthKey) {
		this.appAuthKey = appAuthKey;
	}

	/**
	 * 
	 * 
	 */
	public void setWsdl(Blob wsdl){
		this.wsdl = wsdl;
	}

	/**
	 * 
	 * 
	 */
	public Blob getWsdl(){
		return wsdl;
	}

	/**
	 * 
	 * 
	 */
	public List<ServiceEndpoint> getServiceEndpoints(){
		 return endpoints;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceEndpoints(List<ServiceEndpoint> endpoints){
		this.endpoints.clear();
		this.endpoints.addAll(endpoints);
	}

	/**
	 * 
	 * 
	 */
	public List<ServiceDeployment> getServiceDeployments(){
		 return deployments;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceDeployments(List<ServiceDeployment> deployments){
		this.deployments.clear();
		this.deployments.addAll(deployments);
	}

	/**
	 * 
	 * 
	 */
	public boolean isVisible(){
		return visible;
	}

	/**
	 * 
	 * 
	 */
	public void setVisible(boolean visible){
		this.visible = visible;
	}

	/**
	 * 
	 * 
	 */
	public int getTimeoutMillis() {
		return timeoutMillis;
	}

	/**
	 * 
	 * 
	 */
	public void setTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	/**
	 * 
	 * 
	 */
	public String getAlternateServiceId() {
		return alternateServiceId;
	}

	/**
	 * 
	 * 
	 */
	public void setAlternateServiceId(String alternateServiceId) {
		this.alternateServiceId = alternateServiceId;
	}

	/**
	 * 
	 * 
	 */
	public boolean isUseAlternateService() {
		return useAlternateService;
	}

	/**
	 * 
	 * 
	 */
	public void setUseAlternateService(boolean useAlternateService) {
		this.useAlternateService = useAlternateService;
	}

	/**
	 * 
	 * 
	 */
	public Set<String> getAllowedUse() {
		return allowedUse;
	}

	/**
	 * 
	 * 
	 */
	public void setAllowedUse(Set<String> uses) {
		if(this.allowedUse != null){
			this.allowedUse.clear();
		} else{
			this.allowedUse = new HashSet<String>();
		}
		this.allowedUse.addAll(uses);
	}

	/**
	 * 
	 * 
	 */
	public Set<String> getAllowedAppProvision() {
		return allowedAppProvision;
	}

	/**
	 * 
	 * 
	 */
	public void setAllowedAppProvision(Set<String> apps) {
		if(this.allowedAppProvision != null){
			this.allowedAppProvision.clear();
		} else{
			this.allowedAppProvision = new HashSet<String>();
		}
		this.allowedAppProvision.addAll(apps);
	}

	/**
	 * 
	 * 
	 */
	public boolean isFederatedUseAllowed(){
		return federatedUseAllowed;
	}
	
	/**
	 * 
	 * 
	 */
	public void setFederatedUseAllowed(boolean federatedUseAllowed){
		this.federatedUseAllowed = federatedUseAllowed;
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

	/**
	 * 
	 * 
	 */
	public Set<Invocation> getInvocations(){
		return invocations;
	}

	/**
	 * 
	 * 
	 */
	public void setInvocations(Set<Invocation> invocations){
		this.invocations.clear();
		this.invocations.addAll(invocations);
	}

	public Collection<ServiceAttribute> getAttributes(){
		return attributes.values();
	}

	public void setAttributes(Collection<ServiceAttribute> attributes){
		this.attributes.clear();
		for(ServiceAttribute a : attributes){
			this.attributes.put(a.getName(), a);
		}
	}

	public ServiceAttribute getAttribute(String name){
		return attributes.get(name);
	}

	public void setAttribute(ServiceAttribute attribute){
		attributes.put(attribute.getName(), attribute);
	}

	public String getAttributeValue(String attributeName){
		Attribute a = attributes.get(attributeName);
		if(a == null) return null;
		return a.getValue();
	}

	public void setAttributeValue(String attributeName, String attributeValue){
		ServiceAttribute a = getAttribute(attributeName);
		if(a == null){
			attributes.put(attributeName
					, new ServiceAttribute(gridId, serviceId, attributeName, attributeValue));
		} else{
			a.setValue(attributeValue);
			a.touchUpdatedDateTime();
		}
	}

	public void removeAttribute(String attributeName){
		if(attributes.containsKey(attributeName)){
			attributes.remove(attributeName);
		}
	}

	@Override
	public abstract Service clone();

	/**
	 * 
	 * 
	 */
	public boolean isMembersOnly() {
		return membersOnly;
	}

	/**
	 * 
	 * 
	 */
	public void setMembersOnly(boolean membersOnly) {
		this.membersOnly = membersOnly;
	}

	/**
	 * 
	 * 
	 */
	public String getHowToGetMembershipInfo() {
		return howToGetMembershipInfo;
	}

	/**
	 * 
	 * 
	 */
	public void setHowToGetMembershipInfo(String howToGetMembershipInfo) {
		this.howToGetMembershipInfo = howToGetMembershipInfo;
	}


	/**
	 * 
	 * 
	 */
	public void setWrapperSourceCodeUrl(URL wrapperSourceCodeUrl) {
		if(wrapperSourceCodeUrl == null){
			this.wrapperSourceCodeUrl = null;
		} else if(this.wrapperSourceCodeUrl == null){
			this.wrapperSourceCodeUrl = new EmbeddableStringValueClass<URL>(wrapperSourceCodeUrl);
		} else{
			this.wrapperSourceCodeUrl.setValue(wrapperSourceCodeUrl);
		}
	}

	/**
	 * 
	 * 
	 */
	public URL getWrapperSourceCodeUrl() {
		if(wrapperSourceCodeUrl == null) return null;
		return wrapperSourceCodeUrl.getValue();
	}
	
	public Boolean getStreaming() {
		if(streaming == null){
			streaming = false;
		}
		return streaming;
	}

	public void setStreaming(Boolean streaming) {
		this.streaming = streaming;
	}

	@Override
	protected EqualsBuilder appendSpecialEquals(EqualsBuilder builder,
			Object value, Collection<String> appendedFields, boolean ignoreDates) {
		Service s = (Service)value;
		EqualsBuilder b = super.appendSpecialEquals(
				builder, value, appendedFields, ignoreDates);
		appendedFields.add("instance");
		appendedFields.add("attributes");
		appendedFields.add("endpoints");
		appendedFields.add("deployments");
		if(!ignoreDates){
			EqualsBuilderUtil.appendAsSet(
					b, getAttributes(), s.getAttributes()
					);
			EqualsBuilderUtil.appendAsSet(
					b, getServiceEndpoints(), s.getServiceEndpoints()
					);
			EqualsBuilderUtil.appendAsSet(
					b, getServiceDeployments(), s.getServiceDeployments()
					);
			return b;
		}
		try{
			EqualsBuilderUtil.appendAsSet(
					b, getAttributes(), s.getAttributes()
					, ServiceAttribute.class, "equalsIgnoreDates"
					);
			EqualsBuilderUtil.appendAsSet(
					b, getServiceEndpoints(), s.getServiceEndpoints()
					, ServiceEndpoint.class, "equalsIgnoreDates"
					);
			EqualsBuilderUtil.appendAsSet(
					b, getServiceDeployments(), s.getServiceDeployments()
					, ServiceDeployment.class, "equalsIgnoreDates"
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
		appendedFields.add("instance");
		appendedFields.add("attributes");
		appendedFields.add("endpoints");
		appendedFields.add("deployments");
		appendedFields.add("invocations");
		return b
			.append(attributes.entrySet())
			.append(endpoints)
			.append(deployments)
			.append(invocations)
			;
	}

	@Id
	private String gridId;
	@Id
	private String serviceId;
	private String resourceId;

	/*
	 * 
	 * 
	 */
	private String serviceName;
	@Type(type="text")
	private String serviceDescription;
	private String copyrightInfo;
	@Type(type="text")
	private String licenseInfo;

	private String serviceTypeDomainId;
	private String serviceTypeId;
	private InstanceType instanceType;
	private int instanceSize;
	@Column(length=4000000)
	private Blob instance;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="serviceId")
	@JoinColumns({
		@JoinColumn(name="gridId")
		, @JoinColumn(name="serviceId")
	})
	@MapKey(name="name")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Map<String, ServiceAttribute> attributes = new HashMap<String, ServiceAttribute>();

	private String ownerUserId;
	private boolean active;

	@Enumerated(EnumType.STRING)
	private ServiceContainerType containerType = ServiceContainerType.ATOMIC;
	@JSONHint(ignore=true)
	private String appAuthKey;

	@JSONHint(ignore=true)
	@OneToMany(
			cascade={CascadeType.ALL}
			, mappedBy="serviceId"
			)
	@JoinColumns({
		@JoinColumn(name="gridId")
		, @JoinColumn(name="serviceId")
	})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ServiceEndpoint> endpoints = new ArrayList<ServiceEndpoint>();

	@JSONHint(ignore=true)
	@OneToMany(
			cascade={CascadeType.ALL}
			, mappedBy="serviceId"
			)
	@JoinColumns({
		@JoinColumn(name="gridId")
		, @JoinColumn(name="serviceId")
	})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ServiceDeployment> deployments = new ArrayList<ServiceDeployment>();

	@Column(length=100000)
	private Blob wsdl;
	@JSONHint(ignore=true)
	private boolean visible = true;
	@JSONHint(ignore=true)
	private int timeoutMillis = 0;
	@JSONHint(ignore=true)
	private String alternateServiceId;
	@JSONHint(ignore=true)
	private boolean useAlternateService = false;
	@JSONHint(ignore=true)
	private boolean approved = false;
	@CollectionOfElements
	private Set<String> allowedUse = new HashSet<String>();
	@CollectionOfElements
	private Set<String> allowedAppProvision = new HashSet<String>();
	private boolean federatedUseAllowed;
	private boolean membersOnly;
	@Type(type="text")
	private String howToGetMembershipInfo;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="stringValue"
			, column=@Column(name="wrapperSourceCodeUrlStringValue")
		),
		@AttributeOverride(name="clazz"
			, column=@Column(name="wrapperSourceCodeUrlClazz")
		)
	})
	private EmbeddableStringValueClass<URL> wrapperSourceCodeUrl;

	@JSONHint(ignore=true)
	@CollectionOfElements
	@OneToMany(
			cascade={CascadeType.ALL}
			, mappedBy="serviceId"
			)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumns({
			@JoinColumn(name="ownerServiceId", referencedColumnName="serviceId")
			, @JoinColumn(name="ownerServiceGridId", referencedColumnName="gridId")
			})
	private Set<Invocation> invocations = new HashSet<Invocation>();

	private Boolean streaming = false;

	private static final long serialVersionUID = 3048945463767544388L;	
}
