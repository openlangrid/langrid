package jp.go.nict.langrid.commons.codec;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class URLCodec {
	/**
	 * 
	 * 
	 */
	public static String encode(String value){
		try{
			return URLEncoder.encode(value, "UTF-8");
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static String decode(String value){
		try{
			return URLDecoder.decode(value, "UTF-8");
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}
}
