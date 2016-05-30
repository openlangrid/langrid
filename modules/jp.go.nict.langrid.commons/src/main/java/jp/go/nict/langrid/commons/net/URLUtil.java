/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2008-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.net;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class URLUtil {
	/**
	 * 
	 * 
	 */
	public static URL replaceHost(URL url, String host)
	throws MalformedURLException{
		String protocol = url.getProtocol();
		int port = url.getPort();
		String file = url.getFile();
		return new URL(protocol, host, port, file);
	}

	/**
	 * 
	 * 
	 */
	public static URL replaceHostAndPort(URL url, String host, int port)
	throws MalformedURLException{
		String protocol = url.getProtocol();
		String file = url.getFile();
		return new URL(protocol, host, port, file);
	}

	/**
	 * 
	 * 
	 */
	public static URL dropUserInfo(URL url){
		try{
			return new URL(String.format(
					"%s://%s%s%s%s"
					, url.getProtocol()
					, url.getHost()
					, url.getPort() != -1 ? ":" + url.getPort() : ""
					, url.getFile()
					, url.getRef() != null ? "#" + url.getRef() : ""
					));
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static String getRootUrl(URL url){
		return url.getProtocol() + "://"
				+ url.getAuthority() + "/";
	}

	/**
	 * 
	 * 
	 */
	public static String getContextUrl(URL url){
		String[] p = url.getPath().split("/");
		String c = p[0];
		if(c.length() == 0 && p.length > 1){
			c = p[1];
		}
		return url.getProtocol() + "://"
				+ url.getAuthority() + "/"
				+ c + (c.length() > 0 ? "/" : "");
	}

	/**
	 * 
	 * 
	 */
	public static String getUntilQuery(URL url){
		return url.toString().split("\\?")[0];
	}

	/**
	 * 
	 * 
	 */
	public static Map<String, String> getQueryParameters(URL url){
		Map<String, String> params = new HashMap<String, String>();
		String q = url.getQuery();
		if(q == null) return params;

		try{
			for(String p : q.split("&")){
				String[] values = p.split("=", 2);
				if(values.length != 2) continue;
				params.put(
						URLDecoder.decode(values[0], "UTF-8")
						, URLDecoder.decode(values[1], "UTF-8")
						);
			}
		} catch(UnsupportedEncodingException e){
			// Java must support "UTF-8"
			throw new RuntimeException(e);
		}
		return params;
	}

	/**
	 * 
	 * 
	 */
	public static URL addParam(URL url, String paramString) throws MalformedURLException{
		if(paramString == null) return url;
		String s = url.toString();
		int i = s.lastIndexOf("?");
		if(i == -1){
			return new URL(url + "?" + paramString);
		}
		return new URL(url + "&" + paramString);
	}

	public static URL mergePath(URL url, String pathAndSearchPart) throws MalformedURLException{
		if(pathAndSearchPart == null) return url;
		String s = url.toString();
		int i = s.lastIndexOf("?");
		if(i == -1){
			return new URL(concatPath(s, pathAndSearchPart));
		} else{
			String[] srcs = s.split("\\?", 2);
			String[] values = pathAndSearchPart.split("\\?", 2);
			StringBuilder b = new StringBuilder(concatPath(srcs[0], values[0]));
			if(srcs.length == 2){
				if(b.indexOf("?") != -1){
					b.append('&');
				} else{
					b.append('?');
				}
				b.append(srcs[1]);
			}
			if(values.length == 2){
				if(b.indexOf("?") != -1){
					b.append('&');
				} else{
					b.append('?');
				}
				b.append(values[1]);
			}
			return new URL(b.toString());
		}
	}

	private static String concatPath(String src, String dst){
		if(dst == null || dst.length() == 0) return src;
		int slash = dst.indexOf('/');
		int question = dst.indexOf('?');
		int equal = dst.indexOf('=');
		String sep = "";
		if(slash == 0 || question == 0){
		} else if(slash != -1 || question != -1 || equal == -1){
			sep = "/";
		} else{
			sep = "?";
		}
		return src + sep + dst;
	}
}
