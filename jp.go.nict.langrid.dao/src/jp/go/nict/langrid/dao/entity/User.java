/*
 * $Id:User.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
@IdClass(UserPK.class)
@javax.persistence.Table(name="users")
public class User
extends UpdateManagedEntity
implements AttributedElement<UserAttribute>, Serializable{
	/**
	 * 
	 * 
	 */
	public User(){
	}

	/**
	 * 
	 * 
	 */
	public User(String gridId, String userId, String password){
		this.gridId = gridId;
		this.userId = userId;
		this.password = password;
	}

	/**
	 * 
	 * 
	 */
	public boolean isAdminUser(){
		return roles.contains(new UserRole(gridId, userId, UserRole.ADMIN_ROLE));
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
	public String getUserId(){
		return userId;
	}

	/**
	 * 
	 * 
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}

	/**
	 * 
	 * 
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * 
	 * 
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * 
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * 
	 * 
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * 
	 * 
	 */
	public String getRepresentative(){
		return representative;
	}

	/**
	 * 
	 * 
	 */
	public void setRepresentative(String representative){
		this.representative = representative;
	}

	/**
	 * 
	 * 
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * 
	 * 
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * 
	 * 
	 */
	public URL getHomepageUrl() {
		if(homepageUrl != null){
			return homepageUrl.getValue();
		} else{
			return null;
		}
	}

	/**
	 * 
	 * 
	 */
	public void setHomepageUrl(URL homepageUrl) {
		if(homepageUrl == null){
			this.homepageUrl = null;
		} else if(this.homepageUrl == null){
			this.homepageUrl = new EmbeddableStringValueClass<URL>(homepageUrl);
		} else{
			this.homepageUrl.setValue(homepageUrl);
		}
	}

	/**
	 * 
	 * 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * 
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getPasswordChangedDate() {
		return passwordChangedDate;
	}

	/**
	 * 
	 * 
	 */
	public void setPasswordChangedDate(Calendar passwordChangedDate) {
		this.passwordChangedDate = passwordChangedDate;
	}

	/**
	 * 
	 * 
	 */
	public void touchPasswordChangedDateTime(){
		setPasswordChangedDate(Calendar.getInstance());
	}

	/**
	 * 
	 * 
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * 
	 * 
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * 
	 * 
	 */
	public boolean isAbleToCallServices() {
		return ableToCallServices;
	}

	/**
	 * 
	 * 
	 */
	public void setCanCallServices(boolean ableToCallServices) {
		this.ableToCallServices = ableToCallServices;
	}

	/**
	 * 
	 * 
	 */
	public void setAbleToCallServices(boolean ableToCallServices) {
		this.ableToCallServices = ableToCallServices;
	}

	public Collection<UserAttribute> getAttributes(){
		return attributes.values();
	}

	public void setAttributes(Collection<UserAttribute> attributes){
		this.attributes.clear();
		for(UserAttribute a : attributes){
			this.attributes.put(a.getName(), a);
		}
	}

	public UserAttribute getAttribute(String name){
		return attributes.get(name);
	}

	public void setAttribute(UserAttribute attribute){
		attributes.put(attribute.getName(), attribute);
	}

	public String getAttributeValue(String attributeName){
		Attribute a = attributes.get(attributeName);
		if(a == null) return null;
		return a.getValue();
	}

	public void setAttributeValue(String attributeName, String attributeValue){
		UserAttribute a = getAttribute(attributeName);
		if(a == null){
			attributes.put(attributeName, new UserAttribute(
					gridId, userId, attributeName, attributeValue));
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

	/**
	 * 
	 * 
	 */
	public Set<UserRole> getRoles() {
		return roles;
	}

	/**
	 * 
	 * 
	 */
	public void setRoles(Set<UserRole> roles) {
		this.roles.clear();
		this.roles.addAll(roles);
	}

	/**
	 * 
	 * 
	 */
	public String getDefaultUseType() {
		return defaultUseType;
	}

	/**
	 * 
	 * 
	 */
	public void setDefaultUseType(String defaultUseType) {
		this.defaultUseType = defaultUseType;
	}

	/**
	 * 
	 * 
	 */
	public String getDefaultAppProvisionType() {
		return defaultAppProvisionType;
	}

	/**
	 * 
	 * 
	 */
	public void setDefaultAppProvisionType(String defaultAppProvisionType) {
		this.defaultAppProvisionType = defaultAppProvisionType;
	}

	protected EqualsBuilder appendSpecialEquals(
			EqualsBuilder builder, Object value
			, Collection<String> appendedFields, boolean ignoreDates){
		User s = (User)value;
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
			appendedFields.add("passwordChangedDate");
			appendedFields.add("lastAuthenticatedDate");
			EqualsBuilderUtil.appendAsSet(
					b, getAttributes(), s.getAttributes()
					, UserAttribute.class, "equalsIgnoreDates"
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
		return b
			.append(attributes.entrySet())
			;
	}

	@Id
	private String gridId;
	@Id
	private String userId;
	private String password;
	private String organization;
	private String representative;
	private String emailAddress;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="stringValue", column=@Column(name="homepageUrlStringValue"))
			, @AttributeOverride(name="clazz", column=@Column(name="homepageUrlClazz"))
			})
	private EmbeddableStringValueClass<URL> homepageUrl;
	private String address;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar passwordChangedDate;

	@OneToMany(
			cascade={CascadeType.ALL}
			, mappedBy="userId"
			)
	@JoinColumns({
		@JoinColumn(name="gridId")
		, @JoinColumn(name="userId")
	})	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<UserRole> roles = new HashSet<UserRole>();

	@OneToMany(cascade=CascadeType.ALL, mappedBy="userId")
	@JoinColumns({
		@JoinColumn(name="gridId")
		, @JoinColumn(name="userId")
	})
	@MapKey(name="name")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Map<String, UserAttribute> attributes = new HashMap<String, UserAttribute>();

	private boolean visible = true;
	private boolean ableToCallServices = true;
	private String defaultUseType = UseType.NONPROFIT_USE.name();
	private String defaultAppProvisionType = AppProvisionType.CLIENT_CONTROL.name();

	private static final long serialVersionUID = -2625107316739101834L;
}
