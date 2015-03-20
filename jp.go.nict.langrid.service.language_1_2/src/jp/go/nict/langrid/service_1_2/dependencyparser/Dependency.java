/*
 * $Id: Dependency.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the dependency relationship between words (chunks).
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class Dependency 
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public Dependency(){
	}

	/**
	 * 
	 * Constructor.
	 * @param dependencyLabel Dependency label
	 * @param headChunkId Modifee word (chunk) ID
	 * 
	 */
	public Dependency(String dependencyLabel, String headChunkId){
		this.label = dependencyLabel;
		this.headChunkId = headChunkId;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	/**
	 * 
	 * Get modifee word (chunk) ID.
	 * @return Modifee word (chunk) ID
	 * 
	 */
	public String getHeadChunkId(){
		return headChunkId;
	}

	/**
	 * 
	 * Gets the dependency label.
	 * @return Label of the dependent
	 * 
	 */
	public String getLabel(){
		return label;
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * 
	 * Set modifee word (chunk) ID.
	 * @param headChunkId Modifee word (chunk) ID
	 * 
	 */
	public void setHeadChunkId(String headChunkId){
		this.headChunkId = headChunkId;
	}

	/**
	 * 
	 * Sets the dependency label.
	 * @param label Label of the dependent
	 * 
	 */
	public void setLabel(String label){
		this.label = label;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	@Field(order=1)
	private String headChunkId;
	@Field(order=2)
	private String label;
	private static final long serialVersionUID = 1950381153048212880L;
}
