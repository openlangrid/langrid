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
package jp.go.nict.langrid.webapps.mock;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.Category;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.templateparalleltext.BoundChoiceParameter;
import jp.go.nict.langrid.service_1_2.templateparalleltext.BoundValueParameter;
import jp.go.nict.langrid.service_1_2.templateparalleltext.Template;

public class TemplateParallelTextService
implements jp.go.nict.langrid.service_1_2.templateparalleltext.TemplateParallelTextService{

	@Override
	public Category[] listTemplateCategories(String language)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getCategoryNames(String categoryId, String[] languages)
			throws InvalidParameterException, ProcessFailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Template[] searchTemplates(String language, String text,
			String matchingMethod, String[] categoryIds)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException, UnsupportedMatchingMethodException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Template[] getTemplatesByTemplateId(String language,
			String[] templateIds) throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateSentence(String language, String templateId,
			BoundChoiceParameter[] boundChoiceParameters,
			BoundValueParameter[] boundValueParameters)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		return language;
	}
	
}
