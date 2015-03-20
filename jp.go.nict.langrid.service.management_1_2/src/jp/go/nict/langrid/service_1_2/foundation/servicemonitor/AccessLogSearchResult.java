/*
 * $Id: AccessLogSearchResult.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.servicemonitor;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.foundation.SearchResult;

/**
 * 
 * Returns the result of the access log search.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class AccessLogSearchResult
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AccessLogSearchResult(){
	}

	/**
	 * 
	 * Constructor.
	 * @param elements Access log
	 * @param totalCount Total number of search results
	 * @param totalCountFixed Whether or not the total number is fixed
	 * 
	 */
	public AccessLogSearchResult(
			AccessLog[] elements
			, int totalCount, boolean totalCountFixed)
	{
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}

	/**
	 * 
	 * Sets access log.
	 * @param elements Access log specified
	 * 
	 */
	public void setElements(AccessLog[] elements){
		this.elements = elements;
	}

	/**
	 * 
	 * Gets access log.
	 * @return Access log
	 * 
	 */
	public AccessLog[] getElements(){
		return elements;
	}

	private AccessLog[] elements;

	private static final long serialVersionUID = 8316888719152157069L;
}
