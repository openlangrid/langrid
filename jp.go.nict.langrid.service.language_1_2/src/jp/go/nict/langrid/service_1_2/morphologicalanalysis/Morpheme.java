/*
 * $Id: Morpheme.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.morphologicalanalysis;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores morphemes.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class Morpheme
implements Serializable
{
	/**
	 * 
	 * Default constructor.
	 * 
	 */
	public Morpheme(){
	}

	/**
	 * 
	 * Constructor.
	 * @param word String
	 * @param lemma Lemma (canonical form of the word)
	 * @param partOfSpeech Part of speech(one of either "noun.proper","noun.common","noun.other","noun","verb","adjective","adverb","other","unknown")
	 * 
	 */
	public Morpheme(String word, String lemma, String partOfSpeech){
		this.word = word;
		this.lemma = lemma;
		this.partOfSpeech = partOfSpeech;
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
	 * Returns the term.
	 * @return Term
	 * 
	 */
	public String getWord(){
		return this.word;
	}

	/**
	 * 
	 * Sets the term.
	 * @param word Word
	 * 
	 */
	public void setWord(String word){
		this.word = word;
	}

	/**
	 * 
	 * Returns the lemma (canonical form).
	 * @return Lemma (canonical form of a word)
	 * 
	 */
	public String getLemma(){
		return this.lemma;
	}

	/**
	 * 
	 * Sets the lemma (canonical form).
	 * @param lemma Lemma (canonical form of the word)
	 * 
	 */
	public void setLemma(String lemma){
		this.lemma = lemma;
	}
	
	/**
	 * 
	 * Returns part of speech.
	 * @return Part of speech(one of either "noun.proper","noun.common","noun.other","noun","verb","adjective","adverb","other","unknown")
	 * 
	 */
	public String getPartOfSpeech(){
		return this.partOfSpeech;
	}

	/**
	 * 
	 * Sets part of speech.
	 * @param partOfSpeech Part of speech
	 * 
	 */
	public void setPartOfSpeech(String partOfSpeech){
		this.partOfSpeech = partOfSpeech;
	}

	@Field(order=1)
	private String word;
	@Field(order=2)
	private String lemma;
	@Field(order=3)
	private String partOfSpeech;

	private static final long serialVersionUID = 2315149539376257576L;
}
