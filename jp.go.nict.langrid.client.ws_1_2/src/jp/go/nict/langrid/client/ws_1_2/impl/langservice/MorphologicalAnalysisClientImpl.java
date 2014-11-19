/*
 * $Id: MorphologicalAnalysisClientImpl.java 199 2010-10-02 12:33:18Z t-nakaguchi $
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

import jp.go.nict.langrid.client.ws_1_2.MorphologicalAnalysisClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.typed.Morpheme;
import localhost.wrapper_mock_1_2.services.MorphologicalAnalysis.MorphologicalAnalysis_ServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 199 $
 */
public class MorphologicalAnalysisClientImpl
	extends ServiceClientImpl implements MorphologicalAnalysisClient
{
	/**
	 * 
	 * 
	 */
	public MorphologicalAnalysisClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	public Morpheme[] analyze(Language language, String text)
	throws LangridException {
		return (Morpheme[])invoke(language, text);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		MorphologicalAnalysis_ServiceLocator locator = new MorphologicalAnalysis_ServiceLocator();
		setUpService(locator);
		return (Stub)locator.getMorphologicalAnalysis(url);
	}

	private static final long serialVersionUID = -4684394050638188653L;
}
