/*
 * $Id: TermEntrySearchCondition.java 587 2012-10-19 06:41:14Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or (at 
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
package jp.go.nict.langrid.service_1_2.bilingualdictionary;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.commons.rpc.intf.Schema;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 587 $
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/")
public class TermEntrySearchCondition
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public TermEntrySearchCondition(){}

	/**
	 * 
	 * 
	 */
	public TermEntrySearchCondition(
			String language
			, String text
			, String matchingMethod){
		this.language = language;
		this.text = text;
		this.matchingMethod = matchingMethod;
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
	 * 
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * 
	 */
	public void setLanguage(String language){
		this.language = language;
	}

	/**
	 * 
	 * 
	 */
	public String getText() {
		return text;
	}

	/**
	 * 
	 * 
	 */
	public void setText(String text){
		this.text = text;
	}

	/**
	 * 
	 * 
	 */
	public String getMatchingMethod() {
		return matchingMethod;
	}

	/**
	 * 
	 * 
	 */
	public void setMatchingMethod(String matchingMethod){
		this.matchingMethod = matchingMethod;
	}

	@Field(order=1)
	private String language;
	@Field(order=2)
	private String text;
	@Field(order=3)
	private String matchingMethod;

	private static final long serialVersionUID = 6377421398152007009L;
}
