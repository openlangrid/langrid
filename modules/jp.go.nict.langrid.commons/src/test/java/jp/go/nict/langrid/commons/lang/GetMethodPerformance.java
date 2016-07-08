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

import java.lang.reflect.Method;
import java.util.function.LongConsumer;

import jp.go.nict.langrid.commons.util.LapTimer;

public class GetMethodPerformance {
	public class Test{
		public void func1(){}
		public void func2(){}
		public void func3(){}
		public void func4(){}
		public void func5(){}
		public void func6(){}
		public void func7(){}
		public void func8(){}
		public void func9(){}
		public void func10(){}
		public void func1(int a){}
		public void func2(int a){}
		public void func3(int a){}
		public void func4(int a){}
		public void func5(int a){}
		public void func6(int a){}
		public void func7(int a){}
		public void func8(int a){}
		public void func9(int a){}
		public void func10(int a){}
	}
	@org.junit.Test
	public void test() throws Exception{
		LongConsumer print = new LongConsumer(){
			@Override
			public void accept(long value) {
				System.out.println("getMethod: " + value);
			}
		};
		String[] names = new String[10];
		for(int i = 0; i < 10; i++){
			names[i] = "func" + (i + 1);
		}
		Class<?> c = Test.class;
		LapTimer t = new LapTimer();
		for(int i = 0; i < 10000; i++){
			for(String n : names){
				c.getMethod(n, int.class);
			}
		}
		t.lapNanos(print);

		for(int i = 0; i < 10000; i++){
			for(String n : names){
				for(Method m : c.getMethods()){
					Class<?>[] pt = m.getParameterTypes();
					if(m.getName().equals(n) && pt.length == 1 && pt[0].equals(int.class)) break;
				}
			}
		}
		t.lapNanos(print);
	}
}
