/*
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.wrapper.workflowsupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.go.nict.langrid.language.Language;
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
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;

/**
 * 空白でスプリットする、形態素解析です。
 * @author Jun Koyama
 * @author Takao Nakaguchi
 */
public class DefaultMorphologicalAnalysis
implements MorphologicalAnalysisService{
	@Override
	public Morpheme[] analyze(
			String language, String text) throws AccessLimitExceededException,
			InvalidParameterException, LanguageNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		String txt = new StringValidator("text", text)
			.notNull().trim().notEmpty().getValue();

		try{
			return doAnalyze(null, txt);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			throw e;
		} catch(Throwable t){
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			if(t instanceof RuntimeException){
				logger.severe("language: " + language + ", text: " + text);
			}
			throw new ProcessFailedException(t);
		}
	}

	protected Morpheme[] doAnalyze(Language language, String text)
	throws InvalidParameterException, ProcessFailedException {
		// 全角が存在する場合、半角へ変換
		text = text.replaceAll("　", " ");

		List<Morpheme> result = new ArrayList<Morpheme>();
		for (String word : split(text)) {
			// Part of speech of all morphemes is "unknown"
			result.add(new Morpheme(word, word, PartOfSpeech.unknown.getExpression()));
		}
		return result.toArray(new Morpheme[]{});
	}

	/**
	 * 文字列をスプリットします。<br>
	 * <li>space</li>
	 * <li>'</li>
	 * <li>"</li>
	 * <li>?</li>
	 * <li>!</li>
	 * <li>.</li>
	 * @param text 対象文字列
	 * @return スプリット後文字列
	 */
	protected Collection<String> split(String text) {
		List<String> list = new ArrayList<String>();
		Matcher shortMatcher = shortPattern.matcher(text);
		Map<String, String> replaced = new HashMap<String, String>();
		int i = 0;
		// 一旦、縮約形、小数は置き換え
		StringBuffer textBuf = new StringBuffer();
		while (shortMatcher.find()) {
			String key = "{" + i++ + "}";
			replaced.put(key, shortMatcher.group());
			shortMatcher.appendReplacement(textBuf, key);
		}
		shortMatcher.appendTail(textBuf);
		String[] str = pattern.split(textBuf);
		Matcher m = pattern.matcher(textBuf);
		i = 0;
		while (m.find()) {
			if (i < str.length) {
				String tmp = str[i++];
				chopNonAsciiAndAdd(tmp, replaced, list);
			}
			String delim = m.group();
			if (!delim.trim().equals("")) {
				list.add(delim);
			}
		}
		if (i < str.length) {
			if (str[i] != null && !str[i].equals("")) {
				chopNonAsciiAndAdd(str[i], replaced, list);
			}
		}
		return list;
	}

	private void chopNonAsciiAndAdd(String word, Map<String, String> map, List<String> result){
		StringBuilder asciis = new StringBuilder();
		for(char c : word.toCharArray()){
			if(treatAsKanji.contains(Character.UnicodeBlock.of(c))){
				if(asciis.length() > 0){
					String b = asciis.toString();
					if(map.containsKey(b)){
						b = map.get(b);
					}
					result.add(b);
					asciis = new StringBuilder();
				}
				result.add(new String("" + c));
			} else{
				asciis.append(c);
			}
		}
		if(asciis.length() > 0){
			String b = asciis.toString();
			if(map.containsKey(b)){
				b = map.get(b);
			}
			result.add(b);
			asciis = new StringBuilder();
		}
	}

	private static Logger logger = Logger.getLogger(
			DefaultMorphologicalAnalysis.class.getName()
			);
	// 縮約形、小数
	protected static Pattern shortPattern = Pattern.compile("([a-zA-Z]+'[a-zA-Z]+)|((\\d)+\\.(\\d)+)");
	// delimiter
	protected static Pattern pattern = Pattern.compile("[[,、。．\\.！？!?<>\"”’]|^\\s+\\.|']");
	private static Set<Character.UnicodeBlock> treatAsKanji = new HashSet<Character.UnicodeBlock>();
	static{
//		treatAsAscii.add(Character.UnicodeBlock.BASIC_LATIN);
//		treatAsAscii.add(Character.UnicodeBlock.LATIN_1_SUPPLEMENT);
//		treatAsAscii.add(Character.UnicodeBlock.LATIN_EXTENDED_A);
//		treatAsAscii.add(Character.UnicodeBlock.LATIN_EXTENDED_B);
//		treatAsAscii.add(Character.UnicodeBlock.LATIN_EXTENDED_ADDITIONAL);
		treatAsKanji.add(Character.UnicodeBlock.HIRAGANA);
		treatAsKanji.add(Character.UnicodeBlock.KATAKANA);
		treatAsKanji.add(Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS);
		treatAsKanji.add(Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
		treatAsKanji.add(Character.UnicodeBlock.CJK_COMPATIBILITY);
		treatAsKanji.add(Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS);
		treatAsKanji.add(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
		treatAsKanji.add(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
		treatAsKanji.add(Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
		treatAsKanji.add(Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
		treatAsKanji.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
		treatAsKanji.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
		treatAsKanji.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
	}
}
