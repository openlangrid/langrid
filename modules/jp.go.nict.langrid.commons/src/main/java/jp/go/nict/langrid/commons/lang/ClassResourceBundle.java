/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ClassResourceBundle {
	public static String getString(Class<?> clazz, String key){
		return getString(Locale.getDefault(), clazz, key);
	}

	public static String getString(Locale locale, Class<?> clazz, String key){
		return ResourceBundle.getBundle(clazz.getName(), locale, new ResourceBundle.Control() {
			@Override
			public ResourceBundle newBundle(String baseName, Locale locale, String format,
					ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {
				String name = toResourceName(toBundleName(baseName, locale), "properties");
				InputStream is = loader.getResourceAsStream(name);
				try{
					return new PropertyResourceBundle(new BufferedReader(
							new InputStreamReader(is, "UTF-8")));
				} finally{
					is.close();
				}
			}
		}).getString(key);
	}
}
