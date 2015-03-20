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
import java.util.Calendar;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePair;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import localhost.wrapper_mock_1_2.services.BilingualDictionaryWithLongestMatchSearch.BilingualDictionaryWithLongestMatchSearch;
import localhost.wrapper_mock_1_2.services.BilingualDictionaryWithLongestMatchSearch.BilingualDictionaryWithLongestMatchSearchServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 */
public class AxisBilingualDictionaryWithLongestMatchSearchServiceExecutor
extends AbstractAxisServiceExecutor
implements BilingualDictionaryWithLongestMatchSearchService{
	public AxisBilingualDictionaryWithLongestMatchSearchServiceExecutor(
			String invocationName, long iid, Endpoint endpoint){
		super(invocationName, iid, endpoint);
	}
	
	@Override
	public Translation[] search(String headLang, String targetLang,
			String headWord, String matchingMethod)
	throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException,
			UnsupportedMatchingMethodException {
		try{
			BilingualDictionaryWithLongestMatchSearch port = locator.getBilingualDictionaryWithLongestMatchSearch();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub);
			long s = System.currentTimeMillis( ) ;
			try{
				return convert(
						port.search(headLang, targetLang, headWord, matchingMethod)
						, Translation[].class
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
	public TranslationWithPosition[] searchLongestMatchingTerms(
			String headLang, String targetLang, Morpheme[] morphemes)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException,
			UnsupportedMatchingMethodException {
		try{
			BilingualDictionaryWithLongestMatchSearch port = locator.getBilingualDictionaryWithLongestMatchSearch();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub);
			long s = System.currentTimeMillis( ) ;
			try{
				return convert(
						port.searchLongestMatchingTerms(
								headLang, targetLang, convert(morphemes, jp.go.nict.langrid.ws_1_2.morphologicalanalysis.Morpheme[].class)
								)
						, TranslationWithPosition[].class);
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
	public LanguagePair[] getSupportedLanguagePairs()
			throws AccessLimitExceededException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		try{
			BilingualDictionaryWithLongestMatchSearch port = locator.getBilingualDictionaryWithLongestMatchSearch();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub);
			long s = System.currentTimeMillis( ) ;
			try{
				return convert(
						port.getSupportedLanguagePairs()
						, LanguagePair[].class
						);
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
	public String[] getSupportedMatchingMethods()
			throws AccessLimitExceededException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		try{
			BilingualDictionaryWithLongestMatchSearch port = locator.getBilingualDictionaryWithLongestMatchSearch();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub);
			long s = System.currentTimeMillis( ) ;
			try{
				return port.getSupportedMatchingMethods();
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
	public Calendar getLastUpdate() throws AccessLimitExceededException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		try{
			BilingualDictionaryWithLongestMatchSearch port = locator.getBilingualDictionaryWithLongestMatchSearch();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub);
			long s = System.currentTimeMillis( ) ;
			try{
				return port.getLastUpdate();
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

	private final BilingualDictionaryWithLongestMatchSearchServiceLocator locator = new BilingualDictionaryWithLongestMatchSearchServiceLocator();
}
