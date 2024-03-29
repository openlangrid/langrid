/*
 * $Id: AnnotationParameterValidator.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.util.validator.annotation;

import java.lang.reflect.Method;

import jp.go.nict.langrid.commons.validator.ParameterValidationException;
import jp.go.nict.langrid.commons.validator.ValidationProcessException;
import jp.go.nict.langrid.commons.validator.annotation.AnnotationParameterValidationExecutor;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class AnnotationParameterValidator {
	/**
	 * 
	 * 
	 */
	public static void validate(
			Method method, String[] parameterNames
			, Object[] parameters
			)
		throws InvalidParameterException
	{
		try{
			AnnotationParameterValidationExecutor.execute(
					method, parameterNames, parameters
					);
		} catch(ParameterValidationException e){
			throw new InvalidParameterException(
					e.getParameterName(), e.getMessage()
					);
		} catch(ValidationProcessException e){
			throw new RuntimeException(e);
		}
	}
}
