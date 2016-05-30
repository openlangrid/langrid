/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or (at 
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
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.examplebasedtranslation.TrainingCorpusInfo;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelText;

/**
 * 
 * @author Takao Nakaguchi
 */
public class ExampleBasedTranslationService
implements jp.go.nict.langrid.service_1_2.examplebasedtranslation.ExampleBasedTranslationService{
	@Override
	public String createTrainingCorpus(String sourceLang, String targetLang,
			ParallelText[] examples) throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ProcessFailedException, NoValidEndpointsException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguagePairException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyTrainingCorpus(String corpusId)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TrainingCorpusInfo getTrainingCorpusInfo(String corpusId)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void appendExamples(String corpusId, String sourceLang,
			String targetLang, ParallelText[] examples)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replaceExamples(String corpusId, String sourceLang,
			String targetLang, ParallelText[] examples)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String translateWithTrainingCorpus(String[] corpusIds,
			String sourceLang, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String translate(String sourceLang, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		// TODO Auto-generated method stub
		return null;
	}

}
