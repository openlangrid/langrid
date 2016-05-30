/*
 * $Id:DictType.java 3343 2006-10-13 14:06:48 +0900 (金, 13 10 2006) nakaguchi $
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
package jp.go.nict.langrid.service_1_2.dictionary.typed;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public enum Relation {
	/**
	 * 
	 * 
	 */
	synonym,

	/**
	 * 
	 * 
	 */
	antonym,

	/**
	 * 
	 * 
	 */
	hypernym,

	/**
	 * 
	 * 
	 */
	hyponym,

	/**
	 * 
	 * 
	 */
	holonym,

	/**
	 * 
	 * 
	 */
	meronym,

	/**
	 * 
	 * 
	 */
	upper,

	/**
	 * 
	 * 
	 */
	lower,
	;

	/**
	 * 
	 * 
	 */
	public static Collection<Relation> valuesCollection(){
		return values;
	}

	private static final Collection<Relation> values;

	static{
		values = Collections.unmodifiableCollection(
			Arrays.asList(values())
			);
	}
}
