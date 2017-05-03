/*
 * $Id: AeUserOverridingEndpointReference.java 260 2010-10-03 09:49:40Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 2 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.cosee.ae_5_0_2;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.activebpel.wsio.IAeWebServiceEndpointReference;

import jp.go.nict.langrid.cosee.Endpoint;

/**
 * 
 * 
 */
public class AeUserOverridingEndpointReference 
	implements IAeWebServiceEndpointReference
{
	/**
	 * 
	 * 
	 */
	public AeUserOverridingEndpointReference(
			IAeWebServiceEndpointReference orig
			, Endpoint endpoint)
	{
		this.orig = orig;
		this.endpoint = endpoint;
	}

	public String getUsername(){
		return endpoint.getUserName();
	}

	public String getPassword(){
		return endpoint.getPassword();
	}

	@SuppressWarnings("unchecked")
	public List getPolicies(){
		return orig.getPolicies();
	}

	@SuppressWarnings("unchecked")
	public Iterator getExtensibilityElements(){
		return orig.getExtensibilityElements();
	}

	public QName getServiceName(){
		return orig.getServiceName();
	}

	public String getServicePort(){
		return orig.getServicePort();
	}

	@SuppressWarnings("unchecked")
	public Map getProperties(){
		return orig.getProperties();
	}

	public String getAddress(){
		return endpoint.getAddress().toString();
	}

	public QName getPortType(){
		return orig.getPortType();
	}

	@SuppressWarnings("unchecked")
	public List getReferenceProperties(){
		return orig.getReferenceProperties();
	}

	public String getSourceNamespace(){
		return orig.getSourceNamespace();
	}

	private IAeWebServiceEndpointReference orig;
	private Endpoint endpoint;
	private static final long serialVersionUID = -4020724600121676440L;
}
