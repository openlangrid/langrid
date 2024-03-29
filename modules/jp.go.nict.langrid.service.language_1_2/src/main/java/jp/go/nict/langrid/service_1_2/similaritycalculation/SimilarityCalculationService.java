/*
 * $Id: SimilarityCalculationService.java 903 2013-07-12 04:29:01Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.similaritycalculation;

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

/**
 * 
 * Defines the interface of the similarity calculation service.
 * If you're providing the similarity calculation service, then if you implement this interface, ...
 * You can access it using a client provided by langrid.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 903 $
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:SimilarityCalculation")
public interface SimilarityCalculationService{
	/**
	 * 
	 * Runs the text's similarity calculation.
	 * @param language Language of text(RFC3066 compliant)
	 * @param text1 Source text for comparison (required)
	 * @param text2 Destination text for comparison (required)
	 * @return Calculation results
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException One of either text1,text2,or language is null.language does not comply with RFC3066
	 * @throws LanguageNotUniquelyDecidedException A supported language candidate could not be singly determined (Ex: when the language is specified as zh, the case where zh-Hans and zh-Hant exist as supported languages)
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ProcessFailedException Calculation process failed
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException The service is not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnsupportedLanguageException The specified language is not supported
	 * 
	 */
	public double calculate(
			@Parameter(name="language") String language
			, @Parameter(name="text1") String text1
			, @Parameter(name="text2") String text2)
		throws AccessLimitExceededException, InvalidParameterException
			, LanguageNotUniquelyDecidedException, NoAccessPermissionException
			, NoValidEndpointsException, ProcessFailedException
			, ServerBusyException, ServiceNotActiveException
			, ServiceNotFoundException, UnsupportedLanguageException
			;
}
