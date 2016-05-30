/*
 * $Id: MatchingMethodValidator.java 224 2010-10-03 00:17:47Z t-nakaguchi $
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

import java.util.Set;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.transformer.MatchingMethodToStringTransformer;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.LanguageParameterValidator;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public class MatchingMethodValidator
extends AbstractStringValidator<MatchingMethodValidator>
{
	/**
	 * 
	 * 
	 */
	public MatchingMethodValidator(
			String parameterName, String value
			){
		super(parameterName, value);
	}

	/**
	 * 
	 * 
	 */
	public MatchingMethod getMatchingMethod(Set<MatchingMethod> supportedMethods)
		throws InvalidParameterException, UnsupportedMatchingMethodException
	{
		MatchingMethod m = LanguageParameterValidator.getValidMatchingMethod(
				getParameterName(), getValue()
				);
		if(supportedMethods.contains(m)) return m;
		else{
			throw new UnsupportedMatchingMethodException(
					getParameterName()
					, transformer.transform(m)
					, ArrayUtil.collect(
							supportedMethods.toArray(new MatchingMethod[]{})
							, String.class
							, transformer
					));
		}
	}

	private static MatchingMethodToStringTransformer transformer
		= new MatchingMethodToStringTransformer();
}
