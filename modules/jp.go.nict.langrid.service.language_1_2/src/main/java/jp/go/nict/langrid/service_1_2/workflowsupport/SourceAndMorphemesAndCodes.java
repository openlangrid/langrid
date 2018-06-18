/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 * Copyright Language Grid Project.
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

import java.util.Collection;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

/**
 * @author Jun Koyama
 * @author Takao Nakaguchi
 */
public class SourceAndMorphemesAndCodes {
	/**
	 * 
	 * 
	 */
	public SourceAndMorphemesAndCodes(String source, Morpheme[] morphemes, String[] codes, String[] headWords, String[] targetWords) {
		this.source = source;
		this.morphemes = morphemes;
		this.codes = codes;
		this.headWords = headWords;
		this.targetWords = targetWords;
	}

	public SourceAndMorphemesAndCodes(String source, Collection<Morpheme> morphemes, Collection<String> codes,
			Collection<String> headWords, Collection<String> targetWords) {
		this.source = source;
		this.morphemes = morphemes.toArray(new Morpheme[]{});
		this.codes = codes.toArray(new String[]{});
		this.headWords = headWords.toArray(new String[]{});
		this.targetWords = targetWords.toArray(new String[]{});
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Morpheme[] getMorphemes() {
		return morphemes;
	}

	public void setMorphemes(Morpheme[] morphemes) {
		this.morphemes = morphemes;
	}

	public String[] getCodes() {
		return codes;
	}

	public void setCodes(String[] codes) {
		this.codes = codes;
	}

	public String[] getHeadWords() {
		return headWords;
	}

	public void setHeadWords(String[] headWords) {
		this.headWords = headWords;
	}

	public String[] getTargetWords() {
		return targetWords;
	}

	public void setTargetWords(String[] targetWords) {
		this.targetWords = targetWords;
	}
	
	private String source;
	private Morpheme[] morphemes;
	private String[] codes;
	private String[] headWords;
	private String[] targetWords;
}
