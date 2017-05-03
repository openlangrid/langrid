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

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPHeaderElement;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.commons.xml.soap.SoapHeaders;

public class PbHeaderRequestHeaders
implements SoapHeaders{
	public PbHeaderRequestHeaders(){
	}

	public List<Header> getHeaders(){
		return headers;
	}

	@Override
	public void append(SOAPHeaderElement header) {
		String uri = header.getNamespaceURI();
		String value = header.getValue();
		if(uri != null && uri.length() > 0
				&& value != null && value.length() > 0){
			headers.add(Header.newBuilder()
					.setName(header.getNamespaceURI())
					.setValue(header.getValue())
					.build()
					);
		}
	}

	private List<Header> headers = new ArrayList<Header>();
}
