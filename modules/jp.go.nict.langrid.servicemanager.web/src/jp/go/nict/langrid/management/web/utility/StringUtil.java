/*
 * $Id: StringUtil.java 406 2011-08-25 02:12:29Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class StringUtil {
	public static boolean isValidString(String target){
		if(target == null){
			return false;
		}
		if(target.equals("")){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * 
	 */
	public static String toUppercaseHeadCharactor(String str) {
		StringBuffer sb = new StringBuffer();
		sb.append(str.substring(0, 1).toUpperCase());
		sb.append(str.substring(1).toLowerCase());
		return sb.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String[] extrateUrl(String str){
		if(str != null && !str.equals("")){
			List<String> results = new ArrayList<String>();
			String[] temp = str.split("http://");
			int i = 0;
			String prefix = "";
			for(String temp2 : temp){
				String[] temp3 = temp2.split(" ");
				String[] temp4 = temp3[0].split("\n");
				String[] temp5 = temp4[0].split("\r");
				String[] temp6 = temp5[0].split("\t");
				String[] temp7 = temp6[0].split("]");				
				if(i++ != 0){
					prefix = "http://";
					results.add(prefix + temp7[0]);
				}
			}
			return results.toArray(new String[]{});
		}
		return new String[]{};
	}
	
	/**
	 * 
	 * 
	 */
	public static String insertStringPerCount(String orig, String inserted, int count) {
	   StringBuilder sb = new StringBuilder();
	   int start = 0;
	   int end = orig.length();
	   while(start < end) {
	      int tempCount = start + count < end ? start + count : end;
	      sb.append(orig.substring(start, tempCount) + inserted);
	      start = tempCount;
	   }
	   return sb.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String getLimitStyle(String str, int count){
		if(str == null)
			return "";
		String tempStr = "";
		int tempCount = count - 1;
		for(int i = 0; i < str.length(); i++){
			tempStr += StringUtils.substring(str, i, i + 1);
			if(!isHalfSizeAlphaNumeral(tempStr)){
				tempCount = i + count;
				tempStr = "";
			}
			if(i > tempCount - 1){
				if(isHalfSizeAlphaNumeral(tempStr)){
					return "word-break:break-all;";
				}
				tempCount = i + count;
				tempStr = "";
			}
		}
		return "";
	}

	/**
	 * 
	 * 
	 */
	public static String getUniqueString(){
		MessageDigest sha;
		try{
			sha = MessageDigest.getInstance("SHA-1");
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			return "";
		}
		sha.reset();
		sha.update(String.valueOf(Math.random()).getBytes());
		byte[] hash = sha.digest();
		StringBuffer sb = new StringBuffer();
		int cnt = hash.length;
		for(int i = 0; i < cnt; i++){
			int d = hash[i];
			if(d < 0){
				d += 256;
			}
			sb.append(Integer.toHexString(d & 0x0F));
		}
		return sb.toString();
	}

	/**
	 * 
	 * 
	 */
	public static boolean isHalfSizeAlphaNumeral(String str){
		if(!Pattern.compile("[!-,.-~]+").matcher(str).matches()){
			// if (!Pattern.compile("[a-zA-Z0-9]+").matcher(str).matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	public static String nullString(String str){
		if(str == null){
			return "";
		}
		return str;
	}

	/**
	 * 
	 * 
	 */
	public static String nullTohyphen(String str){
		return (str == null || str.equals("")) ? "-" : str;
	}

	/**
	 * 
	 * 
	 */
	public static String nullandStringTohyphen(String str){
	   return (str == null || str.equals("") || str.equals("null")) ? "-" : str;
	}

	/**
	 * 
	 * 
	 */
	public static String randomPassword(int size){
		SecureRandom random = new SecureRandom();
		char[] pass = new char[size];
		for(int k = 0; k < pass.length; k++){
			switch(random.nextInt(3)){
			case 0: // 'a' - 'z'
				pass[k] = (char)(97 + random.nextInt(26));
				break;
			case 1: // 'A' - 'Z'
				pass[k] = (char)(65 + random.nextInt(26));
				break;
			case 2: // '0' - '9'
				pass[k] = (char)(48 + random.nextInt(10));
				break;
			default:
				pass[k] = 'a';
			}
		}
		return new String(pass);
	}

	/**
	 * 
	 * 
	 */
	public static String replaceAtMark(String str){
		return str.replace("@", " [at] ");
	}

	/**
	 * 
	 * 
	 */
	public static String shortenString(String str, int i){
		if((str != null) && (str.length() > i)){
			str = str.substring(0, i) + "...";
		}
		return nullString(str);
	}
	
	/**
	 * 
	 * 
	 */
	public static String makeMasked(String message){
		if(message == null || message.equals("")){
			return "";
		}
		String masked = "";
		for(int i = 0; i < message.length(); i++){
			masked += "*";
		}
		
		return masked;
	}

	/**
	 * 
	 * 
	 */
	public static String replaceURLStringToLinkTags(String string) {
		String[] urls = StringUtil.extrateUrl(string);
		String result = string;
		for(String url : urls) {
			StringBuilder sb = new StringBuilder();
			sb.append("<a target=\"_blank\" href=\"");
			sb.append(url);
			sb.append("\">");
			sb.append(url);
			sb.append("</a>");
			result = result.replace(url, sb.toString());
		}
		return result;
	}
}
