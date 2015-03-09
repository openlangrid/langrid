/*
 * $Id: MultihopTranslationWithTemporalDictionaryService.java 14314 2011-01-21 07:01:49Z Takao Nakaguchi $
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

import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationResult;

/**
 * テスト用の翻訳実装を提供する。
 * メソッドの戻り値はパラメータをあらわす文字列。
 * @author $Author: Takao Nakaguchi $
 * @version $Revision: 14314 $
 */
public class MultihopTranslationWithTemporalDictionaryService
implements jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationWithTemporalDictionaryService{
	@Override
	public MultihopTranslationResult multihopTranslate(String sourceLang,
			String[] intermediateLangs, String targetLang, String source,
			Translation[] temporalDict, String dictTargetLang,
			String[][][] intermediateDictTargets,
			String[] intermediateDictTargetLangs){
		System.out.println(JSON.encode(intermediateDictTargets));
		String[] targets = new String[intermediateDictTargets.length];
		for(int i = 0; i < targets.length; i++){
			targets[i] = intermediateDictTargets[i][0][0];
		}
		System.out.println(JSON.encode(targets));
		return new MultihopTranslationResult(targets, source);
	}
}
