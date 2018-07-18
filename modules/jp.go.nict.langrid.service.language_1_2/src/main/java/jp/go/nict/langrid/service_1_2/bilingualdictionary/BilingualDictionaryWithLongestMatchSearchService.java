/*
 * $Id: BilingualDictionaryWithLongestMatchSearchService.java 587 2012-10-19 06:41:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.bilingualdictionary;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

/**
 * 
 * Defines the interface of the specialized translation service.
 * If you're providing the specialized translation service, then if you implement this interface, ...
 * You can access it using a client provided by langrid.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 587 $
 */
@Service(namespace="http://bilingualdictionary.ws_1_2.wrapper.langrid.nict.go.jp")
public interface BilingualDictionaryWithLongestMatchSearchService
extends BilingualDictionaryService
{
	/**
	 * 
	 * Runs a search based on an array of morphemes.
	 * Combines the morphemes (excluding verbs), and searches the resulting word.
	 * Adds the largest word from the found words to the search results.
	 * There is a possibility that multiple searches will be run, and multiple search results that differ from the head word will be returned.
	 * @param headLang Source language of bilingual translation(RFC3066 compliant)
	 * @param targetLang Target language of bilingual translation (RFC3066 compliant)
	 * @param morphemes Array of morphemes
	 * @return Search results
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException One of either headLang,targetLang,searchMethod is null or else is an empty string. headLang or targetLang do not comply with RFC3066.String not provided for by searchMethod
	 * @throws LanguagePairNotUniquelyDecidedException The bilingual translation language pair candidates could not be singly resolved(Ex:When headLang, or else targetLang is specified as zh, the case where zh-Hans and zh-Hant both exist as supported language pairs)
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ProcessFailedException Search process failed
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException The service is not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnsupportedLanguagePairException The specified language pair is not supported.
	 * @throws UnsupportedMatchingMethodException The specified matching method is not supported
	 * 
	 */
	TranslationWithPosition[] searchLongestMatchingTerms(
			@Parameter(name="headLang") String headLang
			, @Parameter(name="targetLang") String targetLang
			, @Parameter(name="morphemes") Morpheme[] morphemes)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguagePairException
	, UnsupportedMatchingMethodException
	;
}
