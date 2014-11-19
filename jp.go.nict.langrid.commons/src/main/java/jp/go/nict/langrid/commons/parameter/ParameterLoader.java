/*
 * $Id: ParameterLoader.java 436 2011-12-20 07:37:36Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.parameter;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.parameter.annotation.Parameter;
import jp.go.nict.langrid.commons.parameter.annotation.ParameterConfig;
import jp.go.nict.langrid.commons.transformer.StringToURITransformer;
import jp.go.nict.langrid.commons.transformer.StringToURLTransformer;
import jp.go.nict.langrid.commons.transformer.StringTransformerRepository;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 436 $
 */
public class ParameterLoader{
	/**
	 * パラメータロード時に使用するTransformerを登録する。
	 * @param <T> 変換先の型
	 * @param targetClass 変換先のクラス
	 * @param transformer トランスフォーマ
	 */
	public <T> void registerTransformer(
			Class<T> targetClass, Transformer<String, T> transformer){
		transformers.add(targetClass, transformer);
	}

	/**
	 * 
	 * 
	 */
	public void load(Object bean, ParameterContext context)
	throws ParameterRequiredException{
		load(bean, context, new LinkedList<String>());
	}

	/**
	 * 
	 * 
	 */
	public void load(Object bean, ParameterContext context, Collection<String> noNamedParams)
	throws ParameterRequiredException{
		Queue<String> params = new LinkedList<String>();
		params.addAll(noNamedParams);
		Class<?> clazz = bean.getClass();
		String prefix = "";
		boolean loadAll = false;
		ParameterConfig pc = clazz.getAnnotation(ParameterConfig.class);
		if(pc != null){
			prefix = pc.prefix();
			loadAll = pc.loadAllFields();
		}
		for(Field f : clazz.getDeclaredFields()){
			if(f.isSynthetic()) continue;
			String parameterName = "";
			boolean required = false;
			String defaultValue = "";
			Parameter p = f.getAnnotation(Parameter.class);
			if(p != null){
				parameterName = p.name();
				required = p.required();
				defaultValue = p.defaultValue();
			} else if(!loadAll){
				continue;
			}
			String name = prefix +
				(parameterName.length() > 0 ? parameterName : f.getName());
			Object value = null;

			Class<?> type = f.getType();
			Transformer<String, ?> transformer = findTransformer(type);
			if(transformer == null){
				logger.severe(
						"no suitable transformer found for parameter \"" +
						name + "\"(type: \"" + type + "\")");
			}

			value = getTransformedValue(
					transformer
					, type, name, context.getValue(name)
					);
			if(value == null){
				value = getTransformedValue(
						transformer
						, type, name, params.poll()
						);
			}
			if(value == null && defaultValue.length() > 0){
				value = getTransformedValue(
						transformer, type, name, defaultValue
						);
			}
			boolean valueSet = false;
			if(value != null){
				try {
					if(!f.isAccessible()){
						f.setAccessible(true);
					}
					f.set(bean, value);
					valueSet = true;
				} catch (IllegalArgumentException e) {
					logger.log(Level.SEVERE
							, "failed to convert value." +
							  " parameter:\"" + name + "\" value:\""
							  + value + "\".", e);
				} catch (IllegalAccessException e) {
					logger.log(Level.SEVERE
							, "failed to access parameter field."
							+ " parameter:\"" + name + "\" value:\""
							+ value + "\".", e);
				} catch (SecurityException e) {
					logger.log(Level.SEVERE
							, "failed to store parameter value."
							+ " parameter:\"" + name + "\" value:\""
							+ value + "\".", e);
				}
			}
			if(!valueSet && required){
				throw new ParameterRequiredException(name);
			}
		}
	}

	private static Object getTransformedValue(
			Transformer<String, ?> transformer
			, Class<?> type, String name, String value){
		if(value == null) return null;
		if(transformer == null) return null;
		try{
			return transformer.transform(value);
		} catch(TransformationException e){
			logger.warning("failed to convert value \""
					+ value + "\" to type \""
					+ type + "\" for parameter \""
					+ name + "\"");
			return null;
		}
	}

	private Transformer<String, ?> findTransformer(
			Class<?> clazz){
		return transformers.find(clazz);
	}

	private StringTransformerRepository transformers
			= new StringTransformerRepository();
	private static Logger logger = Logger.getLogger(
			ParameterLoader.class.getName());

	{
		transformers.add(URL.class, new StringToURLTransformer());
		transformers.add(URI.class, new StringToURITransformer());
	}
}
