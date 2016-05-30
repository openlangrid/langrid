package jp.go.nict.langrid.service_1_2.util;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;

public class LanguageExceptionConverter {
	/**
	 * 
	 * 
	 */
	public static InvalidParameterException convertException(
		InvalidLanguageTagException e, String aParameterName)
	{
		return new InvalidParameterException(
			aParameterName, ExceptionUtil.getMessageWithStackTrace(e)
			);
	}
}
