/*
 * $Id: Gloss.java 637 2013-02-19 03:44:41Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.conceptdictionary;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.commons.rpc.intf.Schema;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores an explanation of the concept, more detailed than the concept headword, and the language in which it is expressed.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 637 $
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/conceptdictionary/")
public class Gloss
implements Serializable
{
	/**
	 * 
	 * Constructor
	 * 
	 */
	public Gloss(){
	}

	/**
	 * 
	 * Constructor
	 * @param glossText Concept explanation
	 * @param language Language of glossText
	 * 
	 */
	public Gloss(String glossText, String language){
		this.glossText = glossText;
		this.language = language;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	/**
	 * 
	 * Gets the concept's explanation.
	 * @return Concept and explanation
	 * 
	 */
	public String getGlossText(){
		return glossText;
	}

	/**
	 * 
	 * Gets the language of glossText.
	 * @return Language of glossText
	 * 
	 */
	public String getLanguage(){
		return language;
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * 
	 * Sets the concept's explanation.
	 * @param glossText Concept explanation
	 * 
	 */
	public void setGlossText(String glossText){
		this.glossText = glossText;
	}

	/**
	 * 
	 * Sets the language of glossText.
	 * 
	 */
	public void setLanguage(String language){
		this.language = language;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	@Field(order=1)
	private String glossText;
	@Field(order=2)
	private String language;
	private static final long serialVersionUID = 3139630179953698263L;
}
