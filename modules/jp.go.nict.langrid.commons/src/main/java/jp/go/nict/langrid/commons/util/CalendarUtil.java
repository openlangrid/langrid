/*
 * $Id: CalendarUtil.java 1108 2014-01-15 05:12:36Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1108 $
 */
public class CalendarUtil {
	/**
	 * 
	 * 
	 */
	public static final Calendar MAX_VALUE_IN_EPOC;

	/**
	 * 
	 * 
	 */
	public static final Calendar MIN_VALUE_IN_EPOC;

	/**
	 * 
	 * 
	 */
	public static Calendar createFromMillis(long milliseconds){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds);
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar create(int year, int month, int date){
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, date, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar create(
			int year, int month, int date, TimeZone timeZone)
	{
		Calendar c = Calendar.getInstance(timeZone);
		c.set(year, month - 1, date, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar create(int year, int month, int date
			, int hourOfDay, int minute, int second){
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, date, hourOfDay, minute, second);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar create(int year, int month, int date
			, int hourOfDay, int minute, int second, int milliseconds){
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, date, hourOfDay, minute, second);
		c.set(Calendar.MILLISECOND, milliseconds);
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar createBeginningOfYear(Calendar calendar){
		Calendar c = (Calendar)calendar.clone();
		c.set(c.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar createBeginningOfMonth(Calendar calendar){
		Calendar c = (Calendar)calendar.clone();
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar createBeginningOfDay(Calendar calendar){
		Calendar c = (Calendar)calendar.clone();
		c.set(
				c.get(Calendar.YEAR), c.get(Calendar.MONTH)
				, c.get(Calendar.DATE), 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar createEndingOfDay(Calendar calendar){
		Calendar c = (Calendar)calendar.clone();
		c.set(
				c.get(Calendar.YEAR), c.get(Calendar.MONTH)
				, c.get(Calendar.DATE), 23, 59, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar cloneAndAdd(Calendar source, int field, int amount){
		Calendar r = (Calendar)source.clone();
		r.add(field, amount);
		return r;
	}

	/**
	 * 
	 * 
	 */
	public static String formatToHTTPDate(Calendar calendar){
		return String.format(Locale.ENGLISH, FORMAT_HTTPDATE, calendar);
	}

	
	public static String formatToW3CDTF(Calendar calendar){
		String tz = "Z";
		int value = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / 60000;
		if (value != 0) {
			char sign = '+';
			if (value < 0) {
				value = -value;
				sign = '-';
			}
			tz = String.format("%c%02d:%02d", sign, value / 60, value % 60);
		}
		DateFormat df = w3ctzFormat.get();
		df.setTimeZone(calendar.getTimeZone());
		return df.format(calendar.getTime()) + tz;
	}

	/**
	 * 
	 * 
	 */
	public static String formatToDefault(Calendar calendar){
		return DateFormat.getDateTimeInstance().format(calendar.getTime());
	}

	/**
	 * 
	 * 
	 */
	public static String formatNowToHTTPDate(){
		return formatToHTTPDate(
				Calendar.getInstance(TimeZone.getTimeZone("GMT"))
				);
	}

	public static String formatNowToW3CTDF(){
		return formatToW3CDTF(Calendar.getInstance());
	}

	/**
	 * 
	 * 
	 */
	public static String encodeToSimpleDate(Calendar value){
		return codeFormat.get().format(value.getTime());
	}

	/**
	 * 
	 * 
	 */
	public static Calendar decodeFromSimpleDate(String line)
		throws ParseException
	{
		Calendar c = Calendar.getInstance();
		c.setTime(codeFormat.get().parse(line));
		return c;
	}

	public static Calendar decodeFromW3CDTF(String line)
	throws ParseException{
		Calendar c = Calendar.getInstance();
		try{
			c.setTime(w3ctzFormat.get().parse(line));
			return c;
		} catch(ParseException e){
		}
		try{
			c.setTime(w3ctzFormatYMD.get().parse(line));
			return c;
		} catch(ParseException e){
		}
		try{
			c.setTime(w3ctzFormatYM.get().parse(line));
			return c;
		} catch(ParseException e){
		}
		c.setTime(w3ctzFormatY.get().parse(line));
		return c;
	}

	/**
	 * 
	 * 
	 */
	public static Calendar toDefaultTimeZone(Calendar calendar){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(calendar.getTimeInMillis());
		return c;
	}

	private static final String FORMAT_HTTPDATE = "%ta, %1$td %1$tb %1$tY %1$tT %1$tZ";
	private static final String FORMAT_W3CDTF_Y = "yyyy";
	private static final String FORMAT_W3CDTF_YM = "yyyy-MM";
	private static final String FORMAT_W3CDTF_YMD = "yyyy-MM-dd";
	private static final String FORMAT_W3CDTF_WO_TZ = "yyyy-MM-dd'T'HH:mm:ss.S";
	private static final String FORMAT_SIMPLEDATE = "yyyy-MM-dd HH:mm:ss.SSS z";
	private static ThreadLocal<DateFormat> codeFormat = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue(){
			return new SimpleDateFormat(FORMAT_SIMPLEDATE);
		}
	};
	private static ThreadLocal<DateFormat> w3ctzFormatY = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue(){
			return new SimpleDateFormat(FORMAT_W3CDTF_Y);
		}
	};
	private static ThreadLocal<DateFormat> w3ctzFormatYM = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue(){
			return new SimpleDateFormat(FORMAT_W3CDTF_YM);
		}
	};
	private static ThreadLocal<DateFormat> w3ctzFormatYMD = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue(){
			return new SimpleDateFormat(FORMAT_W3CDTF_YMD);
		}
	};
	private static ThreadLocal<DateFormat> w3ctzFormat = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue(){
			return new SimpleDateFormat(FORMAT_W3CDTF_WO_TZ);
		}
	};

	static{
		MAX_VALUE_IN_EPOC = Calendar.getInstance();
		MAX_VALUE_IN_EPOC.set(9999, 11, 31, 23, 59, 59);
		MAX_VALUE_IN_EPOC.set(Calendar.MILLISECOND, 999);
		MIN_VALUE_IN_EPOC = Calendar.getInstance();
		MIN_VALUE_IN_EPOC.setTimeInMillis(0);
	}
}
