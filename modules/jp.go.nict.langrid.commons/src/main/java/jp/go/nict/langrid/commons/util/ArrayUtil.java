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
package jp.go.nict.langrid.commons.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;
import jp.go.nict.langrid.commons.transformer.Transformer;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ArrayUtil {
	/**
	 * 
	 * 
	 */
	@SafeVarargs
	public static <T> T[] array(T... elements){
		return elements;
	}

	@SuppressWarnings("unchecked")
	public static  <T> T[] array(Class<T> elementClass, Object... elements){
		Object ret = Array.newInstance(elementClass, elements.length);
		System.arraycopy(
				elements,
				0,
				ret,
				0,
				elements.length
				);
		return (T[])ret;
	}

	/**
	 * 
	 * 
	 */
	public static Character[] box(char[] elements){
		Character[] ret = new Character[elements.length];
		for(int i = 0; i < elements.length; i++){
			ret[i] = elements[i];
		}
		return ret;
	}

	/**
	 * 
	 * 
	 */
	public static <T> T first(T[] elements){
		return elements[0];
	}

	/**
	 * 
	 * 
	 */
	public static <T> T last(T[] elements){
		return elements[elements.length - 1];
	}

	/**
	 * 
	 * 
	 */
	public static <T> T getWithinBound(T[] elements, int index){
		if(index < 0 || elements.length <= index){
			return null;
		} else{
			return elements[index];
		}
	}

	/**
	 * 
	 * 
	 */
	public static String toString(Object array){
		Function<Object, String> transformer
			= primitiveArrayTransformers.get(array.getClass());
		if(transformer != null){
			return transformer.apply(array);
		} else if(Object[].class.isAssignableFrom(array.getClass())){
			return Arrays.toString((Object[])array);
		} else{
			return array.toString();
		}
	}

	/**
	 * 
	 * 
	 */
	public static <T> boolean elementsEqual(T[] src, T[] dst){
		if(src.length != dst.length) return false;
		for(int i = 0; i < src.length; i++){
			if(src[i] != null){
				if(src[i].equals(dst[i])) continue;
				else return false;
			}
			if(dst[i] != null){
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	public static <T> T[] toArray(Iterable<T> elements, T[] a){
		List<T> array = new ArrayList<T>();
		for(T e : elements){
			array.add(e);
		}
		return array.toArray(a);
	}

	/**
	 * 
	 * 
	 */
	public static String[] emptyStrings(){
		return emptyStrings_;
	}

	/**
	 * 
	 * 
	 */
	public static <T> T[] clone(T[] elements){
		if(elements != null){
			return elements.clone();
		} else{
			return null;
		}
	}

	/**
	 * 
	 * 
	 */
	public static <T> T[] append(T[] elements, T element){
		List<T> list = new ArrayList<T>(Arrays.asList(elements));
		list.add(element);
		return list.toArray(elements);
	}

	/**
	 * 
	 * 
	 */
	@SafeVarargs
	public static <T> T[] append(T[] elements, T... elements2){
		List<T> list = new ArrayList<T>(Arrays.asList(elements));
		for(T e : elements2){
			list.add(e);
		}
		return list.toArray(elements);
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] subArray(T[] elements, int beginIndex, int endIndex){
		return Arrays.asList(elements).subList(beginIndex, endIndex).toArray(
				(T[])Array.newInstance(
						elements.getClass().getComponentType()
						, endIndex - beginIndex
						)
				);
	}

	/**
	 * 
	 * 
	 */
	public static <T> T[] subArray(T[] elements, int beginIndex){
		return subArray(elements, beginIndex, elements.length);
	}

	/**
	 * 
	 * 
	 */
	public static String join(String[] elements, String separator){
		if(elements.length == 0) return "";
		StringBuilder b = new StringBuilder();
		for(String e : elements){
			b.append(e);
			b.append(separator);
		}
		b.delete(b.length() - separator.length(), b.length());
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String join(Object[] elements, String separator){
		StringBuilder b = new StringBuilder();
		for(Object e : elements){
			b.append(e.toString());
			b.append(separator);
		}
		b.delete(b.length() - separator.length(), b.length());
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> U[] collect(
			T[] elements, Class<U> clazz, Function<T, U> mapper)
	{
		U[] r = (U[])Array.newInstance(clazz, elements.length);
		for(int i = 0; i < elements.length; i++){
			r[i] = mapper.apply(elements[i]);
		}
		return r;
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> U[] collect(
			T[] elements, Function<T, U> mapper)
	{
		Class<?>[] types = GenericsUtil.getTypeArgumentClasses(
				mapper.getClass(), Transformer.class);
		if(types == null || types[1] == null){
			throw new IllegalArgumentException(
					"failed to resolve target class"
					);
		}
		U[] r = (U[])Array.newInstance(types[1], elements.length);
		for(int i = 0; i < elements.length; i++){
			r[i] = mapper.apply(elements[i]);
		}
		return r;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] filter(T[] elements, Predicate<T> pred){
		List<T> ret = new ArrayList<T>();
		for(T e : elements){
			if(pred.test(e)){
				ret.add(e);
			}
		}
		return ret.toArray((T[])Array.newInstance(elements.getClass().getComponentType(), 0));
	}
	
	/**
	 * 
	 * 
	 */
	public static byte[][] toBytesArray(String[] value, String encoding)
		throws UnsupportedEncodingException
	{
		byte[][] ret = new byte[value.length][];
		for(int i = 0; i < value.length; i++){
			ret[i] = value[i].getBytes(encoding);
		}
		return ret;
	}
	
	/**
	 * 
	 * 
	 */
	public static String[] toStringArray(byte[][] value, String encoding)
		throws UnsupportedEncodingException
	{
		String[] ret = new String[value.length];
		for(int i = 0; i < value.length; i++){
			ret[i] = new String(value[i], encoding);
		}
		return ret;
	}

	/**
	 * 
	 * 
	 */
	public static <T> Object toPrimitiveArray(Collection<T> collection, Class<T> clazz)
	throws IllegalArgumentException
	{
		Class<?> p = ClassUtil.getPrimitiveClass(clazz);
		if(p == null){
			throw new IllegalArgumentException(clazz.getName() + " is not a wrapper class");
		}
		int len = collection.size();
		Iterator<T> it = collection.iterator();
		Object ret = Array.newInstance(p, len);
		for(int i = 0; i < len; i++){
			Array.set(ret, i, it.next());
		}
		return ret;
	}

	public static <T> Stream<T> stream(T[] array){
		return Arrays.stream(array);
	}

	private static String[] emptyStrings_ = new String[]{};
	private static Map<Class<?>, Function<Object, String>>
		primitiveArrayTransformers = new HashMap<Class<?>, Function<Object,String>>(); 

	static{
		primitiveArrayTransformers.put(
				boolean[].class
				, value -> Arrays.toString((boolean[])value));
		primitiveArrayTransformers.put(
				byte[].class
				, value -> Arrays.toString((byte[])value));
		primitiveArrayTransformers.put(
				char[].class
				, value -> Arrays.toString((char[])value));
		primitiveArrayTransformers.put(
				double[].class
				, value -> Arrays.toString((double[])value));
		primitiveArrayTransformers.put(
				float[].class
				, value -> Arrays.toString((float[])value));
		primitiveArrayTransformers.put(
				int[].class
				, value -> Arrays.toString((int[])value));
		primitiveArrayTransformers.put(
				long[].class
				, value -> Arrays.toString((long[])value));
		primitiveArrayTransformers.put(
				short[].class
				, value -> Arrays.toString((short[])value));
	}
}
