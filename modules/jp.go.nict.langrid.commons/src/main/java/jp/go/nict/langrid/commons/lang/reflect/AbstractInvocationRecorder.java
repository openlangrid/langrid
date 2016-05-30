/*
 * $Id: AbstractInvocationRecorder.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public abstract class AbstractInvocationRecorder<T> {
	/**
	 * 
	 * 
	 */
	public AbstractInvocationRecorder(Class<T> targetClazz){
		this.targetClazz = targetClazz;
	}

	/**
	 * 
	 * 
	 */
	public void play(T target)
	throws InvocationTargetException, IllegalAccessException
	{
		for(Record r : records){
			r.play(target);
		}
	}

	/**
	 * 
	 * 
	 */
	protected void record(Object... args){
		StackTraceElement ste = new Exception().getStackTrace()[1];
		String methodName = ste.getMethodName();

		Method method = null;
		for(Method m : targetClazz.getMethods()){
			if(!m.getName().equals(methodName)) continue;
			if(m.getParameterTypes().length != args.length) continue;
			if(method != null){
				throw new RuntimeException("failed to detect suitable method");
			}
			method = m;
		}
		if(method == null){
			throw new RuntimeException("method not found: " + methodName);
		}

		records.add(new Record(method, args));
	}

	private static class Record{
		/**
		 * 
		 * 
		 */
		public Record(Method method, Object[] args){
			this.method = method;
			this.args = args;
		}

		/**
		 * 
		 * 
		 */
		public void play(Object target)
		throws InvocationTargetException, IllegalAccessException
		{
			method.invoke(target, args);
		}

		private Method method;
		private Object[] args;
	}

	private List<Record> records = new ArrayList<Record>();
	private Class<T> targetClazz;
}
