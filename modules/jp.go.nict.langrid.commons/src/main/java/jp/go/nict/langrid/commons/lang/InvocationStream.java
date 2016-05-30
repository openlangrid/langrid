/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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

public class InvocationStream {
	public static InvocationStream of(Object object){
		return new InvocationStream(object);
	}

	public InvocationStream(Object object){
		this.object = object;
	}

	public Object get(){
		return object;
	}

	public InvocationStream invoke(String methodName, Object...args)
	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		return new InvocationStream(ObjectUtil.invoke(object, methodName, args));
	}

	@SuppressWarnings("unchecked")
	public <T> T invokeAndGet(String methodName, Object...args)
	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		return (T)ObjectUtil.invoke(object, methodName, args);
	}

	public int invokeAndGetInt(String methodName, Object...args)
	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		return ((Number)(ObjectUtil.invoke(object, methodName, args))).intValue();
	}

	public long invokeAndGetLong(String methodName, Object...args)
	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		return ((Number)(ObjectUtil.invoke(object, methodName, args))).longValue();
	}

	private Object object;
}
