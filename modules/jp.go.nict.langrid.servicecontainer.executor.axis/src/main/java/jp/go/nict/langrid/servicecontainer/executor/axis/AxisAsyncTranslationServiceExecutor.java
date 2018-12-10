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

import org.apache.axis.client.Stub;

import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.translation.AsyncTranslationResult;
import jp.go.nict.langrid.service_1_2.translation.AsyncTranslationService;
import localhost.jp_go_nict_langrid_webapps_mock.services.AsyncTranslation.AsyncTranslationServiceServiceLocator;

/**
 * 
 * 
 */
public class AxisAsyncTranslationServiceExecutor
extends AbstractAxisServiceExecutor
implements AsyncTranslationService{
	public AxisAsyncTranslationServiceExecutor(
			String invocationName, long iid, Endpoint endpoint){
		super(invocationName, iid, endpoint);
	}

	@Override
	public String startTranslation(String sourceLang, String targetLang, String[] sources)
	throws InvalidParameterException, ProcessFailedException {
		try{
			localhost.jp_go_nict_langrid_webapps_mock.services.AsyncTranslation.AsyncTranslationService port =
					transLocator.getAsyncTranslation();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub, 
					getMethod(AsyncTranslationService.class, "startTranslation"),
					sourceLang, targetLang, sources);
			long s = System.currentTimeMillis( ) ;
			try{
				return port.startTranslation(sourceLang, targetLang, sources);
			} catch( jp.go.nict.langrid.ws_1_2.InvalidParameterException e ) {
				throw new InvalidParameterException( e.getParameterName( ), e.getDescription( ) ) ;
			} catch( jp.go.nict.langrid.ws_1_2.LangridException e ) {
				throw new ProcessFailedException( e.getDescription( ), e ) ;
			} catch( RemoteException e ) {
				throw new ProcessFailedException( e ) ;
			} finally{
				postprocessSoap(iid, stub, System.currentTimeMillis() - s ) ;
			}
		} catch(SOAPException e){
			throw new ProcessFailedException(e);
		} catch(ServiceException e){
			throw new ProcessFailedException(e);
		} catch(ServiceNotActiveException e){
			throw new ProcessFailedException(e);
		}
	}

	@Override
	public AsyncTranslationResult getCurrentResult(String token)
	throws InvalidParameterException, ProcessFailedException {
		try{
			localhost.jp_go_nict_langrid_webapps_mock.services.AsyncTranslation.AsyncTranslationService port =
					transLocator.getAsyncTranslation();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub,
					getMethod(AsyncTranslationService.class, "getCurrentResult"),
					token);
			long s = System.currentTimeMillis( ) ;
			try{
				return convert(
						port.getCurrentResult(token)
						, AsyncTranslationResult.class
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
		} catch(SOAPException e){
			throw new ProcessFailedException(e);
		} catch(ServiceException e){
			throw new ProcessFailedException(e);
		} catch(ServiceNotActiveException e){
			throw new ProcessFailedException(e);
		}
	}

	@Override
	public void terminate(String token)
	throws InvalidParameterException, ProcessFailedException {
		try{
			localhost.jp_go_nict_langrid_webapps_mock.services.AsyncTranslation.AsyncTranslationService port =
					transLocator.getAsyncTranslation();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub,
					getMethod(AsyncTranslationService.class, "terminate"),
					token);
			long s = System.currentTimeMillis( ) ;
			try{
				port.terminate(token);
			} catch( jp.go.nict.langrid.ws_1_2.InvalidParameterException e ) {
				throw new InvalidParameterException( e.getParameterName( ), e.getDescription( ) ) ;
			} catch( jp.go.nict.langrid.ws_1_2.LangridException e ) {
				throw new ProcessFailedException( e.getDescription( ), e ) ;
			} catch( RemoteException e ) {
				throw new ProcessFailedException( e ) ;
			} finally{
				postprocessSoap(iid, stub, System.currentTimeMillis() - s ) ;
			}
		} catch(SOAPException e){
			throw new ProcessFailedException(e);
		} catch(ServiceException e){
			throw new ProcessFailedException(e);
		} catch(ServiceNotActiveException e){
			throw new ProcessFailedException(e);
		}
	}

	private final AsyncTranslationServiceServiceLocator transLocator = new AsyncTranslationServiceServiceLocator();
}
