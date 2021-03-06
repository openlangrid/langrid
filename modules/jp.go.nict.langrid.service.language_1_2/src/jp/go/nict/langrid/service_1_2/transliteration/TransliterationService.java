/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 The Language Grid Project.
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
package jp.go.nict.langrid.service_1_2.transliteration;

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

public interface TransliterationService{
	/**
	 * @throws AccessLimitExceededException Violated an access restriction
	 * @throws InvalidParameterException Either text or language is null or else an empty string.language does not comply with RFC3066
	 * @throws LanguageNotUniquelyDecidedException A supported language candidate could not be singly determined (Ex: when the language is specified as zh, the case where zh-Hans and zh-Hant exist as supported languages)
	 * @throws ProcessFailedException Response search failed due to some cause
	 * @throws NoAccessPermissionException The user executing the call lacks execution privileges
	 * @throws NoValidEndpointsException There is no valid endpoint
	 * @throws ServerBusyException The server is loaded and cannot process it.
	 * @throws ServiceNotActiveException Service not active
	 * @throws ServiceNotFoundException The specified service was not found
	 * @throws UnsupportedLanguageException The specified language is not supported
	 */
	String transliterate(String language, String sourceScript, String targetScript, String text)
	throws AccessLimitExceededException, InvalidParameterException,
	LanguageNotUniquelyDecidedException, NoAccessPermissionException,
	ProcessFailedException, NoValidEndpointsException,
	ServerBusyException, ServiceNotActiveException,
	ServiceNotFoundException, UnsupportedLanguageException
	;
}
