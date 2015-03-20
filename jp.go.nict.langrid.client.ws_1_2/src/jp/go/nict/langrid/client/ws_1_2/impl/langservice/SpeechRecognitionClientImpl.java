/*
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
package jp.go.nict.langrid.client.ws_1_2.impl.langservice;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.SpeechRecognitionClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.service_1_2.speech.Speech;
import localhost.service_mock.services.SpeechRecognition.SpeechRecognitionServiceLocator;
import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class SpeechRecognitionClientImpl  
extends ServiceClientImpl
implements SpeechRecognitionClient{
	/**
	 * 
	 * 
	 */
	public SpeechRecognitionClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		SpeechRecognitionServiceLocator locator = new SpeechRecognitionServiceLocator();
		setUpService(locator);
		return (Stub)locator.getSpeechRecognition(url);
	}

	@Override
	public String recognize(String language, Speech speech)
			throws LangridException {
		return (String)invoke(language, speech);
	}

	@Override
	public String[] getSupportedLanguages() throws LangridException {
		return (String[])invoke();
	}

	@Override
	public String[] getSupportedVoiceTypes() throws LangridException {
		return (String[])invoke();
	}

	@Override
	public String[] getSupportedAudioTypes() throws LangridException {
		return (String[])invoke();
	}

	private static final long serialVersionUID = -448092211511837207L;
}
