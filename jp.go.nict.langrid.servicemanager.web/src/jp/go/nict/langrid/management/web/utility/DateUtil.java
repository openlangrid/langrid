/*
 * $Id: DateUtil.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class DateUtil{
	/**
	 * 
	 * 
	 */
	public static String defaultTimeZone(){
		return YMD_LOCALE.format(new Date());
	}

	/**
	 * 
	 * 
	 */
	public static String formatDAY(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return DAY.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static String formatHMS(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return HMS.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static String formatTIME(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return TIME.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static String formatYMD(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return YMD.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static String formatYMDHM0WithSlash(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return YMDHM0_SLASH.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static String formatYMDHM59WithSlash(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return YMDHM59_SLASH.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static String formatYMDHMSWithSlash(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return YMDHMS_SLASH.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static String formatYMDHMWithSlash(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return YMDHM_SLASH.format(date);
	}
	
	/**
	 * 
	 * 
	 */
	public static String formatYMDWithSlash(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return YMD_SLASH.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static String formatYMDWithSlashLocale(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return YMD_SLASH_LOCALE.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static String formatYMDHMSWithSlashLocale(Date date){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		return YMDHMS_SLASH_LOCALE.format(date);
	}

	/**
	 * 
	 * 
	 */
	public static Date getDate(String text) throws ParseException{
		ParsePosition pp = new ParsePosition(0);
		if(text == null){
			throw new ParseException("", pp.getIndex());
		}
		int textLength = text.length();
		for(SimpleDateFormat format : formats){
			format.setLenient(false);
			pp = new ParsePosition(0);
			Date ret = format.parse(text, pp);
			if(textLength == pp.getIndex()){
				return ret;
			}
		}
		throw new ParseException("", pp.getIndex());
	}

	/**
	 * 
	 * 
	 */
	public static Date getDayBefore(Date date, int n){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		long msec = date.getTime();
		return new Date(msec - ONE_DAY * n);
	}

	/**
	 * 
	 * 
	 */
	public static Date getMinuteAfter(Date date, int n){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		long msec = date.getTime();
		return new Date(msec + ONE_MINUTE * n);
	}

	/**
	 * 
	 * 
	 */
	public static Date getMinuteBefore(Date date, int n){
		if(date == null){
			throw new IllegalArgumentException("Date is Null.");
		}
		long msec = date.getTime();
		return new Date(msec - ONE_MINUTE * n);
	}

	/**
	 * 
	 * 
	 */
	public static Date getSecondBefore(Date date, int n){
		if(date == null){
			throw new IllegalArgumentException("Date is null.");
		}
		long msec = date.getTime();
		return new Date(msec - ONE_SECOND * n);
	}

	/**
	 * 
	 * 
	 */
	public static boolean isSameDay(Date date1, Date date2){
		return org.apache.commons.lang.time.DateUtils.isSameDay(date1, date2);
	}

	/**
	 * 
	 * 
	 */
	public static Date parseDateTextWithSlash(String dateText) throws ParseException{
		return YMD_SLASH.parse(dateText);
	}

	public static final String STR_DAY = "d";
	public static final String STR_HMS = "HH:mm:ss";
	public static final String STR_LOCALE = "z";
	public static final String STR_LOCALE_PAUSE = ":";
	public static final String STR_TIME = "HH:mm";
	public static final String STR_YMD = "yyyyMMdd";
	public static final String STR_YMD_SLASH = "yyyy/MM/dd";
	public static final String STR_YMD_SLASH_LOCALE = "yyyy/MM/dd:z";
	public static final String STR_YMDHM0_SLASH = "yyyy/MM/dd HH:mm:00";
	public static final String STR_YMDHM59_SLASH = "yyyy/MM/dd HH:mm:59";
	public static final String STR_YMDHMS_SLASH = "yyyy/MM/dd HH:mm:ss";
	public static final String STR_YMDHM_SLASH = "yyyy/MM/dd HH:mm";
	public static final String STR_YMDHMS_SLASH_LOCALE = "yyyy/MM/dd HH:mm:ss:z";
	private static final SimpleDateFormat DAY = new SimpleDateFormat(STR_DAY);
	private static final SimpleDateFormat HMS = new SimpleDateFormat(STR_HMS);
	// 1Day（msec）
	private static final long ONE_DAY = 1000 * 60 * 60 * 24;
	// 1Minute（msec）
	private static final long ONE_MINUTE = 1000 * 60;
	// 1Second（msec）
	private static final long ONE_SECOND = 1000;
	private static final SimpleDateFormat TIME = new SimpleDateFormat(STR_TIME);
	private static final SimpleDateFormat YMD = new SimpleDateFormat(STR_YMD);
	private static final SimpleDateFormat YMD_LOCALE = new SimpleDateFormat(STR_LOCALE);
	private static final SimpleDateFormat YMD_SLASH = new SimpleDateFormat(STR_YMD_SLASH);
	private static final SimpleDateFormat YMD_SLASH_LOCALE = new SimpleDateFormat(
			STR_YMD_SLASH_LOCALE);
	private static final SimpleDateFormat YMDHMS_SLASH_LOCALE = new SimpleDateFormat(
			STR_YMDHMS_SLASH_LOCALE);
	private static final SimpleDateFormat YMDHM0_SLASH = new SimpleDateFormat(
			STR_YMDHM0_SLASH);
	private static final SimpleDateFormat YMDHM59_SLASH = new SimpleDateFormat(
			STR_YMDHM59_SLASH);
	private static final SimpleDateFormat YMDHMS_SLASH = new SimpleDateFormat(
			STR_YMDHMS_SLASH);
	private static final SimpleDateFormat YMDHM_SLASH = new SimpleDateFormat(
			STR_YMDHM_SLASH);
	private static final SimpleDateFormat[] formats = {YMD, YMD_SLASH, YMDHMS_SLASH};
}
