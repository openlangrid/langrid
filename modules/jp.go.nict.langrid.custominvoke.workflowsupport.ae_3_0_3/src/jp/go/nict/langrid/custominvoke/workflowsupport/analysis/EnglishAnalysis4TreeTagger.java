/*
 * $Id: EnglishAnalysis4TreeTagger.java 260 2010-10-03 09:49:40Z t-nakaguchi $
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
package jp.go.nict.langrid.custominvoke.workflowsupport.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.custominvoke.workflowsupport.util.StringUtil;
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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;
import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.Analysis;

/**
 * 英文文字解析クラス
 * @author koyama
 *
 */
public class EnglishAnalysis4TreeTagger implements Analysis {
	
	public SourceAndMorphemesAndCodes doConstructSMC(Morpheme[] morphemes, Map<Integer, TranslationWithPosition> positionMap)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		return invoke(morphemes, positionMap, false);
	}
	private static final Set<String> SPECIAL_SENTENCE = new HashSet<String>();
	static {
		SPECIAL_SENTENCE.add("'s");
		SPECIAL_SENTENCE.add("n't");
		SPECIAL_SENTENCE.add("'ll");
		SPECIAL_SENTENCE.add("'d");
		SPECIAL_SENTENCE.add("'m");
		SPECIAL_SENTENCE.add("'ve");
		SPECIAL_SENTENCE.add("'re");
	};
	private static final Map<String, String> ESCAPE_SENTENCE = new HashMap<String, String>();
	static {
		ESCAPE_SENTENCE.put("&#39;", "'");
		ESCAPE_SENTENCE.put("&quot;", "\"");
		ESCAPE_SENTENCE.put("&nbsp;", " ");
		ESCAPE_SENTENCE.put("&amp;", "&");
	}
	public SourceAndMorphemesAndCodes doConstructSMCMarking(
			Morpheme[] morphemes,
			Map<Integer, TranslationWithPosition> positionMap)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		return invoke(morphemes, positionMap, true);
	};

	
	/**
	 * invoke
	 * @param morphemes
	 * @param positionMap
	 * @param marking
	 * @return
	 * @throws AccessLimitExceededException
	 * @throws InvalidParameterException
	 * @throws LanguageNotUniquelyDecidedException
	 * @throws NoAccessPermissionException
	 * @throws NoValidEndpointsException
	 * @throws ProcessFailedException
	 * @throws ServerBusyException
	 * @throws ServiceNotActiveException
	 * @throws ServiceNotFoundException
	 * @throws UnsupportedLanguageException
	 */
	protected SourceAndMorphemesAndCodes invoke(Morpheme[] morphemes, Map<Integer, TranslationWithPosition> positionMap, boolean marking) 
		throws AccessLimitExceededException, InvalidParameterException,
		LanguageNotUniquelyDecidedException, NoAccessPermissionException,
		NoValidEndpointsException, ProcessFailedException,
		ServerBusyException, ServiceNotActiveException,
		ServiceNotFoundException, UnsupportedLanguageException {
		StringBuffer source = new StringBuffer(); 				// 文章生成
		List<String> codes = new ArrayList<String>();			// 中間コード配列
		List<String> targetWords = new ArrayList<String>();		// 対象ワード配列
		List<Morpheme> morphemeResult = new ArrayList<Morpheme>(); // 形態素結果配列
		int markingCount = 1;
		int length = morphemes.length;
		for (int i = 0; i < length; i++) {
			TranslationWithPosition translation = positionMap.get(Integer.valueOf(i));
			if (translation != null) {
				String term = StringUtil.createWord(true, morphemes, translation.getStartIndex(), translation.getNumberOfMorphemes());
				// 中間コード生成
				String intermediateCode = StringUtil.generateCode(term, i);
				if (marking) {
					intermediateCode = StringUtil.markingWord(intermediateCode, markingCount++);
				}
				source.append(intermediateCode);
				// 中間コード配列追加
				codes.add(intermediateCode);
				// 対象ワード配列追加
				targetWords.add(translation.getTranslation().getTargetWords()[0]);
				// 結果形態素配列追加
				morphemeResult.add(new Morpheme(intermediateCode, intermediateCode, PartOfSpeech.noun.name()));
				i = i + translation.getNumberOfMorphemes() - 1;
			} else {
				source.append(morphemes[i].getWord());
				morphemeResult.add(morphemes[i]);
				// TreeTagger暫定対応 2008-11-05 start
				if ((i + 1) < length) {
					String str = morphemes[i + 1].getWord();
					if (str == null) {
						continue;
					} else if (str.indexOf("'") > -1) {
						String nextStr = null;
						if ((i + 2) < length) {
							nextStr = morphemes[i + 2].getWord();
						}
						if (str.equals("'")) {
							source.append(morphemes[++i].getWord());
							if (nextStr == null) {
								continue;
							}
							if (nextStr.equals("s")
									|| nextStr.equals("d")
									|| nextStr.equals("ll")
									|| nextStr.equals("ve")
									|| nextStr.equals("re")
									|| nextStr.equals("t")
									|| nextStr.equals("m")) {
								morphemeResult.add(morphemes[i]);
								continue;
							}
						}
						if (SPECIAL_SENTENCE.contains(str)) {
							source.append(morphemes[++i].getWord());
							morphemeResult.add(morphemes[i]);
						}
					} else if (str.equals(".")) {
						// ピリオドの前のスペース対応 2009-02-26 start						
						source.append(morphemes[++i].getWord());
						morphemeResult.add(morphemes[i]);
						// ピリオドの前のスペース対応 2009-02-26 end
					}
				}
			}
			source.append(" "); // 半角スペース挿入
		}
		if (source != null && source.length() > 0) {
			// 最後の空白を削除
			if (source.charAt(source.length() - 1) == ' ') {
				source = source.deleteCharAt(source.length() - 1);
			}
			// ピリオドの前の空白を削除
			char end = source.charAt(source.length() -1);
			if (end == '.') {
				if (source.charAt(source.length() - 2) == ' ') {
					source = source.deleteCharAt(source.length() - 2);
				}
			}
		}
		SourceAndMorphemesAndCodes smc = new SourceAndMorphemesAndCodes(
				source.toString(), morphemeResult.toArray(new Morpheme[]{}), codes.toArray(new String[]{}), targetWords.toArray(new String[]{})); 
		return smc;
	}

}
