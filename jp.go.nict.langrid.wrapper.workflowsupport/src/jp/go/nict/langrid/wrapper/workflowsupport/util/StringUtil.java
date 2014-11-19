/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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

package jp.go.nict.langrid.wrapper.workflowsupport.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

/**
 * 文字列関連クラス
 * @author Jun Koyama
 * @author Takao Nakaguchi
 */
public class StringUtil {
	/**
	 * 文字列から中間コードを生成する。SHA1版。
	 * 衝突確立は上がるが、生成される文字列の長さは常に41文字以内になる。
	 * @param term 対象文字列
	 * @return 中間コード文字列
	 */
	public static String generateCode(String term, int primary) {
		char[] chars = Integer.toString(
				(int)(0x7fffffff & (
						System.currentTimeMillis()
						+ (generateCodeCounter.incrementAndGet() << 16)
						+ generateCodeRand
						+ (primary << 24)
						))
				, Character.MAX_RADIX - 10).toCharArray();
		for(int i = 0; i < chars.length; i++){
			if('0' <= chars[i] && chars[i] <= '9'){
				chars[i] = (char)('q' + (chars[i] - '0'));
			}
		}
		return "xxx" + new String(chars) + "xxx";
	}

	/**
	 * 文字列から中間コードを生成する。SHA1版。
	 * 衝突確立は上がるが、生成される文字列の長さは常に41文字以内になる。
	 * @param term 対象文字列
	 * @return 中間コード文字列
	 */
	public static String generateCode_sha1(String term, int primary) {
		try{
			byte[] bytes = term.getBytes("UTF-8");
			byte[] digestBytes = new byte[bytes.length + 1 + 8 + 8];
			System.arraycopy(bytes,  0, digestBytes, 0, bytes.length);
			digestBytes[bytes.length] = (byte)(primary & 0xff);
			storeLong(digestBytes, bytes.length + 1, Calendar.getInstance().getTimeInMillis());
			storeLong(digestBytes, bytes.length + 9, System.nanoTime());
			MessageDigest md = MessageDigest.getInstance("SHA1");
			char[] chars = new BigInteger(1, md.digest(digestBytes)).toString(Character.MAX_RADIX - 10).toCharArray();
			for(int i = 0; i < chars.length; i++){
				if('0' <= chars[i] && chars[i] <= '9'){
					chars[i] = (char)('z' - (chars[i] - '0'));
				}
			}
			return "xxx" + new String(chars) + "xxx";
		} catch(NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	private static void storeLong(byte[] bytes, int offset, long value){
		bytes[offset++] = (byte)(value & 0xff);
		bytes[offset++] = (byte)((value >>= 8) & 0xff);
		bytes[offset++] = (byte)((value >>= 8) & 0xff);
		bytes[offset++] = (byte)((value >>= 8) & 0xff);
		bytes[offset++] = (byte)((value >>= 8) & 0xff);
		bytes[offset++] = (byte)((value >>= 8) & 0xff);
		bytes[offset++] = (byte)((value >>= 8) & 0xff);
		bytes[offset++] = (byte)((value >>= 8) & 0xff);
	}

	/**
	 * 文字列から中間コードを生成する。
	 * @param term 対象文字列
	 * @return 中間コード文字列
	 */
	public static String generateCode_old(String term, int primary) {
		StringBuffer buf = new StringBuffer();
		buf.append("xxx");
		char[] ch = term.toCharArray();
		for (char c : ch) {
			String hex = Integer.toHexString(c);
			// 数値をアルファベットに置き換える
			buf.append(number2alphabet(hex));
		}
		buf.append(number2alphabet(String.valueOf(primary)));
		buf.append("xxx");
		return buf.toString();
	}

	// １６進数の数値部分をFより後に変換する。
	private static final Map<String, String> NUMBER_2_ALPHABET = new HashMap<String, String>();
	static {
		NUMBER_2_ALPHABET.put("0", "G");
		NUMBER_2_ALPHABET.put("1", "H");
		NUMBER_2_ALPHABET.put("2", "I");
		NUMBER_2_ALPHABET.put("3", "J");
		NUMBER_2_ALPHABET.put("4", "K");
		NUMBER_2_ALPHABET.put("5", "L");
		NUMBER_2_ALPHABET.put("6", "M");
		NUMBER_2_ALPHABET.put("7", "N");
		NUMBER_2_ALPHABET.put("8", "O");
		NUMBER_2_ALPHABET.put("9", "P");
	}
	/**
	 * 数字をアルファベットに変換して返す
	 * @param str 対象文字列
	 * @return 変換結果
	 */
	public static String number2alphabet(String str) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			String tmp = str.substring(i, i + 1);
			if (NUMBER_2_ALPHABET.containsKey(tmp)) {
				buf.append(NUMBER_2_ALPHABET.get(tmp));
				continue;
			}
			buf.append(tmp.toUpperCase());
		}
		return buf.toString().toLowerCase();
	}

	/**
	 * 形態素配列から文章を生成する
	 * @param isBlank 空白を挿入するかどうか
	 * @param morphemes 形態素配列
	 * @param startIndex 開始インデックス
	 * @param numberOfMorphemes 形態素配列数
	 * @return 生成された文章
	 */
	public static String createWord(boolean isBlank, Morpheme[] morphemes, int startIndex, int numberOfMorphemes) {
		if (morphemes == null) return "";
		StringBuffer buf = new StringBuffer();
		int endIndex = startIndex + numberOfMorphemes;
		for (int i = startIndex; i < endIndex; i++) {
			buf.append(morphemes[i].getWord());
			// 直接繋げる言語でない場合
			if (isBlank) {
				buf.append(" "); // 半角スペース挿入
			}
		}
		return buf.toString().trim();
	}

	/**
	 * 全角アルファベット及び数字を半角にして返す。
	 * @param str 対象文字列
	 * @return 半角にした結果
	 */
	public static String full2HalfAlphabetNumeric(String str) {
		if (str == null) return "";
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
	        if (ch[i] >= 65296 && ch[i] <= 65370) {
	            ch[i]-=65248;
	        }
		}
		return new String(ch);
	}

	/**
	 * HTMLエスケープ文字列が含まれている形態素を再構成し返す。<br>
	 * エスケープ文字列がバラバラになっているものを１つにして、再構成する。
	 * @param morphemes 形態素の配列
	 * @return 構成しなおした形態素の配列
	 */
	public static Morpheme[] escapeCharacterRestructing(Morpheme[] morphemes) {
		List<Morpheme> results = new ArrayList<Morpheme>();
		for (Morpheme m : morphemes) {
			String word = reSanitize(m.getWord());
			String lemma = reSanitize(m.getLemma());
			m.setWord(word);
			m.setLemma(lemma);
		}
		for (int i = 0; i < morphemes.length; i++) {
			Morpheme m = morphemes[i];
			String tmp = m.getWord();
			if (m.getWord() != null && m.getWord().length() > 0 && m.getWord().indexOf("&") == 0) {
				for (int counter = (i + 1); counter < morphemes.length; counter++) {
					tmp += morphemes[counter].getWord();
					if (isSanitizeString(tmp)) {
						if (ESCAPE_SENTENCE.containsKey(tmp)) {
							String word = ESCAPE_SENTENCE.get(tmp);
							m = new Morpheme(word, word, PartOfSpeech.other.name());
							results.add(m);
							i = counter;
							break;
						} else {
							continue;
						}
					} else {
						break;
					}
				}
			} else {
				results.add(m);
			}
		}
		return results.toArray(new Morpheme[]{});
	}
	/**
	 * Sanitize string.
	 */
	private static final Map<String, String> ESCAPE_SENTENCE = new HashMap<String, String>();
	static {
		ESCAPE_SENTENCE.put("&#39;", "'");
		ESCAPE_SENTENCE.put("&quot;", "\"");
		ESCAPE_SENTENCE.put("&nbsp;", " ");
		ESCAPE_SENTENCE.put("&amp;", "&");
		ESCAPE_SENTENCE.put("&lt;", "<");
		ESCAPE_SENTENCE.put("&gt;", ">");
	};

	public static boolean isSanitizeString(String str) {
		if (str == null || str.length() == 0) return false;
		boolean result = false;
		for (int i = 0; i < SANITIZE_STRING.length; i++) {
			if (str.indexOf(SANITIZE_STRING[i][0]) > -1) {
				result = true;
				break;
			}
		}
		return result;
	}
	/**
	 * It returns resanitize string
	 * @param string message
	 * @return
	 */
	public static String reSanitize(String str) {
		String result = str;
		if (result != null && result.length() > 0) {
			for (int i = 0; i < SANITIZE_STRING.length; i++) {
				result = Pattern.compile(SANITIZE_STRING[i][1]).matcher(result).replaceAll(SANITIZE_STRING[i][0]);
			}
		}
		return result;
	}

	/**
	 * Sanitize string.
	 */
	private static final String SANITIZE_STRING[][] = {
		{ "&",  "&amp;" },
		{ "<",  "&lt;" },
		{ ">",  "&gt;" },
		{ "\"", "&quot;" },
		{ "'",  "&#39;" }
	};

	/**
	 * Marking of word.
	 * @param word sentence
	 * @param wid word id
	 * @return
	 */
	public static String markingWord(String word, int wid) {
		word = MessageFormat.format(REPLACE_TAG_B, new Object[]{String.valueOf(wid)}) + word + REPLACE_TAG_E;
		return word;
	}
	private static final String REPLACE_TAG_B = "&lt;replaced wid=&quot;{0}&quot;&gt;";
	private static final String REPLACE_TAG_E = "&lt;/replaced&gt;";
	private static AtomicLong generateCodeCounter = new AtomicLong();
	private static int generateCodeRand = (int)Math.floor(Math.random() * Integer.MAX_VALUE);
//	private static Logger logger = Logger.getLogger(StringUtil.class.getName());

}
