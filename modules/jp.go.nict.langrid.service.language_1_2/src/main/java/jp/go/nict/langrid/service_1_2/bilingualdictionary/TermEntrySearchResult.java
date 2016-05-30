/*
 * $Id: TermEntrySearchResult.java 749 2013-03-29 02:28:04Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.bilingualdictionary;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.commons.rpc.intf.Schema;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 749 $
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/")
public class TermEntrySearchResult
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public TermEntrySearchResult(){
	}

	/**
	 * 
	 * 
	 */
	public TermEntrySearchResult(
			TermEntry[] elements, int totalCount
			, boolean totalCountFixed
			){
		super(totalCount, totalCountFixed);
		this.elements = elements;
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
	public TermEntry[] getElements() {
		return elements;
	}

	/**
	 * 
	 * 
	 */
	public void setElements(TermEntry[] elements) {
		this.elements = elements;
	}

	@Field(order=1)
	private TermEntry[] elements = new TermEntry[]{};

	private static final long serialVersionUID = 2099266385104138878L;
}
