/*
 * $Id: EditableBilingualDictionaryClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.impl;

import java.net.URL;
import java.util.Calendar;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.EditableBilingualDictionaryClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Term;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TermEntry;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TermEntrySearchCondition;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TermEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import localhost.wrapper_mock_1_2_N.services.EditableBilingualDictionary.EditableBilingualDictionaryServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class EditableBilingualDictionaryClientImpl  
extends ServiceClientImpl
implements EditableBilingualDictionaryClient
{
	/**
	 * 
	 * 
	 */
	public EditableBilingualDictionaryClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	public TermEntrySearchResult searchTerm(int startIndex, int maxCount
	    , String[] language, TermEntrySearchCondition[] conditions
	    , Order[] orders
	    )
	throws LangridException
	{
		return (TermEntrySearchResult)invoke(
				startIndex, maxCount, language, conditions, orders);
	}
 
	public void addTerm(Term[] terms)
	throws LangridException{
		invoke((Object)terms);
	}

	public void deleteTerm(int[] termIds)
	throws LangridException{
		invoke((Object)termIds);
	}

	public void setTerm(TermEntry[] entries)
	throws LangridException{
		invoke((Object)entries);
	}

	public String[] listLanguage()
	throws LangridException{
		return (String[])invoke();
	}

	public void addLanguage(String[] languages)
	throws LangridException{
		invoke((Object)languages);
	}

	public void deleteLanguage(String[] languages)
	throws LangridException{
		invoke((Object)languages);
	}

	public String[] getSupportedMatchingMethods() throws LangridException {
		return (String[])invoke();
	}

	public Calendar getLastUpdate() throws LangridException {
		return (Calendar)invoke();
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		EditableBilingualDictionaryServiceLocator locator = new EditableBilingualDictionaryServiceLocator();
		setUpService(locator);
		return (Stub)locator.getEditableBilingualDictionary(url);
	}

	private static final long serialVersionUID = -5340304971576790149L;
}
