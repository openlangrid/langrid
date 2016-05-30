/*
 * $Id: MatchingCondition.java 388 2011-08-23 10:24:50Z t-nakaguchi $
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
package jp.go.nict.langrid.dao;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 388 $
 */
public class MatchingCondition implements Serializable{
	/**
	 * 
	 * 
	 */
	public MatchingCondition(){
	}

	/**
	 * 
	 * 
	 */
	public MatchingCondition(
			String fieldName
			, Object matchingValue
			, MatchingMethod matchingMethod
			)
	{
		this.fieldName = fieldName;
		this.matchingValue = matchingValue;
		this.matchingMethod = matchingMethod;
	}

	/**
	 * 
	 * 
	 */
	public MatchingCondition(
			String fieldName
			, Object matchingValue
			)
	{
		this.fieldName = fieldName;
		this.matchingValue = matchingValue;
		this.matchingMethod = MatchingMethod.COMPLETE;
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
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 
	 * 
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 
	 * 
	 */
	public Object getMatchingValue() {
		return matchingValue;
	}

	/**
	 * 
	 * 
	 */
	public void setMatchingValue(Object matchingValue) {
		this.matchingValue = matchingValue;
	}

	/**
	 * 
	 * 
	 */
	public MatchingMethod getMatchingMethod() {
		return matchingMethod;
	}

	/**
	 * 
	 * 
	 */
	public void setMatchingMethod(MatchingMethod matchingMethod) {
		this.matchingMethod = matchingMethod;
	}

	private String fieldName;
	private Object matchingValue;
	private MatchingMethod matchingMethod;

	private static final long serialVersionUID = -6774938926873453289L;
}
