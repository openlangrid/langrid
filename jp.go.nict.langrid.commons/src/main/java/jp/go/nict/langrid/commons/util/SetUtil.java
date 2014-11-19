/*
 * $Id: SetUtil.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class SetUtil {
	/**
	 * 
	 * 
	 */
	public static <T> Set<T> newCopyOnWriteArraySet(){
		return new CopyOnWriteArraySet<T>();
	}

	/**
	 * 
	 * 
	 */
	public static <T> Set<T> newHashSet(){
		return new HashSet<T>();
	}

	/**
	 * 
	 * 
	 */
	public static <T> Set<T> newLinkedHashSet(){
		return new LinkedHashSet<T>();
	}

	/**
	 * 
	 * 
	 */
	public static <T> Set<T> newTreeSet(){
		return new TreeSet<T>();
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> emptySet(){
		return Collections.EMPTY_SET;
	}
}
