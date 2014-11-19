/*
 * $Id: TranslationWithPosition.java 587 2012-10-19 06:41:14Z t-nakaguchi $
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the bilingual translation and/or the equivalent morphemes and starting index of the morpheme array.
 * 
 * @author Takao Nakaguchi
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 587 $
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/")
public class TranslationWithPosition implements Serializable{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public TranslationWithPosition(){}
	
	/**
	 * 
	 * Constructor.
	 * @param translation Bilingual translation
	 * @param startIndex Starting index of array of matching morphemes in bilingual translation
	 * @param numberOfMorphemes Number of morphemes matching in the bilingual translation
	 * 
	 */
	public TranslationWithPosition(Translation translation, int startIndex,
			int numberOfMorphemes){
		this.translation = translation;
		this.startIndex = startIndex;
		this.numberOfMorphemes = numberOfMorphemes;
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
	 * Returns numberOfMorphemes.
	 * 
	 */
	public int getNumberOfMorphemes(){
		return numberOfMorphemes;
	}

	/**
	 * 
	 * Returns startIndex.
	 * 
	 */
	public int getStartIndex(){
		return startIndex;
	}

	/**
	 * 
	 * Returns translation.
	 * 
	 */
	public Translation getTranslation(){
		return translation;
	}

	/**
	 * 
	 * Sets numberOfMorphemes.
	 * 
	 */
	public void setNumberOfMorphemes(int numberOfMorphemes){
		this.numberOfMorphemes = numberOfMorphemes;
	}

	/**
	 * 
	 * Sets startIndex.
	 * 
	 */
	public void setStartIndex(int startIndex){
		this.startIndex = startIndex;
	}

	/**
	 * 
	 * Sets translation.
	 * 
	 */
	public void setTranslation(Translation translation){
		this.translation = translation;
	}

	@Field(order=1)
	private int numberOfMorphemes;
	@Field(order=2)
	private int startIndex;
	@Field(order=3)
	private Translation translation;
	
	private static final long serialVersionUID = 7185428571175751639L;
}
