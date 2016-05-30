/*
 * $Id: WSDL.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Revision: 184 $
 */
public class WSDL {
	/**
	 * 
	 * 
	 */
	public URI getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetNamespace(URI targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	/**
	 * 
	 * 
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * 
	 * 
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * 
	 * 
	 */
	public Map<String, Service> getServices() {
		return services;
	}

	/**
	 * 
	 * 
	 */
	public void setServices(Map<String, Service> services) {
		this.services = services;
	}

	/**
	 * 
	 * 
	 */
	public Map<String, PartnerLinkType> getPlinks() {
		return plinks;
	}

	/**
	 * 
	 * 
	 */
	public void setPlinks(Map<String, PartnerLinkType> plinks) {
		this.plinks = plinks;
	}

	/**
	 * 
	 * 
	 */
	public Map<String, QName> getBindingTypes() {
		return bindingTypes;
	}

	/**
	 * 
	 * 
	 */
	public void setBindingTypes(Map<String, QName> bindingTypes) {
		this.bindingTypes = bindingTypes;
	}

	/**
	 * 
	 * 
	 */
	public Map<String, EndpointReference> getPortTypeToEndpointReference() {
		return portTypeToEndpointReference;
	}

	/**
	 * 
	 * 
	 */
	public void setPortTypeToEndpointReference(
		Map<String, EndpointReference> portTypeToEndpointReference) {
		this.portTypeToEndpointReference = portTypeToEndpointReference;
	}

	/**
	 * 
	 * 
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * 
	 * 
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	private URI targetNamespace;
	private String filename;
	private Map<String, Service> services
		= new HashMap<String, Service>();
	private Map<String, PartnerLinkType> plinks
		= new HashMap<String, PartnerLinkType>();
	private Map<String, QName> bindingTypes
		= new HashMap<String, QName>();
	private Map<String, EndpointReference> portTypeToEndpointReference
		= new HashMap<String, EndpointReference>();
	private byte[] body;
}
