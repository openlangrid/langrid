/*
 * $Id: Translation.java 749 2013-03-29 02:28:04Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.bilingualdictionary;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.commons.rpc.intf.Schema;
import jp.go.nict.langrid.commons.util.ArrayUtil;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the bilingual translation.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 749 $
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/")
public class Translation
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public Translation(){}

	/**
	 * 
	 * Constructor.
	 * @param headWord Heading
	 * @param targetWords Array of bilingual translation
	 * 
	 */
	public Translation(String headWord, String[] targetWords){
		this.headWord = headWord;
		this.targetWords = ArrayUtil.clone(targetWords);
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
	 * Gets headword.
	 * @return Headword
	 * 
	 */
	public String getHeadWord() {
		return headWord;
	}

	/**
	 * 
	 * Sets headword.
	 * @param headWord Heading
	 * 
	 */
	public void setHeadWord(String headWord){
		this.headWord = headWord;
	}

	/**
	 * 
	 * Gets the array of the bilingual translation.
	 * @return Array of bilingual translation
	 * 
	 */
	public String[] getTargetWords() {
		return targetWords;
	}

	/**
	 * 
	 * Sets the array of the bilingual translation.
	 * @param targetWords Array of bilingual translation
	 * 
	 */
	public void setTargetWords(String[] targetWords){
		this.targetWords = targetWords;
	}

	@Field(order=1)
	private String headWord;
	@Field(order=2)
	private String[] targetWords;

	private static final long serialVersionUID = 490106616477811064L;
}
