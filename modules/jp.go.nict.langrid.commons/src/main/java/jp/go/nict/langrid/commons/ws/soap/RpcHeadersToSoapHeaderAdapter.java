/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.ws.soap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;

import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class RpcHeadersToSoapHeaderAdapter extends SoapHeaderAdapter{
	public RpcHeadersToSoapHeaderAdapter(){
		this.headers = new ArrayList<RpcHeader>();
	}

	public RpcHeadersToSoapHeaderAdapter(Collection<RpcHeader> headers){
		this.headers = headers;
	}

	public Collection<RpcHeader> getHeaders(){
		return headers;
	}

	@Override
	public Iterator<SOAPHeaderElement> examineAllHeaderElements() {
		final Iterator<RpcHeader> i = headers.iterator();
		return new Iterator<SOAPHeaderElement>(){
			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public SOAPHeaderElement next() {
				final RpcHeader h = i.next();
				return new SoapHeaderElementAdapter() {
					@Override
					public String getValue() {
						return h.getValue();
					}

					@Override
					public String getNamespaceURI() {
						return h.getNamespace();
					}

					@Override
					public String getNodeName() {
						return h.getName();
					}
				};
			}

			@Override
			public void remove() {
				i.remove();
			}			
		};
	}

	@Override
	public SOAPElement addChildElement(SOAPElement arg0) throws SOAPException {
		String uri = arg0.getNamespaceURI();
		if(uri == null) return null;
		String value = arg0.getValue();
		if(value == null) value = arg0.getNodeValue();
		if(value == null) return null;

		headers.add(new RpcHeader(uri, arg0.getLocalName(), value));
		return arg0;
	}

	private Collection<RpcHeader> headers;
}
