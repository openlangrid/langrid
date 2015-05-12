/*
 * $Id: AccessRightSearchResult.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.serviceaccessrightmanagement;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.foundation.SearchResult;

/**
 * 
 * Returns the result of the access privilege search.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class AccessRightSearchResult
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AccessRightSearchResult(){
	}

	/**
	 * 
	 * Constructor.
	 * @param elements Access privilege array
	 * @param totalCount Total number
	 * @param totalCountFixed Whether or not the total number is fixed
	 * 
	 */
	public AccessRightSearchResult(
			AccessRight[] elements, int totalCount
			, boolean totalCountFixed)
	{
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}

	/**
	 * 
	 * Gets access privilege.
	 * @return Access permission
	 * 
	 */
	public AccessRight[] getElements(){
		return elements;
	}

	/**
	 * 
	 * Sets access privilege.
	 * @param elements Access privilege(s)
	 * 
	 */
	public void setElements(AccessRight[] elements){
		this.elements = elements;
	}

	private AccessRight[] elements;

	private static final long serialVersionUID = -4300728323883073164L;
}
