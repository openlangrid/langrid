/*
 * $Id: LanguagePathDirection.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public enum LanguagePathDirection{
	BOTH("<->")
	, SIMPLEX("->")
	, COMBINATION("Combination")
	;
	
	private LanguagePathDirection(String directionCode) {
	   code = directionCode;
   }

	public static LanguagePathDirection getByValue(String value){
		for(LanguagePathDirection e : LanguagePathDirection.values()){
			if(e.getValue().equals(value)){
				return e;
			}
		}
		return null;
	}
	
	public static List<String> getCodeList() {
	   List<String> list = new ArrayList<String>();
	   for(LanguagePathDirection lpd : values()){
	      list.add(lpd.code);
	   }
	   return list;
	}
	
	public String getCode() {
	   return code;
	}

	/**
	 * 
	 * 
	 */
	public String getValue(){
		return String.valueOf(ordinal());
	}
	
	public static List<LanguagePathDirection> getListWithoutCombination(){
	   List<LanguagePathDirection> list = new ArrayList<LanguagePathDirection>();
	   for(LanguagePathDirection lpd : values()){
	      if( ! lpd.equals(LanguagePathDirection.COMBINATION)){
	         list.add(lpd);
	      }
	   }
	   return list;
	}
	
	private String code;
}
