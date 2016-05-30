/*
 * $Id: ParameterValidator.java 490 2012-05-24 02:44:18Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.util;

import java.net.URI;
import java.net.URISyntaxException;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 490 $
 */
public class ParameterValidator {
	/**
	 * 
	 * 
	 */
	public static void objectNotNull(String parameterName, Object value)
		throws InvalidParameterException
	{
		if(value == null){
			throw new InvalidParameterException(
				parameterName
				, "must not be null"
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void stringNotEmpty(String parameterName, String value)
		throws InvalidParameterException
	{
		if(value.length() == 0){
			throw new InvalidParameterException(
				parameterName
				, "must not be empty"
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void trimmedStringNotEmpty(String parameterName, String value)
		throws InvalidParameterException
	{
		if(value.trim().length() == 0){
			throw new InvalidParameterException(
				parameterName
				, "must not be empty"
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void numberNotNegative(String parameterName, Number value)
		throws InvalidParameterException
	{
		if(value.doubleValue() < 0){
			throw new InvalidParameterException(
				parameterName
				, "must not be negative"
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void numberNotZero(String parameterName, Number value)
		throws InvalidParameterException
	{
		if(value.doubleValue() == 0){
			throw new InvalidParameterException(
				parameterName
				, "must not be zero"
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void numberLessThan(
			String parameterName, Number value, double threshold)
		throws InvalidParameterException
	{
		if(value.doubleValue() >= threshold){
			throw new InvalidParameterException(
				parameterName
				, "must be less than " + threshold
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void numberLessThanOrEqualsTo(
			String parameterName, Number value, double threshold)
		throws InvalidParameterException
	{
		if(value.doubleValue() > threshold){
			throw new InvalidParameterException(
				parameterName
				, "must be less than " + threshold
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void numberGreaterThan(
			String parameterName, Number value, double threshold)
		throws InvalidParameterException
	{
		if(value.doubleValue() <= threshold){
			throw new InvalidParameterException(
				parameterName
				, "must be greater than " + threshold
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void numberGreaterThanOrEqualsTo(
			String parameterName, Number value, double threshold)
		throws InvalidParameterException
	{
		if(value.doubleValue() < threshold){
			throw new InvalidParameterException(
				parameterName
				, "must be greater than " + threshold
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static <T> void arrayNotEmpty(String parameterName, T[] value)
	throws InvalidParameterException{
		if(value.length == 0){
			throw new InvalidParameterException(
				parameterName
				, "must not be empty "
				);
		}
	}

	public static <T> void arrayLessThanOrEqualTo(String parameterName, T[] value, int length)
	throws InvalidParameterException{
		if(value.length >= length){
			throw new InvalidParameterException(
				parameterName
				, "must be less than or equals to " + length
				);
		}
	}
	
	/**
	 * 
	 * 
	 */
	public static URI getValidURI(String aParamName, String aValue)
	throws InvalidParameterException{
		try{
			return new URI(aValue);
		} catch(URISyntaxException e){
			throw new InvalidParameterException(aParamName, "not a valid uri");
		}
	}

	public static void stringLengthShorterThanOrEqualTo(String parameterName, String value,
			int length)
	throws InvalidParameterException{
		if(value.length() > length){
			throw new InvalidParameterException(parameterName, "length must be shorter than or equal to " + length);
		}
	}
}
