/*
 * $Id: MimeHeadersUtil.java 1121 2014-02-07 00:31:22Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.ws.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.MimeHeaders;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1121 $
 */
public class MimeHeadersUtil {
	/**
	 * 
	 * 
	 */
	public static boolean isTrue(MimeHeaders headers, String name){
		String[] values = headers.getHeader(name);
		if(values == null) return false;
		if(values.length == 0) return false;
		for(String v : values){
			if(!v.equals("true")) return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	public static String getJoinedValue(MimeHeaders headers, String name){
		String[] values = headers.getHeader(name);
		if(values == null) return null;
		if(values.length == 0) return "";
		else return StringUtil.join(values, ",");
	}

	/**
	 * 
	 * 
	 */
	public static String getJoinedAndDecodedValue(
			MimeHeaders headers, String name){
		String[] values = headers.getHeader(name);
		if(values == null) return null;
		else try{
			return URLDecoder.decode(
					StringUtil.join(values, ",")
					, "UTF-8"
					);
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static String getJoinedAndDecodedValue(
			MimeHeaders headers, String name, String separator){
		String[] values = headers.getHeader(name);
		if(values == null) return null;
		else try{
			return URLDecoder.decode(
					StringUtil.join(values, separator)
					, "UTF-8"
					);
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void setToHttpServletResponse(MimeHeaders headers, HttpServletResponse response){
		Iterator<Pair<String, String>> i = headers.getAllHeaders();
		while(i.hasNext()){
			Pair<String, String> h = i.next();
			response.addHeader(h.getFirst(), h.getSecond());
		}
	}

	public static void setToHttpServletResponse(Map<String, String> headers, HttpServletResponse response){
		for(Map.Entry<String, String> e : headers.entrySet()) {
			response.addHeader(e.getKey(), e.getValue());
		}
	}

	/**
	 * 
	 * 
	 */
	public static MimeHeaders fromStringObjectMap(Map<String, Object> source){
		MimeHeaders ret = new MimeHeaders();
		for(Map.Entry<String, Object> entry : source.entrySet()){
			String name = entry.getKey();
			Object value = entry.getValue();
			if(value instanceof String){
				ret.addHeader(name, value.toString());
			} else if(value instanceof String[]){
				for(String v : (String[])value){
					ret.addHeader(name, v);
				}
			} else if(value instanceof Iterable){
				for(Object v : (Iterable<?>)value){
					ret.addHeader(name, v.toString());
				}
			} else{
				ret.addHeader(name, value.toString());
			}
		}
		return ret;
	}
}
