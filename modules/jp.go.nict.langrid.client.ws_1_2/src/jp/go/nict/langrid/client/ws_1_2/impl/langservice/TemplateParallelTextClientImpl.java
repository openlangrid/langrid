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

import jp.go.nict.langrid.client.ws_1_2.TemplateParallelTextClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.Category;
import jp.go.nict.langrid.service_1_2.templateparalleltext.BoundChoiceParameter;
import jp.go.nict.langrid.service_1_2.templateparalleltext.BoundValueParameter;
import jp.go.nict.langrid.service_1_2.templateparalleltext.Template;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import localhost.service_mock.services.TemplateParallelText.TemplateParallelTextServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class TemplateParallelTextClientImpl  
extends ServiceClientImpl
implements TemplateParallelTextClient{
	/**
	 * 
	 * 
	 */
	public TemplateParallelTextClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		TemplateParallelTextServiceLocator locator = new TemplateParallelTextServiceLocator();
		setUpService(locator);
		return (Stub)locator.getTemplateParallelText(url);
	}

	@Override
	public Category[] listTemplateCategories(Language language)
	throws LangridException {
		return (Category[])invoke(language);
	}

	@Override
	public String[] getCategoryNames(String categoryId, Language[] languages)
	throws LangridException{
		return (String[])invoke(categoryId, languages);
	}

	@Override
	public Template[] searchTemplates(Language language, String text,
			MatchingMethod matchingMethod, String[] categoryIds)
			throws LangridException {
		return (Template[])invoke(language, text, matchingMethod, categoryIds);
	}

	@Override
	public Template[] getTemplatesByTemplateId(Language language,
			String[] templateIds) throws LangridException {
		return (Template[])invoke(language, templateIds);
	}

	@Override
	public String generateSentence(Language language, String templateId,
			BoundChoiceParameter[] boundChoiceParameters,
			BoundValueParameter[] boundValueParameters) throws LangridException {
		return (String)invoke(language, templateId
				, boundChoiceParameters, boundValueParameters);
	}
	
	private static final long serialVersionUID = 8131497077731572494L;
}
