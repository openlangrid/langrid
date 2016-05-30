/*
 * $Id: LanguagePathValidator.java 224 2010-10-03 00:17:47Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.util.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.language.util.LanguageFinder;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePath;
import jp.go.nict.langrid.service_1_2.LanguagePathNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePathException;
import jp.go.nict.langrid.service_1_2.transformer.LanguagePath_LanguageToWITransformer;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public class LanguagePathValidator {
	/**
	 * 
	 * 
	 */
	public LanguagePathValidator(
			String sourceLangParamName, String sourceLang
			, String intermediateLangsParamName, String[] intermediateLangs
			, String targetLangParamName, String targetLang)
	{
		languages.add(new LanguageValidator(sourceLangParamName, sourceLang));
		for(int i = 0; i < intermediateLangs.length; i++){
			languages.add(new LanguageValidator(
					intermediateLangsParamName + "[" + i + "]", intermediateLangs[i]
					));
		}
		languages.add(new LanguageValidator(targetLangParamName, targetLang));
		parameterNames = new String[]{
				sourceLangParamName
				, intermediateLangsParamName
				, targetLangParamName
				};
	}

	public LanguagePathValidator notNull()
		throws InvalidParameterException
	{
		for(LanguageValidator v : languages){
			v.notNull();
		}
		return this;
	}

	public LanguagePathValidator trim(){
		for(LanguageValidator v : languages){
			v.trim();
		}
		return this;
	}

	/**
	 * 
	 * 
	 */
	public LanguagePathValidator notEmpty()
		throws InvalidParameterException
	{
		for(LanguageValidator v : languages){
			v.notEmpty();
		}
		return this;
	}

	public jp.go.nict.langrid.language.LanguagePath getLanguagePath()
		throws InvalidParameterException
	{
		List<jp.go.nict.langrid.language.Language> langs = new ArrayList<jp.go.nict.langrid.language.Language>();
		for(LanguageValidator v : languages){
			langs.add(v.getLanguage());
		}
		return new jp.go.nict.langrid.language.LanguagePath(langs.toArray(new jp.go.nict.langrid.language.Language[]{}));
	}

	public jp.go.nict.langrid.language.LanguagePath getUniqueLanguagePath(
			Collection<jp.go.nict.langrid.language.LanguagePath> supportedPathes)
		throws InvalidParameterException
			, LanguagePathNotUniquelyDecidedException
			, UnsupportedLanguagePathException
	{
		jp.go.nict.langrid.language.LanguagePath path = getLanguagePath();
		Collection<jp.go.nict.langrid.language.LanguagePath> candidates
				= LanguageFinder.find(supportedPathes, path);
		switch(candidates.size()){
			case 0:
				throw new UnsupportedLanguagePathException(
						parameterNames
						, lToWI.transform(path)
						);
			case 1:
				return candidates.iterator().next();
			default:
				throw new LanguagePathNotUniquelyDecidedException(
						parameterNames, ArrayUtil.collect(candidates.toArray(
								new jp.go.nict.langrid.language.LanguagePath[]{}
								), LanguagePath.class, lToWI)
						);
		}
	}

	private String[] parameterNames;
	private List<LanguageValidator> languages
		= new ArrayList<LanguageValidator>();
	private static LanguagePath_LanguageToWITransformer lToWI
		= new LanguagePath_LanguageToWITransformer();
}
