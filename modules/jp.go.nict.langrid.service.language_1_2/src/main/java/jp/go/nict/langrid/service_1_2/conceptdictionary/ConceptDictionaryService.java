/*
 * $Id: ConceptDictionaryService.java 637 2013-02-19 03:44:41Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.conceptdictionary;

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
 * Defines the concept dictionary interface.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 637 $
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:ConceptDictionary")
public interface ConceptDictionaryService {
	/**
	 * 
	 * Gets concepts related to a concept by specific relationships
	 * @param language Language that expresses the concept
	 * @param conceptId The Concept's ID
	 * @param relation Relation between concepts
	 * @return An array of concepts associated by specified relations to a concept expressed as a concept ID-language pair.
	 * @throws AccessLimitExceededException Exceeded an access restriction
	 * @throws InvalidParameterException An invalid parameter was passed.
	 * @throws LanguageNotUniquelyDecidedException Multiple languages supported by the service exist, and one is not singly determined.
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges.
	 * @throws NoValidEndpointsException No valid endpoint exists.
	 * @throws ProcessFailedException Process failed.
	 * @throws ServerBusyException Cannot complete the process as the server is in a busy state
	 * @throws ServiceNotActiveException The service is not active.
	 * @throws ServiceNotFoundException The requested service was not found.
	 * @throws UnsupportedLanguageException An unsupported language was specified.
	 * 
	 */
	Concept[] getRelatedConcepts(
			@Parameter(name="language") String language
			, @Parameter(name="conceptId") String conceptId
			, @Parameter(name="relation") String relation)
	throws AccessLimitExceededException
			, InvalidParameterException, LanguageNotUniquelyDecidedException
			, NoAccessPermissionException, ProcessFailedException
			, NoValidEndpointsException, ServerBusyException
			, ServiceNotActiveException, ServiceNotFoundException
			, UnsupportedLanguageException;

	/**
	 *
	 * Search concepts.
	 * @param language The language of the concept headword(s)
	 * @param word Concept headword
	 * @param matchingMethod Search method
	 * @return Array(s) of concepts expressed by concept headword(s)
	 * @throws AccessLimitExceededException Exceeded the access limit
	 * @throws InvalidParameterException An invalid parameter given
	 * @throws LanguageNotUniquelyDecidedException Multiple searchable languages exist, and one is not singly determined
	 * @throws NoAccessPermissionException The user making the call does not have execution privileges.
	 * @throws ProcessFailedException The process failed.
	 * @throws NoValidEndpointsException No valid endpoint exists.
	 * @throws ServerBusyException The server is in a busy state and could not complete the process.
	 * @throws ServiceNotActiveException The service is not active.
	 * @throws ServiceNotFoundException The service called was not found.
	 * @throws UnsupportedLanguageException An unsupported language was specified.
	 * @throws UnsupportedMatchingMethodException An unsupported matching method was specified.
	 * 
	 */
	Concept[] searchConcepts(
			@Parameter(name="language") String language
			, @Parameter(name="word") String word
			, @Parameter(name="matchingMethod") String matchingMethod)
	throws AccessLimitExceededException, InvalidParameterException,
	LanguageNotUniquelyDecidedException, NoAccessPermissionException,
	NoValidEndpointsException, ProcessFailedException,
	ServerBusyException, ServiceNotActiveException,
	ServiceNotFoundException, UnsupportedLanguageException,
	UnsupportedMatchingMethodException;
}
