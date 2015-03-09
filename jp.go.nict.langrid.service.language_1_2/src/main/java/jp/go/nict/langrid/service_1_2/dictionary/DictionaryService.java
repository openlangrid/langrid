/*
 * $Id: DictionaryService.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.dictionary;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
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
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public interface DictionaryService {
	/**
	 * 
	 * 
	 */
	String[] searchLemmaNodes(
			@Parameter(name="headLang") String headLang
			, @Parameter(name="lemmaLang") String lemmaLang
			, @Parameter(name="headWord") String headWord
			, @Parameter(name="pronounciation") String pronounciation
			, @Parameter(name="partOfSpeech") String partOfSpeech
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
	 * 
	 */
	LemmaNode getLemma(
			@Parameter(name="lemmaNodeId") String lemmaNodeId
		)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, NodeNotFoundException
		, NoValidEndpointsException, ProcessFailedException
		, ServerBusyException, ServiceNotActiveException
		, ServiceNotFoundException;

	/**
	 * 
	 * 
	 */
	ConceptNode getConcept(
			@Parameter(name="conceptNodeId") String conceptNodeId
		)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, NodeNotFoundException
		, NoValidEndpointsException, ProcessFailedException
		, ServerBusyException, ServiceNotFoundException
		, ServiceNotActiveException
		;
}
