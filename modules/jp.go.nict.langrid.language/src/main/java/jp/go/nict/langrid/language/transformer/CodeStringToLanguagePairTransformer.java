/*
 * $Id: CodeStringToLanguagePairTransformer.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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
package jp.go.nict.langrid.language.transformer;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.language.util.LanguageUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 */
public class CodeStringToLanguagePairTransformer
implements Transformer<String, LanguagePair>{
	public LanguagePair transform(String value) throws TransformationException {
		if(value == null) return null;
		try{
			Language[] langs = LanguageUtil.decodeLanguageArray(value);
			if(langs.length != 2){
				throw new TransformationException(String.format(
						"length of languages must be 2: \"%s\"", value
						));
			}
			return new LanguagePair(langs[0], langs[1]);
		} catch(InvalidLanguageTagException e){
			throw new TransformationException(e);
		}
	}
}
