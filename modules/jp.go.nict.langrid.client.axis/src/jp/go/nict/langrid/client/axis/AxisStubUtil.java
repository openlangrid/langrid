/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
package jp.go.nict.langrid.client.axis;

import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Stub;
import org.apache.axis.transport.http.HTTPConstants;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class AxisStubUtil {
	/**
	 * 
	 * 
	 */
	public static void setUrl(Stub stub, URL url){
		stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url.toString());
	}

	/**
	 * 
	 * 
	 */
	public static void setUserName(Stub stub, String username){
		setUserName(stub, username, true);
	}

	/**
	 * 
	 * 
	 */
	public static void setUserName(Stub stub, String username, boolean httpPreemptive){
		stub.setUsername(username);
		stub._setProperty("HTTPPreemptive", (httpPreemptive && username != null) ? "true" : "false");
	}

	/**
	 * 
	 * 
	 */
	public static void setPassword(Stub stub, String password){
		stub.setPassword(password);
	}

	public static void setTimeout(Stub stub, int timeoutMillis){
		stub.setTimeout(timeoutMillis);
	}

	/**
	 * 
	 * 
	 */
	public static void setMimeHeaders(Stub stub, Iterable<Map.Entry<String, Object>> headers){
		@SuppressWarnings("unchecked")
		Map<String, Object> origMimeHeaders = (Map<String, Object>)stub._getProperty(
				HTTPConstants.REQUEST_HEADERS);
		if(origMimeHeaders == null){
			origMimeHeaders = new Hashtable<String, Object>();
			stub._setProperty(HTTPConstants.REQUEST_HEADERS, origMimeHeaders);
		}
		for(Map.Entry<String, Object> entry : headers){
			origMimeHeaders.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 
	 * 
	 */
	public static void setSoapHeaders(Stub stub, Iterable<Map.Entry<QName, Object>> headers){
		for(Map.Entry<QName, Object> h : headers){
			stub.setHeader(new org.apache.axis.message.SOAPHeaderElement(
					h.getKey().getNamespaceURI()
					, h.getKey().getLocalPart()
					, h.getValue()
					));
		}
	}
}
