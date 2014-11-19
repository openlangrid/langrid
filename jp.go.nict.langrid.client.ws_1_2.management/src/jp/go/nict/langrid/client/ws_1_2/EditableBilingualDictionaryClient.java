/*
 * $Id: EditableBilingualDictionaryClient.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2;

import java.util.Calendar;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Term;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TermEntry;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TermEntrySearchCondition;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TermEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public interface EditableBilingualDictionaryClient extends ServiceClient{
	/**
	 * 
	 * 
	 */		
	public TermEntrySearchResult searchTerm(int startIndex, int maxCount
		    , String[] language, TermEntrySearchCondition[] conditions
		    , Order[] orders
		    )
	throws LangridException;
 
	/**
	 * 
	 * 
	 */			 
	public void addTerm(Term[] terms)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	public void deleteTerm(int[] termIds)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	public void setTerm(TermEntry[] entries)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	public String[] listLanguage()
	throws LangridException;

	/**
	 * 
	 * 
	 */
	public void addLanguage(String[] languages)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	public void deleteLanguage(String[] languages)
	throws LangridException;

	/**
	 * 
	 * 
	 */
	String[] getSupportedMatchingMethods()
	throws LangridException;

	/**
	 * 
	 * 
	 */
	Calendar getLastUpdate()
	throws LangridException;
}
