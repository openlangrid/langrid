/*
 * $Id: DictionaryService.java 14314 2011-01-21 07:01:49Z Takao Nakaguchi $
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
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.dictionary.ConceptNode;
import jp.go.nict.langrid.service_1_2.dictionary.LemmaNode;
import jp.go.nict.langrid.service_1_2.dictionary.NodeNotFoundException;

/**
 * テスト用の辞書実装を提供する。
 * 各インターフェースから返されるDictEntryは、
 * extendedAttrプロパティに渡された引数を保持し、
 * そのほかのプロパティにプロパティ名と同じ文字列を保持する。
 * @author $Author: Takao Nakaguchi $
 * @version $Revision: 14314 $
 */
public class DictionaryService
implements jp.go.nict.langrid.service_1_2.dictionary.DictionaryService{
	@Override
	public String[] searchLemmaNodes(String headLang, String lemmaLang,
			String headWord, String pronounciation, String partOfSpeech,
			String matchingMethod) throws AccessLimitExceededException,
			InvalidParameterException, LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException,
			UnsupportedMatchingMethodException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LemmaNode getLemma(String lemmaNodeId)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NodeNotFoundException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConceptNode getConcept(String conceptNodeId)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NodeNotFoundException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotFoundException,
			ServiceNotActiveException {
		// TODO Auto-generated method stub
		return null;
	}
}
