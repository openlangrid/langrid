/*
 * $Id: ObjectToEnumTransformer.java 561 2012-08-06 10:47:12Z t-nakaguchi $
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import jp.go.nict.langrid.commons.util.ArrayUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 561 $
 */
public class ObjectToEnumTransformer<T extends Enum<T>> 
implements Transformer<Object, T>{
	/**
	 * 
	 * 
	 */
	public ObjectToEnumTransformer(Class<T> clazz){
		this.clazz = clazz;
		this.constants = clazz.getEnumConstants();
		try{
			valueOf = clazz.getDeclaredMethod("valueOf", String.class);
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
	}

	public T transform(Object value) throws TransformationException {
		try{
			if(value instanceof Number && constants != null){
				int i = ((Number)value).intValue();
				if(i < constants.length){
					return constants[i];
				}
			}
			return TransformerUtil.convertWithStaticMethod(
					valueOf, value.toString(), clazz);
		} catch(TransformationException e){
			if(e.getCause() instanceof InvocationTargetException){
				Throwable t = ((InvocationTargetException)e.getCause()).getCause();
				if(t instanceof IllegalArgumentException){
					try{
						throw new TransformationException(
								"\"" + value + "\" is not valid and must be one of "
								+ Arrays.toString(getCandidates(clazz))
								);
					} catch(IllegalAccessException ex){
						throw e;
					} catch(InvocationTargetException ex){
						throw e;
					} catch(NoSuchMethodException ex){
						throw e;
					}
				}
			}
			throw e;
		}
	}

	private String[] getCandidates(Class<T> c)
	throws IllegalAccessException, InvocationTargetException
	, NoSuchMethodException
	{
		Object[] candidates = (Object[])c.getMethod("values").invoke(null);
		return ArrayUtil.collect(candidates, new ToStringTransformer<Object>());
	}

	private Class<T> clazz;
	private T[] constants;
	private Method valueOf;
}
