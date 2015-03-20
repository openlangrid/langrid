/*
 * $Id: AccessRightValidation.aj 302 2010-12-01 02:49:42Z t-nakaguchi $
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

import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.util.validation.AnnotationAccessRightValidator;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserNotFoundException;

import org.aspectj.lang.reflect.CodeSignature;

/**
 * アクセス権の検証を行うアスペクトの基底アスペクト。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 * @param <T> 適用対象のクラス
 */
public aspect AccessRightValidation {
	before(AbstractLangridService self)
		throws NoAccessPermissionException
		, ServiceConfigurationException
		, UnknownException
		: execution(@AccessRightValidatedMethod * 
				(jp.go.nict.langrid.foundation..* && AbstractLangridService+).*(..)
				throws NoAccessPermissionException
				, ServiceConfigurationException
				, UnknownException)
		&& this(self)
	{
		try{
			CodeSignature sig = (CodeSignature)thisJoinPoint.getSignature();
			Class<?>[] types = sig.getParameterTypes();
			String[] names = sig.getParameterNames();
			Object[] args = thisJoinPoint.getArgs();
			Method m = thisJoinPoint.getThis().getClass().getDeclaredMethod(
					sig.getName(), types
					);
			AccessRightValidatedMethod annotation = m.getAnnotation(
					AccessRightValidatedMethod.class
					);
			assert annotation != null;
			AnnotationAccessRightValidator.validate(
					self, annotation, names, args
			);
		} catch(UserNotFoundException e){
			throw new NoAccessPermissionException(e.getUserId());
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
	}
}
