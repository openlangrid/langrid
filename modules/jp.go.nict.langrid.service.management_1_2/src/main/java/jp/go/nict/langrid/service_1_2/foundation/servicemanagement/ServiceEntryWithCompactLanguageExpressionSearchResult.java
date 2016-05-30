/*
 * $Id: ServiceEntryWithCompactLanguageExpressionSearchResult.java 15013 2011-08-19 11:04:32Z Takao Nakaguchi $
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

import jp.go.nict.langrid.service_1_2.foundation.SearchResult;

/**
 * 
 * Stores the result of the service entry search.
 * 
 * @author $Author: Takao Nakaguchi $
 * @version $Revision: 15013 $
 */
public class ServiceEntryWithCompactLanguageExpressionSearchResult
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceEntryWithCompactLanguageExpressionSearchResult(){
	}

	/**
	 * 
	 * Constructor.
	 * @param elements Entry array
	 * @param totalCount Total number of cases
	 * @param totalCountFixed Whether or not the total number of cases is fixed
	 * 
	 */
	public ServiceEntryWithCompactLanguageExpressionSearchResult(
			ServiceEntryWithCompactLanguageExpression[] elements, int totalCount
			, boolean totalCountFixed
			){
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}

	/**
	 * 
	 * Gets the entry array.
	 * @return Entry array
	 * 
	 */
	public ServiceEntryWithCompactLanguageExpression[] getElements() {
		return elements;
	}

	/**
	 * 
	 * Sets entry array.
	 * @param elements Entry array
	 * 
	 */
	public void setElements(ServiceEntryWithCompactLanguageExpression[] elements) {
		this.elements = elements;
	}

	private ServiceEntryWithCompactLanguageExpression[] elements = new ServiceEntryWithCompactLanguageExpression[]{};

	private static final long serialVersionUID = 3581336351432405305L;
}
