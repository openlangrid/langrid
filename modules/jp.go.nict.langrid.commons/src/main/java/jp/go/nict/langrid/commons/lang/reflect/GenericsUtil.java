/*
 * $Id: GenericsUtil.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.lang.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.commons.util.ArrayUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class GenericsUtil {
	/**
	 * 
	 * 
	 */
	public static Type[] getActualTypeArgumentTypes(
			Class<?> clazz, Class<?> parameterizedSuperOrInterface)
	{
		do{
			{
				Type sp = clazz.getGenericSuperclass();
				if(sp instanceof ParameterizedType){
					ParameterizedType pt = (ParameterizedType)sp;
					if(pt.getRawType().equals(parameterizedSuperOrInterface)){
						return pt.getActualTypeArguments();
					}
				}
			}
			for(Type intf : clazz.getGenericInterfaces()){
				if(intf instanceof ParameterizedType){
					ParameterizedType pt = (ParameterizedType)intf;
					if(pt.getRawType().equals(parameterizedSuperOrInterface)){
						return pt.getActualTypeArguments();
					}
				}
			}
			clazz = clazz.getSuperclass();
		} while(clazz != null);
		return null;
	}

	/**
	 * 
	 * 
	 */
	public static Class<?>[] getTypeArgumentClasses(
			Class<?> rootClass, Class<?> targetClass)
	{
		return doGetTypeArgumentClasses(
			rootClass, null, targetClass
			);
	}

	private static Class<?>[] doGetTypeArgumentClasses(
			Class<?> clazz, Class<?>[] typeArgs, Class<?> targetClass)
	{
		if(clazz == null) return null;
		if(clazz.equals(targetClass)) return typeArgs;

		// make arg map[TypeVariable -> Class]
		Map<TypeVariable<?>, Class<?>> params = new HashMap<TypeVariable<?>, Class<?>>();
		TypeVariable<?>[] variables = clazz.getTypeParameters();
		if((typeArgs != null) && (typeArgs.length == variables.length)){
			for(int i = 0; i < variables.length; i++){
				params.put(variables[i], typeArgs[i]);
			}
		}

		// search interfaces and super class
		for(Type superType : ArrayUtil.append(
				clazz.getGenericInterfaces()
				, clazz.getGenericSuperclass()
				)){
			Class<?> superClass = null;
			Class<?>[] argClasses = null;
			if(superType instanceof ParameterizedType){
				ParameterizedType pt = (ParameterizedType)superType;
				Type[] argTypes = pt.getActualTypeArguments();
				argClasses = new Class<?>[argTypes.length];
				for(int i = 0; i < argTypes.length; i++){
					Type at = argTypes[i];
					if(at instanceof Class<?>){
						argClasses[i] = (Class<?>)at;
					} else{
						argClasses[i] = params.get(at);
					}
				}
				superClass = (Class<?>)pt.getRawType();
			} else{
				superClass = (Class<?>)superType;
			}
			Class<?>[] ret = doGetTypeArgumentClasses(
					superClass, argClasses, targetClass
					);
			if(ret != null) return ret;
		}

		return null;
	}
}
