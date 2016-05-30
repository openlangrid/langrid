/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.util.ParameterValidator;

public class ArrayValidator<T>
extends AbstractObjectValidator<ArrayValidator<T>, T[]>{
	/**
	 * 
	 * 
	 */
	public ArrayValidator(String parameterName, T[] value){
		super(parameterName, value);
	}

	public ArrayValidator<T> notEmpty()
	throws InvalidParameterException{
		ParameterValidator.arrayNotEmpty(getParameterName(), getValue());
		return this;
	}
	
	public ArrayValidator<T> lessThanOrEqualTo(int size)
	throws InvalidParameterException{
		ParameterValidator.arrayLessThanOrEqualTo(getParameterName(), getValue(), size);
		return this;
	}

}
