/*
 * $Id: LanguageUtil.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.transformer.CodeStringToLanguageTransformer;
import jp.go.nict.langrid.language.transformer.LanguageToCodeStringTransformer;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 */
public class LanguageUtil {
	/**
	 * 
	 * 
	 */
	public static String encodeLanguageArray(Language[] languages){
		return ArrayUtil.join(arrayToCodeArray(languages), " ");
	}

	/**
	 * 
	 * 
	 */
	public static Language[] decodeLanguageArray(String value)
		throws InvalidLanguageTagException
	{
		return codeArrayToArray(value.split(" "));
	}

	/**
	 * 
	 * 
	 */
	public static String[] arrayToCodeArray(Language[] languages){
		return ArrayUtil.collect(
				languages, String.class, new LanguageToCodeStringTransformer()
				);
	}

	/**
	 * 
	 * 
	 */
	public static Language[] codeArrayToArray(String[] languages)
		throws InvalidLanguageTagException
	{
		try{
			return ArrayUtil.collect(
					languages, Language.class, new CodeStringToLanguageTransformer()
					);
		} catch(TransformationException e){
			if(e.getCause() instanceof InvalidLanguageTagException){
				throw (InvalidLanguageTagException)e.getCause();
			} else{
				throw e;
			}
		}
	}
}
