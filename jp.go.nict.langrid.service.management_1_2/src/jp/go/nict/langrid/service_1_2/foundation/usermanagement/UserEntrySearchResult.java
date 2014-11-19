/*
 * $Id: UserEntrySearchResult.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.usermanagement;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.foundation.SearchResult;

/**
 * 
 * Returns the result of the user data search.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class UserEntrySearchResult
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public UserEntrySearchResult(){
	}

	/**
	 * 
	 * Constructor.
	 * @param elements Array of user entries
	 * @param totalCount Total number of user entry data
	 * @param totalCountFixed Whether or not we fixed the total number
	 * 
	 */
	public UserEntrySearchResult(
			UserEntry[] elements, int totalCount
			, boolean totalCountFixed)
	{
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}

	/**
	 * 
	 * Sets user entry array.
	 * @param elements User entry array specified
	 * 
	 */
	public void setElements(UserEntry[] elements){
		this.elements = elements;
	}

	/**
	 * 
	 * Returns user entry array.
	 * @return Array of user entries
	 * 
	 */
	public UserEntry[] getElements(){
		return elements;
	}

	private UserEntry[] elements = new UserEntry[]{};

	private static final long serialVersionUID = 6435986363694606507L;
}
