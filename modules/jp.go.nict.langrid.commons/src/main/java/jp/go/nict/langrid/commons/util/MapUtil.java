/*
 * $Id: MapUtil.java 956 2013-10-16 16:25:02Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.util;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 956 $
 */
public class MapUtil {
	/**
	 * 
	 * 
	 */
	public static <T, U> Map<T, U> newConcurrentHashMap(){
		return new ConcurrentHashMap<T, U>();
	}

	/**
	 * 
	 * 
	 */
	public static <T extends Enum<T>, U> Map<T, U> newEnumMap(Class<T> clazz){
		return new EnumMap<T, U>(clazz);
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> Map<T, U> newHashMap(){
		return new HashMap<T, U>();
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> Map<T, U> newLinkedHashMap(){
		return new LinkedHashMap<T, U>();
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> Map<T, U> newTreeMap(){
		return new TreeMap<T, U>();
	}

	/**
	 * 
	 * 
	 */
	public static <T, U> Map<T, U> newWeakHashMap(){
		return new WeakHashMap<T, U>();
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> Map<T, U> emptyMap(){
		return Collections.EMPTY_MAP;
	}

	public static SOMapBuilder newSOMapBuilder(){
		return new SOMapBuilder();
	}
}
