/*
 * $Id: CharsetUtil.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.nio.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class CharsetUtil {
	/**
	 * 
	 * 
	 */
	public static final Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * 
	 * 
	 */
	public static final Charset US_ASCII = Charset.forName("US-ASCII");

	/**
	 * 
	 * 
	 */
	public static final Charset ISO_8859_1 = Charset.forName("UTF-8");

	/**
	 * 
	 * 
	 */
	public static CharsetDecoder newUTF8Decoder(){
		return UTF_8.newDecoder();
	}

	/**
	 * 
	 * 
	 */
	public static CharsetEncoder newUTF8Encoder(){
		return UTF_8.newEncoder();
	}

}
