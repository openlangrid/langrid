/*
 * $Id: Endpoint.java 1187 2014-04-10 14:25:28Z t-nakaguchi $
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
package jp.go.nict.langrid.cosee;

import java.net.URI;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1187 $
 */
public class Endpoint {
	/**
	 * 
	 * 
	 */
	public Endpoint(String serviceId, URI address
			, String userName, String password){
		this.serviceId = serviceId;
		this.address = address;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * 
	 * 
	 */
	public Endpoint(URI address){
		this.address = address;
	}

	/**
	 * 
	 * 
	 */
	public Endpoint(Endpoint original){
		this.serviceId = original.serviceId;
		this.address = original.address;
		this.userName = original.userName;
		this.password = original.password;
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
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * 
	 */
	public void setAddress(URI address) {
		this.address = address;
	}

	/**
	 * 
	 * 
	 */
	public URI getAddress() {
		return address;
	}

	/**
	 * 
	 * 
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * 
	 * 
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * 
	 * 
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * 
	 */
	public String getUserName() {
		return userName;
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
	public String getPassword() {
		return password;
	}

	private String serviceId;
	private URI address;
	private String protocol;
	private String userName;
	private String password;
}
