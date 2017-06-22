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
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;
import localhost.wrapper_mock_1_2.services.SimilarityCalculation.SimilarityCalculation_PortType;
import localhost.wrapper_mock_1_2.services.SimilarityCalculation.SimilarityCalculation_ServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 */
public class AxisSimilarityCalculationServiceExecutor
extends AbstractAxisServiceExecutor
implements SimilarityCalculationService{
	public AxisSimilarityCalculationServiceExecutor(
			String invocationName, long iid, Endpoint endpoint){
		super(SimilarityCalculationService.class, invocationName, iid, endpoint);
	}

	@Override
	public double calculate(String language, String text1, String text2)
		throws AccessLimitExceededException, InvalidParameterException,
		LanguageNotUniquelyDecidedException, NoAccessPermissionException,
		NoValidEndpointsException, ProcessFailedException,
		ServerBusyException, ServiceNotActiveException,
		ServiceNotFoundException, UnsupportedLanguageException
	{
		try{
			SimilarityCalculation_PortType port = locator.getSimilarityCalculation();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub, language, text1, text2);
			long s = System.currentTimeMillis( ) ;
			try{
				return port.calculate(language, text1, text2);
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

	private final SimilarityCalculation_ServiceLocator locator = new SimilarityCalculation_ServiceLocator();
}
