/*
 * $Id: Quartet.java 182 2010-10-02 03:16:36Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2008 NICT Language Grid Project.
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
 * @version $Revision: 182 $
 */
public class Quartet <T, U, V, W>
implements Serializable{
	/**
	 * 
	 * 
	 */
	public Quartet(T first, U second, V third, W fourth){
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
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

	/**
	 * 
	 * 
	 */
	public V getThird(){
		return third;
	}

	/**
	 * 
	 * 
	 */
	public W getFourth(){
		return fourth;
	}

	@Override
	public boolean equals(Object value){
		if(value == null) return false;
		if(!getClass().equals(value.getClass())) return false;
		return equals((Quartet<?, ?, ?, ?>)value);
	}

	/**
	 * 
	 * 
	 */
	public boolean equals(Quartet<?, ?, ?, ?> value){
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
		if(third == null){
			if(value.getThird() != null) return false;
		} else{
			if(!third.equals(value.getThird())) return false;
		}
		if(fourth == null){
			if(value.getFourth() != null) return false;
		} else{
			if(!fourth.equals(value.getFourth())) return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int h = 0;
		h = (first != null) ? first.hashCode() : 0;
		h = h * 31 + ((second != null) ? second.hashCode() : 0);
		h = h * 31 + ((third != null) ? third.hashCode() : 0);
		h = h * 31 + ((fourth != null) ? fourth.hashCode() : 0);
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
		b.append(",third:");
		b.append(third);
		b.append(",fourth:");
		b.append(fourth);
		b.append("]");
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static <T, U, V, W> Quartet<T, U, V, W>
		create(T v1, U v2, V v3, W v4)
	{
		return new Quartet<T, U, V, W>(v1, v2, v3, v4);
	}

	private T first;
	private U second;
	private V third;
	private W fourth;
	private static final long serialVersionUID = 3119027305218242983L;
}
