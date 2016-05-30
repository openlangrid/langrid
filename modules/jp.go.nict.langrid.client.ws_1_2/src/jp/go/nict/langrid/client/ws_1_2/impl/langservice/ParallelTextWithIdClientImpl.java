/*
 * $Id: ParallelTextWithIdClientImpl.java 199 2010-10-02 12:33:18Z t-nakaguchi $
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

import jp.go.nict.langrid.client.ws_1_2.ParallelTextWithIdClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.Category;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithId;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import localhost.wrapper_mock_1_2.services.ParallelTextWithId.ParallelTextWithIdServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Revision: 199 $
 */
public class ParallelTextWithIdClientImpl  
extends ParallelTextClientImpl implements ParallelTextWithIdClient
{
	/**
	 * 
	 * 
	 */
	public ParallelTextWithIdClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	public ParallelTextWithId[] searchParallelTexts(Language sourceLang,
			Language targetLang, String text, MatchingMethod matchingMethod,
			String[] categoryIds) throws LangridException {
		return (ParallelTextWithId[])invoke(sourceLang, targetLang, text
				, matchingMethod, categoryIds);
	}

	@Override
	public Category[] listCategories() throws LangridException {
		return (Category[])invoke();
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		ParallelTextWithIdServiceLocator locator = new ParallelTextWithIdServiceLocator();
		setUpService(locator);
		return (Stub)locator.getParallelTextWithId(url);
	}

	private static final long serialVersionUID = 6270549284071037069L;
}
