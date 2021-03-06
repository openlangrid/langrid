/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.composite.commons.test;

import java.io.IOException;
import java.net.URL;

import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.client.ws_1_2.ClientFactory;
import jp.go.nict.langrid.client.ws_1_2.SpeechRecognitionClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.speech.Speech;
import jp.go.nict.langrid.service_1_2.speech.SpeechRecognitionService;

public class SpeechRecognitionClientAdapter implements SpeechRecognitionService{
	public SpeechRecognitionClientAdapter(String serviceId){
		try{
			TestContext tc = new TestContext(getClass(), new JsonRpcClientFactory());
			client = ClientFactory.createSpeechRecognitionClient(new URL(
					tc.getBaseUrl() + serviceId
					));
			client.setUserId(tc.getUserId());
			client.setPassword(tc.getPassword());
		} catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	@Override
	public String recognize(String language, Speech speech)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		try{
			return client.recognize(language, speech);
		} catch(LangridException e){
			throw new ProcessFailedException(e);
		}
	}
	@Override
	public String[] getSupportedAudioTypes() throws NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] getSupportedLanguages() throws NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] getSupportedVoiceTypes() throws NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	private SpeechRecognitionClient client;
}
