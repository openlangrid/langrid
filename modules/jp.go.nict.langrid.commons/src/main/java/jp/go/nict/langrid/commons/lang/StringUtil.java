/*
 * $Id: StringUtil.java 1221 2014-06-27 06:16:37Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.lang;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.transformer.Transformer;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1221 $
 */
public final class StringUtil {
	/**
	 * 
	 * 
	 */
	public static byte[] toUTF8Bytes(String value){
/*
		ByteBuffer buff = CharsetUtil.UTF_8.encode(value);
		return Arrays.copyOf(buff.array(), buff.remaining());
*/
		try{
			return value.getBytes(UTF_8);
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static byte[] toUSASCIIBytes(String value){
/*
		ByteBuffer buff = CharsetUtil.UTF_8.encode(value);
		return Arrays.copyOf(buff.array(), buff.remaining());
*/
		try{
			return value.getBytes(US_ASCII);
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static String utf8BytesToString(byte[] bytes){
/*
		return CharsetUtil.UTF_8.decode(ByteBuffer.wrap(bytes)).toString();
*/
		try{
			return new String(bytes, UTF_8);
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static String randomString(int count){
		StringBuilder b = new StringBuilder();
		int len = chars.length();
		for(int i = 0; i < count; i++){
			b.append(chars.charAt((int)(Math.random() * len)));
		}
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String repeatedString(String value, int count){
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < count; i++){
			b.append(value);
		}
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String repeatedString(String value, int count, String separator){
		if(count <= 0) return "";
		if(count == 1){
			return value;
		}
		StringBuilder b = new StringBuilder();
		for(int i = 1; i < count; i++){
			b.append(value);
			b.append(separator);
		}
		b.append(value);
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String repeatedString(StringGenerator generator, int count, String separator){
		if(count <= 1){
			if(count == 0) return "";
			else if(count == 1) return generator.generate();
			else throw new IllegalArgumentException("count: " + count);
		}
		StringBuilder b = new StringBuilder();
		for(int i = 1; i < count; i++){
			b.append(generator.generate());
			b.append(separator);
		}
		b.append(generator.generate());
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String join(String[] elements, String separator){
		return join(elements, separator, 0, elements.length);
	}

	/**
	 * 
	 * 
	 */
	public static String join(String[] elements, String separator, int begin, int end){
		if (begin < 0) {
		    throw new ArrayIndexOutOfBoundsException(begin);
		}
		if (end > elements.length) {
		    throw new ArrayIndexOutOfBoundsException(end);
		}
		if (begin > end) {
		    throw new ArrayIndexOutOfBoundsException(end - begin);
		}
		if(elements.length == 0) return "";
		StringBuilder b = new StringBuilder(elements[0]);
		for(int i = (begin + 1); i < end; i++){
			b.append(separator);
			b.append(elements[i]);
		}
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static <T> String join(
			T[] elements, Transformer<T, String> textizer, String separator)
	{
		return join(elements, textizer, separator, 0, elements.length);
	}

	/**
	 * 
	 * 
	 */
	public static <T> String join(
			T[] elements, Transformer<T, String> textizer, String separator
			, int begin, int end)
	{
		if (begin < 0) {
		    throw new ArrayIndexOutOfBoundsException(begin);
		}
		if (end > elements.length) {
		    throw new ArrayIndexOutOfBoundsException(end);
		}
		if (begin > end) {
		    throw new ArrayIndexOutOfBoundsException(end - begin);
		}
		if(elements.length == 0) return "";
		if(begin == end) return "";
		StringBuilder b = new StringBuilder(textizer.transform(elements[0]));
		for(int i = (begin + 1); i < end; i++){
			b.append(separator);
			b.append(textizer.transform(elements[i]));
		}
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String encodeTuple(String... values){
		return new StringBuilder()
				.append("((")
				.append(join(values, ")("))
				.append("))")
				.toString();
	}
	/**
	 * 
	 * 
	 */
	public static String[] decodeTuple(String value)
		throws ParseException
	{
		List<String> tuples = new ArrayList<String>();
		int offset = -1;
		int nest = 0;
		int valueNest = -1;
		StringBuilder b = new StringBuilder();
		for(char c : value.toCharArray()){
			offset++;
			switch(c){
				case '(':
					nest++;
					if(b.toString().trim().length() != 0){
						throw new ParseException(
								"Tuple contains invalid expression: " + value
								, offset);
					}
					b = new StringBuilder();
					break;
				case ')':
					if(nest == 0){
						throw new ParseException(
								"Tuple contains invalid expression: " + value
								, offset);
					}
					String v = b.toString().trim();
					if(v.length() > 0){
						if(valueNest == -1){
							valueNest = nest;
						} else if(valueNest != nest){
							throw new ParseException(
									"Tuple contains invalid expression: " + value
									, offset);
						}
						tuples.add(v);
						b = new StringBuilder();
					}
					nest--;
					break;
				default:
					b.append(c);
				break;
			}
		}
		if(nest != 0){
			throw new ParseException(
					"Too few close bracket: " + value
					, offset);
		}
		String v = b.toString().trim();
		if(v.length() > 0){
			if(valueNest == -1){ 
				tuples.add(v);
			} else{
				throw new ParseException(
						"Tuple contains invalid expression: " + value
						, offset);
			}
		}
		return tuples.toArray(new String[]{});
	}

	/**
	 * 
	 * 
	 */
	public static String unquote(String value){
		if(value.equals("\"\"")) return "";
		if(value.startsWith("\"") && value.endsWith("\"")){
	   		return value.substring(1, value.length() - 1);
	   	} else{
	   		return value;
	   	}
	}

	/**
	 * 
	 * 
	 */
	public static String escape(String value, String escapeChars, char metaChar){
		StringBuilder b = new StringBuilder();
		for(char c : value.toCharArray()){
			if(c == metaChar){
				b.append(metaChar);
				b.append(metaChar);
			} else if(escapeChars.indexOf(c) != -1){
				b.append(metaChar);
				b.append(String.format("%02X", (int)c));
			} else{
				b.append(c);
			}
		}
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String encodeHttpHeaderValueAsUTF8(String text){
		try{
			StringBuilder b = new StringBuilder();
			for(char c : text.toCharArray()){
				if(0x20 <= c && c != '%' && c != '+' && c <= 0x7e){
					b.append(c);
				} else{
					byte[] bytes = new String(new char[]{c}).getBytes("UTF-8");
					for(byte by : bytes){
						b.append('%');
						b.append(String.format("%02X", by));
					}
				}
			}
			return b.toString();
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	private static enum PrevChar{META,CODE,NORMAL};
	/**
	 * 
	 * 
	 */
	public static String unescape(String value, String escapeChars, char metaChar){
		StringBuilder b = new StringBuilder();
		PrevChar p = PrevChar.NORMAL;
		char prevCodeChar = 0;
		for(char c : value.toCharArray()){
			switch(p){
				case META:
					if(c == metaChar){
						b.append(metaChar);
						p = PrevChar.NORMAL;
					} else if(CharacterUtil.isHexChar(c)){
						prevCodeChar = c;
						p = PrevChar.CODE;
					} else{
						b.append(c);
						p = PrevChar.NORMAL;
					}
					break;
				case CODE:
					if(CharacterUtil.isHexChar(c)){
						b.append((char)(
								Character.getNumericValue(prevCodeChar) * 16
								+ Character.getNumericValue(c)
								));
					} else{
						b.append(metaChar);
						b.append(prevCodeChar);
						b.append(c);
					}
					p = PrevChar.NORMAL;
					break;
				case NORMAL:
					if(c == metaChar){
						p = PrevChar.META;
					} else{
						b.append(c);
					}
					break;
			}
		}
		if(p == PrevChar.META){
			b.append(metaChar);
		} else if(p == PrevChar.CODE){
			b.append(metaChar);
			b.append(prevCodeChar);
		}
		return b.toString();
	}

	public static String substringUntil(String org, int begin, char term){
		int i = org.indexOf(term, begin);
		if(i == -1) return org.substring(begin);
		return org.substring(begin, i);
	}

	public static String dequote(String value){
		if(value.length() < 2) return value;
		char f = first(value);
		char l = last(value);
		if((f == '"' && l == '"') || (f == '\'' && l == '\'')){
			return value.substring(1, value.length() - 1);
		}
		return value;
	}

	public static char first(String value){
		return value.charAt(0);
	}

	public static char last(String value){
		return value.charAt(value.length() - 1);
	}

	private static final String chars
		= "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String UTF_8 = "UTF-8";
	private static final String US_ASCII = "US-ASCII";
}
