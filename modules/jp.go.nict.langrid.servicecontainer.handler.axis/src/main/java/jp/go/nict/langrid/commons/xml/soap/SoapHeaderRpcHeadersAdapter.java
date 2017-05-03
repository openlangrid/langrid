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

import java.util.Collection;
import java.util.Iterator;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * 
 * @author Takao Nakaguchi
 */
public class SoapHeaderRpcHeadersAdapter implements Collection<RpcHeader>{
	public SoapHeaderRpcHeadersAdapter(SOAPHeader header) {
		this.header = header;
	}

	@Override
	public boolean add(RpcHeader e) {
		try {
			header.addChildElement(e.getName(), "ns", e.getNamespace()).setValue(e.getValue());
		} catch (SOAPException e1) {
			throw new RuntimeException(e1);
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends RpcHeader> c) {
		for(RpcHeader h : c){
			add(h);
		}
		return true;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		return header.equals(obj);
	}

	@Override
	public int hashCode() {
		return header.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return isEmpty();
	}

	@Override
	public Iterator<RpcHeader> iterator() {
		return new Iterator<RpcHeader>() {
			@Override
			public boolean hasNext() {
				return hi.hasNext();
			}
			@Override
			public RpcHeader next() {
				SOAPHeaderElement se = hi.next();
				return new RpcHeader(se.getNamespaceURI(), se.getLocalName(), se.getValue());
			}
			@Override
			public void remove() {
				hi.remove();
			}
			@SuppressWarnings("unchecked")
			private Iterator<SOAPHeaderElement> hi = header.examineAllHeaderElements();
		};
	}

	@Override
	public boolean remove(Object o) {
		return header.removeChild((Node)o) != null;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean removed = false;
		for(Object o : c){
			removed |= remove(o);
		}
		return removed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return header.getChildNodes().getLength();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	private SOAPHeader header;
}
