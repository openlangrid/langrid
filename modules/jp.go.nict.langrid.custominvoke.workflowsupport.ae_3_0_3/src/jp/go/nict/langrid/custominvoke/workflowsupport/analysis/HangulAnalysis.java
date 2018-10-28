/*
 * $Id: HangulAnalysis.java 260 2010-10-03 09:49:40Z t-nakaguchi $
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
import java.util.List;
import java.util.Map;

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
 * ハングル文字解析クラス
 * @author koyama
 *
 */
public class HangulAnalysis implements Analysis {
	
	/**
	 * ハングルの１番最初の文字コード
	 */
	private static final int BASE_CODE_NO = 44032;
	/**
	 * 子音母音のみの組み合わせになる差
	 */
	private static final int INCREMENT = 28;

	/**
	 * 文字が子音＋母音＋子音で出来ていたらTrue
	 * @param c ハングル文字
	 * @return 結果
	 */
	public static boolean isCVC(char c) {
		int diff = ((int)c) - BASE_CODE_NO;
		if ((diff % INCREMENT) == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 単語の最終の文字が子音＋母音＋子音で出来ていたらTrue
	 * @param str ハングル文字列
	 * @return 結果
	 */
	public static boolean isCVC(String str) {
		char c = str.charAt(str.length() - 1);
		return isCVC(c);
	}
	
	
	/**
	 * 子音＋母音＋子音の助詞から子音＋母音の助詞へのマッピング
	 */
	private static final Map<String, String> PARTICL_MAP_2_CV = new HashMap<String, String>();
	
	static {
		PARTICL_MAP_2_CV.put("은", "는");	// は
		PARTICL_MAP_2_CV.put("이", "가");	// が
		PARTICL_MAP_2_CV.put("을", "를");	// を
		PARTICL_MAP_2_CV.put("과", "와");	// と
		PARTICL_MAP_2_CV.put("으로", "로");	// で
		PARTICL_MAP_2_CV.put("의", "의");	// の
	}
	/**
	 * 子音＋母音の助詞から子音＋母音＋子音の助詞へのマッピング
	 */
	private static final Map<String, String> PARTICL_MAP_2_CVC = new HashMap<String, String>();
	
	static {
		PARTICL_MAP_2_CVC.put("는", "은");	// は
		PARTICL_MAP_2_CVC.put("가", "이");	// が
		PARTICL_MAP_2_CVC.put("를", "을");	// を
		PARTICL_MAP_2_CVC.put("와", "과");	// と
		PARTICL_MAP_2_CVC.put("로", "으로");	// で
		PARTICL_MAP_2_CVC.put("의", "의");	// の
	}
	
	/**
	 * 助詞置き換え
	 * @param sentence 文章
	 * @param word 置き換えた言葉
	 * @param index 置き換えたインデックス
	 * @return 置き換え後文章
	 */
	public static String convertParticl(String sentence, String word, int index) {
		String tmp = "";
		boolean cvc = isCVC(word);
		if (sentence.length() == word.length()) {
			return sentence;
		}
		int particlLength = 1;
		String key = "";
		if ((sentence.length() - word.length()) == 1) {
			key = sentence.substring(index + word.length(), index + word.length() + 1);
			particlLength = 1;
		} else if ((sentence.length() - word.length()) == 2) {
			key = sentence.substring(index + word.length(), index + word.length() + 2);
			particlLength = 2;
		}
		if (cvc) {
			tmp = PARTICL_MAP_2_CVC.get(key);
		} else {
			tmp = PARTICL_MAP_2_CV.get(key);
		}
		if (tmp != null) {
			sentence = sentence.substring(0, index + word.length()) + tmp + sentence.substring(index + word.length() + particlLength);
		}
		return sentence;
	}
	
	/**
	 * 助詞を返す
	 * @param term 文章
	 * @param word 最後の形態素：言葉
	 * @param lemma 最後の形態素：原型
	 * @return 置き換え後文章
	 */
	public static String getParticl(String term, String word, String lemma) {
		String tmp = "";
		boolean cvc = isCVC(lemma);
		int index = term.indexOf(word);
		if (index < 0) {
			return "";
		}
		String key = term.substring(index + lemma.length());
		if (cvc) {
			tmp = PARTICL_MAP_2_CVC.get(key);
		} else {
			tmp = PARTICL_MAP_2_CV.get(key);
		}
		if (tmp != null) {
			return tmp;
		}
		return "";
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
		List<String> headWords = new ArrayList<String>();
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
				String particl = ""; // 韓国語用助詞
				int lastIndex = translation.getStartIndex() + translation.getNumberOfMorphemes() - 1;
				particl = getParticl(term, morphemes[lastIndex].getWord(), morphemes[lastIndex].getLemma());
				source.append(intermediateCode + particl);
				// 中間コード配列追加
				codes.add(intermediateCode);
				headWords.add(translation.getTranslation().getHeadWord());
				// 対象ワード配列追加
				targetWords.add(translation.getTranslation().getTargetWords()[0]);
				// 結果形態素配列追加
				morphemeResult.add(new Morpheme(intermediateCode + particl, intermediateCode, PartOfSpeech.noun.name()));
				i = i + translation.getNumberOfMorphemes() - 1;
			} else {
				source.append(morphemes[i].getWord());
				morphemeResult.add(morphemes[i]);
			}
			source.append(" "); // 半角スペース挿入
		}
		// 最後の空白を削除
		if (source.charAt(source.length() - 1) == ' ') {
			source = source.deleteCharAt(source.length() - 1);
		}
		// ピリオドの前の空白を削除
		char end = source.charAt(source.length() -1);
		if (end == '。' || end == '.' || end == '．') {
			if (source.charAt(source.length() - 2) == ' ') {
				source = source.deleteCharAt(source.length() - 2);
			}
		}
		SourceAndMorphemesAndCodes smc = new SourceAndMorphemesAndCodes(
				source.toString(), morphemeResult.toArray(new Morpheme[]{}),
				codes.toArray(new String[]{}),
				headWords.toArray(new String[]{}),
				targetWords.toArray(new String[]{})); 
		return smc;
	}

}
