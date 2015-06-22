package jp.go.nict.langrid.wrapper.ws_1_2.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextParser {
	public static String preprocessOriginal(String lang, String content) {
		content = content.replace("\\", "");
		content = content.replace("&nbsp;", " ");
		
		content = content.replaceAll("[ \\t\\x0B\\f]*\\r\\n[ \\t\\x0B\\f]*", retCodeMap.get("\r\n"));
		content = content.replaceAll("[ \\t\\x0B\\f]*\\r[ \\t\\x0B\\f]*", retCodeMap.get("\r"));
		content = content.replaceAll("[ \\t\\x0B\\f]*\\n[ \\t\\x0B\\f]*", retCodeMap.get("\n"));
		
		content = TextParser.stripTags(content, allowedTags);
		
		content = ExceptionWord.encodeExceptionWord(content);
		content = ExceptionWord.encodeInvalidSeparatorWithLanguage(content, lang);

		return content;
	}
	
	public static HashMap<String, String> getFirstSentence(String lang, String sentences) {
		ArrayList<String> separators = ExceptionWord.getSeparators(lang);
		HashMap<String, String> res = new HashMap<String, String>();
		
		String tag = "";
		int firstTagOffset = MAX_OFFSET;
		while(true) {
			sentences = sentences.trim();
			
			Pattern p = Pattern.compile("<\\/?[^<>]*>");
			Matcher m = p.matcher(sentences);
			if (!m.find()) {
				break;
			} else if (m.start() != 0) {
				firstTagOffset = m.start();
				break;
			} 
			
			tag = m.group();
			sentences = sentences.substring(m.end());	
		}
		
		// separatorIndexが各終端記号において検索され、あとの検索結果によって最初の検索結果が上書きされてしまう。
		int separatorIndex = MAX_OFFSET;
		for (String sep : separators) {
			int pos = sentences.indexOf(sep);
			if (pos != -1 && pos + 1 < separatorIndex) {
				separatorIndex = pos + 1;
			}
		}
		
		Pattern p = Pattern.compile("\\[\\[#ret[^]]*\\]\\]");
		Matcher m = p.matcher(sentences);
		int retIndex;
		if (m.find()) {
			retIndex = m.start();
			if (retIndex == 0) {
				res.put("first", m.group());
				res.put("tag", "");
				res.put("remain", sentences.substring(m.end()));
				return res;
			} 
		} else {
			retIndex = MAX_OFFSET;
		}
		
		int minIndex;
		if (separatorIndex < firstTagOffset) {
			minIndex = separatorIndex;
		} else { 
			minIndex = firstTagOffset;
		}

		if (retIndex < minIndex) minIndex = retIndex;
		
		String first, remain;
		if (minIndex == MAX_OFFSET) {
			first = sentences;
			remain =  "";
		} else {
			first = sentences.substring(0, minIndex);
			remain = sentences.substring(minIndex);
		}
		first = ExceptionWord.decode(first).trim();
		
		res.put("first", first);
		res.put("tag", tag);
		res.put("remain", remain);
		return res;
	}
	
	public static String stripTags(String text, ArrayList<String> allowedTags) {
		String[] tag_list;
		if (allowedTags != null) {
			tag_list = allowedTags.toArray(new String[]{});
			Arrays.sort(tag_list);
		} else {
			tag_list = new String[]{};
		}
			
		final Pattern p = Pattern.compile("<[/!]?([^\\s>]*)\\s*[^>]*>", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);

		StringBuilder out = new StringBuilder();
		int lastPos = 0;
		while (m.find()) {
			String tag = m.group(1);
			// if tag not allowed: skip it
			if (Arrays.binarySearch(tag_list, tag) < 0) {
				String aStr = text.substring(lastPos, m.start());
				if (out.lastIndexOf(">") < out.length() - 1 && out.lastIndexOf(" ") < out.length() - 1
						&& out.lastIndexOf("]]") < out.length() - 2){
					out.append(" ").append(aStr.trim());
				} else if(out.lastIndexOf(" ") == out.length() - 1 ) {
					out.append(aStr.trim());
				} else {
					out.append(aStr);
				}
			} else {
				out.append(text.substring(lastPos, m.end()));
			}
			lastPos = m.end();
		}
		if (lastPos > 0) {
			out.append(text.substring(lastPos));
			return out.toString().trim();
		} else {
			return text;
		}
	}
	
	public static String getRetSymbol(String retCode) {
		return retCodeMap.get(retCode);
	}
	
	private final static int MAX_OFFSET = 100000;
	private static ArrayList<String> allowedTags = new ArrayList<String>();
	private static HashMap<String, String> retCodeMap = new HashMap<String, String>();
	static {
		allowedTags.addAll(new ArrayList<String>(Arrays.asList(
				"ul", "li", "ol", "dt", "dl", "dd", "table", "tr", "th", "td", "br", "h1", "h2", "h3", "h4", "h5", "h6")));
		
		retCodeMap.put("\n", "[[#ret_n]]");
		retCodeMap.put("\r", "[[#ret_r]]");
		retCodeMap.put("\r\n", "[[#ret_rn]]");
	}
}
