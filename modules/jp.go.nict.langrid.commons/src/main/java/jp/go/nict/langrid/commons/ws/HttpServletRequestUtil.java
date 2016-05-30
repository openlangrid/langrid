/*
 * $Id: HttpServletRequestUtil.java 1183 2014-04-10 13:59:44Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import jp.go.nict.langrid.commons.util.Trio;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1183 $
 */
public class HttpServletRequestUtil {
	/**
	 * 
	 * 
	 */
	public static URL getRequestUrl(HttpServletRequest request){
		try{
			String query = request.getQueryString();
			if(query != null){
				query = "?" + query;
			} else{
				query = "";
			}
			return new URL(
					request.getRequestURL()
						.append(query)
						.toString()
					);
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static Trio<String, String, String> parseRequestUrl(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		String query = request.getQueryString();
		Matcher m = urlParsePat.matcher(url);
		if(!m.matches()){
			return null;
		}
		if(m.groupCount() != 4) return null;
		String gid = null, sid = null, additional = null;
		if(m.group(3) != null){
			gid = m.group(1);
			sid = m.group(3);
		} else{
			sid = m.group(1);
		}
		if(m.group(4) != null && m.group(4).length() > 0){
			if(query != null){
				additional = m.group(4) + "?" + query;
			} else{
				additional = m.group(4);
			}
		} else{
			if(query != null){
				additional = "?" + query;
			}
		}
		return Trio.create(gid, sid, additional);
	}
	private static Pattern urlParsePat = Pattern.compile(
			"\\w+:\\/\\/[\\w\\.\\:\\-_]+\\/[\\w\\.\\-]+\\/[\\w\\.\\-_]+" +
			"\\/([\\w\\.\\-_]*)(:([\\w\\.\\-]+))?(.*)");

	/**
	 * 
	 * 
	 */
	public static String getQueryValue(String query, String name){
		for(String nv : query.split("^\\?|\\&")){
			if(nv.length() == 0) continue;
			String[] v = nv.split("=", 2);
			if(v[0].equals(name)){
				if(v.length == 1) return "";
				else return v[1];
			}
		}
		return null;
	}

	public static Map<String, List<String>> queryToMap(String query){
		Map<String, List<String>> ret = new LinkedHashMap<String, List<String>>();
		if(query == null) return ret;
		for(String nv : query.split("^\\?|\\&")){
			if(nv.length() == 0) continue;
			String[] v = nv.split("=", 2);
			String name = v[0].trim();
			if(name.length() == 0) continue;
			List<String> values = ret.get(name);
			if(values == null){
				values = new ArrayList<String>();
				ret.put(name, values);
			}
			if(v.length > 1){
				String value = v[1].trim();
				if(value.length() > 0){
					values.add(value);
				}
			}
		}
		return ret;
	}

	public static String mapToQuery(Map<String, List<String>> map){
		StringBuilder b = new StringBuilder();
		b.append("?");
		for(Map.Entry<String, List<String>> entry : map.entrySet()){
			String name = entry.getKey();
			List<String> values = entry.getValue();
			if(values.size() == 0){
				if(b.length() > 1){
					b.append("&");
				}
				b.append(name);
			} else for(String v : values){
				if(b.length() > 1){
					b.append("&");
				}
				b.append(name).append("=").append(v);
			}
		}
		if(b.length() == 1) return "";
		return b.toString();
	}
}
