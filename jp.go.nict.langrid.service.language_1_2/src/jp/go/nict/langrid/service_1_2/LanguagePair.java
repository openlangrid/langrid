/*
 * $Id: LanguagePair.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the language pair.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class LanguagePair
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public LanguagePair(){
	}

	/**
	 * 
	 * Constructor.
	 * @param first First language
	 * @param second Second language
	 * 
	 */
	public LanguagePair(String first, String second){
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 
	 * Returns the first language.
	 * @return First language
	 * 
	 */
	public String getFirst() {
		return first;
	}

	/**
	 * 
	 * Sets the first language.
	 * @param first First language
	 * 
	 */
	public void setFirst(String first) {
		this.first = first;
	}

	/**
	 * 
	 * Returns the second language.
	 * @return Second language
	 * 
	 */
	public String getSecond() {
		return second;
	}

	/**
	 * 
	 * Sets the second language.
	 * @param second Second language
	 * 
	 */
	public void setSecond(String second) {
		this.second = second;
	}

	@Field(order=1)
	private String first;
	@Field(order=2)
	private String second;

	private static final long serialVersionUID = -7903044880382046782L;
}
