/*
 * $Id: DisplayLocale.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.model.enumeration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public enum DisplayLocale{
	JAPANESE(
			"Japanese",
			"0",
			"ja"),
	ENGLISH(
			"English",
			"1",
			"en")
	;

	/**
	 * 
	 * 
	 */
	public static String toDisplayMessage(String value){
		for(DisplayLocale e : DisplayLocale.values()){
			if(e.value.equals(value)){
				return e.displayMessage;
			}
		}
		return null;
	}

	/**
	 * 
	 * 
	 */
	public static Map<String, String> toMap(){
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(DisplayLocale e : DisplayLocale.values()){
			if(e.isShow){
				map.put(e.displayMessage, e.value);
			}
		}
		return map;
	}

	/**
	 * 
	 * 
	 */
	public String getDisplayMessage(){
		return displayMessage;
	}

	/**
	 * 
	 * 
	 */
	public String getOrder(){
		return order;
	}

	/**
	 * 値を取得する
	 * @return 値
	 */
	public String getValue(){
		return value;
	}

	@Override
	public String toString(){
		return displayMessage;
	}

	/**
	 * 
	 * 
	 */
	public boolean isShow(){
		return isShow;
	}

	/**
	 * 
	 * 
	 */
	private DisplayLocale(String displayMessage, String value, String order){
		this(displayMessage, value, order, true);
	}

	/**
	 * 
	 * 
	 */
	private DisplayLocale(
			String displayMessage, String value, String order, boolean isShow)
	{
		this.displayMessage = displayMessage;
		this.value = value;
		this.order = order;
		this.isShow = isShow;
	}

	private boolean isShow;
	private String displayMessage;
	private String order;
	private String value;
}
