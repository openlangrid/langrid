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
package jp.go.nict.langrid.commons.lang.reflect;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.commons.lang.StringUtil;

public class GenericsUtilTest {
	@SuppressWarnings("serial")
	@Test
	public void test_class() throws Exception{
		Class<?>[] classes = {ArrayList.class, List.class, Collection.class, Iterable.class};
		for(Class<?> c : classes){
			Assert.assertEquals(
					Integer.class,
					GenericsUtil.getTypeArgumentClasses(new ArrayList<Integer>(){}.getClass(), c)[0]
					);
		}
		Assert.assertNull(
				GenericsUtil.getTypeArgumentClasses(new ArrayList<Integer>().getClass(), ArrayList.class)
				);
	}

	class C1<T>{}
	class C2<T> extends C1<T>{}
	class C3 extends C1<Integer>{}
	class C4 extends C2<Integer>{}
	@Test
	public void test_getActualTypeArgumentTypes() throws Throwable{
		System.out.println("-- C3, C1 --");
		for(Type t : GenericsUtil.getActualTypeArgumentTypes(C3.class, C1.class)){
			System.out.println(t);
		}
		System.out.println("-- C4, C2 --");
		for(Type t : GenericsUtil.getActualTypeArgumentTypes(C4.class, C2.class)){
			System.out.println(t);
		}
		System.out.println("-- C4, C1 --");
		for(Type t : GenericsUtil.getActualTypeArgumentTypes(C4.class, C1.class)){
			System.out.println(t);
		}
		System.out.println("-- C4, C1 --");
		for(Class<?> t : GenericsUtil.getTypeArgumentClasses(C4.class, C1.class)){
			System.out.println(t);
		}
	}

	public List<String> func(){ return null;}

	@Test
	public void test_returntype() throws Throwable{
		Method m = getClass().getMethod("func");
		Type t = m.getGenericReturnType();
		System.out.println(t.getClass());
		ParameterizedType pt = (ParameterizedType)t;
		System.out.println("ActualTypeArguments: " + Arrays.toString(pt.getActualTypeArguments()));
		System.out.println("RawType: " + pt.getRawType());
		System.out.println("OwnerType: " + pt.getOwnerType());
		System.out.println(t);
	}

	public void func2(int arg1, Integer arg2, List<Number> arg3){}
	@Test
	public void test_arg() throws Throwable{
		Method m = getClass().getMethod("func2", int.class, Integer.class, List.class);
		Type[] ptypes = m.getGenericParameterTypes();
		for(int i = 0; i < ptypes.length; i++){
			Type type = ptypes[i];
			Function<Type, String> conv = t -> t + ": " + (t instanceof Class) + ": " + t.getClass().getName() +
					" extends " + t.getClass().getGenericSuperclass().getTypeName() +
					" implements " + StringUtil.join(t.getClass().getGenericInterfaces(), it -> it.getTypeName(), ", ");
			System.out.println(conv.apply(type));
			if(type instanceof ParameterizedType){
				System.out.println("  " + conv.apply(((ParameterizedType)type).getRawType()));
			}
		}
		System.out.println("");
		Class<?>[] pclasses = m.getParameterTypes();
		for(int i = 0; i < pclasses.length; i++){
			Class<?> clz = pclasses[i];
			if(clz instanceof GenericDeclaration){
				for(TypeVariable<?> tv : ((GenericDeclaration)clz).getTypeParameters()){
					System.out.println(tv.getTypeName() + ": " + tv.getGenericDeclaration().getClass());
				}
			}
		}
	}
}
