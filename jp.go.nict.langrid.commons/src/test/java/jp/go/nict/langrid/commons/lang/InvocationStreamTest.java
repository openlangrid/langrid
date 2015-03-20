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
package jp.go.nict.langrid.commons.lang;

import org.junit.Assert;
import org.junit.Test;

public class InvocationStreamTest {
	@Test
	public void test1() throws Exception{
		Assert.assertEquals(
				1,
				InvocationStream.of(1).invoke("intValue").get()
				);
	}

	public static class TestClass{
		public TestClass(Object[] ar){
			arg = 4;
		}
		public TestClass(int arg){
			this.arg = arg;
		}
		public int getArg(){
			return arg;
		}
		public void setArg(int v){
			this.arg = v;
		}
		public void setArgs(Object...v){
		}
		private int arg;
	}

	@Test
	public void test2() throws Exception{
		Assert.assertEquals(
				1,
				InvocationStream.of(TestClass.class)
					.invoke("getConstructor", new Object[]{new Class<?>[]{int.class}})
					.invoke("newInstance", new Object[]{new Object[]{1}})
					.invoke("getArg")
					.get()
				);
	}

	@Test
	public void test3() throws Exception{
		Assert.assertEquals(
				4,
				InvocationStream.of(TestClass.class)
					.invoke("getConstructor", new Object[]{new Class<?>[]{Object[].class}})
					.invoke("newInstance", new Object[]{new Object[]{new Integer[]{1}}})
					.invoke("getArg")
					.get()
				);
	}
}
