/*
 * $Id: Concept.java 637 2013-02-19 03:44:41Z t-nakaguchi $
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
 * Stores concept data.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 637 $
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/conceptdictionary/")
public class Concept
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public Concept(){
	}

	/**
	 * 
	 * Constructor.
	 * @param conceptId Concept ID
	 * @param partOfSpeech Part of speech of synset
	 * @param synset Set of synonyms that is a concept headword
	 * @param glosses Concept explanation
	 * @param relation Relation related with another concept
	 * 
	 */
	public Concept(
			String conceptId, String partOfSpeech, Lemma[] synset, Gloss[] glosses, String[] relation){
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
	 * Gets concept ID.
	 * @return Concept ID
	 * 
	 */
	public String getConceptId(){
		return conceptId;
	}

	/**
	 * 
	 * Gets the concept's explanation text.
	 * @return Explanation of the concept
	 * 
	 */
	public Gloss[] getGlosses(){
		return glosses;
	}

	/**
	 * 
	 * Gets the part of speech of synset.
	 * @return Part of speech of synset
	 * 
	 */
	public String getPartOfSpeech(){
		return partOfSpeech;
	}

	/**
	 * 
	 * Gets relations associated with other concepts.
	 * @return Relations associated with other concepts
	 * 
	 */
	public String[] getRelations(){
		return relations;
	}

	/**
	 * 
	 * Gets the set of synonyms that are to be the concept headword(s).
	 * @return Set of synonyms
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
	 * Sets concept ID.
	 * @param conceptId Concept ID
	 * 
	 */
	public void setConceptId(String conceptId){
		this.conceptId = conceptId;
	}

	/**
	 * 
	 * Sets the concept's explanation text.
	 * @param glosses Concept explanation
	 * 
	 */
	public void setGlosses(Gloss[] glosses){
		this.glosses = glosses;
	}

	/**
	 * 
	 * Sets the part of speech of synset.
	 * @param partOfSpeech Part of speech of synset
	 * 
	 */
	public void setPartOfSpeech(String partOfSpeech){
		this.partOfSpeech = partOfSpeech;
	}

	/**
	 * 
	 * Sets relations associated with other concepts.
	 * @param relations Relations related with another concept
	 * 
	 */
	public void setRelations(String[] relations){
		this.relations = relations;
	}

	/**
	 * 
	 * Sets the set of synonyms that are to be the concept headword(s).
	 * @param synset Set of synonyms
	 * 
	 */
	public void setSynset(Lemma[] synset){
		this.synset = synset;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	@Field(order=1)
	private String conceptId;
	@Field(order=2)
	private Gloss[] glosses;
	@Field(order=3)
	private String partOfSpeech;
	@Field(order=4)
	private String[] relations;
	@Field(order=5)
	private Lemma[] synset;
	private static final long serialVersionUID = -6841725945823851682L;
}
