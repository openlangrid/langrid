package jp.go.nict.langrid.management.web.utility;

public class HTMLUtil{
	/**
	 * 
	 * 
	 */
	public static String addBR(String message, int interval){
		if(message == null || message.equals("")){
			return "";
		}
		int count = interval < message.length() ? interval  : message.length();
		int offset = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(message.substring(offset, count));
		while(count < (message.length() - 1)){
			offset += interval;
			count = (count + interval) < message.length() ? interval + count : message.length();
			sb.append("<br/>");
			sb.append(message.substring(offset, count));
		}
		return sb.toString();
	}
}
