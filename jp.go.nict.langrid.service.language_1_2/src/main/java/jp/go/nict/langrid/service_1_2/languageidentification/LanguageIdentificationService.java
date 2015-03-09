/*
 * $Id: LanguageIdentificationService.java 637 2013-02-19 03:44:41Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.service_1_2.languageidentification;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 637 $
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:LanguageIdentification")
public interface LanguageIdentificationService {
	/**
	 * 
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException Either text or language is null or else an empty string.language does not comply with RFC3066
	 * @throws ProcessFailedException Response search failed due to some cause
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException Service not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * 
	 */
	LanguageAndEncoding identifyLanguageAndEncoding(
			@Parameter(name="textBytes") byte[] textBytes)
	throws AccessLimitExceededException
	, InvalidParameterException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;

	/**
	 * 
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException Either text or language is null or else an empty string.language does not comply with RFC3066
	 * @throws ProcessFailedException Response search failed due to some cause
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException Service not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * 
	 */
	String identify(
			@Parameter(name="text") String text
			, @Parameter(name="originalEncoding") String originalEncoding)
	throws AccessLimitExceededException
	, InvalidParameterException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;

	/**
	 * 
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException Either text or language is null or else an empty string.language does not comply with RFC3066
	 * @throws ProcessFailedException Response search failed due to some cause
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException Service not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * 
	 */
	String[] getSupportedLanguages()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;

	/**
	 * 
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException Either text or language is null or else an empty string.language does not comply with RFC3066
	 * @throws ProcessFailedException Response search failed due to some cause
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException Service not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * 
	 */
	String[] getSupportedEncodings()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException
	;
}
