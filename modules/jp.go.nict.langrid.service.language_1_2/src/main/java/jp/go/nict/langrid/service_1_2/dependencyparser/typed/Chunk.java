/*
 * $Id: Chunk.java 224 2010-10-03 00:17:47Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.dependencyparser.typed;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.morphologicalanalysis.typed.Morpheme;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public class Chunk
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public Chunk(){
	}

	/**
	 * 
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
	 * 
	 */
	public String getChunkId(){
		return chunkId;
	}

	/**
	 * 
	 * 
	 */
	public Dependency getDependency(){
		return dependency;
	}

	/**
	 * 
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
	 * 
	 */
	public void setChunkId(String chunkId){
		this.chunkId = chunkId;
	}

	/**
	 * 
	 * 
	 */
	public void setDependency(Dependency dependency){
		this.dependency = dependency;
	}

	/**
	 * 
	 * 
	 */
	public void setMorphemes(Morpheme[] morphemes){
		this.morphemes = morphemes;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	private String chunkId;
	private Dependency dependency;
	private Morpheme[] morphemes;
	private static final long serialVersionUID = 170409772955980915L;
}
