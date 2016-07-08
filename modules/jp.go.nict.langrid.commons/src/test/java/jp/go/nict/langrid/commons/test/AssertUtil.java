/*
 * $Id: AssertUtil.java 11994 2009-04-23 10:43:38Z Takao Nakaguchi $
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
package jp.go.nict.langrid.commons.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;

public class AssertUtil {
	public static <T> void assertArrayEquals(T[] expected, T[] actual){
		Assert.assertEquals(
				Arrays.toString(actual)
				+ " must be equals "
				+ Arrays.toString(expected)
				, expected.length, actual.length
				);
		for(int i = 0; i < expected.length; i++){
			Assert.assertEquals(expected[i], actual[i]);
		}
	}

	public static void assertArrayEquals(byte[] expected, byte[] actual){
		Assert.assertEquals(
				"array size is different."
				, expected.length, actual.length
				);
		for(int i = 0; i < expected.length; i++){
			Assert.assertEquals(
					i + "th element is different"
					, expected[i], actual[i]
					);
		}
	}

	public static <T> void assertArrayEqualsIgnoreOrder(T[] expected, T[] actual){
		assertArrayEqualsIgnoreOrder(null, expected, actual);
	}

	public static <T> void assertArrayEqualsIgnoreOrder(
			String message, T[] expected, T[] actual){
		Assert.assertEquals(getMessage(message
				, String.format("expected %d but was %d.", expected.length, actual.length))
				, expected.length, actual.length);
		Map<T, Integer> exp = new HashMap<T, Integer>();
		for(T e : expected){
			Integer c = exp.get(e);
			if(c != null){
				c++;
			} else{
				c = 1;
			}
			exp.put(e, c);
		}
		for(T a : actual){
			Integer c = exp.get(a);
			Assert.assertNotNull(getMessage(message, a + " is redundant."),  c);
			Assert.assertTrue(c > 0);
			c--;
			if(c == 0){
				exp.remove(a);
			} else{
				exp.put(a, c);
			}
		}
		if(exp.size() != 0){
			StringBuilder b = new StringBuilder();
			for(T e : exp.keySet()){
				b.append(e);
				b.append(",");
			}
			Assert.fail(getMessage(message, b.toString() + " is missing."));
		}
	}

	/**
	 * <#if locale="ja">
	 * 配列の順序を無視して比較する。
	 * 比較メソッドは、booleanを返し、clazz型の引数もしくはObject型の引数をとる必要がある。
	 * @param <T> 要素の型
	 * @param value1 比較する元のコレクション
	 * @param value2 比較する先のコレクション
	 * @param clazz 比較メソッドを取得するクラス
	 * @param equalsMethodName 比較メソッド名
	 * @return 等しい場合true
	 * @throws IllegalAccessException 比較メソッドの実行に失敗した
	 * @throws InvocationTargetException 比較メソッドの実行に失敗した
	 * @throws NoSuchMethodException 比較メソッドが見つからない
	 * <#elseif locale="en">
	 * </#if>
	 */
	public static <T> boolean assertArrayEqualsIgnoreOrder(
			T[] value1, T[] value2
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
		List<T> s1 = new LinkedList<T>();
		for(T v : value1){
			s1.add(v);
		}
		for(T v : value2){
			Iterator<T> i2 = s1.iterator();
			boolean found = false;
			while(i2.hasNext()){
				if(((Boolean)m.invoke(
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
	 * <#if locale="ja">
	 * コレクションをセットとして、順序を無視して比較する。
	 * @param <T> 要素の型
	 * @param value1 比較する元のコレクション
	 * @param value2 比較する先のコレクション
	 * @return 等しい場合true
	 * <#elseif locale="en">
	 * </#if>
	 */
	public static <T> boolean assertEqualsIgnoreOrder(
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
	 * <#if locale="ja">
	 * コレクションをセットとして、順序を無視して比較する。
	 * 比較メソッドは、booleanを返し、clazz型の引数もしくはObject型の引数をとる必要がある。
	 * @param <T> 要素の型
	 * @param value1 比較する元のコレクション
	 * @param value2 比較する先のコレクション
	 * @param clazz 比較メソッドを取得するクラス
	 * @param equalsMethodName 比較メソッド名
	 * @return 等しい場合true
	 * @throws IllegalAccessException 比較メソッドの実行に失敗した
	 * @throws InvocationTargetException 比較メソッドの実行に失敗した
	 * @throws NoSuchMethodException 比較メソッドが見つからない
	 * <#elseif locale="en">
	 * </#if>
	 */
	public static <T> boolean assertEqualsIgnoreOrder(
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
		return assertEqualsIgnoreOrder(value1, value2, m);
	}

	/**
	 * <#if locale="ja">
	 * コレクションをセットとして、順序を無視して比較する。
	 * @param <T> 要素の型
	 * @param value1 比較する元のコレクション
	 * @param value2 比較する先のコレクション
	 * @param equalsMethod 比較に使用するメソッド。要素型引数を一つとり、booleanを返す
	 * @return 等しい場合true
	 * @throws IllegalAccessException 比較メソッドの実行に失敗した
	 * @throws InvocationTargetException 比較メソッドの実行に失敗した
	 * <#elseif locale="en">
	 * </#if>
	 */
	public static <T> boolean assertEqualsIgnoreOrder(
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

	private static String getMessage(String message, String submessage){
		if(message == null) return submessage;
		else return message + "[" + submessage + "]";
	}
}
