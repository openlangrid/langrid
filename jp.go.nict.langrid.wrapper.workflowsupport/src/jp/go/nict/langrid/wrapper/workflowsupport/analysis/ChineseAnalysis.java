/*
 * $Id: ChineseAnalysis.java 409 2011-08-25 03:12:59Z t-nakaguchi $
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
package jp.go.nict.langrid.wrapper.workflowsupport.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import jp.go.nict.langrid.wrapper.workflowsupport.util.LanguageDecision;
import jp.go.nict.langrid.wrapper.workflowsupport.util.StringUtil;

/**
 * 中国語文字解析クラス
 * @author koyama
 *
 */
public class ChineseAnalysis implements Analysis {
	@Override
	public String doConstructSource(Morpheme[] morphemes)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		StringBuffer source = new StringBuffer(); 				// 文章生成
		
		int length = morphemes.length;
		for (int i = 0; i < length; i++) {
			source.append(morphemes[i].getWord());
			// 現在の形態素がLatinかつ次の形態素もLatinの場合、空白を挿入する。
			if (i < (length - 1)) {
				if (LanguageDecision.isBasicLatin(morphemes[i].getWord()) && LanguageDecision.isBasicLatin(morphemes[i + 1].getWord())) {
					// periodは空白を入れない
					if (!morphemes[i + 1].getWord().trim().equals(".")) {
						source.append(" ");
					}
				}
			}
		}
		return source.toString();
	}

	public SourceAndMorphemesAndCodes doConstructSMC(Morpheme[] morphemes, Map<Integer, TranslationWithPosition> positionMap)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		return invoke(morphemes, positionMap, false);
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
	}
	
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
		
		int length = morphemes.length;
		int markingCount = 1;
		for (int i = 0; i < length; i++) {
			TranslationWithPosition translation = positionMap.get(Integer.valueOf(i));
			if (translation != null) {
				String term = StringUtil.createWord(false, morphemes, translation.getStartIndex(), translation.getNumberOfMorphemes());
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
			}
			// 現在の形態素がLatinかつ次の形態素もLatinの場合、空白を挿入する。
			if (i < (length - 1)) {
				if (LanguageDecision.isBasicLatin(morphemes[i].getWord()) && LanguageDecision.isBasicLatin(morphemes[i + 1].getWord())) {
					// periodは空白を入れない
					if (!morphemes[i + 1].getWord().trim().equals(".")) {
						source.append(" ");
					}
				}
			}
		}
		SourceAndMorphemesAndCodes smc = new SourceAndMorphemesAndCodes(
				source.toString(), morphemeResult.toArray(new Morpheme[]{}), codes.toArray(new String[]{}), targetWords.toArray(new String[]{})); 
		return smc;
	}

}
