/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.wrapper.common.composite;

import java.util.Iterator;
import java.util.List;

import javax.xml.soap.SOAPHeaderElement;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.commons.ws.soap.SoapHeaderElementAdapter;

public class PbSOAPHeaderElement {
	public static Iterator<SOAPHeaderElement> iterator(List<Header> headers){
		final Iterator<Header> i = headers.iterator();
		return new Iterator<SOAPHeaderElement>(){
			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public SOAPHeaderElement next() {
				final Header h = i.next();
				return new SoapHeaderElementAdapter() {
					@Override
					public String getValue() {
						return h.getValue();
					}

					@Override
					public String getNamespaceURI() {
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
}
