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
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import localhost.wrapper_mock_1_2.services.MorphologicalAnalysis.MorphologicalAnalysis_PortType;
import localhost.wrapper_mock_1_2.services.MorphologicalAnalysis.MorphologicalAnalysis_ServiceLocator;

/**
 * 
 * 
 */
public class AxisMorphologicalAnalysisServiceExecutor
extends AbstractAxisServiceExecutor
implements MorphologicalAnalysisService{
	public AxisMorphologicalAnalysisServiceExecutor(
			String invocationName, long iid, Endpoint endpoint){
		super(invocationName, iid, endpoint);
	}

	@Override
	public Morpheme[] analyze(String language, String text)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		try{
			MorphologicalAnalysis_PortType port = locator.getMorphologicalAnalysis();
			Stub stub = ( Stub )port ;
			long iid = preprocessSoap(stub,
					getMethod(MorphologicalAnalysisService.class, "analyze"),
					language, text
					);
			long s = System.currentTimeMillis( ) ;
			try{
				return convert(
						port.analyze(language, text)
						, Morpheme[].class
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

	private final MorphologicalAnalysis_ServiceLocator locator = new MorphologicalAnalysis_ServiceLocator();
}
