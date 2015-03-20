/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.protobufrpc.io;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.go.nict.langrid.commons.rpc.ArrayElementsReceiver;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;

import com.google.protobuf.CodedInputStream;

/**
 * @author Takao Nakaguchi
 */
public class ProtobufParser {
	public static Pair<Collection<RpcHeader>, Object[]> parseRpcRequest(CodedInputStream cis, Class<?>[] parameterTypes)
	throws IOException, IllegalArgumentException, InstantiationException, IllegalAccessException
	, InvocationTargetException, NoSuchMethodException{
		List<RpcHeader> headers = new ArrayList<RpcHeader>();
		Object[] values = new Object[parameterTypes.length];
		int tag = -1;
		Map<Integer, Collection<Object>> arrayFields = new HashMap<Integer, Collection<Object>>();
		while((tag = cis.readTag()) != 0){
			int index = (tag >> 3) - 2;
			if(index == -1){
				// header
				readHeader(cis, headers);
			} else if(index < parameterTypes.length){
				Class<?> clazz = parameterTypes[index];
				if(clazz.isArray() && !clazz.equals(byte[].class)){
					Collection<Object> elements = arrayFields.get(index);
					if(elements == null){
						elements = new ArrayList<Object>();
						arrayFields.put(index, elements);
					}
					elements.add((Object)read(cis, clazz.getComponentType()));
				} else{
					values[index] = read(cis, parameterTypes[index]);
				}
			} else{
				cis.skipField(tag);
			}
		}
		for(Map.Entry<Integer, Collection<Object>> entry : arrayFields.entrySet()){
			int index = entry.getKey();
			Class<?> clazz = parameterTypes[index];
			Object value = entry.getValue().toArray((Object[])Array.newInstance(clazz.getComponentType(), 0));
			values[index] = value;
		}
		return new Pair<Collection<RpcHeader>, Object[]>(headers, values);
	}

	@SuppressWarnings("unchecked")
	public static <T> Trio<Collection<RpcHeader>, RpcFault, T> parseRpcResponse(CodedInputStream cis, Class<T> resultType)
	throws IOException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<RpcHeader> headers = new ArrayList<RpcHeader>();
		RpcFault fault = null;
		T result = null;
		int tag = -1;
		boolean isArray = resultType.isArray() && !resultType.equals(byte[].class);
		Collection<Object> arrayResult = null;
		Class<?> componentType = null;
		if(isArray){
			arrayResult = new ArrayList<Object>();
			componentType = resultType.getComponentType();
		}
		while((tag = cis.readTag()) != 0){
			int index = (tag >> 3);
			switch(index){
				case 1: // header
					readHeader(cis, headers);
					break;
				case 2: // fault
					fault = read(cis, RpcFault.class);
					break;
				case 3: // result
					if(isArray){
						arrayResult.add(read(cis, componentType));
					} else{
						result = read(cis, resultType);
					}
					break;
				default:
					cis.skipField(tag);
					break;
			}
		}
		if(isArray){
			result = (T)arrayResult.toArray((Object[])Array.newInstance(componentType, 0));
		}
		return new Trio<Collection<RpcHeader>, RpcFault, T>(headers, fault, result);
	}

	@SuppressWarnings("unchecked")
	public static Pair<Collection<RpcHeader>, RpcFault> parseRpcArrayResponse(
			CodedInputStream cis, Class<?> resultType, ArrayElementsReceiver<?> receiver)
	throws IOException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<RpcHeader> headers = new ArrayList<RpcHeader>();
		RpcFault fault = null;
		int tag = -1;
		boolean isArray = resultType.isArray();
		if(!isArray){
			throw new IllegalArgumentException("resultType is not an ArrayType");
		}

		Class<?> componentType = resultType.getComponentType();
		while((tag = cis.readTag()) != 0){
			int index = (tag >> 3);
			switch(index){
				case 1: // header
					readHeader(cis, headers);
					break;
				case 2: // fault
					fault = read(cis, RpcFault.class);
					break;
				case 3: // result
					((ArrayElementsReceiver<Object>)receiver).receive(read(cis, componentType));
					break;
				default:
					cis.skipField(tag);
					break;
			}
		}
		return new Pair<Collection<RpcHeader>, RpcFault>(headers, fault);
	}

	private static void readHeader(CodedInputStream cis, Collection<RpcHeader> headers)
	throws IOException{
		int length = cis.readRawVarint32();
		int oldLength = cis.pushLimit(length);
		String namespace = null, name = null, value = null;
		int tag = 0;
		while((tag = cis.readTag()) != 0){
			switch(tag){
				case 10:
					namespace = cis.readString();
					break;
				case 18:
					name = cis.readString();
					break;
				case 26:
					value = cis.readString();
					break;
				default:
					cis.skipField(tag);
					break;
			}
		}
		if(value == null && namespace != null && name != null){
			headers.add(new RpcHeader(namespace, null, name));
		} else{
			headers.add(new RpcHeader(namespace, name, value));
		}
		cis.checkLastTagWas(0);
		cis.popLimit(oldLength);
	}

	@SuppressWarnings("unchecked")
	private static <T> T read(CodedInputStream cis, Class<T> type)
	throws IOException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(type.isPrimitive()){
			return readPrimitive(cis, type);
		} else if(type.equals(String.class)){
			return (T)cis.readString();
		} else if(Number.class.isAssignableFrom(type)){
			return readNumber(cis, type);
		} else if(type.equals(Character.class)){
			return (T)(Character)(char)cis.readInt32();
		} else if(type.equals(Calendar.class)){
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(cis.readInt64());
			return (T)c;
		} else if(type.isArray()){
			if(type.getComponentType().equals(byte.class)){
				return (T)cis.readBytes().toByteArray();
			} else{
				return (T)read(cis, type.getComponentType());
			}
		} else if(type.isEnum()){
			return readEnum(cis, type);
		} else{
			return readBean(cis, type);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T readPrimitive(CodedInputStream cis, Class<T> type)
	throws IOException{
		if(type.equals(int.class)){
			return (T)(Integer)cis.readInt32();
		} else if(type.equals(boolean.class)){
			return (T)(Boolean)cis.readBool();
		} else if(type.equals(byte.class)){
			return (T)(Byte)(byte)cis.readInt32();
		} else if(type.equals(char.class)){
			return (T)(Character)(char)cis.readInt32();
		} else if(type.equals(short.class)){
			return (T)(Short)(short)cis.readInt32();
		} else if(type.equals(long.class)){
			return (T)(Long)cis.readInt64();
		} else if(type.equals(float.class)){
			return (T)(Float)cis.readFloat();
		} else if(type.equals(double.class)){
			return (T)(Double)cis.readDouble();
		} else{
			throw new IOException("unknown primitive type " + type);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T readNumber(CodedInputStream cis, Class<T> type)
	throws IOException{
		if(type.equals(Integer.class)){
			return (T)(Integer)cis.readInt32();
		} else if(type.equals(Boolean.class)){
			return (T)(Boolean)cis.readBool();
		} else if(type.equals(Byte.class)){
			return (T)(Byte)(byte)cis.readInt32();
		} else if(type.equals(Short.class)){
			return (T)(Short)(short)cis.readInt32();
		} else if(type.equals(Long.class)){
			return (T)(Long)cis.readInt64();
		} else if(type.equals(Float.class)){
			return (T)(Float)cis.readFloat();
		} else if(type.equals(Double.class)){
			return (T)(Double)cis.readDouble();
		} else{
			throw new IOException("unsupported type " + type);
		}
	}

	private static <T> T readEnum(CodedInputStream cis, Class<T> type)
	throws IOException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return type.getEnumConstants()[cis.readEnum()];
	}

	private static <T> T readBean(CodedInputStream cis, Class<T> type)
	throws IOException, InstantiationException, IllegalAccessException
	, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
	    int length = cis.readRawVarint32();
	    int oldLength = cis.pushLimit(length);
	    int tag = 0;
	    T obj = type.newInstance();
	    Map<Integer, Method> setters = getPropSetters(type);
	    Map<Integer, Collection<Object>> arrayFields = new HashMap<Integer, Collection<Object>>();
		while((tag = cis.readTag()) != 0){
			int index = (tag >> 3);
			Method setter = setters.get(index);
			if(setter == null){
				cis.skipField(tag);
				continue;
			}
			Class<?> clazz = setter.getParameterTypes()[0];
			if(clazz.isArray()){
				Collection<Object> elements = arrayFields.get(index);
				if(elements == null){
					elements = new ArrayList<Object>();
					arrayFields.put(index, elements);
				}
				elements.add(read(cis, clazz.getComponentType()));
			} else{
				setter.invoke(obj, read(cis, clazz));
			}
		}
		for(Map.Entry<Integer, Collection<Object>> entry : arrayFields.entrySet()){
			int index = entry.getKey();
			Method setter = setters.get(index);
			Class<?> clazz = setter.getParameterTypes()[0];
			int n = entry.getValue().size();
			Object value = Array.newInstance(clazz.getComponentType(), n);
			Iterator<Object> it = entry.getValue().iterator();
//*
			Object[] src = new Object[n];
			for(int i = 0; i < n; i++){
				src[i] = it.next();
			}
			System.arraycopy(src, 0, value, 0, n);
/*/
			for(int i = 0; i < n; i++){
				Array.set(value, i, it.next());
			}
//*/
			setter.invoke(obj, value);
		}
		cis.checkLastTagWas(0);
		cis.popLimit(oldLength);
		return obj;
	}

	private static Map<Integer, Method> getPropSetters(Class<?> clazz){
		Map<Integer, Method> methods = cachedMethods.get(clazz);
		if(methods == null){
			methods = new HashMap<Integer, Method>();
			getPropSetters(clazz, methods, 0);
			cachedMethods.put(clazz, methods);
		}
		return methods;
	}

	private static int getPropSetters(Class<?> clazz, Map<Integer, Method> methods, int orderBase){
		if(clazz.equals(Object.class)) return 0;
		Class<?> superClass = clazz.getSuperclass();
		if(superClass != null){
			orderBase = getPropSetters(superClass, methods, orderBase);
		}
		return getDeclaredPropSetters(clazz, methods, orderBase);
	}

	private static int getDeclaredPropSetters(Class<?> clazz, Map<Integer, Method> methods, int orderBase){
		if(clazz.equals(Object.class)) return 0;

		int orderMax = orderBase;
		List<Method> noOrder = new ArrayList<Method>();
		for(Field f : clazz.getDeclaredFields()){
			String name = f.getName();
			try{
				Method m = clazz.getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1)
						, f.getType());
				jp.go.nict.langrid.commons.rpc.intf.Field fa = f.getAnnotation(
						jp.go.nict.langrid.commons.rpc.intf.Field.class);
				if(fa != null){
					int o = fa.order() + orderBase;
					methods.put(o, m);
					orderMax = Math.max(orderMax,  o);
				} else{
					noOrder.add(m);
				}
			} catch(NoSuchMethodException e){
			}
		}

/*
		List<Method> noOrder = new ArrayList<Method>();
		for(Method m : clazz.getDeclaredMethods()){
			String name = m.getName();
			if(!name.startsWith("set")) continue;
			if(!m.getReturnType().equals(void.class)) continue;
			Class<?>[] paramTypes = m.getParameterTypes();
			if(paramTypes.length != 1) continue;
			String fieldName = name.substring(3, 4).toLowerCase() + name.substring(4);
			try{
				Field f = clazz.getDeclaredField(fieldName);
				jp.go.nict.langrid.commons.rpc.intf.Field fa = f.getAnnotation(jp.go.nict.langrid.commons.rpc.intf.Field.class);
				if(fa != null){
					int o = fa.order() + orderBase;
					cache.put(o, m);
					orderMax = Math.max(orderMax,  o);
				} else{
					noOrder.add(m);
				}
			} catch(NoSuchFieldException e){
				noOrder.add(m);
			}
		}
*/
		int cur = 1;
		for(Method m : noOrder){
			while(methods.containsKey(cur++));
			methods.put(cur - 1, m);
		}
		return Math.max(orderMax, cur - 1);
	}

	private static Map<Class<?>, Map<Integer, Method>> cachedMethods = new ConcurrentHashMap<Class<?>, Map<Integer,Method>>();
}
