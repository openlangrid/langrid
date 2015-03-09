/*
 * $Id: ParallelText.java 224 2010-10-03 00:17:47Z t-nakaguchi $
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores the parallel text.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public class ParallelText
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ParallelText(){}

	/**
	 * 
	 * Constructor.
	 * @param source Source string for bilingual translation
	 * @param target Bilingual translation string corresponding to source
	 * 
	 */
	public ParallelText(String source, String target){
		this.source = source;
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
	 * Returns the source string of the bilingual translation.
	 * @return Source string for bilingual translation
	 * 
	 */
	public String getSource(){
		return source;
	}

	/**
	 * 
	 * Sets the source string of the bilingual translation.
	 * @param source Source string for bilingual translation
	 * 
	 */
	public void setSource(String source){
		this.source = source;
	}

	/**
	 * 
	 * Returns the array of the bilingual translation.
	 * @return Bilingual translation string
	 * 
	 */
	public String getTarget(){
		return target;
	}

	/**
	 * 
	 * Sets the array of the bilingual translation.
	 * @param target Bilingual translation string
	 * 
	 */
	public void setTarget(String target){
		this.target = target;
	}

	private String source;
	private String target;

	private static final long serialVersionUID = 5835224322015323889L;
}
