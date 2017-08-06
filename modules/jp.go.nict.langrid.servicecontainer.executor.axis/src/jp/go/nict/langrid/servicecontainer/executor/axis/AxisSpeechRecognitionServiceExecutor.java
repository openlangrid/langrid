/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
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
package jp.go.nict.langrid.servicecontainer.executor.axis;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import jp.go.nict.langrid.cosee.Endpoint;
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
import localhost.service_mock.services.SpeechRecognition.SpeechRecognition;
import localhost.service_mock.services.SpeechRecognition.SpeechRecognitionServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 */
public class AxisSpeechRecognitionServiceExecutor
extends AbstractAxisServiceExecutor
implements SpeechRecognitionService{
	public AxisSpeechRecognitionServiceExecutor(
			String invocationName, long iid, Endpoint endpoint){
		super(SpeechRecognitionService.class, invocationName, iid, endpoint);
	}

	@Override
	public String recognize(String language, Speech speech)
	throws AccessLimitExceededException, InvalidParameterException,
	NoAccessPermissionException, NoValidEndpointsException,
	ProcessFailedException, ServerBusyException,
	ServiceNotActiveException, ServiceNotFoundException,
	UnsupportedLanguageException {
		try{
			SpeechRecognition port = locator.getSpeechRecognition();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub, language, speech);
			long s = System.currentTimeMillis( ) ;
			try{
				return port.recognize(language
						, convert(speech, jp.go.nict.langrid.ws_1_2.texttospeech.Speech.class)
						);
			} catch( jp.go.nict.langrid.ws_1_2.InvalidParameterException e ) {
				throw new InvalidParameterException( e.getParameterName( ), e.getDescription( ) ) ;
			} catch( jp.go.nict.langrid.ws_1_2.LangridException e ) {
				throw new ProcessFailedException( e.getDescription( ), e ) ;
			} catch( RemoteException e ) {
				throw new ProcessFailedException( e ) ;
			} finally{
				postprocessSoap(iid, stub, System.currentTimeMillis() - s ) ;
			}
		} catch( SOAPException e ) {
			throw new ProcessFailedException( e ) ;
		} catch( ServiceException e ) {
			throw new ProcessFailedException( e ) ;
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

	private final SpeechRecognitionServiceLocator locator = new SpeechRecognitionServiceLocator( ) ;
}
