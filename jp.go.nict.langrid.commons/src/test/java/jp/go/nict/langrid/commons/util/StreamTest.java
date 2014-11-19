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
package jp.go.nict.langrid.commons.util;

import jp.go.nict.langrid.commons.util.function.BiFunction;
import jp.go.nict.langrid.commons.util.function.Consumer;
import jp.go.nict.langrid.commons.util.function.Function;
import jp.go.nict.langrid.commons.util.function.Predicate;
import jp.go.nict.langrid.commons.util.stream.Generators;
import junit.framework.Assert;

import org.junit.Test;

public class StreamTest {
	@Test
	public void testFilterMapForeach() throws Exception{
		Generators.intRange(1,  11)
			.filter(new Predicate<Integer>() {
				public boolean test(Integer value) {
					return value % 2 == 0;
				}
			}).map(new Function<Integer, String>() {
				public String apply(Integer value) {
					return new Integer(value + 10).toString();
				}
			}).forEach(new Consumer<String>() {
				public void accept(String value) {
					Assert.assertEquals(Integer.toString(i), value);
					i += 2;
				}
				private int i = 12;
			});
	}

	@Test
	public void testFilterMapReduce() throws Exception{
		int ret = Generators.intRange(1, 11)
			.filter(new Predicate<Integer>() {
				public boolean test(Integer value) {
					return value % 2 == 0;
				}
			})
			.map(new Function<Integer, Integer>() {
				public Integer apply(Integer value) {
					return value + 10;
				}
			}).reduce(0, new BiFunction<Integer, Integer, Integer>() {
				public Integer apply(Integer v1, Integer v2) {
					return v1 + v2;
				}
			});
		Assert.assertEquals(80, ret);
	}
}
