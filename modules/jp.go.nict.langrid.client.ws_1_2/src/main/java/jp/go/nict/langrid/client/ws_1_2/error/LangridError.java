/*
 * $Id: LangridError.java 369 2011-08-19 09:35:21Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.error;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.go.nict.langrid.commons.lang.StringUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 369 $
 */
public enum LangridError {
	/**
	 * 
	 * 
	 */
	E000,

	/**
	 * 
	 * 
	 */
	E001,

	/**
	 * 
	 * 
	 */
	E002,

	/**
	 * 
	 * 
	 */
	E050,

	/**
	 * 
	 * 
	 */
	E051,

	/**
	 * 
	 * 
	 */
	E052,

	/**
	 * 
	 * 
	 */
	E053,

	/**
	 * 
	 * 
	 */
	E054,

	/**
	 * 
	 * 
	 */
	E055,

	/**
	 * 
	 * 
	 */
	E056,

	/**
	 * 
	 * 
	 */
	E057,

	/**
	 * 
	 * 
	 */
	E058,

	/**
	 * 
	 * 
	 */
	E059,

	/**
	 * 
	 * 
	 */
	E060,

	/**
	 * 
	 * 
	 */
	E061,

	/**
	 * 
	 * 
	 */
	E062,

	/**
	 * 
	 * 
	 */
	E063,

	/**
	 * 
	 * 
	 */
	E150,

	/**
	 * 
	 * 
	 */
	E153,

	/**
	 * 
	 * 
	 */
	E154,

	/**
	 * 
	 * 
	 */
	E155,

	/**
	 * 
	 * 
	 */
	E156,

	/**
	 * 
	 * 
	 */
	E157,

	/**
	 * 
	 * 
	 */
	E450,

	/**
	 * 
	 * 
	 */
	E451,

	/**
	 * 
	 * 
	 */
	E1250,

	/**
	 * 
	 * 
	 */
	E1251,

	/**
	 * 
	 * 
	 */
	E1252,

	/**
	 * 
	 * 
	 */
	E1350,

	/**
	 * 
	 * 
	 */
	E1351,
	;

	/**
	 * 
	 * 
	 */
	private LangridError(){}

	/**
	 * 
	 * 
	 */
	public String getMessage(Throwable exception, Locale locale){
		return getMessage(exception, getResourceBundle(locale));
	}

	/**
	 * 
	 * 
	 */
	public String getMessage(Throwable exception){
		return getMessage(exception, defaultBundle);
	}

	private ResourceBundle getResourceBundle(Locale locale){
		return ResourceBundle.getBundle(
				"jp.go.nict.langrid.client.ws_1_2.error.messages"
				, locale);
	}

	private String getMessage(Throwable exception, ResourceBundle bundle){
		if(exception == null) return null;
		Class<?> clazz = exception.getClass();
		String template = bundle.getString(name());
		String varPattern = "\\$\\{([^\\}]*)\\}";
		Matcher m = Pattern.compile(varPattern).matcher(template);
		List<String> values = new ArrayList<String>();
		while(m.find()){
			String param = m.group(1);
			if(param.equals("message")
					&& descriptionPreferredClasses.contains(exception.getClass().getName())){
				String description = getDescription(exception);
				if(description != null){
					values.add(description);
					continue;
				}
			}
			try{
				Method method = clazz.getMethod(
						"get" + param.substring(0,1).toUpperCase()
						+ param.substring(1)
				);
				Object value = method.invoke(exception);
				if(value instanceof String[]){
					values.add(StringUtil.join((String[])value, ","));
				} else if(value != null){
					values.add(value.toString());
				} else{
					values.add("(null)");
				}
			} catch(IllegalAccessException e){
				values.add(e.getMessage());
			} catch(InvocationTargetException e){
				values.add(e.getMessage());
			} catch(NoSuchMethodException e){
				values.add(e.getMessage());
			}
		}
		return String.format(
				Pattern.compile(varPattern).matcher(template).replaceAll("%s")
				, (Object[])(values.toArray(new String[]{}))
				);
	}

	private String getDescription(Object object){
		try {
			return (String)object.getClass().getMethod("getDescription").invoke(object);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return null;
	}

	private static ResourceBundle defaultBundle = ResourceBundle.getBundle(
		"jp.go.nict.langrid.client.ws_1_2.error.messages"
		);
	private static Set<String> descriptionPreferredClasses = new HashSet<String>();
	static{
		descriptionPreferredClasses.add("jp.go.nict.langrid.ws_1_2.LangridException");
	}
}
