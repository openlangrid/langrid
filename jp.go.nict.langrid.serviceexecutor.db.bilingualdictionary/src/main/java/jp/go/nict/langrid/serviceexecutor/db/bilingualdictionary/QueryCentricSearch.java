/*
 * $Id: QueryCentricSearch.java 240 2010-10-03 01:39:10Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.serviceexecutor.db.bilingualdictionary;

import static jp.go.nict.langrid.language.ISO639_1LanguageTags.ja;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.zh;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.COMPLETE;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.PREFIX;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;
import jp.go.nict.langrid.serviceexecutor.db.DictionaryDataBase;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 240 $
 */
public class QueryCentricSearch {
	/**
	 * 
	 * 
	 */
	public static Collection<TranslationWithPosition> search(
			DictionaryDataBase db
			, Language headLang, Language targetLang, Morpheme[] morphemes
			, boolean useCache)
	throws SQLException{
		List<TranslationWithPosition> ret = new ArrayList<TranslationWithPosition>();
		// complete: word
		// complete: lemma
		// prefix: word word
		// complete: word lemma
		DictionaryQuery[] queries4 = {
				new DictionaryQuery("", COMPLETE, true)
				, new DictionaryQuery("", COMPLETE, true),
				new DictionaryQuery("", PREFIX, false)
				, new DictionaryQuery("", COMPLETE, true),
				};
		Translation[] translations4 = {
				new Translation("", new String[]{""}), new Translation("", new String[]{""})
				, new Translation("", new String[]{""}), new Translation("", new String[]{""})
		};
		DictionaryQuery[] queries2 = {
				new DictionaryQuery("", COMPLETE, true)
				, new DictionaryQuery("", COMPLETE, true),
				};
		Translation[] translations2 = {
				new Translation("", new String[]{""}), new Translation("", new String[]{""})
		};
		for(int i = 0; i < morphemes.length; i++){
			StringBuilder words = new StringBuilder();
			addWordAndSetWordsAndLemma(headLang, words, morphemes[i], queries4[2], queries4[3]);
			TranslationWithPosition tempMatch = null;
			for(int j = i; j < morphemes.length; j++){
				DictionaryQuery[] q = null;
				Translation[] t = null;
				if(j == (morphemes.length - 1)){
					q = queries2;
					t = translations2;
					if(j != i || singleTargetMorphes.contains(morphemes[j].getPartOfSpeech())){
						q[0].setHeadWord(queries4[2].getHeadWord());
						q[1].setHeadWord(queries4[3].getHeadWord());
					} else{
						q[0].setHeadWord("");
						q[1].setHeadWord("");
					}
					t[0].setHeadWord("");
					t[1].setHeadWord("");
					if(q[0].getHeadWord().equalsIgnoreCase(q[1].getHeadWord())){
						q[1].setHeadWord("");
					}
				} else{
					q = queries4;
					t = translations4;
					if(j != i || singleTargetMorphes.contains(morphemes[j].getPartOfSpeech())){
						q[0].setHeadWord(queries4[2].getHeadWord());
						q[1].setHeadWord(queries4[3].getHeadWord());
					} else{
						q[0].setHeadWord("");
						q[1].setHeadWord("");
					}
					addWordAndSetWordsAndLemma(headLang, words, morphemes[j + 1], q[2], q[3]);
					t[0].setHeadWord("");
					t[1].setHeadWord("");
					t[2].setHeadWord("");
					t[3].setHeadWord("");
					if(q[0].getHeadWord().equalsIgnoreCase(q[1].getHeadWord())){
						q[1].setHeadWord("");
					}
					if(q[2].getHeadWord().equalsIgnoreCase(q[3].getHeadWord())){
						q[3].setHeadWord("");
					}
				}
				if(useCache){
					getFirstTranslationsWithCache(db, headLang, targetLang, q, t);
				} else{
					db.getFirstTranslations(headLang, targetLang, q, t);
				}

				if(q[0].getHeadWord().length() > 0 && t[0].getHeadWord().length() > 0){
					// wordが一致
					tempMatch = new TranslationWithPosition(
						new Translation(t[0].getHeadWord(), t[0].getTargetWords())
						, i, j - i + 1);
				} else if(q[1].getHeadWord().length() > 0 && t[1].getHeadWord().length() > 0){
					// lemmaが一致
					tempMatch = new TranslationWithPosition(
						new Translation(t[1].getHeadWord(), t[1].getTargetWords())
						, i, j - i + 1);
				}

				if(q.length > 2 && q[2].getHeadWord().length() > 0 && t[2].getHeadWord().length() > 0){
					// word + word の先が存在する
					continue;
				}
				if(q.length > 2 && q[3].getHeadWord().length() > 0 && t[3].getHeadWord().length() > 0){
					// word + lemmaが一致
					ret.add(new TranslationWithPosition(
							new Translation(t[3].getHeadWord(), t[3].getTargetWords())
							, i, j - i + 1));
					i = j;
					break;
				}
				if(tempMatch != null){
					ret.add(tempMatch);
					i = tempMatch.getStartIndex() + tempMatch.getNumberOfMorphemes() - 1;
				}
				break;
			}
		}
		return ret;
	}

	public static void clearCache(){
		cache = new Cache(true, false, false, false, null, 10000);
	}

	private static void addWordAndSetWordsAndLemma(Language headLang, StringBuilder words
			, Morpheme morpheme, DictionaryQuery wordQuery, DictionaryQuery lemmaQuery){
		String w = morpheme.getWord();
		String l = morpheme.getLemma();
		if(l.length() > 0){
			StringBuilder wordsLemma = new StringBuilder(words);
			concat(headLang, wordsLemma, l);
			lemmaQuery.setHeadWord(wordsLemma.toString());
		} else{
			lemmaQuery.setHeadWord("");
		}
		if(w.length() > 0){
			concat(headLang, words, w);
			wordQuery.setHeadWord(words.toString());
		} else{
			wordQuery.setHeadWord("");
		}
	}

	private static void concat(Language language, StringBuilder precedings, String word){
		if(precedings.length() > 0 && !NOSPACELANGS.contains(language) && !TERMINALS.contains(word)){
			precedings.append(" ");
		}
		precedings.append(word);
	}

	@SuppressWarnings("unchecked")
	private static void getFirstTranslationsWithCache(DictionaryDataBase db
			, Language headLang, Language targetLang
			, DictionaryQuery[] queries, Translation[] results)
	throws SQLException{
		String tableName = db.getTableName();
		String[] keys = new String[4];
		String[] hitHws = new String[4];
		Cache c = cache;
		for(int i = 0; i < queries.length; i++){
			String hw = queries[i].getHeadWord();
			if(hw.length() > 0){
				String key = toKey(tableName, headLang, targetLang, hw, queries[i].getMatchingMethod());
				keys[i] = key;
				try{
					Pair<String, String[]> r = (Pair<String, String[]>)c.getFromCache(key);
					results[i].setHeadWord(r.getFirst());
					results[i].setTargetWords(r.getSecond());
					hitHws[i] = hw;
					queries[i].setHeadWord("");
				} catch(NeedsRefreshException e){
					c.cancelUpdate(key);
				}
			}
		}
		db.getFirstTranslations(headLang, targetLang, queries, results);
		for(int i = 0; i < queries.length; i++){
			if(hitHws[i] != null){
				queries[i].setHeadWord(hitHws[i]);
				continue;
			}
			String hw = queries[i].getHeadWord();
			String rhw = results[i].getHeadWord();
			String[] rtws = results[i].getTargetWords();
			if(hw.length() > 0){
				String key = keys[i];
				c.putInCache(key, Pair.create(rhw, rtws));
			}
		}
	}

	private static String toKey(String tableName, Language headLang, Language targetLang
			, String headWord, MatchingMethod matchingMethod){
		return tableName + "," + headLang.getCode() + "," + targetLang.getCode() + ","
			+ headWord.toLowerCase() + "," + matchingMethod.ordinal();
	}

	private static Cache cache = new Cache(true, false, false, false, null, 10000);
	private static Set<Language> NOSPACELANGS = new HashSet<Language>();
	private static Set<String> TERMINALS = new HashSet<String>();
	static{
		NOSPACELANGS.add(ja);
		NOSPACELANGS.add(zh);
		TERMINALS.add("!");
		TERMINALS.add("?");
		TERMINALS.add(".");
		TERMINALS.add(",");
	}
	private static Set<String> singleTargetMorphes = new HashSet<String>();
	static{
		singleTargetMorphes.add(PartOfSpeech.noun.getExpression());
		singleTargetMorphes.add(PartOfSpeech.noun_common.getExpression());
		singleTargetMorphes.add(PartOfSpeech.noun_other.getExpression());
		singleTargetMorphes.add(PartOfSpeech.noun_proper.getExpression());
		singleTargetMorphes.add(PartOfSpeech.unknown.getExpression());
	}
}
