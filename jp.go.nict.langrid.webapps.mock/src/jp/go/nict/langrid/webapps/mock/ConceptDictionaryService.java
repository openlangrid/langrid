/*
 * $Id: ConceptDictionaryService.java 15143 2011-10-05 01:34:28Z Takao Nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.conceptdictionary.Concept;
import jp.go.nict.langrid.service_1_2.conceptdictionary.Gloss;
import jp.go.nict.langrid.service_1_2.conceptdictionary.Lemma;

/**
 * テスト用の概念辞書実装を提供する。
 * @author $Author: Takao Nakaguchi $
 * @version $Revision: 15143 $
 */
public class ConceptDictionaryService
implements jp.go.nict.langrid.service_1_2.conceptdictionary.ConceptDictionaryService {
	@Override
	public Concept[] searchConcepts(String language, String word,
			String matchingMethod) throws AccessLimitExceededException,
			InvalidParameterException, LanguageNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException, UnsupportedMatchingMethodException {
		return new Concept[]{new Concept("conceptId", "other"
				, new Lemma[]{new Lemma(word, language)}
				, new Gloss[]{new Gloss(word, language)}
		, new String[]{})};
	}

	@Override
	public Concept[] getRelatedConcepts(String language, String conceptId,
			String relation) throws AccessLimitExceededException,
			InvalidParameterException, LanguageNotUniquelyDecidedException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		// TODO Auto-generated method stub
		return null;
	}
}
