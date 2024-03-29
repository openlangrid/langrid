/*
 * $Id: AdjacencyPairClientImpl.java 199 2010-10-02 12:33:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.impl.langservice;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.AdjacencyPairClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.adjacencypair.AdjacencyPair;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import localhost.wrapper_mock_1_2_N.services.AdjacencyPair.AdjacencyPairServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 199 $
 */
public class AdjacencyPairClientImpl  
extends ServiceClientImpl
implements AdjacencyPairClient
{
	/**
	 * 
	 * 
	 */
	public AdjacencyPairClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	public AdjacencyPair[] search(
			String category, Language language
			, String firstTurn, MatchingMethod matchingMethod)
	throws LangridException{
		return (AdjacencyPair[])invoke(
				category, language, firstTurn, matchingMethod);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		AdjacencyPairServiceLocator locator = new AdjacencyPairServiceLocator();
		setUpService(locator);
		return (Stub)locator.getAdjacencyPair(url);
	}

	private static final long serialVersionUID = 1913090183078389125L;
}
