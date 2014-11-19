/*
 * $Id: ServiceProfile.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the service's profile data.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ServiceProfile
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceProfile(){
	}

	/**
	 * 
	 * Constructor.
	 * @param serviceName Service name
	 * @param serviceDescription Service description
	 * @param copyrightInfo Copyright information
	 * @param licenseInfo Licence information
	 * 
	 */
	public ServiceProfile(
			String serviceName
			, String serviceDescription
			, String copyrightInfo
			, String licenseInfo
	)
	{
		this.serviceName = serviceName;
		this.serviceDescription = serviceDescription;
		this.copyrightInfo = copyrightInfo;
		this.licenseInfo = licenseInfo;
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
	 * Method that returns service's display name
	 * @return Service's displayed name
	 * 
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * Sets service's display name.
	 * @param serviceName Displayed name of the service
	 * 
	 */
	public void setServiceName(String serviceName){
		this.serviceName = serviceName;
	}

	/**
	 * 
	 * Returns service description.
	 * @return Service description
	 * 
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}

	/**
	 * 
	 * Sets service description.
	 * @param serviceDescription Service description
	 * 
	 */
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	/**
	 * 
	 * Sets copyright data.
	 * @param copyrightInfo Copyright information
	 * 
	 */
	public void setCopyrightInfo(String copyrightInfo) {
		this.copyrightInfo = copyrightInfo;
	}

	/**
	 * 
	 * Gets copyright data.
	 * @return Copyright information
	 * 
	 */
	public String getCopyrightInfo() {
		return copyrightInfo;
	}

	/**
	 * 
	 * Sets license data.
	 * @param licenseInfo Licence information
	 * 
	 */
	public void setLicenseInfo(String licenseInfo) {
		this.licenseInfo = licenseInfo;
	}

	/**
	 * 
	 * Gets license data.
	 * @return License information
	 * 
	 */
	public String getLicenseInfo() {
		return licenseInfo;
	}

	private String serviceName = "";
	private String serviceDescription = "";
	private String copyrightInfo = "";
	private String licenseInfo = "";

	private static final long serialVersionUID = -3803142135659564147L;
}
