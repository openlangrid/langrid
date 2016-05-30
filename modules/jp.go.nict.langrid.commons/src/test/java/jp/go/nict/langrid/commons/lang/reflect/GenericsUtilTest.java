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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

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
}
