/*
 * $Id: ParameterContext.java 918 2013-08-15 02:20:44Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.parameter;

import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.transformer.PassthroughTransformer;
import jp.go.nict.langrid.commons.transformer.StringSplittingTransformer;
import jp.go.nict.langrid.commons.transformer.StringToBooleanTransformer;
import jp.go.nict.langrid.commons.transformer.StringToDoubleTransformer;
import jp.go.nict.langrid.commons.transformer.StringToEnumTransformer;
import jp.go.nict.langrid.commons.transformer.StringToFloatTransformer;
import jp.go.nict.langrid.commons.transformer.StringToIntegerTransformer;
import jp.go.nict.langrid.commons.transformer.StringToLongTransformer;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.function.Supplier;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 918 $
 */
public abstract class ParameterContext {
	/**
	 * 
	 * 
	 */
	public abstract String getValue(String name);

	/**
	 * 
	 * 
	 */
	public boolean getBoolean(String parameterName, boolean defaultValue){
		return getWithTransformer(
				parameterName, defaultValue, s2b
				);
	}

	/**
	 * 
	 * 
	 */
	public Boolean getBoolean(String parameterName){
		return getWithTransformer(parameterName, s2b);
	}

	/**
	 * 
	 * 
	 */
	public int getInteger(String parameterName, int defaultValue){
		return getWithTransformer(
				parameterName, defaultValue, s2i
				);
	}

	/**
	 * 
	 * 
	 */
	public Integer getInteger(String parameterName){
		return getWithTransformer(parameterName, s2i);
	}

	/**
	 * 
	 * 
	 */
	public long getLong(String parameterName, long defaultValue){
		return getWithTransformer(
				parameterName, defaultValue, s2l
				);
	}

	/**
	 * 
	 * 
	 */
	public Long getLong(String parameterName){
		return getWithTransformer(parameterName, s2l);
	}

	/**
	 * 
	 * 
	 */
	public float getFloat(String parameterName, float defaultValue){
		return getWithTransformer(
				parameterName, defaultValue, s2f
				);
	}

	/**
	 * 
	 * 
	 */
	public Float getFloat(String parameterName){
		return getWithTransformer(parameterName, s2f);
	}

	/**
	 * 
	 * 
	 */
	public double getDouble(String parameterName, double defaultValue){
		return getWithTransformer(
				parameterName, defaultValue, s2d
				);
	}

	/**
	 * 
	 * 
	 */
	public Double getDouble(String parameterName){
		return getWithTransformer(parameterName, s2d);
	}

	/**
	 * 
	 * 
	 */
	public String getString(String parameterName, String defaultValue){
		return getWithTransformer(
				parameterName, defaultValue, s2s
				);
	}

	public String getString(String parameterName, Supplier<String> supplier){
		return getWithTransformer(
				parameterName, supplier, s2s
				);
	}

	/**
	 * 
	 * 
	 */
	public String[] getStrings(String parameterName, String[] defaultValue){
		return getWithTransformer(
				parameterName, defaultValue, s2sa
				);
	}

	/**
	 * 
	 * 
	 */
	public <T extends Enum<T>> T getEnum(String parameterName, T defaultValue, Class<T> clazz){
		return getWithTransformer(
				parameterName, defaultValue, new StringToEnumTransformer<T>(clazz)
				);
	}

	/**
	 * 
	 * 
	 */
	public <T extends Enum<T>> T getEnum(String parameterName, Class<T> clazz){
		return getWithTransformer(
				parameterName, new StringToEnumTransformer<T>(clazz)
				);
	}

	/**
	 * 
	 * 
	 */
	public <T> T getWithTransformer(
			String parameterName, T defaultValue
			, Transformer<String, T> transformer)
	{
		String v = getValue(parameterName);
		if(v == null){
			return defaultValue;
		}
		try{
			return transformer.transform(v);
		} catch(TransformationException e){
			logger.log(
					Level.WARNING, "failed to convert parameter. name:\""
					+ parameterName + "\" value:\"" + v + "\"", e					
					);
			return defaultValue;
		}
	}

	/**
	 * 
	 * 
	 */
	public <T> T getWithTransformer(
			String parameterName, Supplier<T> supplier
			, Transformer<String, T> transformer)
	{
		String v = getValue(parameterName);
		if(v == null){
			return supplier.get();
		}
		try{
			return transformer.transform(v);
		} catch(TransformationException e){
			logger.log(
					Level.WARNING, "failed to convert parameter. name:\""
					+ parameterName + "\" value:\"" + v + "\"", e					
					);
			return supplier.get();
		}
	}

	/**
	 * 
	 * 
	 */
	public <T> T getWithTransformer(
			String parameterName
			, Transformer<String, T> transformer)
	{
		String v = getValue(parameterName);
		if(v == null) return null;
		try{
			return transformer.transform(v);
		} catch(TransformationException e){
			logger.log(
					Level.WARNING, "failed to convert parameter. name:\""
					+ parameterName + "\" value:\"" + v + "\"", e					
					);
			return null;
		}
	}

	/**
	 * 
	 * 
	 */
	public void load(Object bean)
	throws ParameterRequiredException{
		loader.load(bean, this);
	}

	private ParameterLoader loader = new ParameterLoader();

	private static Transformer<String, Boolean> s2b = new StringToBooleanTransformer();
	private static Transformer<String, Integer> s2i = new StringToIntegerTransformer();
	private static Transformer<String, Long> s2l = new StringToLongTransformer();
	private static Transformer<String, Float> s2f = new StringToFloatTransformer();
	private static Transformer<String, Double> s2d = new StringToDoubleTransformer();
	private static Transformer<String, String> s2s = new PassthroughTransformer<String>();
	private static Transformer<String, String[]> s2sa = new StringSplittingTransformer(",");
	private static Logger logger = Logger.getLogger(ParameterContext.class.getName());
}
