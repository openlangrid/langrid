/*
 * $Id: BilingualDictionaryWithLongestMatchSearchClientImpl.java 199 2010-10-02 12:33:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.impl.langservice;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.BilingualDictionaryWithLongestMatchSearchClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import localhost.wrapper_mock_1_2.services.BilingualDictionaryWithLongestMatchSearch.BilingualDictionaryWithLongestMatchSearchServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 199 $
 */
public class BilingualDictionaryWithLongestMatchSearchClientImpl  
extends BilingualDictionaryClientImpl
implements BilingualDictionaryWithLongestMatchSearchClient
{
	/**
	 * 
	 * 
	 */
	public BilingualDictionaryWithLongestMatchSearchClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	public TranslationWithPosition[] searchLongestMatchingTerms(
			Language headLang, Language targetLang, Morpheme[] morphemes)
	throws LangridException{
		
		return (TranslationWithPosition[])invoke(headLang, targetLang, morphemes);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		BilingualDictionaryWithLongestMatchSearchServiceLocator locator =
			new BilingualDictionaryWithLongestMatchSearchServiceLocator();
		setUpService(locator);
		return (Stub)locator.getBilingualDictionaryWithLongestMatchSearch(url);
	}

	private static final long serialVersionUID = 6320562983520079387L;
	
}
