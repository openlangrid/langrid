/*
 * $Id: StringTransformerRepository.java 182 2010-10-02 03:16:36Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.transformer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;
import jp.go.nict.langrid.commons.lang.reflect.TypeUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @author $Revision: 182 $
 * @see jp.go.nict.langrid.commons.transformer.StringToEnumTransformer
 */
public class StringTransformerRepository {
	/**
	 * 
	 * 
	 */
	public StringTransformerRepository(){
		init();
	}

	/**
	 * 
	 * 
	 */
	public StringTransformerRepository(boolean autoAppendEnumTransformers){
		this.autoAppendEnumTransformers = autoAppendEnumTransformers;
	}

	/**
	 * 
	 * 
	 */
	public synchronized <T> void add(
			Class<T> target
			, Transformer<String, T> transformer)
	{
		transformers.put(target, transformer);
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> void addTransformer(Transformer<String, T> transformer)
	{
		Type[] types = GenericsUtil.getActualTypeArgumentTypes(
				transformer.getClass()
				, Transformer.class
				);

		Class<T> target = (Class<T>)TypeUtil.toClass(types[1]);
		add(target, transformer);
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> Transformer<String, T> find(Class<T> target) {
		Transformer<String, ?> ret = transformers.get(target);
		if(ret != null) return (Transformer<String, T>)ret;

		if(target.isEnum()){
			StringToEnumTransformer<? extends Enum<?>> t = 
					new StringToEnumTransformer(target);
			if(autoAppendEnumTransformers){
				transformers.put(target, t);
			}
			return (Transformer<String, T>)t;
		}
		return null;
	}

	private void init(){
		transformers.put(boolean.class, new StringToBooleanTransformer());
		transformers.put(Boolean.class, new StringToBooleanTransformer());
		transformers.put(double.class, new StringToDoubleTransformer());
		transformers.put(Double.class, new StringToDoubleTransformer());
		transformers.put(float.class, new StringToFloatTransformer());
		transformers.put(Float.class, new StringToFloatTransformer());
		transformers.put(int.class, new StringToIntegerTransformer());
		transformers.put(Integer.class, new StringToIntegerTransformer());
		transformers.put(long.class, new StringToLongTransformer());
		transformers.put(Long.class, new StringToLongTransformer());
		transformers.put(String.class, new PassthroughTransformer<String>());
	}

	private Map<Class<?>, Transformer<String, ?>> transformers
		= new HashMap<Class<?>, Transformer<String,?>>();
	private boolean autoAppendEnumTransformers;
}
