/*
 * $Id: LemmaNode.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.dictionary;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class LemmaNode
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public LemmaNode(){
	}

	/**
	 * 
	 * 
	 */
	public LemmaNode(
		String nodeId, String language, String headWord, String pronounciation
		, String partOfSpeech
		, String domain, String[] conceptNodes, String[] relations
		)
	{
		this.nodeId = nodeId;
		this.language = language;
		this.headWord = headWord;
		this.pronounciation = pronounciation;
		this.partOfSpeech = partOfSpeech;
		this.domain = domain;
		setConceptNodes(conceptNodes);
		setRelations(relations);
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
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * 
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 
	 * 
	 */
	public String getLanguage(){
		return language;
	}
	
	/**
	 * 
	 * 
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 
	 * 
	 */
	public String getHeadWord() {
		return headWord;
	}

	/**
	 * 
	 * 
	 */
	public void setHeadWord(String headWord) {
		this.headWord = headWord;
	}

	/**
	 * 
	 * 
	 */
	public String getPronounciation() {
		return pronounciation;
	}

	/**
	 * 
	 * 
	 */
	public void setPronounciation(String pronounciation) {
		this.pronounciation = pronounciation;
	}

	/**
	 * 
	 * 
	 */
	public String getPartOfSpeech() {
		return partOfSpeech;
	}

	/**
	 * 
	 * 
	 */
	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}

	/**
	 * 
	 * 
	 */
	public String getDomain(){
		return domain;
	}

	/**
	 * 
	 * 
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * 
	 * 
	 */
	public String[] getConceptNodes(){
		return conceptNodes;
	}

	/**
	 * 
	 * 
	 */
	public void setConceptNodes(String[] conceptNodes){
		this.conceptNodes = getClonedElements(conceptNodes);
	}

	/**
	 * 
	 * 
	 */
	public String[] getRelations(){
		return relations;
	}

	/**
	 * 
	 * 
	 */
	public void setRelations(String[] relations){
		this.relations = getClonedElements(relations);
	}

	/**
	 * 
	 * 
	 */
	private static <T> T[] getClonedElements(T[] values){
		if(values != null){
			return values.clone();
		} else{
			return null;
		}
	}

	@Field(order=1)
	private String nodeId;
	@Field(order=2)
	private String language;
	@Field(order=3)
	private String headWord;
	@Field(order=4)
	private String pronounciation;
	@Field(order=5)
	private String partOfSpeech;
	@Field(order=6)
	private String domain;
	@Field(order=7)
	private String[] conceptNodes;
	@Field(order=8)
	private String[] relations;

	private static final long serialVersionUID = -3669443640215621141L;
}
