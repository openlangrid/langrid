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
package test.java.lang.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

public class ParameterizedTypeTest {
	@SuppressWarnings("unused")
	private Collection<Integer> intCollection;
	@Test
	public void test_field() throws Exception{
		Field f = ParameterizedTypeTest.class.getDeclaredField("intCollection");
		ParameterizedType type = (ParameterizedType)f.getGenericType();
		Assert.assertEquals(1, type.getActualTypeArguments().length);
		Assert.assertEquals(Integer.class, type.getActualTypeArguments()[0]);
	}
}
