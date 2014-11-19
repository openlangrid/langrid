/*
 * $Id: AccessLimitSearchResult.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.serviceaccesslimitmanagement;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.foundation.SearchResult;

/**
 * 
 * Stores the result of the access restriction search.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class AccessLimitSearchResult
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AccessLimitSearchResult(){
	}

	/**
	 * 
	 * Constructor.
	 * @param elements Access restriction array
	 * @param totalCount Total number
	 * @param totalCountFixed Whether or not the total number is fixed
	 * 
	 */
	public AccessLimitSearchResult(AccessLimit[] elements
			, int totalCount, boolean totalCountFixed){
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}
	
	/**
	 * 
	 * Gets access restriction.
	 * @return Access restriction
	 * 
	 */
	public AccessLimit[] getElements(){
		return elements;
	}

	/**
	 * 
	 * Sets access restriction.
	 * @param elements Access restriction(s)
	 * 
	 */
	public void setElements(AccessLimit[] elements){
		this.elements = elements;
	}

	private AccessLimit[] elements;

	private static final long serialVersionUID = -2531171116498192984L;
}
