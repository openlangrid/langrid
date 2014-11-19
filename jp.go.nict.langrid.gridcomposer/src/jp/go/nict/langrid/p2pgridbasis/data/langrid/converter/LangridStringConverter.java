/*
 * $Id: LangridStringConverter.java 318 2010-12-03 03:10:29Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.data.langrid.converter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.Converter;

public class LangridStringConverter implements Converter {

	private Map<Class, Converter> converters;

	public LangridStringConverter() {
		converters = new HashMap<Class, Converter>();
	}

	public void register(Class clazz, Converter converter) {
		converters.put(clazz, converter);
	}

	public Object convert(Class type, Object value) {
		if (value == null) {
			return ((String) null);
		} 

		if (String.class.equals(type)) {
			for (Class<?> clazz : converters.keySet()) {
				if (clazz.isAssignableFrom(value.getClass())) {
					return converters.get(clazz).convert(type, value);
				}
			}
		}

		return value.toString();
	}

	public Converter lookup(Class clazz) {
		return converters.get(clazz);
	}
}
