/*
 * $Id: WindowsFileNameCodec.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
public class WindowsFileNameCodec extends FileNameCodec {
	@Override
	public String encode(String value){
		if(value.equals(".")){
			return sharpEncode('.'); 
		} else if(value.equals("..")){
			return sharpEncode('.') + sharpEncode('.'); 
		}
		char[] chars = value.toCharArray();
		StringBuilder b = new StringBuilder();
		int i = 0;
		if(chars.length >= 3 ){
			String head = new String(chars, i, 3);
			if(head.equalsIgnoreCase("aux")
					|| head.equalsIgnoreCase("con")
					|| head.equalsIgnoreCase("nul")
					|| head.equalsIgnoreCase("prn")
					)
			{
				if(chars.length >= 4){
					if(chars[3] == '.'){
						sharpEncode(chars, i, 3, b);
						i += 3;
					}
				} else{
					sharpEncode(chars, i, 3, b);
					i += 3;
				}
			}
			if(head.equalsIgnoreCase("lpt")
					|| head.equalsIgnoreCase("com")
					)
			{
				if(chars.length >= 4){
					if(Character.isDigit(chars[3])){
						sharpEncode(chars, i, 3, b);
						i += 3;
					}
				}
			}
		}
		for(; i < chars.length; i++){
			char c = chars[i];
			switch(c){
				case '\\':
				case '/':
				case ',':
				case ';':
				case ':':
				case '*':
				case '?':
				case '"':
				case '<':
				case '>':
				case '|':
					b.append(sharpEncode(c));
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
		return decode(value, '#');
	}

	protected void sharpEncode(char[] chars, int start, int count, StringBuilder builder){
		for(int i = start; i < start + count; i++){
			builder.append(String.format("#%02x", (int)chars[i]));
		}
	}

	protected String sharpEncode(char c){
		return String.format("#%02x", (int)c);
	}
}
