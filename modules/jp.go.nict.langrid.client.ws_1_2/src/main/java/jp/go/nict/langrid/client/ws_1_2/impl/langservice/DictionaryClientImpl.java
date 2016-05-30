/*
 * $Id: DictionaryClientImpl.java 199 2010-10-02 12:33:18Z t-nakaguchi $
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

import java.net.URI;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.DictionaryClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.dictionary.ConceptNode;
import jp.go.nict.langrid.service_1_2.dictionary.LemmaNode;
import jp.go.nict.langrid.service_1_2.dictionary.typed.DictMatchingMethod;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;
import localhost.wrapper_mock_1_2.services.Dictionary.Dictionary_ServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 199 $
 */
public class DictionaryClientImpl 
	extends ServiceClientImpl implements DictionaryClient
{
	/**
	 * 
	 * 
	 */
	public DictionaryClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	public URI[] searchLemmaNodes(
		Language headLang, Language lemmaLang
		, String headWord, String reading
		, PartOfSpeech partOfSpeech, DictMatchingMethod matchingMethod
		)
	throws LangridException{
		return (URI[])invoke(
				headLang, lemmaLang, headWord, reading
				, partOfSpeech, matchingMethod
				);
	}

	public LemmaNode getLemma(URI id) throws LangridException{
		return (LemmaNode)invoke(id);
	}

	public ConceptNode getConcept(URI id) throws LangridException{
		return (ConceptNode)invoke(id);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		Dictionary_ServiceLocator locator = new Dictionary_ServiceLocator();
		setUpService(locator);
		return (Stub)locator.getDictionary(url);
	}

	private static final long serialVersionUID = -5790339124852196184L;
}
