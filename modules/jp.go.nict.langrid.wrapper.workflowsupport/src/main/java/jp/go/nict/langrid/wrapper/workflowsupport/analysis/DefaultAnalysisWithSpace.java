/*
 * $Id: DefaultAnalysisWithSpace.java 409 2011-08-25 03:12:59Z t-nakaguchi $
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

import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;
import jp.go.nict.langrid.wrapper.workflowsupport.util.StringUtil;

/**
 * デフォルト言語文字解析クラス（with space）
 * @author koyama
 *
 */
public class DefaultAnalysisWithSpace implements Analysis {
	@Override
	public String doConstructSource(Morpheme[] morphemes) {
		StringBuilder source = new StringBuilder(); 				// 文章生成
		
		int length = morphemes.length;
		for (int i = 0; i < length; i++) {
			source.append(morphemes[i].getWord());
			source.append(" "); // 半角スペース挿入
		}
		// 最後の空白を削除
		if (source.charAt(source.length() - 1) == ' ') {
			source = source.deleteCharAt(source.length() - 1);
		}
		// ピリオドの前の空白を削除
		char end = source.charAt(source.length() -1);
		// 文末がピリオドかつピリオドだけでない場合
		if (end == '.' && source.length() > 1) {
			if (source.charAt(source.length() - 2) == ' ') {
				source = source.deleteCharAt(source.length() - 2);
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
			CodeGenerator codeGenerator){
		StringBuilder source = new StringBuilder();
		List<String> codes = new ArrayList<String>();
		List<String> headWords = new ArrayList<String>();
		List<String> targetWords = new ArrayList<String>();
		List<Morpheme> morphemeResult = new ArrayList<Morpheme>();
		
		int length = morphemes.length;
		for (int i = 0; i < length; i++) {
			TranslationWithPosition translation = positionMap.get(Integer.valueOf(i));
			if (translation != null) {
				String term = StringUtil.createWord(true, morphemes, translation.getStartIndex(), translation.getNumberOfMorphemes());
				String intermediateCode = codeGenerator.generate(term, i);
				source.append(intermediateCode);
				codes.add(intermediateCode);
				headWords.add(translation.getTranslation().getHeadWord());
				targetWords.add(translation.getTranslation().getTargetWords()[0]);
				morphemeResult.add(new Morpheme(intermediateCode, intermediateCode, PartOfSpeech.noun.name()));
				i = i + translation.getNumberOfMorphemes() - 1;
				source.append(" ");
				continue;
			}
			// when no head words were found
			source.append(morphemes[i].getWord());
			morphemeResult.add(morphemes[i]);
			source.append(" "); // 半角スペース挿入
		}
		// 最後の空白を削除
		if (source.charAt(source.length() - 1) == ' ') {
			source = source.deleteCharAt(source.length() - 1);
		}
		// ピリオドの前の空白を削除
		char end = source.charAt(source.length() -1);
		// 文末がピリオドかつピリオドだけでない場合
		if (end == '.' && source.length() > 1) {
			if (source.charAt(source.length() - 2) == ' ') {
				source = source.deleteCharAt(source.length() - 2);
			}
		}
		SourceAndMorphemesAndCodes smc = new SourceAndMorphemesAndCodes(
				source.toString(), morphemeResult,
				codes, headWords, targetWords); 
		return smc;
	}

}
