/*
 * $Id: WSDLUtil.java 191 2010-10-02 11:29:54Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.jxpath;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import jp.go.nict.langrid.commons.ws.Constants;

import org.apache.commons.jxpath.JXPathContext;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 191 $
 */
public class WSDLUtil {
	/**
	 * 
	 * 
	 */
	public static JXPathContext newWSDLContext(
		InputStream wsdl, String wsdlNSPrefix)
		throws IOException, SAXException
	{
		JXPathContext context = JXPathUtil.newXMLContext(wsdl);
		context.setLenient(true);
		context.registerNamespace(wsdlNSPrefix, Constants.WSDL_URI);
		return context;
	}

	/**
	 * 
	 * 
	 */
	public static String getPortTypeName(InputStream body)
		throws IOException, SAXException
	{
		return (String)newWSDLContext(body, "_").getValue(
			"_:definitions/_:portType/@name"
			);
	}

	/**
	 * 
	 * 
	 */
	public static URI getTargetNamespace(InputStream body)
		throws IOException, SAXException, URISyntaxException
	{
		String value = (String)newWSDLContext(body, "_").getValue(
				"_:definitions/@targetNamespace"
				);
		if(value == null) return null;
		return new URI(value);
	}

	/**
	 * 
	 * 
	 */
	public static String getServiceName(InputStream body)
		throws IOException, SAXException
	{
		return (String)newWSDLContext(body, "_").getValue(
			"_:definitions/_:service/@name"
			);
	}

	/**
	 * 
	 * 
	 */
	public static URL getServiceAddress(InputStream wsdl)
		throws IOException, MalformedURLException
		, SAXException
	{
		JXPathContext context = newWSDLContext(wsdl, "_");
		context.registerNamespace(
			"wsdlsoap"
			, "http://schemas.xmlsoap.org/wsdl/soap/"
			);
		String url = (String)context.getValue(
				"_:definitions/_:service/_:port/wsdlsoap:address/@location"
				);
		if(url == null) return null;
		else return new URL(url);
	}
}
