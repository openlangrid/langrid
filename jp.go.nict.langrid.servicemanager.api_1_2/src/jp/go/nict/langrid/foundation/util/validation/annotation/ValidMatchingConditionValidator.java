/*
 * $Id: ValidMatchingConditionValidator.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.validator.ValidationException;
import jp.go.nict.langrid.commons.validator.annotation.impl.ValidatorImpl;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ValidMatchingConditionValidator
implements ValidatorImpl<ValidMatchingCondition, MatchingCondition>{
	/**
	 * 
	 * 
	 */
	public void validate(ValidMatchingCondition annotation, MatchingCondition value)
	throws ValidationException{
		String field = value.getFieldName();
		if(!field.matches("[A-Za-z][A-Za-z0-9]+"))
			throw new ValidationException("invalid fieldName format");
		boolean supported = false;
		for(MatchingMethod m : annotation.supportedMatchingMethods()){
			if(m.name().equals(value.getMatchingMethod())){
				supported = true;
				break;
			}
		}
		if(!supported){
			throw new ValidationException("unsupported matching method.");
		}
	}
}
