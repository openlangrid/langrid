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
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import localhost.wrapper_mock_1_2.services.BackTranslationWithTemporalDictionary.BackTranslationWithTemporalDictionary;
import localhost.wrapper_mock_1_2.services.BackTranslationWithTemporalDictionary.BackTranslationWithTemporalDictionaryServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 */
public class AxisBackTranslationWithTemporalDictionaryServiceExecutor
extends AbstractAxisServiceExecutor
implements BackTranslationWithTemporalDictionaryService{
	public AxisBackTranslationWithTemporalDictionaryServiceExecutor(
			String invocationName, long iid, Endpoint endpoint){
		super(invocationName, iid, endpoint);
	}

	@Override
	public BackTranslationResult backTranslate(String sourceLang,
			String intermediateLang, String source, Translation[] temporalDict,
			String dictTargetLang)
	throws AccessLimitExceededException,
		InvalidParameterException, LanguagePairNotUniquelyDecidedException,
		NoAccessPermissionException, ProcessFailedException,
		NoValidEndpointsException, ServerBusyException,
		ServiceNotActiveException, ServiceNotFoundException,
		UnsupportedLanguagePairException
	{
		try{
			BackTranslationWithTemporalDictionary port
				= backTransLocator.getBackTranslationWithTemporalDictionary();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub,
					getMethod(BackTranslationWithTemporalDictionaryService.class, "backTranslate"),
					sourceLang, intermediateLang, source, temporalDict, dictTargetLang);
			long s = System.currentTimeMillis( ) ;
			try{
				return convert(port.backTranslate(sourceLang, intermediateLang, source
						, convert(temporalDict, jp.go.nict.langrid.ws_1_2.bilingualdictionary.Translation[].class)
						, dictTargetLang), BackTranslationResult.class);
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

	private final BackTranslationWithTemporalDictionaryServiceLocator backTransLocator
			= new BackTranslationWithTemporalDictionaryServiceLocator();
}
