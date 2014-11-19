/*
 * $Id: SourceAndMorphemesAndCodes.java 749 2013-03-29 02:28:04Z t-nakaguchi $
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

package jp.go.nict.langrid.service_1_2.workflowsupport;

import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 749 $
 */
public class SourceAndMorphemesAndCodes {
	/**
	 * 
	 * 
	 */
	public SourceAndMorphemesAndCodes(String source, Morpheme[] morphemes, String[] codes, String[] targetWords) {
		this.source = source;
		this.morphemes = morphemes;
		this.codes = codes;
		this.targetWords = targetWords;
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
	public String getSource() {
		return source;
	}

	/**
	 * 
	 * 
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 
	 * 
	 */
	public Morpheme[] getMorphemes() {
		return morphemes;
	}

	/**
	 * 
	 * 
	 */
	public void setMorphemes(Morpheme[] morphemes) {
		this.morphemes = morphemes;
	}

	/**
	 * 
	 * 
	 */
	public String[] getCodes() {
		return codes;
	}

	/**
	 * 
	 * 
	 */
	public void setCodes(String[] codes) {
		this.codes = codes;
	}

	/**
	 * 
	 * 
	 */
	public String[] getTargetWords() {
		return targetWords;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetWords(String[] targetWords) {
		this.targetWords = targetWords;
	}
	
	/**
	 * 
	 * 
	 */
	private String source;
	/**
	 * 
	 * 
	 */
	private Morpheme[] morphemes;
	/**
	 * 
	 * 
	 */
	private String[] codes;
	/**
	 * 
	 * 
	 */
	private String[] targetWords;
}
