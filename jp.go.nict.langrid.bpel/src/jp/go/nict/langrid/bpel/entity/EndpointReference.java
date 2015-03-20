/*
 * $Id: EndpointReference.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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
package jp.go.nict.langrid.bpel.entity;

import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Revision: 184 $
 */
public class EndpointReference {
	/**
	 * 
	 * 
	 */
	public URL getAddress() {
		return address;
	}

	/**
	 * 
	 * 
	 */
	public void setAddress(URL address) {
		this.address = address;
	}

	/**
	 * 
	 * 
	 */
	public String getServicePortName() {
		return servicePortName;
	}

	/**
	 * 
	 * 
	 */
	public void setServicePortName(String servicePortName) {
		this.servicePortName = servicePortName;
	}

	/**
	 * 
	 * 
	 */
	public QName getServiceName() {
		return serviceName;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceName(QName serviceName) {
		this.serviceName = serviceName;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	private URL address;
	private String servicePortName;
	private QName serviceName;
}
