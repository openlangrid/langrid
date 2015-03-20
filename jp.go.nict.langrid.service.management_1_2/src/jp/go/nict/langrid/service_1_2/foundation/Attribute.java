/*
 * $Id: Attribute.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation;

import java.io.Serializable;

/**
 * 
 * Stores attribute(s).
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class Attribute
implements Serializable
{
	/**
	 * 
	 * Default constructor.
	 * 
	 */
	public Attribute(){}

	/**
	 * 
	 * Constructor.
	 * @param name Attribute name
	 * @param value Attribute value
	 * 
	 */
	public Attribute(String name, String value){
		this.name = name;
		this.value = value;
	}

	/**
	 * 
	 * Gets the attribute name.
	 * @return Attribute name
	 * 
	 */
	public String getName(){
		return name;
	}

	/**
	 * 
	 * Sets the attribute name.
	 * @param name Attribute name
	 * 
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 
	 * Gets the attribute value.
	 * @return Attribute value
	 * 
	 */
	public String getValue(){
		return value;
	}

	/**
	 * 
	 * Sets the attribute value.
	 * @param value Attribute value
	 * 
	 */
	public void setValue(String value){
		this.value = value;
	}

	@Override
	public boolean equals(Object value){
		if(!(value instanceof Attribute)) return false;
		return equals((Attribute)value);
	}

	/**
	 * 
	 * Judges whether the object is the same or not.
	 * @param value Object
	 * @return true when the same
	 * 
	 */
	public boolean equals(Attribute value){
		if(!this.name.equals(value.name)) return false;
		if(!this.value.equals(value.value)) return false;
		return true;
	}

	@Override
	public int hashCode(){
		int h = 0;
		h = h * 31 + name.hashCode();
		h = h * 31 + value.hashCode();
		return h;
	}

	@Override
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append(name);
		b.append(":");
		b.append(value);
		return b.toString();
	}

	private String name;
	private String value;

	private static final long serialVersionUID = -3034129712562054707L;
}
