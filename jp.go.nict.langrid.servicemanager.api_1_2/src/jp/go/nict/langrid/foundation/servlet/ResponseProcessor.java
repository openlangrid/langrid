/*
 * $Id: ResponseProcessor.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation.servlet;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.service_1_2.LangridException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;

import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class ResponseProcessor {
	/**
	 * 
	 * 
	 */
	public static ServiceResponse processFaultTemplate(
			HttpServletResponse response
			, LangridException exception, String hostName
			, Map<String, List<String>> responseHeaders
			, OutputStream os
			)
		throws IOException
	{
		addLangridHeaders(responseHeaders, response);
		addFaultHeader(response);
		return processTemplate(exception, hostName, os);
	}

	private static void addFaultHeader(HttpServletResponse response){
		response.setStatus(500);
		response.setContentType("text/xml; charset=utf-8");
		response.setHeader("Server", "LanguageGrid-CoreNode/1.0");
		response.setDateHeader("Date", new Date().getTime());
	}

	private static void addLangridHeaders(Map<String, List<String>> src
			, HttpServletResponse dst){
		for(Map.Entry<String, List<String>> e : src.entrySet()){
			if(e.getKey().startsWith("X-LanguageGrid-")){
				for(String v : e.getValue()){
					dst.addHeader(e.getKey(), v);
				}
			}
		}
	}

	static ServiceResponse processTemplate(
			LangridException exception, String hostName
			, OutputStream out)
		throws IOException
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("escapeUtils", StringEscapeUtils.class);
		map.put("exception", exception);
		map.put("hostName", hostName);
		String cn = exception.getClass().getName();
		map.put("nsSuffix", "");

		List<Attribute> properties = new ArrayList<Attribute>();
		for(Method m : exception.getClass().getDeclaredMethods()){
			String methodName = m.getName();
			if(!methodName.startsWith("get")) continue;

			try{
				String name = m.getName().substring("get".length());
				name = name.substring(0, 1).toLowerCase()
					+ name.substring(1);
				String value = (String)m.invoke(exception);
				if(value == null) continue;
				properties.add(new Attribute(name, value));
			} catch(ClassCastException e){
			} catch(IllegalAccessException e){
			} catch(InvocationTargetException e){
			}
		}
		map.put("properties", properties);

		if(cn.startsWith("jp.go.nict.langrid.service_1_2.")){
			String prefix = cn.substring("jp.go.nict.langrid.service_1_2.".length());
			int i = prefix.lastIndexOf(".");
			if(i != -1){
				map.put(
						"nsSuffix"
						, prefix.substring(0, i).replace(".", "/") + "/"
						);
			}
		}
		CountingOutputStream co = new CountingOutputStream(out);
		OutputStreamWriter w = new OutputStreamWriter(co);
		exceptionTemplate.make(map).writeTo(w);
		w.flush();

		return new ServiceResponse(
				500, co.getCount()
				, "soapenv:Server.userException"
				, exception.getDescription()
				);
	}

	private static final String RES_EXCEPTION = "exception.template";
	private static Template exceptionTemplate;
	static{
		SimpleTemplateEngine engine = new SimpleTemplateEngine();
		try {
			exceptionTemplate = engine.createTemplate(
					ResponseProcessor.class.getResource(RES_EXCEPTION)
					);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
