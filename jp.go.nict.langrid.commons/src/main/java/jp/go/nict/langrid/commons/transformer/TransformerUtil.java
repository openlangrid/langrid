/*
 * $Id: TransformerUtil.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.transformer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
class TransformerUtil {
	static <T> T fromString(Constructor<T> ctor, String source)
		throws TransformationException
	{
		try{
			return ctor.newInstance(source);
		} catch(IllegalAccessException e){
			throw new TransformationException(e);
		} catch(InstantiationException e){
			throw new TransformationException(e);
		} catch(InvocationTargetException e){
			throw new TransformationException(e);
		}
	}

	static Object convertWithStaticMethod(
			Method method, Object source)
	{
		try{
			if(!method.isAccessible()){
				method.setAccessible(true);
			}
			return method.invoke(null, source);
		} catch(IllegalAccessException e){
			throw new TransformationException(e);
		} catch(InvocationTargetException e){
			throw new TransformationException(e);
		} catch(IllegalArgumentException e){
			throw new TransformationException(e);
		}
	}

	@SuppressWarnings("unchecked")
	static <T> T convertWithStaticMethod(
			Method method, Object source, Class<T> type)
	{
		return (T)convertWithStaticMethod(method, source);
	}
}
