/*
 * $Id: ValidAttributeValidator.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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

import java.util.regex.Pattern;

import jp.go.nict.langrid.commons.validator.ValidationException;
import jp.go.nict.langrid.commons.validator.annotation.impl.ValidatorImpl;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class ValidAttributeValidator
implements ValidatorImpl<ValidAttribute, Attribute>{
	/**
	 * 
	 * 
	 */
	public void validate(ValidAttribute annotation, Attribute value)
	throws ValidationException{
		String name = value.getName();
		String v = value.getValue();
		if(name == null){
			throw new ValidationException(".name must not be null.");
		}
		if(name.trim().length() == 0){
			throw new ValidationException(".name must not be empty.");
		}
		if(v == null){
			throw new ValidationException(".value must not be null.");
		}
		if(!annotation.allowEmptyValue() && v.trim().length() == 0){
			throw new ValidationException(".value must not be empty.");
		}
		if(!namePattern.matcher(name).matches())
			throw new ValidationException(
					"invalid fieldName format.  must follow");
	}

	private Pattern namePattern = Pattern.compile("[A-Za-z][A-Za-z0-9\\._]+");
}
