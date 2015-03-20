/*
 * $Id: AnnotationValidationExecutor.java 872 2013-04-29 09:29:55Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.validator.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import jp.go.nict.langrid.commons.lang.ObjectUtil;
import jp.go.nict.langrid.commons.validator.ValidationException;
import jp.go.nict.langrid.commons.validator.ValidationProcessException;
import jp.go.nict.langrid.commons.validator.annotation.impl.ValidatorImpl;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 872 $
 */
public class AnnotationValidationExecutor {
	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void execute(
			ValidatorClass validatorClass
			, Annotation annotation
			, Object value
			)
	throws ValidationException, ValidationProcessException
	{
		try{
			((ValidatorImpl<Annotation, Object>)(validatorClass.value().newInstance()))
					.validate(annotation, value);
		} catch(ClassCastException e){
			throw new ValidationProcessException(e);
		} catch(IllegalAccessException e){
			throw new ValidationProcessException(e);
		} catch (InstantiationException e) {
			throw new ValidationProcessException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void executeForArray(
			ValidatorClass validatorClass
			, Annotation annotation
			, Object value
			)
	throws ValidationException, ValidationProcessException
	{
		int n = Array.getLength(value);
		for(int i = 0; i < n; i++){
			execute(validatorClass, annotation, Array.get(value, i));
		}
	}

	/**
	 * 
	 * 
	 */
	public static void executeForField(
			ValidatorClass validatorClass
			, Annotation annotation
			, String fieldName
			, Object object
			)
	throws ValidationException, ValidationProcessException
	{
		try{
			Object field = ObjectUtil.invoke(
					object
					, "get" + Character.toUpperCase(fieldName.charAt(0))
					+ fieldName.substring(1)
					);
			execute(validatorClass, annotation, field);
		} catch(IllegalAccessException e){
			throw new ValidationProcessException(e);
		} catch(InvocationTargetException e){
			throw new ValidationProcessException(e);
		} catch(NoSuchMethodException e){
			throw new ValidationProcessException(e);
		}
	}
}
