/*
 * $Id: DefaultScanner.java 409 2011-08-25 03:12:59Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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

package jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.scanner;

import static jp.go.nict.langrid.language.ISO639_1LanguageTags.ja;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.zh;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.th;
import static jp.go.nict.langrid.language.LangridLanguageTags.zh_CN;
import static jp.go.nict.langrid.language.LangridLanguageTags.zh_TW;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.TranslationComparator;

/**
 * 
 * 
 * @author Jun Koyama
 * @author Takao Nakaguchi
 */
public class DefaultScanner implements Scanner {
	public int doScan(Language language, int startIndex, Morpheme[] morphemes,
			Translation[] translations,
			Collection<TranslationWithPosition> positions)
			throws InvalidParameterException, ProcessFailedException {
		// 
		// 
		Arrays.sort(translations, new TranslationComparator());
		for (Translation trans : translations){
			if (trans == null || trans.getHeadWord() == null) {
				continue;
			}
			String headWord = trans.getHeadWord().toLowerCase().trim();
			if (trans.getTargetWords() == null || trans.getTargetWords().length == 0 ||
					trans.getTargetWords()[0] == null ||
					trans.getTargetWords()[0].length() == 0
					) {
				continue;
			}

			boolean first = true;
			StringBuilder sentence = new StringBuilder();
			for (int j = startIndex; j < morphemes.length; j++) {
				Morpheme morph = morphemes[j];
				String prevSentence = sentence.toString();
				if (!first && !LANGUAGES.contains(language) && !morph.getWord().equals(".")) {
					sentence.append(" ");
				}
				sentence.append(morph.getWord().toLowerCase());

				boolean hit = false;
				boolean cont = false;
				if(startsWith(headWord, sentence)){
					cont = true;
					hit = headWord.length() == sentence.length();
				}
				if(!hit){
					hit = headWord.equals(prevSentence + morph.getLemma().toLowerCase().trim());
				}
				if(hit && (!first || isNoun(morph))){
					if(morph.getPartOfSpeech().equals("noun.other")
							&& (j + 1) < morphemes.length
							&& morphemes[j + 1].getLemma().equals("する")){
						first = false;
						break;
					}
					Translation translation = new Translation(trans.getHeadWord(), trans.getTargetWords());
					positions.add(new TranslationWithPosition(translation, startIndex, (j - startIndex) + 1));
					return j;
				}
				if(!cont) break;
				first = false;
			}
		}
		return -1;
	}

	/**
	 * 
	 * 
	 */
	private boolean isNoun(Morpheme morpheme) {
		return NOUNS.contains(morpheme.getPartOfSpeech());
	}

	private static boolean startsWith(CharSequence seq1, CharSequence seq2){
		int n = Math.min(seq1.length(), seq2.length());
		for(int i = 0; i < n; i++){
			if(seq1.charAt(i) != seq2.charAt(i)) return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	private static final Set<Language> LANGUAGES = new HashSet<Language>();
	private static final Set<String> NOUNS = new HashSet<String>();
/*	private static Logger logger = Logger.getLogger(
			DefaultScanner.class.getName()
			);
*/
	static {
		LANGUAGES.add(ja);
		LANGUAGES.add(zh);
		LANGUAGES.add(th);
		LANGUAGES.add(zh_CN);
		LANGUAGES.add(zh_TW);
		NOUNS.add(PartOfSpeech.noun.getExpression());
		NOUNS.add(PartOfSpeech.noun_proper.getExpression());
		NOUNS.add(PartOfSpeech.noun_common.getExpression());
		NOUNS.add(PartOfSpeech.noun_other.getExpression());
		NOUNS.add(PartOfSpeech.unknown.getExpression());
	}
}
