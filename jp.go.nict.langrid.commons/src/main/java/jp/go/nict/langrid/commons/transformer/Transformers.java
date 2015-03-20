/*
 * $Id: Transformers.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;
import jp.go.nict.langrid.commons.lang.reflect.TypeUtil;
import jp.go.nict.langrid.commons.util.Pair;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class Transformers{
	/**
	 * 
	 * 
	 */
	public synchronized <T, U> void addTransformer(
			Class<T> src, Class<U> dst
			, Transformer<T, U> transformer)
	{
		transformers.put(
				new Pair<Class<?>, Class<?>>(src, dst)
				, transformer
				);
		List<Pair<Class<?>, Transformer<?, ?>>> list = dstToTransformer.get(dst);
		if(list == null){
			list = new ArrayList<Pair<Class<?>, Transformer<?,?>>>();
			dstToTransformer.put(dst, list);
		}
		list.add(new Pair<Class<?>, Transformer<?, ?>>(
						src, (Transformer<?, ?>)transformer)
				);
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T, U> void addTransformer(Transformer<T, U> transformer)
	{
		Type[] types = GenericsUtil.getActualTypeArgumentTypes(
				transformer.getClass()
				, Transformer.class
				);

		Class<T> src = (Class<T>)TypeUtil.toClass(types[0]);
		Class<U> dst = (Class<U>)TypeUtil.toClass(types[1]);
		addTransformer(src, dst, transformer);
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T, U> Transformer<T, U> get(Class<T> src, Class<U> dst) {
		Transformer<T, U> transformer = (Transformer<T, U>)transformers.get(
				Pair.create(src, dst)
				);
		if(transformer != null){
			return transformer;
		}

		List<Pair<Class<?>, Transformer<?, ?>>> list = dstToTransformer.get(dst);
		if(list != null){
			for(Pair<Class<?>, Transformer<?, ?>> p : list){
				if(p.getFirst().isAssignableFrom(src)){
					return (Transformer<T, U>)p.getSecond();
				}
			}
		}
		return null;
	}

	private Map<Pair<Class<?>, Class<?>>, Transformer<?, ?>> transformers
		= new HashMap<Pair<Class<?>,Class<?>>, Transformer<?,?>>();
	private Map<Class<?>, List<Pair<Class<?>, Transformer<?, ?>>>> dstToTransformer
		= new HashMap<Class<?>, List<Pair<Class<?>, Transformer<?, ?>>>>();
}
