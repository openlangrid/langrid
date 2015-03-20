/*
 * $Id: ParameterValidator.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.util;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.AttributeName;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.typed.ProfileKeyParser;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ParameterValidator extends jp.go.nict.langrid.service_1_2.util.ParameterValidator{
	/**
	 * 
	 * 
	 */
	public static MatchingMethod getValidMatchingMethod(
		String aParameterName, String aValue
		)
		throws InvalidParameterException
	{
		if(aValue == null) return null;
		if(aValue.length() == 0) return null;

		try{
			return MatchingMethod.valueOf(aValue.toUpperCase());
		} catch(IllegalArgumentException e){
			throw new InvalidParameterException(
				aParameterName, "invalid searchMethod: \"" + aValue + "\""
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static AttributeName getValidProfileKey(String parameterName, String profileKey)
		throws InvalidParameterException
	{
		AttributeName key = ProfileKeyParser.parse(profileKey);
		if(key == null){
			throw new InvalidParameterException(
					parameterName
					, profileKey + " is not a valid profile key"
					);
		}
		return key;
	}
}
