/*
 * $Id: ServiceEntrySearchResult.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.servicemanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import jp.go.nict.langrid.service_1_2.foundation.SearchResult;

/**
 * 
 * Stores the result of the service entry search.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ServiceEntrySearchResult
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceEntrySearchResult(){
	}

	/**
	 * 
	 * Constructor.
	 * @param elements Entry array
	 * @param totalCount Total number of cases
	 * @param totalCountFixed Whether or not the total number of cases is fixed
	 * 
	 */
	public ServiceEntrySearchResult(
			ServiceEntry[] elements, int totalCount
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
	 * Gets the entry array.
	 * @return Entry array
	 * 
	 */
	public ServiceEntry[] getElements() {
		return elements;
	}

	/**
	 * 
	 * Sets entry array.
	 * @param elements Entry array
	 * 
	 */
	public void setElements(ServiceEntry[] elements) {
		this.elements = elements;
	}

	private ServiceEntry[] elements = new ServiceEntry[]{};

	private static final long serialVersionUID = 3581336351432405305L;
}
