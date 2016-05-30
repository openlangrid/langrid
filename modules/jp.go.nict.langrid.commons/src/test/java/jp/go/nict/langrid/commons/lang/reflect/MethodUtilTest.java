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
package jp.go.nict.langrid.commons.lang.reflect;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Callable;

import org.junit.Assert;
import org.junit.Test;

public class MethodUtilTest {
	@Test
	@SuppressWarnings("rawtypes")
	public void test() throws Exception{
		final Callable c = new Callable() {
			@Override
			public Object call() throws Exception {
				throw new IOException("e");
			}
		};
		Callable proxy = (Callable)Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class[]{Callable.class},
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						return MethodUtil.invokeMethodErasingITE(method, c, args);
					}
				});
		try{
			proxy.call();
			Assert.fail();
		} catch(IOException e){
		}
	}
}
