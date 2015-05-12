/*
 * $Id: MatchingConditionValidator.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.util.validator;

import java.util.Set;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.util.validator.AbstractObjectValidator;
import jp.go.nict.langrid.service_1_2.util.validator.MatchingMethodValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class MatchingConditionValidator
	extends AbstractObjectValidator<MatchingConditionValidator, MatchingCondition>
{
	/**
	 * 
	 * 
	 */
	public MatchingConditionValidator(
			String parameterName, MatchingCondition condition
			)
	{
		super(parameterName, condition);
		init(condition);
	}

	private void init(MatchingCondition condition){
		if(condition == null) return;
		attributeName = new StringValidator(
				getParameterName() + ".attributeName"
				, condition.getFieldName()
				);
		matchingValue = new StringValidator(
				getParameterName() + ".searchValue"
				, condition.getMatchingValue()
				);
		matchingMethod = new MatchingMethodValidator(
				getParameterName() + ".searchMethod"
				, condition.getMatchingMethod()
				);
	}

	@Override
	public MatchingConditionValidator notNull()
			throws InvalidParameterException
	{
		super.notNull();
		attributeName.notNull();
		matchingValue.notNull();
		matchingMethod.notNull();
		return this;
	}

	/**
	 * 
	 * 
	 */
	public MatchingConditionValidator notEmpty()
			throws InvalidParameterException
	{
		attributeName.notEmpty();
		matchingValue.notEmpty();
		matchingMethod.notEmpty();
		return this;
	}

	/**
	 * 
	 * 
	 */
	public jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition
			getMatchingCondition(Set<MatchingMethod> supportedMatchingMethods)
			throws InvalidParameterException, UnsupportedMatchingMethodException
	{
		return new jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition(
				attributeName.getValue()
				, matchingValue.getValue()
				, matchingMethod.getMatchingMethod(supportedMatchingMethods)
				);
	}

	private StringValidator attributeName;
	private StringValidator matchingValue;
	private MatchingMethodValidator matchingMethod;
}