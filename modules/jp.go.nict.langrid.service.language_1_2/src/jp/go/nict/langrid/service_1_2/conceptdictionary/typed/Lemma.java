/*
 * $Id: Lemma.java 224 2010-10-03 00:17:47Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.conceptdictionary.typed;

import java.io.Serializable;

import jp.go.nict.langrid.language.Language;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public class Lemma
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public Lemma(){
	}

	/**
	 * 
	 * 
	 */
	public Lemma(String lemmaForm, Language language){
		this.lemmaForm = lemmaForm;
		this.language = language;
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
	public Language getLanguage(){
		return language;
	}

	/**
	 * 
	 * 
	 */
	public String getLemmaForm(){
		return lemmaForm;
	}

	/**
	 * 
	 * 
	 */
	public void setLanguage(Language language){
		this.language = language;
	}

	/**
	 * 
	 * 
	 */
	public void setLemmaForm(String lemmaForm){
		this.lemmaForm = lemmaForm;
	}

	private Language language;
	private String lemmaForm;
	private static final long serialVersionUID = -5325944232628036929L;
}
