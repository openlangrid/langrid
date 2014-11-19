/*
 * $Id: ClassResourceBundle.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.lang;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import jp.go.nict.langrid.commons.util.Pair;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class ClassResourceBundle {
	/**
	 * 
	 * 
	 */
	public static String getString(Class<?> clazz, String key){
		return getString(Locale.getDefault(), clazz, key);
	}

	/**
	 * 
	 * 
	 */
	public static String getString(Locale locale, Class<?> clazz, String key){
		ResourceBundle bundle;

		WeakReference<ResourceBundle> ref = bundles.get(
				new Pair<Locale, Class<?>>(locale, clazz)
				);
		if(ref == null){
			bundle = create(locale, clazz);
		} else{
			bundle = ref.get();
			if(bundle == null){
				bundle = create(locale, clazz);
			}
		}

		return bundle.getString(key);
	}

	/**
	 * 
	 * 
	 */
	private static ResourceBundle create(Locale locale, Class<?> clazz){
		ResourceBundle b = ResourceBundle.getBundle(clazz.getName(), locale);
		bundles.put(
				new Pair<Locale, Class<?>>(locale, clazz)
				, new WeakReference<ResourceBundle>(b)
				);
		return b;
	}

	private static Map<Pair<Locale, Class<?>>, WeakReference<ResourceBundle>> bundles;
	static{
		bundles = new HashMap<Pair<Locale, Class<?>>, WeakReference<ResourceBundle>>();
	}
}
