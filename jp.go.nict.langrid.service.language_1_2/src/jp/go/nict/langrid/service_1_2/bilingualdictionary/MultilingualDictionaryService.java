/*
 * $Id: MultilingualDictionaryService.java 587 2012-10-19 06:41:14Z t-nakaguchi $
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

import java.util.Calendar;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePair;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;

/**
 * 
 * Defines the interface for the bilingual dictionary service.
 * If you're providing the bilingual dictionary service, then if you implement this class, ...
 * You can access it using a client provided by langrid.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 587 $
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:MultilingualDictionary")
public interface MultilingualDictionaryService{
	/**
	 * 
	 * Searches the bilingual dictionary using the specified search term(s) and matching method, and returns bilingual translation.
	 * @param headLang Source language of bilingual translation(RFC3066 compliant)
	 * @param targetLangs Target language of bilingual translation (RFC3066 compliant)
	 * @param headWord Bilingual translation search phrase
	 * @param matchingMethod Matching method (one of either "COMPLETE","PREFIX","SUFFIX","PARTIAL","REGEX")
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
	Translation[] search(
			@Parameter(name="headLang") String headLang
			, @Parameter(name="targetLangs") String[] targetLangs
			, @Parameter(name="headWord") String headWord
			, @Parameter(name="matchingMethod") String matchingMethod
			)
	throws AccessLimitExceededException, InvalidParameterException
	, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguagePairException
	, UnsupportedMatchingMethodException
	;

	/**
	 * 
	 * Gets the dictionary's supported language.
	 * @return Array of languages
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ProcessFailedException Search process failed
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException The service is not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * 
	 */
	LanguagePair[] getSupportedLanguagePairs()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;

	/**
	 * 
	 * Returns the search methods supported by the dictionary wrapper.
	 * @return Array of supported search methods
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ProcessFailedException Search process failed
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException The service is not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * 
	 */
	String[] getSupportedMatchingMethods()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;

	/**
	 * 
	 * Returns the last date of change to the dictionary data.
	 * @return Date of last change
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ProcessFailedException Search process failed
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException The service is not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * 
	 */
	Calendar getLastUpdate()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;
}
