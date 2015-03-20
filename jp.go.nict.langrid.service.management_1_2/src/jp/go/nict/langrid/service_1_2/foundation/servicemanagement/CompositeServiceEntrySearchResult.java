/*
 * $Id: CompositeServiceEntrySearchResult.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class CompositeServiceEntrySearchResult
extends SearchResult
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public CompositeServiceEntrySearchResult(){
	}

	/**
	 * 
	 * 
	 */
	public CompositeServiceEntrySearchResult(
			CompositeServiceEntry[] elements, int totalCount
			, boolean totalCountFixed
			){
		super(totalCount, totalCountFixed);
		this.elements = elements;
	}

	/**
	 * 
	 * 
	 */
	public CompositeServiceEntry[] getElements() {
		return elements;
	}

	/**
	 * 
	 * 
	 */
	public void setElements(CompositeServiceEntry[] elements) {
		this.elements = elements;
	}

	private CompositeServiceEntry[] elements = new CompositeServiceEntry[]{};

	private static final long serialVersionUID = -3624521357363575235L;
}
