/*
 * $Id: ServiceEntryWithCompactLanguageExpression.java 15013 2011-08-19 11:04:32Z Takao Nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.servicemanagement;

import java.io.Serializable;
import java.util.Calendar;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.service_1_2.LanguagePathWithType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the service's owner data.
 * Used when returning the service list to the client.
 * 
 * @author Takao Nakaguchi
 * @author $Author: Takao Nakaguchi $
 * @version $Revision: 15013 $
 */
public class ServiceEntryWithCompactLanguageExpression
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceEntryWithCompactLanguageExpression(){
	}

	/**
	 * 
	 * Constructor.
	 * @param serviceId Service ID
	 * @param serviceName Displayed name of the service
	 * @param serviceDescription Service description
	 * @param serviceType Type of the service
	 * @param instanceType Type of the instance
	 * @param supportedLanguages Supported languages(Language/Language pair/Language path)
	 * @param endpointUrl Called URL
	 * @param ownerUserId User ID of the user who registered this service
	 * @param registeredDate Date of service registration
	 * @param updatedDate Date that service was updated
	 * @param active Whether or not the service is active
	 * 
	 */
	public ServiceEntryWithCompactLanguageExpression(String serviceId, String serviceName
			, String serviceDescription, String serviceType
			, String instanceType, LanguagePathWithType[] supportedLanguages
			, String endpointUrl, String ownerUserId
			, Calendar registeredDate, Calendar updatedDate
			, boolean active
			)
	{
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceDescription = serviceDescription;
		this.serviceType = serviceType;
		this.instanceType = instanceType;
		this.supportedLanguages = supportedLanguages;
		this.endpointUrl = endpointUrl;
		this.ownerUserId = ownerUserId;
		this.registeredDate = registeredDate;
		this.updatedDate = updatedDate;
		this.active = active;
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
	 * Returns service ID.
	 * @return Service ID
	 * 
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * Sets service ID.
	 * @param serviceId Service ID
	 * 
	 */
	public void setServiceId(String serviceId){
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * Returns service's name.
	 * @return Service name
	 * 
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * Sets service's name.
	 * @param aValue Service name
	 * 
	 */
	public void setServiceName(String aValue){
		serviceName = aValue;
	}

	/**
	 * 
	 * Returns serviceDescription.
	 * 
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}

	/**
	 * 
	 * Sets serviceDescription.
	 * 
	 */
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	/**
	 * 
	 * Returns service type.
	 * @return Service type
	 * 
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * 
	 * Sets service type.
	 * @param serviceType Service type
	 * 
	 */
	public void setServiceType(String serviceType){
		this.serviceType = serviceType;
	}

	/**
	 * 
	 * Returns instanceType.
	 * 
	 */
	public String getInstanceType() {
		return instanceType;
	}

	/**
	 * 
	 * Sets instanceType.
	 * 
	 */
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	/**
	 * 
	 * Returns the supported language.
	 * @return Supported language
	 * 
	 */
	public LanguagePathWithType[] getSupportedLanguages() {
		return supportedLanguages;
	}

	/**
	 * 
	 * Sets supported language.
	 * @param supportedLanguages Supported languages
	 * 
	 */
	public void setSupportedLanguages(LanguagePathWithType[] supportedLanguages){
		this.supportedLanguages = supportedLanguages;
	}

	/**
	 * 
	 * Gets service URL.
	 * @return The service's URL
	 * 
	 */
	public String getEndpointUrl(){
		return endpointUrl;
	}

	/**
	 * 
	 * Sets service URL.
	 * @param endpointUrl URL of the service
	 * 
	 */
	public void setEndpointUrl(String endpointUrl){
		this.endpointUrl = endpointUrl;
	}

	/**
	 * 
	 * Gets user ID of the user who registered the service.
	 * @return User ID of user who registered service
	 * 
	 */
	public String getOwnerUserId(){
		return ownerUserId;
	}

	/**
	 * 
	 * Sets user ID of the user who registered the service.
	 * @param ownerUserId User ID of the user who registered the service
	 * 
	 */
	public void setOwnerUserId(String ownerUserId){
		this.ownerUserId = ownerUserId;
	}

	/**
	 * 
	 * Gets date service was registered.
	 * @return Date service was registered
	 * 
	 */
	public Calendar getRegisteredDate(){
		return registeredDate;
	}

	/**
	 * 
	 * Sets date service was registered.
	 * @param registeredDate Date of service registration
	 * 
	 */
	public void setRegisteredDate(Calendar registeredDate){
		this.registeredDate = registeredDate;
	}

	/**
	 * 
	 * Gets date and time service was changed.
	 * @return Date service was updated
	 * 
	 */
	public Calendar getUpdatedDate(){
		return updatedDate;
	}

	/**
	 * 
	 * Sets date and time service was changed.
	 * @param updatedDate Date that service was updated
	 * 
	 */
	public void setUpdatedDate(Calendar updatedDate){
		this.updatedDate = updatedDate;
	}

	/**
	 * 
	 * Gets whether the service is active or not.
	 * @return Whether or not the service is active
	 * 
	 */
	public boolean isActive(){
		return active;
	}

	/**
	 * 
	 * Sets whether the service is active or not.
	 * @param active Whether or not the service is active
	 * 
	 */
	public void setActive(boolean active){
		this.active = active;
	}

	private String serviceId = "";
	private String serviceName = "";
	private String serviceDescription = "";
	private String serviceType = "";
	private String instanceType = "";
	private LanguagePathWithType[] supportedLanguages = new LanguagePathWithType[]{};
	private String endpointUrl = "";
	private String ownerUserId = "";
	private Calendar registeredDate = CalendarUtil.MAX_VALUE_IN_EPOC;
	private Calendar updatedDate = CalendarUtil.MAX_VALUE_IN_EPOC;
	private boolean active = false;

	private static final long serialVersionUID = 8286064981992474579L;
}
