/*
 * $Id: DbParameterLoader.java 240 2010-10-03 01:39:10Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.serviceexecutor.db;

import jp.go.nict.langrid.commons.parameter.ParameterLoader;
import jp.go.nict.langrid.commons.transformer.StringSplittingTransformer;
import jp.go.nict.langrid.commons.transformer.StringToArrayTransformer;
import jp.go.nict.langrid.commons.transformer.StringToEnumTransformer;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.transformer.CodeStringToLanguageTransformer;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 240 $
 */
public class DbParameterLoader extends ParameterLoader{
	{
		registerTransformer(
				String[].class
				, new StringSplittingTransformer(" ")
				);
		registerTransformer(
				MatchingMethod[].class
				, new StringToArrayTransformer<MatchingMethod>(
						" ", MatchingMethod.class
						, new StringToEnumTransformer<MatchingMethod>(
								MatchingMethod.class
								)
						)
				);
		registerTransformer(
				Language.class
				, new CodeStringToLanguageTransformer()
				);
	}
}
