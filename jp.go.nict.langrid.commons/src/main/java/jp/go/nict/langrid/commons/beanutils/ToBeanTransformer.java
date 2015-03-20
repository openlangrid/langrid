/*
 * $Id: StringGenerator.java 182 2010-10-02 03:16:36Z t-nakaguchi $
 *
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
package jp.go.nict.langrid.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.function.Supplier;

public abstract class ToBeanTransformer<T, U> implements Transformer<T, U>{
	@SuppressWarnings("unchecked")
	public ToBeanTransformer(Converter converter, Class<U> targetClass){
		this(converter, targetClass, Collections.EMPTY_MAP);
	}

	@SuppressWarnings("unchecked")
	protected ToBeanTransformer(Converter converter, Map<String, String> propertyAliases){
		Class<?>[] types = GenericsUtil.getTypeArgumentClasses(getClass(), Transformer.class);
		this.converter = converter;
		this.targetClass = (Class<U>)types[1];
		this.aliases = propertyAliases;
	}

	public ToBeanTransformer(Converter converter, Class<U> targetClass, Map<String, String> propertyAliases){
		this.converter = converter;
		this.targetClass = targetClass;
		this.aliases = propertyAliases;
	}

	@SuppressWarnings("unchecked")
	protected U doTransform(Iterable<Pair<String, Supplier<Object>>> properties) throws TransformationException {
		try{
			Object target = targetClass.newInstance();
			for(Pair<String, Supplier<Object>> prop : properties){
				String name = prop.getFirst();
				if(aliases.containsKey(name)){
					name = aliases.get(name);
				}
				Method setter = ClassUtil.findSetter(targetClass, name);
				if(setter == null) continue;
				Object src = prop.getSecond().get();
				if(src == null) continue;
				Class<?> setterParamClass = setter.getParameterTypes()[0];
				if(!ClassUtil.isAssignableFrom(setterParamClass, src.getClass())){
					src = converter.convert(src, setterParamClass);
				}
				setter.invoke(target, src);
			}
			return (U)target;
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

	private Converter converter;
	private Class<U> targetClass;
	private Map<String, String> aliases;
}
