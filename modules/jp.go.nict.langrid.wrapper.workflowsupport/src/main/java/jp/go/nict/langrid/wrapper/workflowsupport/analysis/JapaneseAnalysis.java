/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 * Copyright Language Grid Project.
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

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;
import jp.go.nict.langrid.service_1_2.workflowsupport.CodeAndWords;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes2;
import jp.go.nict.langrid.wrapper.workflowsupport.util.LanguageDecision;
import jp.go.nict.langrid.wrapper.workflowsupport.util.StringUtil;

/**
 * 日本語文字解析クラス
 * @author Jun Koyama
 * @author Takao Nakaguchi
 */
public class JapaneseAnalysis implements Analysis {
	@Override
	public String doConstructSource(Morpheme[] morphemes) {
		StringBuilder source = new StringBuilder(); 				// 文章生成
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

	/**
	 * invoke
	 * @param morphemes
	 * @param positionMap
	 * @param marking
	 * @return
	 */
	public SourceAndMorphemesAndCodes doConstructSMC(
			Morpheme[] morphemes, Map<Integer, TranslationWithPosition> positionMap,
			CodeGenerator codeGenerator) {
		StringBuilder source = new StringBuilder(); 				// 文章生成
		List<String> codes = new ArrayList<String>();			// 中間コード配列
		List<String> headWords = new ArrayList<String>();		// 元ワード配列
		List<String> targetWords = new ArrayList<String>();		// 対象ワード配列
		List<Morpheme> morphemeResult = new ArrayList<Morpheme>(); // 形態素結果配列
		int length = morphemes.length;
		for (int i = 0; i < length; i++) {
			TranslationWithPosition translation = positionMap.get(Integer.valueOf(i));
			if (translation != null) {
				String term = StringUtil.createWord(false, morphemes, translation.getStartIndex(), translation.getNumberOfMorphemes());
				// 中間コード生成
				String intermediateCode = codeGenerator.generate(term, i);
				source.append(intermediateCode);
				codes.add(intermediateCode);
				headWords.add(translation.getTranslation().getHeadWord());
				targetWords.add(translation.getTranslation().getTargetWords()[0]);
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
				source.toString(), morphemeResult, codes, headWords, targetWords); 
		return smc;
	}

	public SourceAndMorphemesAndCodes2 doConstructSMC2(Morpheme[] morphemes, Map<Integer, TranslationWithPosition> positionMap, boolean marking) 
	throws InvalidParameterException, LanguageNotUniquelyDecidedException,
			ProcessFailedException, UnsupportedLanguageException
	{
		StringBuilder source = new StringBuilder(); 				// 文章生成
		List<CodeAndWords> codeAndWords = new ArrayList<CodeAndWords>();	// 中間コード配列
		List<Morpheme> morphemeResult = new ArrayList<Morpheme>(); // 形態素結果配列
		int markingCount = 1;
		int length = morphemes.length;
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
				codeAndWords.add(new CodeAndWords(
						intermediateCode, term,
						translation.getTranslation().getTargetWords()[0]));
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
		SourceAndMorphemesAndCodes2 smc = new SourceAndMorphemesAndCodes2(
				source.toString(), morphemeResult.toArray(new Morpheme[]{}),
				codeAndWords.toArray(new CodeAndWords[]{})); 
		return smc;
	}
}
