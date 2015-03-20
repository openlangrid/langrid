/*
 * $Id: DictionaryClient.java 199 2010-10-02 12:33:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2;

import java.net.URI;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.dictionary.ConceptNode;
import jp.go.nict.langrid.service_1_2.dictionary.LemmaNode;
import jp.go.nict.langrid.service_1_2.dictionary.typed.DictMatchingMethod;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 199 $
 */
public interface DictionaryClient extends ServiceClient{
	/**
	 * 
	 * 
	 */
	URI[] searchLemmaNodes(
		Language headLang, Language lemmaLang
		, String headword, String reading
		, PartOfSpeech partOfSpeech, DictMatchingMethod matchingMethod
		)
		throws LangridException;

	/**
	 * lemmaIdで示されるLemmaNodeを取得する。
	 * @param lemmaId LemmaノードId
	 * @return 条件にマッチするLemmaノード
	 * @throws LangridException 辞書サービスの利用に失敗した
	 */
	LemmaNode getLemma(URI lemmaId)
		throws LangridException;

	/**
	 * 入力引数中の各conceptIdで示される概念ノードを取得する。
	 * @param conceptId 概念ノードID
	 * @return 条件にマッチする概念ノード
	 * @throws LangridException 辞書サービスの利用に失敗した
	 */
	ConceptNode getConcept(URI conceptId)
		throws LangridException;
}
