/*
 * $Id: TextToSpeechService.java 903 2013-07-12 04:29:01Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.speech;

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
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;

/**
 * 
 * Defines the interface of the speech synthesis service.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 903 $
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:TextToSpeech")
public interface TextToSpeechService{
	/**
	 * 
	 * 
	 */
	Speech speak(
			@Parameter(name="language") String language
			, @Parameter(name="text") String text
			, @Parameter(name="voiceType") String voiceType
			, @Parameter(name="audioType") String audioType)
		throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException
			, NoValidEndpointsException, ProcessFailedException
			, ServerBusyException, ServiceNotActiveException
			, ServiceNotFoundException, UnsupportedLanguageException
			;

	/**
	 * 
	 * 
	 */
	String[] getSupportedLanguages()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException;

	/**
	 * 
	 * 
	 */
	String[] getSupportedVoiceTypes()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException;

	/**
	 * 
	 * 
	 */
	String[] getSupportedAudioTypes()
	throws AccessLimitExceededException, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException;
}
