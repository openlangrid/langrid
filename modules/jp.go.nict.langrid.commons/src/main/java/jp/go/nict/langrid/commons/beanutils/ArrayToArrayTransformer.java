/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 Language Grid Project.
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

import java.lang.reflect.Array;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;

public class ArrayToArrayTransformer<T, U> implements Transformer<T, U>{
	public ArrayToArrayTransformer(Converter converter, Class<U> targetClass){
		this.converter = converter;
		if(!targetClass.isArray()){
			throw new IllegalArgumentException("targetClass must be an array class");
		}
		this.targetClass = targetClass.getComponentType();
	}

	public U transform(final T value) throws TransformationException {
		if(value == null) return null;
		return transformArray(value, targetClass);
	}

	@SuppressWarnings("unchecked")
	private U transformArray(Object array, Class<?> targetClass){
		int n = Array.getLength(array);
		Object ret = Array.newInstance(targetClass, n);
		for(int i = 0; i < n; i++){
			Array.set(ret, i, converter.convert(Array.get(array, i), targetClass));
		}
		return (U)ret;
	}

	private Converter converter;
	private Class<?> targetClass;
}
