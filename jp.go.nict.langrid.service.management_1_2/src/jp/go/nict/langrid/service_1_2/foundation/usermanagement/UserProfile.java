/*
 * $Id: UserProfile.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.usermanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Retains the user's profile.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class UserProfile
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public UserProfile(){
	}

	/**
	 * 
	 * Constructor.
	 * @param organization Attribute organization
	 * @param representative Agent name
	 * @param email E-mail address
	 * @param homepageUrl Homepage URL
	 * @param address Address (residence)
	 * 
	 */
	public UserProfile(String organization, String representative,
			String email, String homepageUrl, String address) {
		super();
		this.organization = organization;
		this.representative = representative;
		this.emailAddress = email;
		this.homepageUrl = homepageUrl;
		this.address = address;
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

	/**
	 * 
	 * Sets organization.
	 * @param organization Attribute organization
	 * 
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * 
	 * Gets organization.
	 * @return Organization
	 * 
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * 
	 * Gets the representative's name.
	 * @return Agent name
	 * 
	 */
	public String getRepresentative(){
		return representative;
	}

	/**
	 * 
	 * Sets the representative's name.
	 * @param representative Agent name
	 * 
	 */
	public void setRepresentative(String representative){
		this.representative = representative;
	}

	/**
	 * 
	 * Gets e-mail address.
	 * @return E-mail address
	 * 
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * 
	 * Sets e-mail address.
	 * @param emailAddress E-mail address
	 * 
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * 
	 * Gets homepage URL.
	 * @return Homepage URL
	 * 
	 */
	public String getHomepageUrl() {
		return homepageUrl;
	}

	/**
	 * 
	 * Sets homepage URL.
	 * @param homepageUrl Homepage URL
	 * 
	 */
	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}

	/**
	 * 
	 * Gets address.
	 * @return Address
	 * 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * Sets address.
	 * @param address Address (residence)
	 * 
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	private String organization;
	private String representative;
	private String emailAddress;
	private String homepageUrl;
	private String address;

	private static final long serialVersionUID = -4387643722243208130L;
}
