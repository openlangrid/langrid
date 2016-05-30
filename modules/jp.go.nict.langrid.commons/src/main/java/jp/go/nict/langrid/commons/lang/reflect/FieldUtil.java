/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.lang.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utilities for java.lang.reflect.Field.
 * @author Takao Nakaguchi
 */
public class FieldUtil {
	public static Field[] listDeclaredFields(Class<?> clazz, String pattern){
		List<Field> ret = new ArrayList<Field>();
		Pattern p = Pattern.compile(pattern);
		for(Field f : clazz.getDeclaredFields()){
			if(p.matcher(f.getName()).matches()){
				ret.add(f);
			}
		}
		return ret.toArray(empty);
	}

	private static Field[] empty = {};
}
