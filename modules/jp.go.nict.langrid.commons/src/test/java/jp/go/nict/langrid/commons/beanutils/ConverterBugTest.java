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

import org.junit.Test;

public class ConverterBugTest {
	public static class C1{
		public C1(int value){
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		private int value;
	}
	public static class C2{
		public C2(){
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		private int value;
	}
	@Test
	public void test_bean_to_bean() throws Exception{
		new Converter().convert(new C1[]{new C1(1), new C1(2), new C1(3)},
				C2[].class);
	}
}
