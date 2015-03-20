/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
 * Copyright (C) 2013 Language Grid Project.
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
package jp.go.nict.langrid.commons.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.commons.lang.reflect.MethodUtil;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;

/**
 * 
 * @author Takao Nakaguchi
 */
public class ObjectUtil {
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object object){
		return (T)object;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object instance, String name)
	throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		return (T)ClassUtil.findGetter(instance.getClass(), name).invoke(instance);
	}

	public static void setProperty(Object instance, String name, Object value)
	throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		ClassUtil.findSetter(instance.getClass(), name).invoke(instance, value);
	}

	public static void padProperties(Object object){
		padProperties(object, new HashSet<Class<?>>(), 0);
	}

	private static void padProperties(Object object, Set<Class<?>> classes, int nest){
		Class<?> c = object.getClass();
		if(classes.contains(c) && nest > 16) return;
		classes.add(c);
		for(Method m : c.getMethods()){
			if(!MethodUtil.isGetter(m)) continue;
			Method s = ClassUtil.findSetter(m);
			if(s == null) return;
			try {
				s.invoke(object, ClassUtil.newDummyInstance(m.getReturnType()));
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			} catch (IllegalArgumentException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public static int getSize(Object... objects){
		int size = 0;
		for(Object o : objects){
			size += o.toString().length();
		}
		return size;
	}

	/**
	 * 
	 * 
	 */
	public static Object invoke(Object instance, String methodName
			, Class<?>[] parameterTypes, Object[] parameters)
	throws IllegalAccessException, InvocationTargetException
	, NoSuchMethodException
	{
		Method m = instance.getClass().getMethod(methodName, parameterTypes);
		return m.invoke(instance, parameters);
	}

	/**
	 * 
	 * 
	 */
	public static Object invoke(Class<?> clazz, String methodName
			, Object... parameters)
	throws IllegalAccessException, InvocationTargetException
	, NoSuchMethodException
	{
		return doInvoke(clazz, null, methodName, parameters);
	}

	/**
	 * 
	 * 
	 */
	public static Object invoke(Object instance, String methodName
			, Object... parameters)
	throws IllegalAccessException, InvocationTargetException
	, NoSuchMethodException
	{
		return doInvoke(instance.getClass(), instance, methodName, parameters);
	}

	public static InvocationStream invocationStream(Object value){
		return new InvocationStream(value);
	}

	@SuppressWarnings("rawtypes")
	private static <T> Object doInvoke(Class<? extends T> clazz, T instance, String methodName
			, Object... parameters)
	throws IllegalAccessException, InvocationTargetException
	, NoSuchMethodException
	{
		Class<?>[] parameterTypes = ArrayUtil.collect(
				parameters, Class.class, new Transformer<Object, Class>(){
					public Class<?> transform(Object value)
					throws TransformationException {
						return value.getClass();
					}
				});
		NoSuchMethodException nsme = null;
		try{
			return invokeSafely(instance, clazz.getMethod(methodName, parameterTypes), parameters);
		} catch(NoSuchMethodException e){
			nsme = e;
		}
		for(Method m : clazz.getMethods()){
			if(!m.getName().equals(methodName)) continue;
			Class<?>[] types = m.getParameterTypes();
			if(types.length != parameterTypes.length) continue;
			boolean matched = true;
			for(int i = 0; i < parameterTypes.length; i++){
				if(!ClassUtil.isAssignableFrom(types[i], parameterTypes[i])){
					matched = false;
					break;
				}
			}
			if(matched){
				return invokeSafely(instance, m, parameters);
			}
		}
		throw nsme;
	}

	public static Object invokeSafely(Object instance, Method method, Object... args)
	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		try{
			return method.invoke(instance, args);
		} catch(IllegalAccessException e){
			if(instance != null){
				Class<?> clazz = instance.getClass();
				String name = method.getName();
				Class<?>[] ptypes = method.getParameterTypes();
				while(clazz != null){
					for(Class<?> i : clazz.getInterfaces()){
						try{
							return i.getMethod(name, ptypes).invoke(instance, args);
						} catch(NoSuchMethodException e2){
						}
					}
					clazz = clazz.getSuperclass();
				}
			}
			throw e;
		}
	}
}
