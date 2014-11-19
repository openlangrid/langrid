/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.transformer.PassthroughTransformer;
import jp.go.nict.langrid.commons.transformer.Transformer;

public class ClassResourceLoader {
	public static void load(Object instance)
	throws IOException, IllegalArgumentException, IllegalAccessException{
		Class<?> clazz = instance.getClass();
		load(clazz, instance, clazz.getDeclaredFields());
	}

	public static <T> void load(T instance, Class<T> clazz)
	throws IOException, IllegalArgumentException, IllegalAccessException{
		load(clazz, instance, clazz.getDeclaredFields());
	}

	public static void load(Class<?> clazz)
	throws IOException, IllegalArgumentException, IllegalAccessException{
		load(clazz, clazz, clazz.getDeclaredFields());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static final void load(Class<?> clazz, Object instance, Field[] fields)
			throws IOException, IllegalArgumentException, IllegalAccessException{
		for(Field f : fields){
			ClassResource a = f.getAnnotation(ClassResource.class);
			if(a == null) continue;
			f.setAccessible(true);
			String name = a.path();
			if(name.length() == 0){
				name = f.getName();
			}
			InputStream is = clazz.getResourceAsStream(name);
			if(is == null){
				throw new IOException("failed to find resource: " + name);
			}
			try{
				byte[] bytes = StreamUtil.readAsBytes(is);
				Class<?> c = a.converter();
				if(c.equals(PassthroughTransformer.class)){
					f.set(instance, bytes);
				} else if(Transformer.class.isAssignableFrom(c)){
					f.set(instance, ((Transformer)c.newInstance()).transform(bytes));
				} else{
					throw new IllegalArgumentException("Unknown converter: "+ c.getName());
				}
			} catch (InstantiationException e) {
				throw new IllegalArgumentException(e);
			} finally{
				is.close();
			}
		}
	}
}
