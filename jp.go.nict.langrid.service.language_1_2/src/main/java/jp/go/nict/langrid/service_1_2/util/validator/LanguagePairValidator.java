/*
 * $Id: LanguagePairValidator.java 224 2010-10-03 00:17:47Z t-nakaguchi $
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

import java.util.Collection;

import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.language.util.LanguageFinder;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.util.LanguageParameterValidator;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public class LanguagePairValidator {
	public LanguagePairValidator(LanguageValidator lang1, LanguageValidator lang2){
		this.lang1 = lang1;
		this.lang2 = lang2;
	}

	public LanguagePairValidator notNull()
		throws InvalidParameterException
	{
		lang1.notNull();
		lang2.notNull();
		return this;
	}

	public LanguagePairValidator trim(){
		lang1.trim();
		lang2.trim();
		return this;
	}

	/**
	 * 
	 * 
	 */
	public LanguagePairValidator notEmpty()
		throws InvalidParameterException
	{
		lang1.notEmpty();
		lang2.notEmpty();
		return this;
	}

	public LanguagePair getLanguagePair()
		throws InvalidParameterException
	{
		return new LanguagePair(
				lang1.getLanguage()
				, lang2.getLanguage()
				);
	}
	
	public LanguagePair getUniqueLanguagePair(
			Collection<LanguagePair> supportedPairs)
		throws InvalidParameterException
			, LanguagePairNotUniquelyDecidedException
			, UnsupportedLanguagePairException
	{
		LanguagePair pair = getLanguagePair();
		Collection<LanguagePair> candidates = LanguageFinder.find(
				supportedPairs, pair
				);
		return LanguageParameterValidator.getUniqueLanguagePair(
				lang1.getParameterName()
				, lang2.getParameterName()
				, pair.getSource()
				, pair.getTarget()
				, candidates
				);
	}

	private LanguageValidator lang1;
	private LanguageValidator lang2;
}
