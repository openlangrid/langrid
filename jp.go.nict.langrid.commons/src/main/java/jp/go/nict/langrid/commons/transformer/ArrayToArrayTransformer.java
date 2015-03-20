/*
 * $Id: ArrayToArrayTransformer.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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

import java.lang.reflect.Type;

import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;
import jp.go.nict.langrid.commons.util.ArrayUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class ArrayToArrayTransformer<T, U>
implements Transformer<T[], U[]>
{
	/**
	 * 
	 * 
	 */
	public ArrayToArrayTransformer(
			Class<U> targetClass
			, Transformer<T, U> elementTransformer)
		throws IllegalArgumentException
	{
		this.targetClass = targetClass;
		this.elementTransformer = elementTransformer;
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayToArrayTransformer(
			Transformer<T, U> elementTransformer)
		throws IllegalArgumentException
	{
		this.elementTransformer = elementTransformer;

		Type[] types = GenericsUtil.getActualTypeArgumentTypes(
				elementTransformer.getClass()
				, Transformer.class);
		this.targetClass = (Class<U>)types[1];
		if(this.targetClass == null){
			throw new IllegalArgumentException(
					"failed to extract target class info from elementTransformer"
					);
		}
	}

	public U[] transform(T[] value) throws TransformationException {
		if(value == null) return null;
		return ArrayUtil.collect(
				value, targetClass, elementTransformer
				);
	}

	private Class<U> targetClass;
	private Transformer<T, U> elementTransformer;
}
