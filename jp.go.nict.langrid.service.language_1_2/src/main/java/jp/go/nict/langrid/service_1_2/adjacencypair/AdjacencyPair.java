/*
 * $Id: AdjacencyPair.java 587 2012-10-19 06:41:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.adjacencypair;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.commons.rpc.intf.Schema;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the adjacency pair.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 587 $
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/adjacencypair/")
public class AdjacencyPair
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AdjacencyPair(){}

	/**
	 * 
	 * Constructor.
	 * @param category Category
	 * @param firstTurn Utterance
	 * @param secondTurns Array of responses to utterances
	 * 
	 */
	public AdjacencyPair(
			String category, String firstTurn, String[] secondTurns){
		this.category = category;
		this.firstTurn = firstTurn;
		this.secondTurns = secondTurns;
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
	 * Returns the category.
	 * @return Category
	 * 
	 */
	public String getCategory(){
		return category;
	}

	/**
	 * 
	 * Sets the category.
	 * @param category Category
	 * 
	 */
	public void setCategory(String category){
		this.category = category;
	}
	
	/**
	 * 
	 * Returns the utterance.
	 * @return Utterance
	 * 
	 */
	public String getFirstTurn(){
		return firstTurn;
	}

	/**
	 * 
	 * Sets the utterance.
	 * @param firstTurn Utterance
	 * 
	 */
	public void setFirstTurn(String firstTurn){
		this.firstTurn = firstTurn;
	}
	
	/**
	 * 
	 * Returns an array of responses to an utterance.
	 * @return Array of responses to utterance
	 * 
	 */
	public String[] getSecondTurns(){
		return secondTurns;
	}

	/**
	 * 
	 * It sets the response array.
	 * @param secondTurns Array of responses
	 * 
	 */
	public void setSecondTurns(String[] secondTurns){
		this.secondTurns = secondTurns;
	}

	@Field(order=1)
	private String category;
	@Field(order=2)
	private String firstTurn;
	@Field(order=3)
	private String[] secondTurns;

	private static final long serialVersionUID = 532746356852693123L;
}
