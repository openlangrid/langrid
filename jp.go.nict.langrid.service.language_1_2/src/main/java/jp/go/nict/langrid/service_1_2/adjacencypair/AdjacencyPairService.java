/*
 * $Id: AdjacencyPairService.java 587 2012-10-19 06:41:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.adjacencypair;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;

/**
 * 
 * Defines the interface for the adjacency pair service.
 * If you're providing the adjacency pair service, then if you implement this class,
 * you can access it with a client provided by langrid.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 587 $
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:AdjacencyPair")
public interface AdjacencyPairService{
	/**
	 * 
	 * Gets a list of responses corresponding to the specified utterance.
	 * @param category Category. an empty string when omitted
	 * @param language Language of utterance (RFC3066 compliant.)
	 * @param firstTurn Utterance(required)
	 * @param matchingMethod Search method (MatchingMethod enumerated value)
	 * @return Search results
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException language, firstPart or searchMethod is null or else an empty string.language does not comply with RFC3066. String not provided for by searchMethod
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws LanguageNotUniquelyDecidedException A supported language candidate could not be singly determined (Ex: when the language is specified as zh, the case where zh-Hans and zh-Hant exist as supported languages)
	 * @throws ProcessFailedException Response search failed due to some cause
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException The service is not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnsupportedLanguageException The specified language is not supported
	 * @throws UnsupportedMatchingMethodException The specified matching method is not supported
	 * 
	 */
	public AdjacencyPair[] search(
			@Parameter(name="category") String category
			, @Parameter(name="language") String language
			, @Parameter(name="firstTurn") String firstTurn
			, @Parameter(name="matchingMethod") String matchingMethod)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, NoValidEndpointsException
	, LanguageNotUniquelyDecidedException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguageException
	, UnsupportedMatchingMethodException;
}
