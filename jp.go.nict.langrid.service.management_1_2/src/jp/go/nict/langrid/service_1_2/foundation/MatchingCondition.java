/*
 * $Id: MatchingCondition.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores search conditions.
 * Stores conditions about the attributes and fields of the target data for search.
 * In the matchingMethod,we store the enumerated value
 * {@link jp.go.nict.langrid.service_1_2.typed.MatchingMethod MatchingMethod} in a string.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class MatchingCondition
implements Serializable
{
	/**
	 * 
	 * Default constructor.
	 * 
	 */
	public MatchingCondition(){}

	/**
	 * 
	 * Constructor.
	 * @param fieldName Field (attribute) name
	 * @param matchingValue The value to be matched
	 * @param matchingMethod Matching method
	 * 
	 */
	public MatchingCondition(
			String fieldName
			, String matchingValue
			, String matchingMethod){
		this.fieldName = fieldName;
		this.matchingValue = matchingValue;
		this.matchingMethod = matchingMethod;
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
	 * Gets field (attribute) name.
	 * @return Field (attribute) name
	 * 
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 
	 * Sets field (attribute) name.
	 * @param attributeName Field (attribute) name
	 * 
	 */
	public void setFieldName(String attributeName){
		this.fieldName = attributeName;
	}

	/**
	 * 
	 * Gets value to run matching on.
	 * @return Value to be matched
	 * 
	 */
	public String getMatchingValue() {
		return matchingValue;
	}

	/**
	 * 
	 * Sets value to run matching on.
	 * @param matchingValue The value to be matched
	 * 
	 */
	public void setMatchingValue(String matchingValue){
		this.matchingValue = matchingValue;
	}

	/**
	 * 
	 * Gets matching method.
	 * @return Matching method
	 * 
	 */
	public String getMatchingMethod() {
		return matchingMethod;
	}

	/**
	 * 
	 * Sets matching method.
	 * @param matchingMethod Matching method
	 * 
	 */
	public void setMatchingMethod(String matchingMethod){
		this.matchingMethod = matchingMethod;
	}

	private String fieldName;
	private String matchingValue;
	private String matchingMethod;

	private static final long serialVersionUID = -7639201265294110549L;
}
