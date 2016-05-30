/*
 * $Id: BackTranslationResult.java 587 2012-10-19 06:41:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.backtranslation;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.commons.rpc.intf.Schema;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the results of the back translation
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 587 $
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/backtranslation/")
public class BackTranslationResult
implements Serializable
{
	/**
	 * 
	 * Default constructor.
	 * 
	 */
	public BackTranslationResult(){
	}

	/**
	 * 
	 * Constructor.
	 * @param intermediate Intermediate translation results
	 * @param target Back translation results
	 * 
	 */
	public BackTranslationResult(String intermediate, String target){
		this.intermediate = intermediate;
		this.target = target;
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
	 * Gets the intermediate translation results.
	 * @return Intermediate translationt results
	 * 
	 */
	public String getIntermediate() {
		return intermediate;
	}

	/**
	 * 
	 * Sets the intermediate translation results.
	 * @param intermediate Intermediate translation results
	 * 
	 */
	public void setIntermediate(String intermediate){
		this.intermediate = intermediate;
	}

	/**
	 * 
	 * Gets results of back translation.
	 * @return Result of back translation
	 * 
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * 
	 * Sets results of back translation.
	 * @param target Back translation results
	 * 
	 */
	public void setTarget(String target){
		this.target = target;
	}

	@Field(order=1)
	private String intermediate;
	@Field(order=2)
	private String target;

	private static final long serialVersionUID = 5606914610019368910L;
}
