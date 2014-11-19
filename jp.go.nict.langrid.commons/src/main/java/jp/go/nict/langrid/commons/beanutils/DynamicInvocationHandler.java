/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.beanutils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.go.nict.langrid.commons.util.Pair;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class DynamicInvocationHandler<T> implements InvocationHandler{
	/**
	 * 
	 * 
	 */
	public DynamicInvocationHandler(T target, Converter converter){
		this.target = target;
		this.converter = converter;
	}

	protected void preInvocation(){}
	protected void postInvocation(){}

	protected T getTarget(){
		return target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
	throws Throwable {
		preInvocation();
		try {
			Pair<Method, Object[]> m = findMethod(target.getClass(), method, args);
			if(m == null){
				throw new RuntimeException("no suitable stub method.");
			}
			return convert(
					m.getFirst().invoke(target, m.getSecond())
					, method.getReturnType()
					);
		} catch(InvocationTargetException e){
			throw convertExcpetion(e.getCause(), method.getExceptionTypes());
		} finally{
			postInvocation();
		}
	}

	private Throwable convertExcpetion(Throwable e, Class<?>[] types){
		Class<?> clazz = e.getClass();
		String sn = clazz.getSimpleName();
		for(Class<?> t : types){
			if(t.isAssignableFrom(clazz)) return e;
			if(sn.equalsIgnoreCase(t.getSimpleName())){
				return (Throwable)convert(e, t);
			}
		}
		return e;
	}

	private Pair<Method, Object[]> findMethod(Class<?> targetClass, Method method, Object[] args){
		for(Method m : target.getClass().getMethods()){
			if(!m.getName().equals(method.getName())) continue;
			try{
				Object[] convertedArgs = convert(args, m.getParameterTypes());
				return Pair.create(m, convertedArgs);
			} catch(ConversionException e){
				continue;
			}
		}
		return null;
	}

	private Object[] convert(Object[] arguments, Class<?>[] convertTypes)
	throws ConversionException
	{
		if(arguments.length != convertTypes.length){
			throw new ConversionException("length not match.");
		}
		Object[] convertedArgs = new Object[arguments.length];
		for(int i = 0; i < arguments.length; i++){
			convertedArgs[i] = converter.convert(arguments[i], convertTypes[i]);
		}
		return convertedArgs;
	}

	private <U> U convert(Object source, Class<U> targetClass){
		if(source == null) return null;
		if(targetClass.isAssignableFrom(source.getClass())) return targetClass.cast(source);
		return converter.convert(source, targetClass);
	}

	private T target;
	private Converter converter;
}
