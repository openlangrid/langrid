/*
 * $Id: Concept.java 224 2010-10-03 00:17:47Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.conceptdictionary.typed;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public class Concept
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public Concept(){
	}

	/**
	 * 
	 * 
	 */
	public Concept(
			String conceptId, PartOfSpeech partOfSpeech, Lemma[] synset, Gloss[] glosses
			, ConceptualRelation[] relation){
		this.conceptId = conceptId;
		this.partOfSpeech = partOfSpeech;
		this.synset = synset;
		this.glosses = glosses;
		this.relations = relation;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	/**
	 * 
	 * 
	 */
	public String getConceptId(){
		return conceptId;
	}

	/**
	 * 
	 * 
	 */
	public Gloss[] getGlosses(){
		return glosses;
	}

	/**
	 * 
	 * 
	 */
	public PartOfSpeech getPartOfSpeech(){
		return partOfSpeech;
	}

	/**
	 * 
	 * 
	 */
	public ConceptualRelation[] getRelations(){
		return relations;
	}

	/**
	 * 
	 * 
	 */
	public Lemma[] getSynset(){
		return synset;
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * 
	 * 
	 */
	public void setConceptId(String conceptId){
		this.conceptId = conceptId;
	}

	/**
	 * 
	 * 
	 */
	public void setGlosses(Gloss[] glosses){
		this.glosses = glosses;
	}

	/**
	 * 
	 * 
	 */
	public void setPartOfSpeech(PartOfSpeech partOfSpeech){
		this.partOfSpeech = partOfSpeech;
	}

	/**
	 * 
	 * 
	 */
	public void setRelations(ConceptualRelation[] relations){
		this.relations = relations;
	}

	/**
	 * 
	 * 
	 */
	public void setSynset(Lemma[] synset){
		this.synset = synset;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	private String conceptId;
	private Gloss[] glosses;
	private PartOfSpeech partOfSpeech;
	private ConceptualRelation[] relations;
	private Lemma[] synset;
	private static final long serialVersionUID = -2955653929496278378L;
}
