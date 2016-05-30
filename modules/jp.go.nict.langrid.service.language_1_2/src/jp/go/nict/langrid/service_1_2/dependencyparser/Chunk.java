/*
 * $Id: Chunk.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.dependencyparser;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the sentence which is the smallest unit of the dependency analysis results, and the dependency relations data.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class Chunk
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public Chunk(){
	}

	/**
	 * 
	 * Constructor.
	 * @param chunkId Word (chunk) ID
	 * @param morphemes Array of morphemes constituting a word (chunk)
	 * @param dependency Dependency relationship
	 * 
	 */
	public Chunk(String chunkId, Morpheme[] morphemes, Dependency dependency){
		this.chunkId = chunkId;
		this.morphemes = morphemes;
		this.dependency = dependency;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	/**
	 * 
	 * Gets word (chunk) ID.
	 * @return Word (chunk)ID
	 * 
	 */
	public String getChunkId(){
		return chunkId;
	}

	/**
	 * 
	 * Get dependency relationship.
	 * @return Dependency relationship
	 * 
	 */
	public Dependency getDependency(){
		return dependency;
	}

	/**
	 * 
	 * Gets the array of the morphemes making up the word (chunk).
	 * @return Array of morphemes constituting the word (chunk)
	 * 
	 */
	public Morpheme[] getMorphemes(){
		return morphemes;
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * 
	 * Sets word (chunk) ID.
	 * @param chunkId Word (chunk) ID
	 * 
	 */
	public void setChunkId(String chunkId){
		this.chunkId = chunkId;
	}

	/**
	 * 
	 * Set dependency relationship.
	 * @param dependency Dependency relationship
	 * 
	 */
	public void setDependency(Dependency dependency){
		this.dependency = dependency;
	}

	/**
	 * 
	 * Sets the array of the morphemes making up the word (chunk).
	 * 
	 */
	public void setMorphemes(Morpheme[] morphemes){
		this.morphemes = morphemes;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	@Field(order=1)
	private String chunkId;
	@Field(order=2)
	private Dependency dependency;
	@Field(order=3)
	private Morpheme[] morphemes;
	private static final long serialVersionUID = 496245190905034049L;
}
