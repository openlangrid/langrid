/*
 * $Id: LanguagePairUtil.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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
package jp.go.nict.langrid.language.util;

import java.util.ArrayList;
import java.util.Collection;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 */
public class LanguagePairUtil {
	/**
	 * 
	 * 
	 */
	public static String[] encodeLanguagePairArray(Collection<LanguagePair> pairs){
		ArrayList<String> cs = new ArrayList<String>();
		for(LanguagePair l : pairs){
			cs.add(l.toString());
		}
		return cs.toArray(new String[]{});
	}

	/**
	 * 
	 * 
	 */
	public static void addPair(
		Collection<LanguagePair> pairs, Language sourceLang, Language targetLang)
	{
		pairs.add(new LanguagePair(sourceLang, targetLang));
	}

	/**
	 * 
	 * 
	 */
	public static void addBidirectionalPair(
		Collection<LanguagePair> pairs, Language l1, Language l2)
	{
		pairs.add(new LanguagePair(l1, l2));
		pairs.add(new LanguagePair(l2, l1));
	}

	/**
	 * 
	 * 
	 */
	public static void addBidirectionalStarformedPairs(
		Collection<LanguagePair> pairs
		, Language centerLang
		, Language[] termLangs)
	{
		for(Language tl : termLangs){
			addBidirectionalPair(pairs, centerLang, tl);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void addBidirectionalRoundrobinformedPairs(
		Collection<LanguagePair> pairs
		, Language... languages)
	{
		int n = languages.length;
		for(int i = 0; i < (n - 1); i++){
			for(int j = i + 1; j < n; j++){
				addBidirectionalPair(pairs, languages[i], languages[j]);
			}
		}
	}
}
