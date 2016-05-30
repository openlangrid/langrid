/*
 * $Id: CollectionUtil.java 639 2013-02-19 10:23:21Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.function.Predicate;
import jp.go.nict.langrid.commons.util.stream.IteratorProvider;
import jp.go.nict.langrid.commons.util.stream.Stream;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 639 $
 */
public class CollectionUtil {
	/**
	 * 
	 * 
	 */
	public static <T> boolean equalsAsSet(
			Collection<T> value1, Collection<T> value2)
	{
		Set<Object> s1 = new HashSet<Object>();
		for(Object v : value1){
			s1.add(v);
		}
		Set<Object> s2 = new HashSet<Object>();
		for(Object v : value2){
			s2.add(v);
		}
		return s1.equals(s2);
	}

	/**
	 * 
	 * 
	 */
	public static <T> boolean equalsAsSet(
			Collection<T> value1, Collection<T> value2
			, Class<T> clazz, String equalsMethodName)
		throws IllegalAccessException, InvocationTargetException
		, NoSuchMethodException
	{
		Method m = null;
		try{
			m = clazz.getMethod(equalsMethodName, clazz);
		} catch(NoSuchMethodException e){
			m = clazz.getMethod(equalsMethodName, Object.class);
		}
		return equalsAsSet(value1, value2, m);
	}

	/**
	 * 
	 * 
	 */
	public static <T> boolean equalsAsSet(
			Collection<T> value1, Collection<T> value2
			, Method equalsMethod)
		throws IllegalAccessException, InvocationTargetException
	{
		if(!equalsMethod.getReturnType().equals(boolean.class)){
			return false;
		}
		List<T> s1 = new LinkedList<T>();
		for(T v : value1){
			s1.add(v);
		}
		for(T v : value2){
			Iterator<T> i2 = s1.iterator();
			boolean found = false;
			while(i2.hasNext()){
				if(((Boolean)equalsMethod.invoke(
						v, i2.next())).booleanValue())
				{
					i2.remove();
					found = true;
					break;
				}
			}
			if(!found) return false;
		}
		return s1.size() == 0;
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> Map<T, U> asMap(Pair<T, U>... elements){
		Map<T, U> map = new HashMap<T, U>();
		for(Pair<T, U> p : elements){
			map.put(p.getFirst(), p.getSecond());
		}
		return map;
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> Map<T, U> asMap(Map.Entry<T, U>... elements){
		Map<T, U> map = new HashMap<T, U>();
		for(Map.Entry<T, U> p : elements){
			map.put(p.getKey(), p.getValue());
		}
		return map;
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> List<U> collect(
			Iterator<T> iterator, Transformer<T, U> transformer)
		throws TransformationException
	{
		List<U> r = new ArrayList<U>();
		collect(iterator, r, transformer);
		return r;
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> List<U> collect(
			Iterable<T> elements, Transformer<T, U> transformer)
		throws TransformationException
	{
		List<U> r = new ArrayList<U>();
		collect(elements.iterator(), r, transformer);
		return r;
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> void collect(
			Iterator<T> iterator, Collection<U> target, Transformer<T, U> transformer)
		throws TransformationException
	{
		while(iterator.hasNext()){
			target.add(transformer.transform(iterator.next()));
		}
	}

	/**
	 * 
	 * 
	 */
	public static <T> T[] toArray(
			Collection<T> collection, Class<T> elementClass){
		return toArray(collection, elementClass, 0, collection.size());
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(
			Collection<T> collection, Class<T> elementClass, int index, int count){
		int n = collection.size();
		int end = Math.min(index + count, n);
		Object result = Array.newInstance(elementClass, end - index);
		Iterator<T> it = collection.iterator();
		int resultIndex = 0;
		for(int i = 0; i < end; i++){
			if(!it.hasNext()) break;
			Object value = it.next();
			if(i < index) continue;
			Array.set(result, resultIndex, value);
			resultIndex++;
		}
		return (T[])result;
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> emptyCollection(){
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <T> Collection<T> filter(Iterable<T> collection, Predicate<T> pred){
		List ret = new ArrayList();
		for(T v : collection){
			if(pred.test(v)) ret.add(v);
		}
		return ret;
	}

	public static <T> Stream<T> stream(Iterable<T> collection){
		return new Stream<T>(new IteratorProvider<T>(collection.iterator()));
	}
}
