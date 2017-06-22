/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class MethodUtil {
	/**
	 * 
	 * 
	 */
	public static boolean isGetter(Method method){
		if((method.getModifiers() & Modifier.PUBLIC) == 0) return false;
		if(method.getReturnType().equals(void.class)) return false;
		if(!method.getName().startsWith("get")) return false;
		if(method.getParameterTypes().length > 0) return false;
		return true;
	}

	public static Object invokeMethodErasingITE(Method method, Object instance, Object... args)
	throws Throwable{
		try{
			return method.invoke(instance, args);
		} catch(InvocationTargetException e){
			throw e.getTargetException();
		}
	}

	public static String[] getParameterNames(Method method){
		java.lang.reflect.Parameter[] params = method.getParameters();
		String[] ret = new String[params.length];
		for(int i = 0; i < ret.length; i++){
			Parameter p = params[i].getAnnotation(Parameter.class);
			ret[i] = (p != null) ? p.name() : "arg" + i;
		}
		return ret;
	}
}
