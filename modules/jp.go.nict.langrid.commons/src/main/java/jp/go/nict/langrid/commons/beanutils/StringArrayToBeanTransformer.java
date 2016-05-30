/*
 * $Id:RefreshPartners.java 8164 2008-05-21 07:06:42Z nakaguchi $
 *
 * Language Grid.
 * Copyright (C) 2005-2007 NICT Language Grid Project.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jp.go.nict.langrid.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ListUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 189 $
 */
public class StringArrayToBeanTransformer<T> implements Transformer<String[], T>{
	/**
	 * 
	 * 
	 */
	public StringArrayToBeanTransformer(Class<T> clazz, Converter converter
			, String[] properties){
		this.clazz = clazz;
		this.converter = converter;
		this.properties = properties;
	}

	public T transform(String[] value) throws TransformationException {
		try{
			T o = clazz.newInstance();
			int n = Math.min(value.length, properties.length);
			for(int i = 0; i < n; i++){
				Method[] methods = getSetters(clazz, properties[i]);
				if(methods.length == 0) continue;
				try{
					methods[0].invoke(o, converter.convert(
							value[i], methods[0].getParameterTypes()[0]
							));
				} catch(InvocationTargetException e){
					e.printStackTrace();
				}
			}
			return o;
		} catch(IllegalAccessException e){
			throw new TransformationException(e);
		} catch(InstantiationException e){
			throw new TransformationException(e);
		}
	}

	private Method[] getSetters(Class<T> clazz, String propertyName){
		String methodName = "set"
			+ Character.toUpperCase(propertyName.charAt(0))
			+ propertyName.substring(1);
		List<Method> methods = ListUtil.newArrayList();
		for(Method m : clazz.getMethods()){
			if(!m.getName().equals(methodName)) continue;
			if(m.getParameterTypes().length != 1) continue;
			methods.add(m);
		}
		return methods.toArray(new Method[]{});
	}

	private Class<T> clazz;
	private Converter converter;
	private String[] properties;
}
