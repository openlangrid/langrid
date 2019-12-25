/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.commons.lang.TypeReference;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class ParameterizedTypeTest {
	public List<String> hello(){ return null;}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test() throws Throwable{
		Object r = new Converter().convert(
				JSON.decode("[\"hello\",\"world\"]")
				, ParameterizedTypeTest.class.getMethod("hello").getGenericReturnType()
				);
		Assert.assertTrue(r instanceof List);
		List<String> ret = (List<String>)r;
		Assert.assertEquals("hello", ret.get(0));
		Assert.assertEquals("world", ret.get(1));
	}

	static class User1{
		private String name;
		public User1() {
		}
		public User1(String name) {
			super();
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	public static class User2{
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	@Test
	public void test2() throws Throwable{
		new Converter().convert(new User1("bob"), User2.class);
	}
	@Test
	public void test3() throws Throwable{
		List<User1> u1 = Arrays.asList(new User1("bob"));
		Collection<User2> u2 = new Converter().convertCollection(u1, List.class, User2.class);
		Assert.assertEquals("bob", u2.iterator().next().getName());
	}

	@SuppressWarnings("serial")
	public static class User2List extends ArrayList<User2>{}
	@Test
	public void test4() throws Throwable{
		List<User1> u1 = Arrays.asList(new User1("bob"));
		Collection<User2> u2 = new Converter().convert(u1, User2List.class.getGenericSuperclass());
		Assert.assertEquals("bob", u2.iterator().next().getName());
	}

	@Test
	public void test5() throws Throwable{
		List<User1> u1 = Arrays.asList(new User1("bob"));
		Collection<User2> u2 = new Converter().convert(u1, new TypeReference<List<User2>>(){}.get());
		Assert.assertEquals("bob", u2.iterator().next().getName());
	}
}
