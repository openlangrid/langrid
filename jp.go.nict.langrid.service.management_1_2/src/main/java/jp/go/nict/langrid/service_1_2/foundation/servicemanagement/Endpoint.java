/*
 * $Id: Endpoint.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
 * Stores the endpoint.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class Endpoint
implements Serializable
{
	/**
	 * 
	 * Default constructor.
	 * 
	 */
	public Endpoint(){
	}

	/**
	 * 
	 * Constructor.
	 * @param userName Authentication username
	 * @param password Authorization password
	 * @param enabled Valid or invalid
	 * 
	 */
	public Endpoint(
			String url, String userName, String password
			, boolean enabled)
	{
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.enabled = enabled;
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
	 * Sets URL.
	 * 
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * Gets URL.
	 * 
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * Sets BASIC authentication username.
	 * @param userName Username for BASIC authentication
	 * 
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * Gets BASIC authentication username.
	 * @return BASIC authentication username
	 * 
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 
	 * Sets BASIC authentication password.
	 * @param password Password for BASIC authorization
	 * 
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * Returns BASIC authentication password.
	 * @return BASIC authentication password
	 * 
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * Returns whether or not endpoint is valid.
	 * @return Whether or not endpoint is valid
	 * 
	 */
	public boolean isEnabled(){
		return enabled;
	}

	/**
	 * 
	 * Sets whether or not endpoint is valid.
	 * @param enabled Whether or not the endpoint is valid
	 * 
	 */
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}

	private String url;
	private String userName;
	private String password;
	private boolean enabled;

	private static final long serialVersionUID = 7015524978211061328L;
}
