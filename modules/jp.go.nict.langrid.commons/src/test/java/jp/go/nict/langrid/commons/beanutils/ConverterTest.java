/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 Language Grid Project.
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

import java.util.HashMap;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;

public class ConverterTest {
	@Test
	public void test_string_short() throws Exception{
		Assert.assertEquals((Short)(short)1, new Converter().convert("1", Short.class));
	}

	@Test
	public void test_string_int() throws Exception{
		Assert.assertEquals((Integer)1, new Converter().convert("1", Integer.class));
	}

	@Test
	public void test_string_float() throws Exception{
		Assert.assertEquals((Float)1.0f, new Converter().convert("1", Float.class));
	}

	@Test
	public void test_string_double() throws Exception{
		Assert.assertEquals((Double)1.0, new Converter().convert("1", Double.class));
	}

	enum EN{enum1}
	@Test
	public void test_enum_String() throws Exception{
		Assert.assertEquals("enum1", new Converter().convert(EN.enum1, String.class));
	}

	public static class TestClass{
		public TestClass() {
		}
		public TestClass(int a, String b, double c, TestClass d) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
		}
		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public String getB() {
			return b;
		}
		public void setB(String b) {
			this.b = b;
		}
		public double getC() {
			return c;
		}
		public void setC(double c) {
			this.c = c;
		}
		public TestClass getD() {
			return d;
		}
		public void setD(TestClass d) {
			this.d = d;
		}
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
		private int a;
		private String b;
		private double c;
		private TestClass d;
	}

	@Test
	@SuppressWarnings("serial")
	public void test_map_bean() throws Exception{
		Assert.assertEquals(
				new TestClass(10, "hello", 3.5, new TestClass(100, "world", 2.8, null)),
				new Converter().convert(new HashMap<String, Object>(){{
					put("a", 10);
					put("b", "hello");
					put("c", 3.5);
					put("d", new HashMap<String, Object>(){{
						put("a", 100);
						put("b", "world");
						put("c", 2.8);
					}});
				}}, TestClass.class));
	}

	public static class TestClass2{
		public TestClass2() {
		}
		public TestClass2(int a, String b, char cc) {
			this.a = a;
			this.b = b;
			this.cc = cc;
		}
		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public String getB() {
			return b;
		}
		public void setB(String b) {
			this.b = b;
		}
		public char getCc() {
			return cc;
		}
		public void setCc(char cc) {
			this.cc = cc;
		}
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
		private int a;
		private String b;
		private char cc;
	}

	@Test
	public void test_bean_bean() throws Exception{
		TestClass2 expected = new TestClass2(30, "converter", (char)0);
		Assert.assertEquals(expected,
				new Converter().convert(new TestClass(30, "converter", -1, null), TestClass2.class));
	}

	@Test
	@SuppressWarnings("serial")
	public void test_bean_bean_withalias() throws Exception{
		Converter c = new Converter();
		c.addTransformerConversion(new BeanToBeanTransformer<TestClass, TestClass2>(
				c, new HashMap<String, String>(){{put("c", "cc");}}){});
		c.addTransformerConversion(new Transformer<Double, Character>(){
			@Override
			public Character transform(Double value) throws TransformationException {
				return (char)value.intValue();
			}
		});
		Assert.assertEquals(
				new TestClass2(0, "converter", (char)30),
				c.convert(new TestClass(0, "converter", 30, null), TestClass2.class));
	}

	@Test
	public void test_intarray_stringarray() throws Exception{
		Converter c = new Converter();
		String[] ret = c.convert(new int[]{1, 2, 3}, String[].class);
		Assert.assertEquals("1", ret[0]);
		Assert.assertEquals("2", ret[1]);
		Assert.assertEquals("3", ret[2]);
	}

	@Test
	public void test_stringarray_intarray() throws Exception{
		Converter c = new Converter();
		int[] ret = c.convert(new String[]{"1", "2", "3"}, int[].class);
		Assert.assertEquals(1, ret[0]);
		Assert.assertEquals(2, ret[1]);
		Assert.assertEquals(3, ret[2]);
	}

	@Test
	public void test_stringarray_Integerarray() throws Exception{
		Converter c = new Converter();
		Integer[] ret = c.convert(new String[]{"1", "2", "3"}, Integer[].class);
		Assert.assertEquals((Integer)1, ret[0]);
		Assert.assertEquals((Integer)2, ret[1]);
		Assert.assertEquals((Integer)3, ret[2]);
	}

	@Test
	public void test_convertEachObject() throws Exception{
		String[] array = {"1", "2", "3"};
		Object[] ret = new Converter().convertEachElement(
				array, new Class<?>[]{Integer.class, Integer.class, Integer.class});
		Assert.assertEquals(1, ret[0]);
		Assert.assertEquals(2, ret[1]);
		Assert.assertEquals(3, ret[2]);
	}
}
