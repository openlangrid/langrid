/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 * Copyright (C) The Language Grid Project.
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
package jp.go.nict.langrid.commons.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jp.go.nict.langrid.commons.lang.StringUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class MessageDigestUtil {
	/**
	 * 
	 * 
	 */
	public static String digestBySHA512(String value){
/*		return value;
/*/
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] digest = md.digest(StringUtil.toUSASCIIBytes(value));
			return String.format("%0128x", new BigInteger(1, digest));
		} catch(NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		}
//*/
	}

	public static String digestByMD5(String value){
/*		return value;
/*/
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(StringUtil.toUSASCIIBytes(value));
			return String.format("%x", new BigInteger(1, digest));
		} catch(NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		}
//*/
	}
}
