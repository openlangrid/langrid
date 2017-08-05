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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;

import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * @author Takao Nakaguchi
 */
public class ProtobufWriter {
	/*
	0000   1d 74 72 61 6e 73 6c 61 74 69 6f 6e 2e 53 65 72  .translation.Ser
	0010   76 69 63 65 2e 74 72 61 6e 73 6c 61 74 65 0a 34  vice.translate.4
	0020   0a 2e 68 74 74 70 3a 2f 2f 6c 61 6e 67 72 69 64  ..http://langrid
	0030   2e 6e 69 63 74 2e 67 6f 2e 6a 70 2f 70 72 6f 63  .nict.go.jp/proc
	0040   65 73 73 2f 62 69 6e 64 69 6e 67 2f 74 72 65 65  ess/binding/tree
	0050   12 02 5b 5d 12 02 65 6e 1a 02 6a 61 22 05 68 65  ..[]..en..ja".he
	0060   6c 6c 6f                                         llo
	 */
	/**
	 *
	 * @param cos
	 * @param headers
	 * @param method
	 * @param args
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void writeRpcRequest(CodedOutputStream cos, Map<QName, Object> headers
			, Method method, Object... args)
	throws IOException, IllegalAccessException, InvocationTargetException{
		// write service header
		cos.writeStringNoTag(
				method.getDeclaringClass().getSimpleName() + "." + method.getName()
				);
		// write request header
		for(Map.Entry<QName, Object> h : headers.entrySet()){
			writeRpcHeader(cos, 1, new RpcHeader(h.getKey().getNamespaceURI()
					, h.getKey().getLocalPart(), h.getValue().toString()));
		}
		// write request
		if(args != null){
			int fnum = 1;
			for(Object o : args){
				fnum++;
				if(o == null) continue;
				writeObject(cos, fnum, o);
			}
		}
	}

	public static void writeRpcRequest(CodedOutputStream cos, Iterable<RpcHeader> headers
			, Method method, Object... args)
	throws IOException, IllegalAccessException, InvocationTargetException{
		// write service header
		cos.writeStringNoTag(
				method.getDeclaringClass().getSimpleName() + "." + method.getName()
				);
		// write request header
		for(RpcHeader h : headers){
			writeRpcHeader(cos, 1, h);
		}
		// write request
		if(args != null){
			int fnum = 1;
			for(Object o : args){
				fnum++;
				if(o == null) continue;
				writeObject(cos, fnum, o);
			}
		}
	}

	public static void writeSuccessRpcResponse(CodedOutputStream cos, Iterable<RpcHeader> headers, Object result)
	throws IOException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		for(RpcHeader h : headers){
			writeRpcHeader(cos, 1, h);
		}
		if(result == null) {
			return;
		}
		Class<?> clazz = result.getClass();
		if(clazz.isArray()){
			if (clazz.equals(byte[].class)) {
				cos.writeBytes(3, ByteString.copyFrom((byte[])result));
			} else {
				int n = Array.getLength(result);
				for(int i = 0; i < n; i++){
					writeObject(cos, 3, Array.get(result,  i));
				}
			}
		} else{
			writeObject(cos, 3, result);
		}
		cos.flush();
	}

	public static void writeFaultRpcResponse(CodedOutputStream cos, Iterable<RpcHeader> headers, RpcFault fault)
	throws IOException{
		for(RpcHeader h : headers){
			writeRpcHeader(cos, 1, h);
		}
		writeRpcFault(cos, 2, fault);
		cos.flush();
	}

	private static void writeRpcHeader(CodedOutputStream cos, int fieldNum, RpcHeader header)
	throws IOException{
		cos.writeTag(fieldNum, WIRETYPE_LENGTH_DELIMITED);
		String namespace = header.getNamespace();
		String name = header.getName();
		String value = header.getValue();
		int size = 0;
		size += computeStringSize(1, namespace);
		size += computeStringSize(2, name);
		size += computeStringSize(3, value);
		cos.writeRawVarint32(size);
		writeString(cos, 1, namespace);
		writeString(cos, 2, name);
		writeString(cos, 3, value);
	}

	private static void writeRpcFault(CodedOutputStream cos, int fieldNum, RpcFault fault)
	throws IOException{
		cos.writeTag(fieldNum, WIRETYPE_LENGTH_DELIMITED);
		String fc = fault.getFaultCode();
		String fs = fault.getFaultString();
		String det = fault.getDetail();
		int size = 0;
		size += computeStringSize(1, fc);
		size += computeStringSize(2, fs);
		size += computeStringSize(3, det);
		cos.writeRawVarint32(size);
		writeString(cos, 1, fc);
		writeString(cos, 2, fs);
		writeString(cos, 3, det);
	}

	public static void writeObject(CodedOutputStream cos, int fieldNum, Object value)
	throws IOException, IllegalAccessException, InvocationTargetException
	{
		Class<?> clazz = value.getClass();
		if(value instanceof Boolean){
			cos.writeInt32(fieldNum, ((Boolean)value) ? 1 : 0);
		} else if(value instanceof Character){
			cos.writeInt32(fieldNum, (Character)value);
		} else if(value instanceof Number){
			writeNumber(cos, fieldNum, value, clazz);
		} else if(value instanceof String){
			writeString(cos, fieldNum, (String)value);
		} else if(value instanceof Calendar){
			cos.writeInt64(fieldNum, ((Calendar)value).getTimeInMillis());
		} else if(value instanceof Iterable){
			for(Object o : (Iterable<?>)value){
				writeObject(cos, fieldNum, o);
			}
		} else if(clazz.isArray()){
			if (clazz.getComponentType().equals(byte.class)) {
				cos.writeBytes(fieldNum, ByteString.copyFrom((byte[])value));
			} else{
				int n = Array.getLength(value);
				for(int i = 0; i < n; i++){
					writeObject(cos, fieldNum, Array.get(value,  i));
				}
			}
		} else if(clazz.isEnum()){
			writeEnum(cos, fieldNum, (Enum<?>)value);
		} else{
			writeBean(cos, fieldNum, value, clazz);
		}
	}

	private static void writeNumber(CodedOutputStream cos, int fieldNum
			, Object value, Class<?> type)
	throws IOException{
		if(type.equals(Integer.class)){
			cos.writeInt32(fieldNum, (Integer)value);
		} else if(type.equals(Boolean.class)){
			cos.writeBool(fieldNum, (Boolean)value);
		} else if(type.equals(Byte.class)){
			cos.writeInt32(fieldNum, (Byte)value);
		} else if(type.equals(Short.class)){
			cos.writeInt32(fieldNum, (Short)value);
		} else if(type.equals(Long.class)){
			cos.writeInt64(fieldNum, (Long)value);
		} else if(type.equals(Float.class)){
			cos.writeFloat(fieldNum, (Float)value);
		} else if(type.equals(Double.class)){
			cos.writeDouble(fieldNum, (Double)value);
		} else{
			throw new IOException("unsupported type " + type);
		}
	}

	private static void writeString(CodedOutputStream cos, int fieldNum, String value)
	throws IOException{
		if(value != null){
			cos.writeString(fieldNum, value);
		}
	}

	private static void writeEnum(CodedOutputStream cos, int fieldNum, Enum<?> value)
	throws IOException{
		if(value != null){
			cos.writeEnum(fieldNum, value.ordinal());
		}
	}

	private static void writeBean(CodedOutputStream cos, int fieldNum, Object value, Class<?> type)
	throws IOException, IllegalAccessException
	, InvocationTargetException{
		cos.writeTag(fieldNum, WIRETYPE_LENGTH_DELIMITED);
		Map<Integer, Method> getters = getPropGetters(type);
		cos.writeRawVarint32(computeBeanSizeNoTagNoSize(value, getters));
		for(Map.Entry<Integer, Method> entry : getters.entrySet()){
			Object v = entry.getValue().invoke(value);
			if(v != null){
				writeObject(cos, entry.getKey(), v);
			}
		}
	}

	private static int computeObjectSize(int fieldNum, Object value)
	throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		int size = 0;
		Class<?> clazz = value.getClass();
		if(value instanceof Number){
			size += computeNumberSize(fieldNum, value, clazz);
		} else if(value instanceof Character){
			size += CodedOutputStream.computeInt32Size(fieldNum, (Character)value);
		} else if(value instanceof String){
			size += computeStringSize(fieldNum, (String)value);
		} else if(value instanceof Calendar){
			size += CodedOutputStream.computeInt64Size(fieldNum, ((Calendar)value).getTimeInMillis());
		} else if(value instanceof Iterable){
			for(Object o : (Iterable<?>)value){
				size += computeObjectSize(fieldNum, o);
			}
		} else if(clazz.isArray()){
			int n = Array.getLength(value);
			for(int i = 0; i < n; i++){
				size += computeObjectSize(fieldNum, Array.get(value,  i));
			}
		} else if(clazz.isEnum()){
			size += CodedOutputStream.computeEnumSize(fieldNum, ((Enum<?>)value).ordinal());
		} else{
			int sz = computeBeanSizeNoTagNoSize(value, getPropGetters(clazz));
			size += CodedOutputStream.computeTagSize(fieldNum);
			size += CodedOutputStream.computeRawVarint32Size(sz);
			size += sz;
		}
		return size;
	}

	private static int computeBeanSizeNoTagNoSize(Object value, Map<Integer, Method> getters)
	throws IllegalAccessException, InvocationTargetException, IOException{
		int size = 0;
		for(Map.Entry<Integer, Method> entry : getters.entrySet()){
			Object v = entry.getValue().invoke(value);
			if(v != null){
				size += computeObjectSize(entry.getKey(), v);
			}
		}
		return size;
	}

	private static int computeNumberSize(int fieldNum, Object value, Class<?> type)
	throws IOException{
		if(type.equals(Integer.class)){
			return CodedOutputStream.computeInt32Size(fieldNum, (Integer)value);
		} else if(type.equals(Boolean.class)){
			return CodedOutputStream.computeBoolSize(fieldNum, (Boolean)value);
		} else if(type.equals(Byte.class)){
			return CodedOutputStream.computeInt32Size(fieldNum, (Byte)value);
		} else if(type.equals(Short.class)){
			return CodedOutputStream.computeInt32Size(fieldNum, (Short)value);
		} else if(type.equals(Long.class)){
			return CodedOutputStream.computeInt64Size(fieldNum, (Long)value);
		} else if(type.equals(Float.class)){
			return CodedOutputStream.computeFloatSize(fieldNum, (Float)value);
		} else if(type.equals(Double.class)){
			return CodedOutputStream.computeDoubleSize(fieldNum, (Double)value);
		} else{
			throw new IOException("unsupported type " + type);
		}
	}

	private static int computeStringSize(int fieldNum, String value){
		if(value == null) return 0;
		return CodedOutputStream.computeStringSize(fieldNum, value);
	}

	private static Map<Integer, Method> getPropGetters(Class<?> clazz){
		Map<Integer, Method> methods = new HashMap<Integer, Method>();
		getPropGetters(clazz, methods, 0);
		return methods;
	}

	private static int getPropGetters(Class<?> clazz, Map<Integer, Method> methods, int orderBase){
		if(clazz.equals(Object.class)) return 0;
		Class<?> superClass = clazz.getSuperclass();
		if(superClass != null){
			orderBase = getPropGetters(superClass, methods, orderBase);
		}
		return getDeclaredPropGetters(clazz, methods, orderBase);
	}

	private static int getDeclaredPropGetters(Class<?> clazz, Map<Integer, Method> methods, int orderBase)
	throws SecurityException{
		int orderMax = orderBase;
		List<Method> noOrder = new ArrayList<Method>();
		for(Method m : clazz.getDeclaredMethods()){
			String name = m.getName();
			if(!name.startsWith("get")) continue;
			if(m.getParameterTypes().length != 0) continue;
			Class<?> retType = m.getReturnType();
			if(retType.equals(Void.class)) continue;
			String setterName = "set" + name.substring(3);
			String fieldName = name.substring(3, 4).toLowerCase() + name.substring(4);
			try{
				Method setter = clazz.getDeclaredMethod(setterName, retType);
				if(!setter.getReturnType().equals(void.class)) continue;
				try{
					Field f = clazz.getDeclaredField(fieldName);
					jp.go.nict.langrid.commons.rpc.intf.Field fa = f.getAnnotation(jp.go.nict.langrid.commons.rpc.intf.Field.class);
					if(fa != null){
						int o = fa.order() + orderBase;
						methods.put(o, m);
						orderMax = Math.max(orderMax, o);
					} else{
						noOrder.add(m);
					}
				} catch(NoSuchFieldException e){
					noOrder.add(m);
				}
			} catch(NoSuchMethodException e){
			}
		}
		int cur = 1;
		for(Method m : noOrder){
			while(methods.containsKey(cur++));
			methods.put(cur - 1, m);
		}
		return Math.max(orderMax, cur - 1);
	}

	static final int WIRETYPE_VARINT           = 0;
	static final int WIRETYPE_FIXED64          = 1;
	static final int WIRETYPE_LENGTH_DELIMITED = 2;
	static final int WIRETYPE_START_GROUP      = 3;
	static final int WIRETYPE_END_GROUP        = 4;
	static final int WIRETYPE_FIXED32          = 5;
}
