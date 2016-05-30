/*
 * $Id: ConceptNode.java 749 2013-03-29 02:28:04Z t-nakaguchi $
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
import jp.go.nict.langrid.commons.util.ArrayUtil;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 749 $
 */
public class ConceptNode
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public ConceptNode(){
	}

	/**
	 * 
	 * 
	 */
	public ConceptNode(
		String nodeId, String gloss, String[] synset, String[] usageExamples
		, String[] relations)
	{
		this.nodeId = nodeId;
		this.gloss = gloss;
		setSynset(synset);
		setUsageExamples(usageExamples);
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
	public String getGloss() {
		return gloss;
	}

	/**
	 * 
	 * 
	 */
	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	/**
	 * 
	 * 
	 */
	public String[] getSynset() {
		return synset;
	}

	/**
	 * 
	 * 
	 */
	public void setSynset(String[] synset) {
		this.synset = ArrayUtil.clone(synset);
	}

	/**
	 * 
	 * 
	 */
	public String[] getUsageExamples() {
		return usageExamples;
	}

	/**
	 * 
	 * 
	 */
	public void setUsageExamples(String[] usageExamples) {
		this.usageExamples = ArrayUtil.clone(usageExamples);
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
		this.relations = ArrayUtil.clone(relations);
	}

	@Field(order=1)
	private String nodeId;
	@Field(order=2)
	private String gloss;
	@Field(order=3)
	private String[] synset;
	@Field(order=4)
	private String[] usageExamples;
	@Field(order=5)
	private String[] relations;

	private static final long serialVersionUID = 8825743482004376359L;
}
