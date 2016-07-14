/*
 * $Id: ParameterValidation.aj 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.reflect.CodeSignature;

import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.foundation.util.validator.annotation.AnnotationParameterValidator;

/**
 * メソッドの引数が有効かどうかを、アノテーションに基づき検証するアスペクトの基底アスペクト。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 * @param <T> 適用対象のクラス
 */
public aspect ParameterValidation {
	before() throws InvalidParameterException
		: execution(@ValidatedMethod public *
				(jp.go.nict.langrid.foundation..* && AbstractLangridService+).*(..)
				throws InvalidParameterException)
	{
		try{
			CodeSignature sig = (CodeSignature)thisJoinPoint.getSignature();
			Class<?>[] types = sig.getParameterTypes();
			String[] names = sig.getParameterNames();
			Object[] args = thisJoinPoint.getArgs();
			Method m = thisJoinPoint.getThis().getClass().getDeclaredMethod(
					sig.getName(), types);
			AnnotationParameterValidator.validate(m, names, args);
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
	}
}
