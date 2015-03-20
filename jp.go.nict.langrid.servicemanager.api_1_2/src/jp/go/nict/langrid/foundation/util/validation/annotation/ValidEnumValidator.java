/*
 * $Id: ValidEnumValidator.java 872 2013-04-29 09:29:55Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.foundation.util.validation.annotation;

import java.lang.reflect.InvocationTargetException;

import jp.go.nict.langrid.commons.lang.ObjectUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.transformer.ToStringTransformer;
import jp.go.nict.langrid.commons.validator.ValidationException;
import jp.go.nict.langrid.commons.validator.annotation.impl.ValidatorImpl;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 872 $
 */
public class ValidEnumValidator
implements ValidatorImpl<ValidEnum, String>{
	/**
	 * 
	 * 
	 */
	public void validate(ValidEnum annotation, String value)
	throws ValidationException
	{
		Class<?> enClass = annotation.value();
		try{
			ObjectUtil.invoke(enClass, "valueOf", value.toUpperCase());
			return;
		} catch(InvocationTargetException e){
			if(e.getCause().getClass().equals(IllegalArgumentException.class)){
				try{
					throw new ValidationException(
							"must be one of [\""
							+ StringUtil.join(
									(Object[])ObjectUtil.invoke(enClass, "values")
									, new ToStringTransformer<Object>()
									, "\", \"")
							+ "\"]"
							);
				} catch(Exception ex){
					throw new ValidationException(ex);
				}
			}
			throw new ValidationException(e);
		} catch(NoSuchMethodException e){
			throw new ValidationException(e);
		} catch(IllegalAccessException e){
			throw new ValidationException(e);
		}
	}
}
