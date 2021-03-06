/*
 * $Id: ParallelTextService.java 903 2013-07-12 04:29:01Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.paralleltext;

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

/**
 * 
 * Defines the standard bilingual translation interface.
 * If you're providing the bilingual translation service,
 * then if you implement this interface, you can access it using a client offered by langrid
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 903 $
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:ParallelText")
public interface ParallelTextService {
	/**
	 * 
	 * Runs a search on the parallel text using the specified search term(s) and matching method.
	 * @param sourceLang Source language. RFC3066 compliant
	 * @param targetLang Language to search bilingual translation. RFC3066 compliant
	 * @param text Sentence to search bilingual translation with
	 * @param matchingMethod Search method
	 * @return Array with search results stored.If none exist, an empty array.
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws LanguagePairNotUniquelyDecidedException Multiple candidate language pairs were found
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
	ParallelText[] search(
			@Parameter(name="sourceLang") String sourceLang
			, @Parameter(name="targetLang") String targetLang
			, @Parameter(name="text") String text
			, @Parameter(name="matchingMethod") String matchingMethod
		)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ProcessFailedException, ServerBusyException
		, ServiceNotActiveException, ServiceNotFoundException
		, UnsupportedLanguagePairException, UnsupportedMatchingMethodException
		;
}
