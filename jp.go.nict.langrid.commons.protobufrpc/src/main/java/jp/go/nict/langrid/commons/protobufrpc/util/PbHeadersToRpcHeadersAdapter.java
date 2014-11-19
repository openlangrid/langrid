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
package jp.go.nict.langrid.commons.protobufrpc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: nakaguchi $
 * @version $Revision: 15534 $
 */
public class PbHeadersToRpcHeadersAdapter implements Collection<RpcHeader>{
	public PbHeadersToRpcHeadersAdapter(){
		this.headers = new ArrayList<Header>();
	}

	public PbHeadersToRpcHeadersAdapter(List<Header> headers){
		this.headers = headers;
	}

	public List<Header> getHeaders(){
		return headers;
	}

	@Override
	public boolean add(RpcHeader e) {
		this.headers.add(Header.newBuilder().setName(e.getNamespace()).setValue(e.getValue()).build());
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
		headers.clear();
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
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return headers.isEmpty();
	}

	@Override
	public boolean remove(Object o) {
	throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return headers.size();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RpcHeader> iterator() {
		return new Iterator<RpcHeader>(){
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}
			@Override
			public RpcHeader next() {
				Header h = it.next();
				return new RpcHeader(h.getName(), null, h.getValue());
			}
			@Override
			public void remove() {
				it.remove();
			}
			private Iterator<Header> it = headers.iterator();
		};
	}

	private List<Header> headers;
}
