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

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.commons.util.stream.Generators;

public class StreamTest {
	@Test
	public void testIntRange() throws Exception{
		Assert.assertEquals(55, Generators.intRange(1, 10).sum());
	}

	@Test
	public void testFilterMapForeach() throws Exception{
		Generators.intRange(1,  11)
			.filter(value -> value % 2 == 0)
			.mapToObj(value -> "" + (value + 10))
			.forEach(new Consumer<String>() {
				@Override
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
			.filter(value -> value % 2 == 0)
			.map(value -> value + 10)
			.reduce(0, (v1, v2) -> v1 + v2);
		Assert.assertEquals(80, ret);
	}
}
