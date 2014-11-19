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
package jp.go.nict.langrid.service_1_3.foundation.federation;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class GridEntry
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public GridEntry(){
	}

	/**
	 * 
	 * Constructor.
	 * @param userId User ID
	 * @param organization Organization name
	 * @param representative Agent
	 * @param emailAddress E-mail address
	 * @param homepageUrl Homepage URL
	 * @param address Address
	 * @param registeredDate Date of registration
	 * @param updatedDate Date of update
	 * 
	 */
	public GridEntry(String gridId, String organization, String representative
			, String emailAddress, String homepageUrl, String address
			, Calendar registeredDate, Calendar updatedDate) {
		this.gridId = gridId;
		this.organization = organization;
		this.representative = representative;
		this.emailAddress = emailAddress;
		this.homepageUrl = homepageUrl;
		this.address = address;
		this.registeredDate = registeredDate;
		this.updatedDate = updatedDate;
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
	 * 
	 */
	public String getGridId() {
		return gridId;
	}

	/**
	 * 
	 * 
	 */
	public void setGridId(String gridId){
		this.gridId = gridId;
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
	 * Sets organization.
	 * @param organization Attribute organization
	 * 
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
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
	 * @param address Address
	 * 
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * Gets the date of registration.
	 * @return Date of registration
	 * 
	 */
	public Calendar getRegisteredDate() {
		return registeredDate;
	}

	/**
	 * 
	 * Sets the date of registration.
	 * @param registeredDate Date of registration
	 * 
	 */
	public void setRegisteredDate(Calendar registeredDate) {
		this.registeredDate = registeredDate;
	}

	/**
	 * 
	 * Gets date and time of change.
	 * @return Date of change
	 * 
	 */
	public Calendar getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * 
	 * Sets date and time of change.
	 * @param updatedDate Date of update
	 * 
	 */
	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}

	private String gridId;
	private String organization;
	private String representative;
	private String emailAddress;
	private String homepageUrl;
	private String address;
	private Calendar registeredDate;
	private Calendar updatedDate;

	private static final long serialVersionUID = -2279766886877060280L;
}
