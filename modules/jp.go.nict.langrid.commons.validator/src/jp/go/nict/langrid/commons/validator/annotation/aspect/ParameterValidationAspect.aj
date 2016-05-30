/*
 * $Id: ParameterValidationAspect.aj 54 2010-04-21 04:50:24Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.validator.annotation.aspect;

import jp.go.nict.langrid.commons.validator.ParameterValidationException;
import jp.go.nict.langrid.commons.validator.ValidationProcessException;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;

/**
 * パラメータの検証を行うアドバイスを挿入するアスペクト。
 * 継承し、タイプパラメータでクラスを指定する。
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 54 $
 * @param <T> パラメータの検証を行うクラス
 */
public abstract aspect ParameterValidationAspect<T> {
	protected pointcut validated()
		: execution(
				@ValidatedMethod * T.*(..)
				throws ParameterValidationException
				);

	before() throws ParameterValidationException
		: validated()
	{
		try{
			AspectParameterValidationExecutor.execute(thisJoinPoint);
		} catch(ValidationProcessException e){
			throw new RuntimeException(e);
		}
	}
}
