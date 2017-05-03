/*
 * $Id: DynamicBindingUtil.java 1143 2014-02-13 01:18:48Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.cs.binding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import jp.go.nict.langrid.commons.codec.URLCodec;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.CollectionUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.function.Function;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1143 $
 */
public class DynamicBindingUtil {
	/**
	 * 
	 * 
	 */
	public static String encodeDefaults(Map<String, String> bindings){
		return StringUtil.join(
				CollectionUtil
					.collect(bindings.entrySet().iterator(), defaultBindingToString)
					.toArray(new String[]{})
				, ",");
	}

	/**
	 * 
	 * 
	 */
	public static Map<String, String> decodeDefaults(String value){
		return decodeMappings(value, stringToDefaultBinding
				, new HashMap<String, String>());
	}

	/**
	 * 
	 */
	public static String encodeOverrides(Map<String, Map<String, String>> bindings){
		return StringUtil.join(
				CollectionUtil
					.collect(filter(bindings).entrySet().iterator(), bindingOverrideToString)
					.toArray(new String[]{})
				, ",");
	}

	private static Map<String, Map<String, String>> filter(Map<String, Map<String, String>> map){
		Iterator<Map.Entry<String, Map<String, String>>> i = map.entrySet().iterator();
		while(i.hasNext()){
			if(i.next().getValue().size() == 0){
				i.remove();
			}
		}
		return map;
	}

	/**
	 * 
	 * 
	 */
	public static Map<String, Map<String, String>> decodeOverrides(
			String value){
		Map<String, Map<String, String>> mappings = new HashMap<String, Map<String,String>>();
		for(String line : value.split(",")){
			String[] values = line.split("@");
			if(values.length != 2) continue;
			String[] values2 = values[0].split(":");
			if(values2.length != 2) continue;
			String wfId = values[1];
			String sourceId = values2[0];
			String dstId = URLCodec.decode(values2[1]);
			Map<String, String> b = mappings.get(wfId);
			if(b == null){
				b = new HashMap<String, String>();
				mappings.put(wfId, b);
			}
			b.put(sourceId, dstId);
		}
		return mappings;
	}

	/**
	 * 
	 * 
	 */
	public static String encodeTree(Collection<BindingNode> value){
		return JSON.encode(value);
	}

	/**
	 * 
	 * 
	 */
	public static String encodeTree(Collection<BindingNode> value
			, int indentFactor){
		return JSON.encode(value, true);
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("serial")
	public static Collection<BindingNode> decodeTree(String value)
	throws ParseException
	{
		try{
			return JSON.decode(
					value, new ArrayList<BindingNode>(){}.getClass().getGenericSuperclass()
					);
		} catch(JSONException e){
			throw new ParseException(e.getMessage(), -1);
		}
	}

	/**
	 * 
	 * 
	 */
	private static <T, U> Map<T, U> decodeMappings(String value
			, Transformer<String, Pair<T, U>> transformer, Map<T, U> map)
	throws TransformationException
	{
		for(String line : value.split(",")){
			Pair<T, U> v = transformer.transform(line);
			if(v != null){
				map.put(v.getFirst(), v.getSecond());
			}
		}
		return map;
	}

	private static Transformer<Map.Entry<String, String>, String> defaultBindingToString =
			new Transformer<Map.Entry<String,String>, String>() {
				public String transform(Map.Entry<String,String> value) throws TransformationException {
					return value.getKey() + ":" + URLCodec.encode(value.getValue());
				}
			};

	private static Transformer<String, Pair<String, String>> stringToDefaultBinding =
			new Transformer<String, Pair<String,String>>() {
				public Pair<String,String> transform(String value) throws TransformationException {
					String[] values = value.split(":");
					if(values.length != 2){
						throw new TransformationException(
								"value format is invalid. \"" + value + "\""
								);		
					}
					return Pair.create(values[0], URLCodec.decode(values[1]));
				}
			};

	private static Transformer<Map.Entry<String, Map<String, String>>, String> bindingOverrideToString =
			new Transformer<Map.Entry<String,Map<String,String>>, String>() {
				public String transform(Map.Entry<String,Map<String,String>> value) throws TransformationException {
					return StringUtil.join(
							CollectionUtil.collect(value.getValue().entrySet()
									, new BindingOverrideEntryToString(value.getKey())
							).toArray(new String[]{})
							, ","
							);
				}
			};
	private static class BindingOverrideEntryToString
	implements Function<Map.Entry<String, String>, String>{
		/**
		 * 
		 * 
		 */
		public BindingOverrideEntryToString(String serviceId){
			this.serviceId = serviceId;
		}
		public String apply(Entry<String, String> value){
			return value.getKey()
			+ ":" + URLCodec.encode(value.getValue())
			+ "@" + serviceId;
		}
		private String serviceId;
	}
}
