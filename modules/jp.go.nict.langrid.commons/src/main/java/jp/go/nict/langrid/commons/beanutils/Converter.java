/*
 * $Id: Converter.java 1232 2014-07-28 06:44:55Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.beanutils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;
import jp.go.nict.langrid.commons.lang.reflect.TypeUtil;
import jp.go.nict.langrid.commons.rpc.intf.ComponentConverter;
import jp.go.nict.langrid.commons.transformer.FromToStringTransformer;
import jp.go.nict.langrid.commons.transformer.ObjectToEnumTransformer;
import jp.go.nict.langrid.commons.transformer.StringToEnumTransformer;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.transformer.Transformers;
import jp.go.nict.langrid.commons.util.MapUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONHint;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.util.Base64;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1232 $
 */
public class Converter{
	/**
	 * 
	 * 
	 */
	public Converter(){
		// BeanUtilsのURLConverterは使わない
		addFromToStringConverter(URL.class);
	}

	/**
	 * 
	 * 
	 */
	public <T> void addArrayConversion(Class<T> dst)
	throws IllegalArgumentException
	{
		if(!dst.isArray()) throw new IllegalArgumentException("dst must be array class.");
		
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T, U> void addArrayConversion(Class<T[]> src, Class<U[]> dst)
	throws IllegalArgumentException
	{
		if(!src.isArray()) throw new IllegalArgumentException(
				"src must be an array class.");
		if(!dst.isArray()) throw new IllegalArgumentException(
				"dst must be an array class.");
		transformers.addTransformer(
				src, dst, new ArrayToArrayTransformer(this, dst));
	}

	/**
	 * 
	 * 
	 */
	public <T extends Enum<T>> void addEnumConversion(Class<T> dst)
	throws IllegalArgumentException
	{
		Transformer<Object, T> transformer = new ObjectToEnumTransformer<T>(dst);
		transformers.addTransformer(Object.class, dst, transformer);
		transformers.addTransformer(String.class, dst, new StringToEnumTransformer<T>(dst));
	}

	/**
	 * 
	 * 
	 */
	public <T, U extends T> void addConcreteClassAlias(Class<T> src, final Class<U> dst)
	throws IllegalArgumentException
	{
		aliases.put(src, dst);
	}

	/**
	 * 
	 * 
	 */
	public <T, U extends T> void addFromToStringConverter(Class<T> dst){
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T, U> void addTransformerConversion(Transformer<T, U> transformer)
	throws IllegalArgumentException
	{
		Type[] types = GenericsUtil.getTypeArgumentClasses(
				transformer.getClass(), Transformer.class);
		Class<T> t1 = (Class<T>)TypeUtil.toClass(types[0]);
		Class<U> t2 = (Class<U>)TypeUtil.toClass(types[1]);
		addTransformerConversion(t1, t2, transformer);
	}

	/**
	 * 
	 * 
	 */
	public <T, U> void addTransformerConversion(
			Class<T> src, Class<U> dst, Transformer<T, U> transformer){
		transformers.addTransformer(src, dst, transformer);
	}

	public Object[] convertEachElement(Object[] objects, Class<?>[] types){
		Object[] ret = new Object[objects.length];
		for(int i = 0; i < objects.length; i++){
			ret[i] = convert(objects[i], types[i]);
		}
		return ret;
	}

	public Object[] convertEachElement(Object array, Class<?>[] types){
		Object[] ret = new Object[Array.getLength(array)];
		for(int i = 0; i < ret.length; i++){
			ret[i] = convert(Array.get(array, i), types[i]);
		}
		return ret;
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	public <T extends Collection<?>> T convertCollection(Collection<?> value, ComponentConverter componentConverter)
	throws ConversionException{
		List ret = new ArrayList();
		for(Object element : value){
			ret.add(componentConverter.convert(element));
		}
		return (T)ret;
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	public <T> Collection<T> convertCollection(Collection<?> value, Class<T> componentType)
	throws ConversionException{
		List ret = new ArrayList();
		for(Object element : (Collection)value){
			ret.add(convert(element, componentType));
		}
		return (Collection<T>)ret;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] convertCollectionToArray(Collection<?> value, Class<T> componentType)
	throws ConversionException{
		if(value == null) return null;

		int length = value.size();
		Object array = Array.newInstance(componentType, length);
		int i = 0;
		for(Object v : value){
			Object element = convert(v, componentType);
			try{
				Array.set(array, i++, element);
			} catch(IllegalArgumentException e){
				throw new ConversionException(e);
			}
		}
		return (T[])array;
	}

	@SuppressWarnings("unchecked")
	public <T> T convertMapToBean(Map<String, Object> value, Class<T> targetClass)
	throws ConversionException{
		if(value == null) return null;

		try{
			Object o = targetClass.newInstance();
			for(Map.Entry<String, Object> prop : value.entrySet()){
				String name = prop.getKey();
				Method m = ClassUtil.findSetter(targetClass, name);
				if(m == null) continue;
				JSONHint h = m.getAnnotation(JSONHint.class);
				if(h != null && h.ignore()) continue;
				try{
					Field f = targetClass.getField(name);
					JSONHint fh = f.getAnnotation(JSONHint.class);
					if(fh != null && fh.ignore()) continue;
				} catch(NoSuchFieldException e){
				}
				Object v = value.get(name);
				if(v == null) continue;
				Class<?> setterPropClass = m.getParameterTypes()[0];
				if(!setterPropClass.isAssignableFrom(v.getClass())){
					v = convert(v, setterPropClass);
					if(v == null) continue;
				}
				m.invoke(o, v);
			}
			return (T)o;
		} catch(IllegalAccessException e){
			throw new ConversionException(e);
		} catch(InstantiationException e){
			throw new ConversionException(e);
		} catch (IllegalArgumentException e) {
			throw new ConversionException(e);
		} catch (InvocationTargetException e) {
			throw new ConversionException(e);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T> T convert(Object value, Type type)
	throws ConversionException{
		if(value instanceof Collection){
			if(type instanceof ParameterizedType){
				ParameterizedType pt = (ParameterizedType)type;
				if(Collection.class.isAssignableFrom((Class)pt.getRawType())){
					return (T)convertCollection((Collection)value, (Class)pt.getActualTypeArguments()[0]);
				}
			}
		}
		if(type instanceof Class){
			return (T)convert(value, (Class)type);
		}
		throw new ConversionException("unsupported conversion from " + value.getClass().getName() + " to " + type);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T> T convert(Object value, Class<T> target)
	throws ConversionException{
		if(value == null) return null;
		if(target.isAssignableFrom(value.getClass())) return (T)value;
		Class<?> alias = aliases.get(target);
		if(alias != null){
			target = (Class<T>)alias;
		}
		if(target.isPrimitive()) target = (Class<T>)ClassUtil.getWrapperClass(target.getName());

		// transformer
		Transformer<Object, T> transformer = (Transformer<Object, T>)transformers.get(value.getClass(), target);
		if(transformer != null){
			try{
				return (T)transformer.transform(value);
			} catch(TransformationException e){
				throw new ConversionException(String.format(
						"failed to convert value:\"%s\" to %s"
						+ " using transformer:%s", value, target, transformer
						), e);
			}
		}

		if(value.getClass().isArray() && target.isArray()){
			addArrayConversion(
					(Class<Object[]>)value.getClass()
					, (Class<Object[]>)target
					);
			return convert(value, target);
		} else if(value instanceof Collection<?> && target.isArray()){
			return (T)convertCollectionToArray((Collection<?>)value, target.getComponentType());
		} else if(value instanceof Map<?, ?> && !target.isArray()){
			return (T)convertMapToBean((Map<String, Object>)value, target);
		} else if(target.isEnum()){
			addEnumConversion((Class<Enum>)target);
			return convert(value, target);
		} else if(target.equals(byte[].class)){
			return (T)Base64.decode(value.toString());
		} else{
			if(target.isPrimitive()){
				try{
					Constructor<?> c = primitiveToWrapperConstructor.get(target);
					if(c != null && c.getParameterTypes()[0].isAssignableFrom(value.getClass())){
						return (T)c.newInstance(value);
					}
					return (T)stringToWrapperMethods.get(target).invoke(null, value.toString());
				} catch(IllegalAccessException e){
					throw new ConversionException(e);
				} catch(InstantiationException e){
					throw new ConversionException(e);
				} catch(InvocationTargetException e){
					throw new ConversionException(e);
				}
			} else if(target.equals(String.class)){
				return (T)value.toString();
			} else if(target.equals(Date.class)){
				if(value instanceof Number){
					return (T)new Date(((Number)value).longValue());
				} else if(value instanceof String){
					try{
						return (T)new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS Z").parse((String)value);
					} catch(ParseException e){
						throw new ConversionException(e);
					}
				} else{
					throw new ConversionException(value + " can't be converted to Date");
				}
			} else if(target.equals(Calendar.class)){
				Calendar cal = Calendar.getInstance();
				if(value instanceof Number){
					cal.setTimeInMillis(((Number)value).longValue());
				} else if(value instanceof String){
					try{
						cal.setTime(
								new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS Z").parse((String)value)
								);
					} catch(ParseException e){
						throw new ConversionException(e);
					}
				} else{
					throw new ConversionException(value + " can't be converted to Calendar");
				}
				return (T)cal;
			} else{
				try{
					Constructor c = target.getConstructor();
					Object ret = c.newInstance();
					copyProperties(ret, value);
					return (T)ret;
				} catch(NoSuchMethodException e){
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				} catch (IllegalArgumentException e) {
				} catch (InvocationTargetException e) {
				}
				try{
					Constructor c = target.getConstructor(String.class);
					addTransformerConversion(
							(Class<Object>)value.getClass()
							, target
							, new FromToStringTransformer<T>(c)
							);
					return convert(value, target);
				} catch(NoSuchMethodException e){
				}
				throw new ConversionException("no suitable conversion logic for class " + target.toString());
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public void copyProperties(Object target, Object source) throws ConversionException{
		for(Pair<String, Method> prop : ClassUtil.getReadableProperties(source.getClass())){
			for(Method s : ClassUtil.findSetters(target.getClass(), prop.getFirst())){
				try{
					s.invoke(target, convert(prop.getSecond().invoke(source), s.getParameterTypes()[0]));
					break;
				} catch(IllegalAccessException e){
				} catch(InvocationTargetException e){
				} catch(ConversionException e){
				}
			}
		}
	}

	private Transformers transformers = new Transformers();
	private Map<Class<?>, Class<?>> aliases = new HashMap<Class<?>, Class<?>>();
	private static Map<Class<?>, Constructor<?>> primitiveToWrapperConstructor
			= MapUtil.newHashMap();
	private static Map<Class<?>, Method> stringToWrapperMethods
			= MapUtil.newHashMap();

	private static class CharacterParser{
		/**
		 * 
		 * @param value
		 * @return Character
		 * 
		 */
		@SuppressWarnings("unused")
		public static Character parseCharacter(String value){
			if(value.length() == 0) return null;
			return value.charAt(0);
		}
	}

	static{
		try{
			primitiveToWrapperConstructor.put(boolean.class, Boolean.class.getConstructor(boolean.class));
			primitiveToWrapperConstructor.put(byte.class, Byte.class.getConstructor(byte.class));
			primitiveToWrapperConstructor.put(char.class, Character.class.getConstructor(char.class));
			primitiveToWrapperConstructor.put(short.class, Short.class.getConstructor(short.class));
			primitiveToWrapperConstructor.put(int.class, Integer.class.getConstructor(int.class));
			primitiveToWrapperConstructor.put(long.class, Long.class.getConstructor(long.class));
			primitiveToWrapperConstructor.put(float.class, Float.class.getConstructor(float.class));
			primitiveToWrapperConstructor.put(double.class, Double.class.getConstructor(double.class));
			stringToWrapperMethods.put(boolean.class, Boolean.class.getMethod("parseBoolean", String.class));
			stringToWrapperMethods.put(byte.class, Byte.class.getMethod("parseByte", String.class));
			stringToWrapperMethods.put(char.class, CharacterParser.class.getMethod("parseCharacter", String.class));
			stringToWrapperMethods.put(short.class, Short.class.getMethod("parseShort", String.class));
			stringToWrapperMethods.put(int.class, Integer.class.getMethod("parseInt", String.class));
			stringToWrapperMethods.put(long.class, Long.class.getMethod("parseLong", String.class));
			stringToWrapperMethods.put(float.class, Float.class.getMethod("parseFloat", String.class));
			stringToWrapperMethods.put(double.class, Double.class.getMethod("parseDouble", String.class));
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
	}
}
