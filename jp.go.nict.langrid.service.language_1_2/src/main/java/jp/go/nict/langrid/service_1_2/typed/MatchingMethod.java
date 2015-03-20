/*
 * $Id: MatchingMethod.java 458 2011-12-21 10:27:33Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.typed;

/**
 * 
 * Defines the matching method.
 * Depending on the search service called, some unsupported matching methods exist.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 458 $
 */
public enum MatchingMethod {
	/**
	 * 
	 * The strings completely match.
	 * 
	 */
	COMPLETE,

	/**
	 * 
	 * Partial matching.
	 * 
	 */
	PARTIAL,

	/**
	 * 
	 * Prefix matching.
	 * 
	 */
	PREFIX,

	/**
	 * 
	 * Suffix matching.
	 * 
	 */
	SUFFIX,

	/**
	 * 
	 * Regular expression matching.
	 * 
	 */
	REGEX,

	/**
	 * 
	 * Language path matching.
	 * Matches prefix and suffix, checks indermediates only in order of appearance.
	 * Match (ja zh en) with (ja ko zh de fr en).
	 * 
	 */
	LANGUAGEPATH,

	/**
	 * 
	 * 
	 */
	IN,

	/**
	 * 
	 * 
	 */
	EQ,

	/**
	 * 
	 * 
	 */
	GT,

	/**
	 * 
	 * 
	 */
	GE,

	/**
	 * 
	 * 
	 */
	LT,

	/**
	 * 
	 * 
	 */
	LE,
	;
}
