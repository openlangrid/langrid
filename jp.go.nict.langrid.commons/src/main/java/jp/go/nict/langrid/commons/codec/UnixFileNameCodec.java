/*
 * $Id: UnixFileNameCodec.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class UnixFileNameCodec extends FileNameCodec {
	@Override
	public String encode(String value) {
		StringBuilder b = new StringBuilder();
		if(value.equals(".")){
			return backslashEncode('.'); 
		} else if(value.equals("..")){
			return backslashEncode('.') + backslashEncode('.'); 
		}
		char[] chars = value.toCharArray();
		for(int i = 0; i < chars.length; i++){
			char c = chars[i];
			switch(c){
				case '/':
					b.append(backslashEncode(c));
					break;
				default:
					b.append(c);
					break;
			}
		}
		return b.toString();
	}

	@Override
	public String decode(String value){
		return decode(value, '\\');
	}

	protected String backslashEncode(char c){
		return String.format("\\%02x", (int)c);
	}
}
