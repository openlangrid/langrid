/*
 * $Id: ValidOrderValidator.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.transformer.ToStringTransformer;
import jp.go.nict.langrid.commons.validator.ValidationException;
import jp.go.nict.langrid.commons.validator.annotation.impl.ValidatorImpl;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.OrderDirection;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class ValidOrderValidator
implements ValidatorImpl<ValidOrder, Order>{
	/**
	 * 
	 * 
	 */
	public void validate(ValidOrder annotation, Order value)
	throws ValidationException
	{
		String field = value.getFieldName();
		if(!field.matches("[A-Za-z][A-Za-z0-9]+"))
			throw new ValidationException("invalid fieldName format");
		try{
			OrderDirection.valueOf(value.getDirection().toUpperCase());
			return;
		} catch(IllegalArgumentException e){
			throw new ValidationException(
					"must be one of [\""
					+ StringUtil.join(
							OrderDirection.values()
							, new ToStringTransformer<OrderDirection>()
							, "\", \"")
					+ "\"]"
					);
		}
	}
}
