/*
 * $Id: Order.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
 * Stores the sort order.
 * The direction is, {@link jp.go.nict.langrid.service_1_2.foundation.typed.OrderDirection OrderDirection}
 * Stores the enumerated value as a string.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class Order
implements Serializable
{
	/**
	 * 
	 * Default constructor.
	 * 
	 */
	public Order(){}

	/**
	 * 
	 * Constructor.
	 * @param fieldName Field to be sorted
	 * @param direction Sort direction ("ASCENDANT" or "DESCENDANT")
	 * 
	 */
	public Order(String fieldName, String direction) {
		this.fieldName = fieldName;
		this.direction = direction;
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
	 * Returns a field.
	 * 
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 
	 * Sets a field.
	 * 
	 */
	public void setFieldName(String field) {
		this.fieldName = field;
	}

	/**
	 * 
	 * Returns direction.
	 * 
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * 
	 * Sets direction.
	 * "ASCENDANT"(ascending order) and also "DESCENDANT"(descending order)
	 * 
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	private String fieldName;
	private String direction;

	private static final long serialVersionUID = 1659338680589092500L;
}
