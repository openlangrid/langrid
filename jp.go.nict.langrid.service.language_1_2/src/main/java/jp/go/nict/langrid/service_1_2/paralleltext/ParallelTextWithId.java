/*
 * $Id: ParallelTextWithId.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.paralleltext;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.service_1_2.Category;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class ParallelTextWithId
extends ParallelText
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ParallelTextWithId(){}

	/**
	 * 
	 * 
	 */
	public ParallelTextWithId(
			String parallelTextId, String source, String target
			, Category[] categories)
	{
		super(source, target);
		this.paralleltTextId = parallelTextId;
		this.categories = categories;
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
	public String getParalleltTextId(){
		return paralleltTextId;
	}

	/**
	 * 
	 * 
	 */
	public void setParalleltTextId(String paralleltTextId){
		this.paralleltTextId = paralleltTextId;
	}

	/**
	 * 
	 * 
	 */
	public Category[] getCategories() {
		return categories;
	}

	/**
	 * 
	 * 
	 */
	public void setCategories(Category[] categories) {
		this.categories = categories;
	}

	@Field(order=1)
	private String paralleltTextId;
	@Field(order=2)
	private Category[] categories;

	private static final long serialVersionUID = -8067893682604978630L;
}
