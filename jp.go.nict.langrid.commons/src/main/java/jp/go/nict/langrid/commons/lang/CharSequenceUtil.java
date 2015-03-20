/*
 * $Id: CharacterUtil.java 182 2010-10-02 03:16:36Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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

/**
 * @author Takao Nakaguchi
 */
public class CharSequenceUtil {
	public static CharSequence empty(){
		return new CharSequence() {
			@Override
			public CharSequence subSequence(int start, int end) {
				checkIndex(start, end, length());
				return empty();
			}

			@Override
			public int length() {
				return 0;
			}

			@Override
			public char charAt(int index) {
				throw new IndexOutOfBoundsException();
			}

			@Override
			public String toString() {
				return new StringBuilder().append(this).toString();
			}
		};
	}

	public static CharSequence repeat(final char c, final int count){
		return new CharSequence() {
			@Override
			public CharSequence subSequence(int start, int end) {
				checkIndex(start, end, length());
				return repeat(c, end - start);
			}

			@Override
			public int length() {
				return count;
			}

			@Override
			public char charAt(int arg0) {
				return c;
			}

			@Override
			public String toString() {
				return new StringBuilder().append(this).toString();
			}
		};
	}

	private static void checkIndex(int start, int end, int length){
		if(start < 0) throw new IndexOutOfBoundsException();
		if(end < 0) throw new IndexOutOfBoundsException();
		if(end > length) throw new IndexOutOfBoundsException();
		if(start > end) throw new IndexOutOfBoundsException();
	}
}
