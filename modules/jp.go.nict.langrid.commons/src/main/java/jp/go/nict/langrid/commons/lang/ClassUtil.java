/*
 * Copyright (C) 2002, 2004 Takao Nakaguchi.
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import jp.go.nict.langrid.commons.util.Pair;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ClassUtil {
	public static Object newInstance(Class<?> clazz)
	throws InstantiationException, IllegalAccessException{
		if(clazz.isPrimitive()){
			return primitiveToDefault.get(clazz);
		} else if(Calendar.class.isAssignableFrom(clazz)){
			return Calendar.getInstance();
		} else if(clazz.isArray()){
			Class<?> ct = clazz.getComponentType();
			Object a = Array.newInstance(ct, 1);
			Array.set(a, 0, newInstance(ct));
			return a;
		}
		return clazz.newInstance();
	}

	@SuppressWarnings("rawtypes")
	public static Object newDummyInstance(Class<?> clazz)
	throws InstantiationException, IllegalAccessException{
		if(clazz.isPrimitive()){
			return primitiveToDefault.get(clazz);
		} else if(Calendar.class.isAssignableFrom(clazz)){
			return Calendar.getInstance();
		} else if(clazz.isArray()){
			Class<?> ct = clazz.getComponentType();
			Object a = Array.newInstance(ct, 1);
			Array.set(a, 0, newDummyInstance(ct));
			return a;
		} else if(clazz.isAssignableFrom(ArrayList.class)){
			return new ArrayList();
		} else if(clazz.isAssignableFrom(HashSet.class)){
			return new HashSet();
		} else if(clazz.isAssignableFrom(HashMap.class)){
			return new HashMap();
		}
		Object ret = clazz.newInstance();
		ObjectUtil.padProperties(ret);
		return ret;
	}

	public static Collection<Pair<String, Method>> getReadableProperties(Class<?> clazz){
		List<Pair<String, Method>> ret = new ArrayList<Pair<String,Method>>();
		for(Method m : clazz.getMethods()){
			if(m.getReturnType().equals(void.class)) continue;
			if(m.getParameterTypes().length != 0) continue;
			String name = m.getName();
			if(name.startsWith("get")){
				if(name.length() == 3) continue;
				if(m.getDeclaringClass().equals(Object.class)) continue;
				ret.add(Pair.create(name.substring(3, 4).toLowerCase() + (name.length() > 4 ? name.substring(4) : ""), m));
			} else if(name.startsWith("is")){
				if(name.length() == 2) continue;
				if(m.getDeclaringClass().equals(Object.class)) continue;
				if(!m.getReturnType().equals(boolean.class)) continue;
				ret.add(Pair.create(name.substring(2, 3).toLowerCase() + (name.length() > 3 ? name.substring(3) : ""), m));
			}
		}
		return ret;
	}

	public static Method findGetter(Class<?> clazz, String name)
	throws SecurityException, NoSuchMethodException{
		{
			String getterName = "get" + name.substring(0).toUpperCase() +
					((name.length() > 1) ? name.substring(1) : "");
			Method m = clazz.getMethod(getterName);
			if(m != null){
				if(!m.getReturnType().equals(void.class)) return m;
				return null;
			}
		}
		{
			String getterName = "is" + name.substring(0).toUpperCase() +
					((name.length() > 1) ? name.substring(1) : "");
			Method m = clazz.getMethod(getterName);
			if(m != null){
				if(m.getReturnType().equals(boolean.class)) return m;
				return null;
			}
		}
		return null;
	}

	public static Method findSetter(Method getter){
		String n = getter.getName();
		if(!n.startsWith("get")) return null;
		if(n.length() <= 3) return null;
		try {
			return getter.getDeclaringClass().getMethod("set" + n.substring(3), getter.getReturnType());
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		} 
	}

	public static Method findSetter(Class<?> clazz, String property){
		String setterName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
		for(Method m : clazz.getMethods()){
			if(!m.getReturnType().equals(void.class)) continue;
			if(m.getParameterTypes().length != 1) continue;
			if(m.getName().equals(setterName)) return m;
		}
		return null;
	}

	public static Iterable<Method> findSetters(Class<?> clazz, String property){
		List<Method> ret = new ArrayList<Method>();
		String setterName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
		for(Method m : clazz.getMethods()){
			if(!m.getName().equals(setterName)) continue;
			if(!m.getReturnType().equals(void.class)) continue;
			if(m.getParameterTypes().length != 1) continue;
			ret.add(m);
		}
		return ret;
	}

	public static Method findMethod(Class<?> clazz, String methodName){
		for(Method m : clazz.getMethods()){
			if(!m.getName().equals(methodName)) continue;
			return m;
		}
		return null;
	}

	public static Method findMethod(Class<?> clazz, String methodName, int paramCount){
		for(Method m : clazz.getMethods()){
			if(!m.getName().equals(methodName)) continue;
			Class<?>[] t = m.getParameterTypes();
			if(t.length != paramCount) continue;
			return m;
		}
		return null;
	}

	public static Object getDefaultValueForPrimitive(Class<?> type){
		return primitiveToDefault.get(type);
	}
	
	public static Object getRandomValueForPrimitive(Class<?> type){
		Callable<Object> c = p2r.get(type);
		if(c == null) return null;
		try {
			return c.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 
	 * 
	 */
	public static Class<?> forName(String name)
	throws ClassNotFoundException
	{
		Class<?> p = nameToPrimitiveClass.get(name);
		if(p != null) return p;
		return Class.forName(name);
	}

	/**
	 * 
	 * 
	 */
	public static Class<?> forName(String name, boolean initialize, ClassLoader loader)
	throws ClassNotFoundException
	{
		Class<?> p = nameToPrimitiveClass.get(name);
		if(p != null) return p;
		return Class.forName(name, initialize, loader);
	}

	/**
	 * 
	 * 
	 */
	public static boolean isPrimitive(String className) {
		return nameToPrimitiveClass.containsKey(className);
	}

	/**
	 * 
	 * 
	 */
	public static boolean isAssignableFrom(Class<?> class1, Class<?> class2){
		if(class1.isAssignableFrom(class2)) return true;
		if(!class1.isPrimitive()) return false;
		Class<?> wrapper = primitiveToWrapperClass.get(class1.getName());
		return wrapper.isAssignableFrom(class2);
	}

	/**
	 * 
	 * 
	 */
	public static Class<?> getPrimitiveClass(String primitiveName) {
		return nameToPrimitiveClass.get(primitiveName);
	}

	/**
	 * 
	 * 
	 */
	public static Class<?> getPrimitiveClass(Class<?> wrapperClass) {
		return wrapperToPrimitive.get(wrapperClass);
	}

	/**
	 * 
	 * 
	 */
	public static Class<?> getWrapperClass(String primitiveName) {
		return primitiveToWrapperClass.get(primitiveName);
	}

	/**
	 * 
	 * 
	 */
	public static Class<?> getArrayClass(Class<?> clazz){
		StringBuilder name = new StringBuilder("[");
		if(clazz.isPrimitive()){
			String literal = primitiveToArrayLiteral.get(clazz);
			if(literal == null){
				throw new IllegalArgumentException(
						"unknown primitive type: " + clazz.toString()
						);
			}
			name.append(literal);
		} else{
			name.append("L");
			name.append(clazz.getName());
			name.append(";");
		}
		try{
			return Class.forName(name.toString());
		} catch(ClassNotFoundException e){
			// 発生しないはず
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static Calendar getLastModified(Class<?> clazz){
		String classResourceName =
			"/"
			+ clazz.getName().replace(".", "/")
			+ ".class";
		URL url = clazz.getResource(classResourceName);
		if(url == null) return null;

		Calendar c = Calendar.getInstance();
		if(url.toString().startsWith("jar:file:")) do{
			String fileString = url.toString().substring("jar:".length());
			int jarEntrySepIndex = fileString.indexOf("!/");
			if(jarEntrySepIndex == -1) break;
			if(jarEntrySepIndex >= fileString.length() + 2) break;
			String entryName = fileString.substring(jarEntrySepIndex + 2); 
			String jarName = fileString.substring("file:".length(), jarEntrySepIndex);
			try{
				jarName = URLDecoder.decode(
						jarName, Charset.defaultCharset().name()
						);
				JarFile jf = new JarFile(jarName);
				try{
					ZipEntry e = jf.getEntry(entryName);
					if(e == null) break;
					c.setTimeInMillis(e.getTime());
				} finally{
					jf.close();
				}
				return c;
			} catch(UnsupportedEncodingException e){
			} catch(IOException e){
			}
		} while(false);
		try{
			c.setTimeInMillis(url.openConnection().getLastModified());
			return c;
		} catch(IOException e){
			return null;
		}
	}

	public static Optional<InputStream> getResourceAsStream(Class<?> clazz, String name){
		return Optional.ofNullable(clazz.getResourceAsStream(name));
	}


	private static Map<String, Class<?>> nameToPrimitiveClass = new HashMap<String, Class<?>>();
	private static Map<String, Class<?>> primitiveToWrapperClass = new HashMap<String, Class<?>>();
	private static Map<Class<?>,String> primitiveToArrayLiteral = new HashMap<Class<?>, String>();
	private static Map<Class<?>, Class<?>> wrapperToPrimitive = new HashMap<Class<?>, Class<?>>();
	private static Map<Class<?>, Object> primitiveToDefault = new HashMap<Class<?>, Object>();
	private static Map<Class<?>, Callable<Object>> p2r = new HashMap<Class<?>, Callable<Object>>();
	private static Random random = new Random();

	static {
		nameToPrimitiveClass.put("byte", byte.class);
		nameToPrimitiveClass.put("char", char.class);
		nameToPrimitiveClass.put("double", double.class);
		nameToPrimitiveClass.put("float", float.class);
		nameToPrimitiveClass.put("int", int.class);
		nameToPrimitiveClass.put("long", long.class);
		nameToPrimitiveClass.put("short", short.class);
		nameToPrimitiveClass.put("boolean", boolean.class);
		nameToPrimitiveClass.put("void", void.class);

		wrapperToPrimitive.put(Boolean.class, boolean.class);
		wrapperToPrimitive.put(Byte.class, byte.class);
		wrapperToPrimitive.put(Character.class, char.class);
		wrapperToPrimitive.put(Double.class, double.class);
		wrapperToPrimitive.put(Float.class, float.class);
		wrapperToPrimitive.put(Integer.class, int.class);
		wrapperToPrimitive.put(Long.class, long.class);
		wrapperToPrimitive.put(Short.class, short.class);
		wrapperToPrimitive.put(Void.class, void.class);

		primitiveToWrapperClass.put("byte", Byte.class);
		primitiveToWrapperClass.put("char", Character.class);
		primitiveToWrapperClass.put("double", Double.class);
		primitiveToWrapperClass.put("float", Float.class);
		primitiveToWrapperClass.put("int", Integer.class);
		primitiveToWrapperClass.put("long", Long.class);
		primitiveToWrapperClass.put("short", Short.class);
		primitiveToWrapperClass.put("boolean", Boolean.class);
		primitiveToWrapperClass.put("void", Void.class);

		primitiveToArrayLiteral.put(boolean.class, "Z");
		primitiveToArrayLiteral.put(byte.class, "B");
		primitiveToArrayLiteral.put(char.class, "C");
		primitiveToArrayLiteral.put(short.class, "S");
		primitiveToArrayLiteral.put(int.class, "I");
		primitiveToArrayLiteral.put(float.class, "F");
		primitiveToArrayLiteral.put(double.class, "D");
		primitiveToArrayLiteral.put(long.class, "J");

		primitiveToDefault.put(boolean.class, false);
		primitiveToDefault.put(byte.class, (byte)0);
		primitiveToDefault.put(char.class, (char)0);
		primitiveToDefault.put(short.class, (short)0);
		primitiveToDefault.put(int.class, 0);
		primitiveToDefault.put(long.class, 0L);
		primitiveToDefault.put(float.class, 0f);
		primitiveToDefault.put(double.class, 0.0);
		primitiveToDefault.put(void.class, null);

		p2r.put(boolean.class, new Callable<Object>(){ public Object call(){ return random.nextBoolean();}});
		p2r.put(char.class, new Callable<Object>(){ public Object call(){ return (char)(random.nextDouble() * (Character.MAX_VALUE + 1));}});
		p2r.put(short.class, new Callable<Object>(){ public Object call(){ return (short)(random.nextDouble() * (Short.MAX_VALUE + 1));}});
		p2r.put(int.class, new Callable<Object>(){ public Object call(){ return random.nextInt();}});
		p2r.put(long.class, new Callable<Object>(){ public Object call(){ return random.nextLong();}});
		p2r.put(float.class, new Callable<Object>(){ public Object call(){ return random.nextFloat();}});
		p2r.put(double.class, new Callable<Object>(){ public Object call(){ return random.nextDouble();}});
	}
}
