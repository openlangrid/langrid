/*
 * $Id: AnnotationParameterValidationExecutor.java 193 2010-10-02 11:34:46Z t-nakaguchi $
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
import java.lang.reflect.Method;

import jp.go.nict.langrid.commons.validator.ParameterValidationException;
import jp.go.nict.langrid.commons.validator.ValidationException;
import jp.go.nict.langrid.commons.validator.ValidationProcessException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 193 $
 */
public class AnnotationParameterValidationExecutor {
	/**
	 * 
	 * 
	 */
	public static void execute(
			Method method, String[] names, Object[] args
			)
	throws ParameterValidationException, ValidationProcessException
	{
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		String currentName = null;
		try{
			for(int i = 0; i < names.length; i++){
				currentName = names[i];
				Annotation[] annots = parameterAnnotations[i];
				boolean arrayMode = false;
				Object arg = args[i];
				Object[] arrayArg = null;
				for(Annotation a : annots){
					if(a.annotationType().equals(EachElement.class)){
						arrayMode = true;
						arrayArg = (Object[])arg;
						continue;
					}
					ValidatorClass vc =
						a.annotationType().getAnnotation(ValidatorClass.class);
					if(vc == null){
						continue;
					}
					if(arrayMode){
						for(Object o : arrayArg){
							AnnotationValidationExecutor.execute(vc, a, o);
						}
					} else{
						AnnotationValidationExecutor.execute(vc, a, arg);
					}
				}
			}
		} catch(ValidationException e){
			throw new ParameterValidationException(
					currentName, e.getMessage(), e.getCause()
					);
		}
	}
}
