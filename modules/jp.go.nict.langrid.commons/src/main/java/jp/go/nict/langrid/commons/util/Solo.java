/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.commons.util;

/**
 * 
 * 
 * @author Takao nakaguchi
 */
public class Solo<T> {
	/**
	 * 
	 * 
	 */
	public Solo(T first){
		this.first = first;
	}

	/**
	 * 
	 * 
	 */
	public T getFirst(){
		return first;
	}

	/**
	 * 
	 * 
	 */
	public boolean equals(Solo<?> value){
		if(value == null) return false;
		if(first == null){
			if(value.getFirst() != null) return false;
		} else{
			if(!first.equals(value.getFirst())) return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object value){
		if(value == null) return false;
		if(!getClass().equals(value.getClass())) return false;
		return equals((Solo<?>)value);
	}

	@Override
	public int hashCode() {
		int h = 0;
		h = (first != null) ? first.hashCode() : 0;
		return h;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("[");
		b.append(getClass().getName());
		b.append(" ");
		b.append("first:");
		b.append(first);
		b.append("]");
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static <T> Solo<T> create(T v1){
		return new Solo<T>(v1);
	}

	private T first;
}
