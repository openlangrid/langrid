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
package jp.go.nict.langrid.commons.ws.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class Base64Util {
	/**
	 * 
	 * 
	 */
	public static String encode(String src){
		try{
			return new String(encode(src.getBytes("ISO8859_1")), "ISO8859_1");
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static byte[] encode(byte[] src){
		return Base64.encodeBase64(src);
	}

	/**
	 * 
	 * 
	 */
	public static String decode(String src){
		try{
			return new String(decode(src.getBytes("ISO8859_1")), "ISO8859_1");
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static byte[] decode(byte[] src){
		return Base64.decodeBase64(src);
	}
}
