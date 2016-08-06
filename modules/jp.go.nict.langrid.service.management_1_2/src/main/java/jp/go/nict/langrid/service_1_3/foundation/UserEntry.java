/*
 * $Id: GridEntry.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_3.foundation;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class UserEntry extends UpdateManagedEntry
implements Serializable{
	public UserEntry(){
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getHomepageUrl() {
		return homepageUrl;
	}

	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Calendar getPasswordChangedDate() {
		return passwordChangedDate;
	}

	public void setPasswordChangedDate(Calendar passwordChangedDate) {
		this.passwordChangedDate = passwordChangedDate;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isAbleToCallServices() {
		return ableToCallServices;
	}

	public void setAbleToCallServices(boolean ableToCallServices) {
		this.ableToCallServices = ableToCallServices;
	}

	public String getDefaultUseType() {
		return defaultUseType;
	}

	public void setDefaultUseType(String defaultUseType) {
		this.defaultUseType = defaultUseType;
	}

	public String getDefaultAppProvisionType() {
		return defaultAppProvisionType;
	}

	public void setDefaultAppProvisionType(String defaultAppProvisionType) {
		this.defaultAppProvisionType = defaultAppProvisionType;
	}

	private String gridId;
	private String userId;
	private String organization;
	private String representative;
	private String emailAddress;
	private String homepageUrl;
	private String address;
	private Calendar passwordChangedDate;

	private Set<String> roles = new HashSet<String>();
	private Map<String, String> attributes = new HashMap<String, String>();

	private boolean visible;
	private boolean ableToCallServices;
	private String defaultUseType;
	private String defaultAppProvisionType;

	private static final long serialVersionUID = -4382466942028959119L;
}
