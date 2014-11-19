/*
 * $Id: ValidServiceIdValidator.java 404 2011-08-25 01:40:39Z t-nakaguchi $
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

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 404 $
 */
public class ValidServiceIdValidator
implements ValidatorImpl<ValidServiceId, String>{
	/**
	 * 
	 * 
	 */
	public static final Pattern VALID_ID_PATTERN = Pattern.compile(
			"([A-Za-z][\\w_\\-\\.]{3,}:)?[A-Za-z][\\w_\\-\\.]{2,}"
			);

	/**
	 * 
	 * 
	 */
	public static final Pattern VALID_WILDCARD_PATTERN = Pattern.compile(
			"([A-Za-z][\\w_\\-\\.]{3,}:)?\\*"
			);

	/**
	 * 
	 * 
	 */
	public void validate(ValidServiceId annotation, String value)
	throws ValidationException
	{
		if(annotation.allowEmpty()){
			if(value.length() == 0) return;
		}
		if(annotation.allowWildcard()){
			if(VALID_WILDCARD_PATTERN.matcher(value).matches()) return;
		}
		if(!VALID_ID_PATTERN.matcher(value).matches()){
			throw new ValidationException("not a valid node id format.");
		}
	}
}
