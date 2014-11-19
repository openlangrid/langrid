/*
 * $Id: EmbeddableStringPair.java 205 2010-10-02 13:53:40Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import jp.go.nict.langrid.commons.util.Pair;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 205 $
 */
@Embeddable
public class EmbeddableStringPair
implements Serializable{
	/**
	 * 
	 * 
	 */
	public EmbeddableStringPair(){
	}

	/**
	 * 
	 * 
	 */
	public EmbeddableStringPair(Pair<String, String> value){
		this.original = value;
		this.first = value.getFirst();
		this.second = value.getSecond();
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
	public Pair<String, String> getValue(){
		if(original != null){
			return original;
		}
		original = Pair.create(first, second);
		return original;
	}

	/**
	 * 
	 * 
	 */
	public void setValue(Pair<String, String> value){
		this.original = value;
		this.first = value.getFirst();
		this.second = value.getSecond();
	}

	private String first;
	private String second;
	private transient Pair<String, String> original;
	private static final long serialVersionUID = -8236026968055259684L;
}
