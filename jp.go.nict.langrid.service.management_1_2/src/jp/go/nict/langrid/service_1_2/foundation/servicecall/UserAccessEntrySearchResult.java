/*
 * $Id: UserAccessEntrySearchResult.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.servicecall;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.foundation.SearchResult;

/**
 * 
 * Stores the aggregate results of the access.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class UserAccessEntrySearchResult
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public UserAccessEntrySearchResult(){
	}

	/**
	 * 
	 * Constructor.
	 * @param elements Array of entries
	 * @param totalCount Total number of entry data
	 * @param totalCountFixed Whether or not we fixed the total number
	 * 
	 */
	public UserAccessEntrySearchResult(
			UserAccessEntry[] elements, int totalCount
			, boolean totalCountFixed)
	{
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}

	/**
	 * 
	 * Returns the entry array.
	 * @return Array of entries
	 * 
	 */
	public UserAccessEntry[] getElements(){
		return elements;
	}

	/**
	 * 
	 * Sets the entry array.
	 * @param elements Array of entries
	 * 
	 */
	public void setElements(UserAccessEntry[] elements){
		this.elements = elements;
	}

	private UserAccessEntry[] elements = new UserAccessEntry[]{};

	private static final long serialVersionUID = 726855885640558906L;
}
