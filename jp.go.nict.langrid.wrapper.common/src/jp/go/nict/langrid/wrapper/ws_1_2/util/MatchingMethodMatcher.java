/*
 * $Id: MatchingMethodMatcher.java 265 2010-10-03 10:25:32Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.wrapper.ws_1_2.util;

import java.util.Locale;

import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public class MatchingMethodMatcher {
	/**
	 * 
	 * 
	 */
	public MatchingMethodMatcher(String matchingValue, MatchingMethod matchingMethod){
		this.vrhv = matchingValue.toLowerCase(Locale.ENGLISH);
		switch(matchingMethod){
			default:
			case COMPLETE:
				this.matcher = complete;
				break;
			case PARTIAL:
				this.matcher = partial;
				break;
			case PREFIX:
				this.matcher = prefix;
				break;
			case SUFFIX:
				this.matcher = suffix;
				break;
			case REGEX:
				this.matcher = regex;
				break;
		}
	}

	/**
	 * 
	 * 
	 */
	public boolean matches(String lhv){
		return matcher.matches(lhv.toLowerCase(Locale.ENGLISH), vrhv);
	}

	private Matcher<String> matcher;
	private String vrhv;

	private static interface Matcher<T>{
		boolean matches(T lhv, T rhv);
	}

	private static Matcher<String> complete = new Matcher<String>(){
		public boolean matches(String lhv, String rhv) {
			return lhv.equals(rhv);
		}
	};
	private static Matcher<String> partial = new Matcher<String>(){
		public boolean matches(String lhv, String rhv) {
			return lhv.contains(rhv);
		}
	};
	private static Matcher<String> prefix = new Matcher<String>(){
		public boolean matches(String lhv, String rhv) {
			return lhv.startsWith(rhv);
		}
	};
	private static Matcher<String> suffix = new Matcher<String>(){
		public boolean matches(String lhv, String rhv) {
			return lhv.endsWith(rhv);
		}
	};
	private static Matcher<String> regex = new Matcher<String>(){
		public boolean matches(String lhv, String rhv) {
			return lhv.matches(rhv);
		}
	};
}
