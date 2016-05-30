/*
 * $Id: LanguagePathUtil.java 393 2011-08-24 03:16:21Z t-nakaguchi $
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

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.language.InvalidLanguagePathException;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.transformer.CodeStringToLanguagePathTransformer;
import jp.go.nict.langrid.language.transformer.LanguageToCodeStringTransformer;
import jp.go.nict.langrid.language.transformer.LanguageToLocalizedNameTransformer;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 393 $
 */
public class LanguagePathUtil {
	/**
	 * 
	 * 
	 */
	public static String joinCodes(LanguagePath path, String separator){
		return StringUtil.join(
				path.getPath(), new LanguageToCodeStringTransformer()
				, separator
				);
	}

	/**
	 * 
	 * 
	 */
	public static Set<LanguagePair> createSourceTargetPairSet(LanguagePath[] paths){
		LanguagePath[] langs = paths;
		Set<LanguagePair> pairs = new HashSet<LanguagePair>();
		for(LanguagePath p : langs){
			pairs.add(p.createSourceTargetPair());
		}
		return pairs;
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
			pairs.add(new LanguagePair(centerLang, tl));
			pairs.add(new LanguagePair(tl, centerLang));
		}
	}

	/**
	 * 
	 * 
	 */
	public static String encodeLanguagePath(LanguagePath value){
		return LanguageUtil.encodeLanguageArray(value.getPath());
	}

	/**
	 * 
	 * 
	 */
	public static LanguagePath decodeLanguagePath(String value)
		throws InvalidLanguageTagException, InvalidLanguagePathException
	{
		if(value.startsWith("(")){
			value = value.substring(1);
		}
		if(value.endsWith(")")){
			value = value.substring(0, value.length() - 1);
		}
		return new LanguagePath(LanguageUtil.decodeLanguageArray(value));
	}

	/**
	 * 
	 * 
	 */
	public static String encodeLanguagePathArray(LanguagePath... value){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for(LanguagePath path : value){
			boolean first = true;
			builder.append("(");
			for(Language language : path.getPath()){
				if(!first){
					builder.append(" ");
				} else{
					first = false;
				}
				builder.append(language.getCode());
			}
			builder.append(")");
		}
		builder.append(")");
		return builder.toString();
	}

	/**
	 * 
	 * 
	 */
	public static LanguagePath[] decodeLanguagePathArray(String line)
		throws InvalidLanguageTagException, InvalidLanguagePathException
	{
		String[] codeTuples = null;
		try{
			codeTuples = StringUtil.decodeTuple(line);
		} catch(ParseException e){
			throw new InvalidLanguagePathException(e);
		}
		try{
			return ArrayUtil.collect(codeTuples, new CodeStringToLanguagePathTransformer());
		} catch(TransformationException e){
			throw new InvalidLanguagePathException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static String encodeToSimplifiedExpressionByCode(
			LanguagePath[] paths)
	{
		return encodeToSimplefiedExpression(
				paths
				, new LanguageToCodeStringTransformer()
				, "), ("
		);
	}

	/**
	 * 
	 * 
	 */
	public static String encodeToSimplifiedExpressionByName(
			LanguagePath[] paths, final Locale locale)
	{
		return encodeToSimplefiedExpression(
				paths
				, new LanguageToLocalizedNameTransformer(locale)
				, "), ("
		);
	}

	private static String encodeToSimplefiedExpression(
			LanguagePath[] paths, Transformer<Language, String> textizer
			, String separator)
	{
		boolean first = true;
		StringBuilder b = new StringBuilder();
		b.append("(");
		Set<LanguagePath> pathset = new LinkedHashSet<LanguagePath>();
		for(LanguagePath p : paths){
			pathset.add(p);
		}
		Iterator<LanguagePath> i = pathset.iterator();
		while(i.hasNext()){
			LanguagePath p = i.next();
			i.remove();
			if(first){
				first = false;
			} else{
				b.append(separator);
			}
			if(p.getPath().length == 1){
				b.append(textizer.transform(p.getPath()[0]));
			} else if(p.getPath().length == 2){
				LanguagePath r = p.reverse();
				if(pathset.contains(r)){
					pathset.remove(r);
					i = pathset.iterator();
					b.append(StringUtil.join(p.getPath(), textizer, "<->"));
				} else{
					Language[] path = p.getPath();
					b.append(StringUtil.join(path, textizer, "-", 0, path.length - 1));
					b.append("->");
					b.append(textizer.transform(path[path.length - 1]));
				}
			} else{
				Language[] path = p.getPath();
				b.append(StringUtil.join(path, textizer, "-", 0, path.length - 1));
				b.append("->");
				b.append(textizer.transform(path[path.length - 1]));
			}
		}
		b.append(")");
		return b.toString();
	}
}
