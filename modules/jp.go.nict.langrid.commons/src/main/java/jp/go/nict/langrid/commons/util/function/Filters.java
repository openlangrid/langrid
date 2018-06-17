/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
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
package jp.go.nict.langrid.commons.util.function;

public class Filters {
	public static <T> Predicate<T> nonNull(){
		return new Predicate<T>() {
			@Override
			public boolean test(T value) {
				return value != null;
			}
		};
	}

	public static Predicate<String> ignoreBlankAndComment(){
		return new Predicate<String>() {
			@Override
			public boolean test(String value) {
				if(value.length() == 0 || value.trim().startsWith("#")) return false;
				return true;
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static <T> Predicate<T> pass(){
		return PASS;
	}

	@SuppressWarnings("rawtypes")
	private static final Predicate PASS = new Predicate() {
		@Override
		public boolean test(Object value) {
			return true;
		}
	};
}
