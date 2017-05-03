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
package jp.go.nict.langrid.commons.xml.soap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;

public class SoapHeaderImpl extends SoapHeaderAdapter {
	@Override
	public SOAPElement addChildElement(SOAPElement element) throws SOAPException {
		if(element instanceof SOAPHeaderElement){
			elements.add((SOAPHeaderElement)element);
			return element;
		} else{
			return null;
		}
	}

	@Override
	public Iterator<SOAPHeaderElement> examineAllHeaderElements() {
		return elements.iterator();
	}

	private List<SOAPHeaderElement> elements = new ArrayList<SOAPHeaderElement>();
}
