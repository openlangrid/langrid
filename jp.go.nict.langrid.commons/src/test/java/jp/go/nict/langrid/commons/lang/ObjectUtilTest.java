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

import jp.go.nict.langrid.commons.lang.test.AnonymousRunnableFactory;
import jp.go.nict.langrid.commons.lang.test.Impl1Factory;

import org.junit.Assert;
import org.junit.Test;

public class ObjectUtilTest {
	@Test
	public void invoke() throws Exception{
		ObjectUtil.invoke(new Runnable() {
			public void run() {
			}
		}, "run");
	}

	@Test
	public void invoke_nonPublicClass() throws Exception{
		ObjectUtil.invoke(AnonymousRunnableFactory.creatRunnable(), "run");
		Assert.assertTrue(true);
	}

	public static class Class1{
		public void func(Number v){}
	}
	@Test
	public void invoke_methodHasBaseParameter() throws Exception{
		ObjectUtil.invoke(new Class1(), "func", 10);
		Assert.assertTrue(true);
	}

	public static interface Intf1{
		public void func(Number v);
	}
	@Test
	public void invoke_methodHasBaseParameter2() throws Exception{
		ObjectUtil.invoke(Impl1Factory.create(), "func", 10);
		Assert.assertTrue(true);
	}

	public static class C1{}
	public static class C2 extends C1{}
	public static class C3 extends C2{}
	public static class Amb{
		public void func(C1 v, C2 v2){System.out.println("c1");}
		public void func(C2 v, C1 v2){System.out.println("c2");}
	}
	@Test
	public void invoke_amb() throws Exception{
		try{
			System.out.println(Amb.class.getMethod("func", C3.class, C3.class));
			Assert.fail();
		} catch(NoSuchMethodException e){
		}
	}

	public static class C4{
		public C4() {
		}
		public C4(int v) {
			this.v = v;
		}
		public int getV() {
			return v;
		}
		public void setV(int v) {
			this.v = v;
		}
		private int v;
	}
	@Test
	public void test_getProperty() throws Exception{
		Assert.assertEquals(100, ObjectUtil.getProperty(new C4(100), "v"));
	}
	@Test
	public void test_setProperty() throws Exception{
		C4 c = new C4(30);
		ObjectUtil.setProperty(c, "v", 100);
		Assert.assertEquals(100, c.getV());
	}
}
