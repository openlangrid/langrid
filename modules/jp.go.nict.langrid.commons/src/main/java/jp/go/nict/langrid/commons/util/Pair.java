/*
 * $Id: Pair.java 625 2013-02-12 05:56:27Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.util;

import java.io.Serializable;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 625 $
 */
public class Pair<T, U>
implements Serializable{
	/**
	 * 
	 * 
	 */
	public Pair(T first, U second){
		this.first = first;
		this.second = second;
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
	public U getSecond(){
		return second;
	}

	@Override
	public boolean equals(Object value){
		if(value == null) return false;
		if(!getClass().equals(value.getClass())) return false;
		return equals((Pair<?, ?>)value);
	}

	/**
	 * 
	 * 
	 */
	public boolean equals(Pair<?, ?> value){
		if(value == null) return false;
		if(first == null){
			if(value.getFirst() != null) return false;
		} else{
			if(!first.equals(value.getFirst())) return false;
		}
		if(second == null){
			if(value.getSecond() != null) return false;
		} else{
			if(!second.equals(value.getSecond())) return false;
		}
		return first.equals(value.first) && second.equals(value.second);
	}

	@Override
	public int hashCode() {
		int h = 0;
		h = (first != null) ? first.hashCode() : 0;
		h = h * 31 + ((second != null) ? second.hashCode() : 0);
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
		b.append(",second:");
		b.append(second);
		b.append("]");
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> Pair<T, U> create(T v1, U v2){
		return new Pair<T,U>(v1, v2);
	}

	private T first;
	private U second;
	private static final long serialVersionUID = -5082011802943076616L;
}
