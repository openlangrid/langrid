/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2015 The Language Grid Project.
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class LoggingProxy {
	public static Object create(Object instance){
		return create(instance, instance.getClass().getInterfaces(), System.out);
	}

	public static Object create(final Object instance, Class<?>[] interfaces, final Appendable out){
		return Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				interfaces,
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						out.append(method.getDeclaringClass().getSimpleName())
							.append(".")
							.append(method.getName())
							.append("(");
						boolean first = true;
						if(args != null) for(Object o : args){
							if(!first){
								out.append(", ");
							} else{
								first = false;
							}
							if(o != null && o.getClass().isArray()){
								appendArray(o, out);
							} else{
								out.append(o.toString());
							}
						}
						out.append(") -> ");
						try{
							Object r = method.invoke(instance, args);
							if(r != null && r.getClass().isArray()){
								appendArray(r, out);
							} else{
								out.append(r.toString());
							}
							out.append(lineSeparator);
							return r;
						} catch(InvocationTargetException e){
							throw e.getTargetException();
						}
					}
				});
	}

	private static void appendArray(Object ar, Appendable b)
	throws IOException{
		if(ar.getClass().getComponentType().isPrimitive()){
			b.append(ar.toString());
		} else{
			b.append(Arrays.toString((Object[])ar));
		}
	}

	private static final String lineSeparator;
	static{
		String ls = System.getProperty("line.separator");
		if(ls == null){
			ls = "\n";
		}
		lineSeparator = ls;
	}
}
