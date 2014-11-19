/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.composite.commons.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import jp.go.nict.langrid.client.ws_1_2.BilingualDictionaryWithLongestMatchSearchClient;
import jp.go.nict.langrid.client.ws_1_2.ClientFactory;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.LanguagePair;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

public class BilingualDictionaryWithLongestMatchSearchClientAdapter
implements BilingualDictionaryWithLongestMatchSearchService{
	public BilingualDictionaryWithLongestMatchSearchClientAdapter(String serviceId){
		try{
			client = ClientFactory.createBilingualDictionaryWithLongestMatchSearchClient(new URL(
					TestContext.baseUrl + serviceId
					));
			client.setUserId(TestContext.userId);
			client.setPassword(TestContext.password);
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public TranslationWithPosition[] searchLongestMatchingTerms(
			String headLang, String targetLang, Morpheme[] morphemes)
	throws ProcessFailedException{
		try{
			return TestContext.converter.convert(
					client.searchLongestMatchingTerms(
							new Language(headLang), new Language(targetLang), morphemes)
					, TranslationWithPosition[].class);
		} catch(LangridException e){
			throw new ProcessFailedException(e);
		} catch (InvalidLanguageTagException e) {
			throw new ProcessFailedException(e);
		}
	}

	@Override
	public Translation[] search(String headLang, String targetLang,
			String headWord, String matchingMethod) {
		return null;
	}
	@Override
	public Calendar getLastUpdate() {
		return null;
	}
	@Override
	public LanguagePair[] getSupportedLanguagePairs() {
		return null;
	}
	@Override
	public String[] getSupportedMatchingMethods() {
		return null;
	}

	private BilingualDictionaryWithLongestMatchSearchClient client;
}
