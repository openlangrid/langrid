/*
 * $Id: FileNameCodec.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.codec;

import jp.go.nict.langrid.commons.lang.PlatformUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public abstract class FileNameCodec {
	/**
	 * 
	 * 
	 */
	public abstract String encode(String value);

	/**
	 * 
	 * 
	 */
	public abstract String decode(String value);

	/**
	 * 
	 * 
	 */
	public static FileNameCodec getInstance(){
		return instance;
	}

	protected String decode(String value, char headerChar){
		char[] chars = value.toCharArray();
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < chars.length; i++){
			char c = chars[i];
			if(c == headerChar){
				i += decodeChars(chars, i, b, headerChar);
				i--;
			} else{
				b.append(c);
			}
		}
		return b.toString();
	}

	protected int decodeChars(
			char[] sequence, int start, StringBuilder buffer, char headerChar)
	{
		char c1 = sequence[start];
		char c2 = sequence[start + 1];
		char c3 = sequence[start + 2];
		if(c1 != headerChar){
			buffer.append(c1);
			return 1;
		}
		if(!isHexAlphaOrDigit(c2)){
			buffer.append(c1);
			return 1;
		}
		if(!isHexAlphaOrDigit(c3)){
			buffer.append(sequence, start, 2);
			return 2;
		}
		try{
			buffer.append((char)Integer.parseInt(
					new String(sequence, start + 1, 2), 16)
					);
		} catch(NumberFormatException e){
			buffer.append(sequence, start, 3);
		}
		return 3;
	}

	protected boolean isHexAlphaOrDigit(char c){
		int i = c - '0';
		if((0 < i) && (i < digits.length())){
			if(c == digits.charAt(i)) return true;
		}
		i = c - 'a';
		if((0 < i) && (i < lhexalphas.length())){
			if(c == lhexalphas.charAt(i)) return true;
		}
		i = c - 'A';
		if((0 < i) && (i < uhexalphas.length())){
			if(c == uhexalphas.charAt(i)) return true;
		}
		return false;
	}

	private static final String digits = "0123456789";
	private static final String lhexalphas = "abcdef";
	private static final String uhexalphas = "ABCDEF";

	private static FileNameCodec instance;

	static{
		if(PlatformUtil.isMacOSX()){
			instance = new UnixFileNameCodec();
		} else if(PlatformUtil.isLinux()){
			instance = new UnixFileNameCodec();
		} else if(PlatformUtil.isWindows()){
			instance = new WindowsFileNameCodec();
		}
	}
}
