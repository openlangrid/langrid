/*
 * $Id: LangridLanguageTags.java 1468 2015-01-24 07:24:17Z t-nakaguchi $
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
package jp.go.nict.langrid.language;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * 
 * <br>date generated: Fri Jun 23 19:00:00 JST 2006
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1468 $
 */
public final class LangridLanguageTags {

	/**
	 * 
	 * 
	 */
//	private static LanguageTag xtag = new LanguageTag(){
//		public String getCode(){
//			return "x";
//		}
//	};

	/**
	 * 
	 * 
	 */
	public static final Language en_US = Language.get(ISO639_1.EN, ISO3166.US);

	/**
	 * 
	 * 
	 */
	public static final Language en_GB = Language.get(ISO639_1.EN, ISO3166.GB);

	/**
	 * 
	 * 
	 */
//	public static final Language ko_KR = Language.get(ISO639_1.KO, ISO3166.KR);

	/**
	 * 
	 * 
	 */
//	public static final Language ko_KP = Language.get(ISO639_1.KO, ISO3166.KP);

	/**
	 * 
	 * 
	 */
//	public static final Language x_picto_Pangaea = Language.get(xtag, "picto", "Pangaea");

	/**
	 * 
	 * 
	 */
	public static final Language zh_CN = Language.get(ISO639_1.ZH, ISO3166.CN);

	/**
	 * 
	 * 
	 */
	public static final Language zh_TW = Language.get(ISO639_1.ZH, ISO3166.TW);

	public static final Language zh_HK = Language.get(ISO639_1.ZH, ISO3166.HK);

	/**
	 * 
	 * 
	 */
	public static final Language pt_PT = Language.get(ISO639_1.PT, ISO3166.PT);

	/**
	 * 
	 * 
	 */
	public static final Language pt_BR = Language.get(ISO639_1.PT, ISO3166.BR);

	
	/**
	 * 
	 * 
	 */
	public static final Language any;

	private static Set<Language> tags;

	/**
	 * 
	 * 
	 */
	public static synchronized Set<Language> values(){
		if(tags == null){
			tags = new TreeSet<Language>(new Comparator<Language>() {
				@Override
				public int compare(Language o1, Language o2) {
					return o1.getCode().compareTo(o2.getCode());
				}
			});
			for(Field f : LangridLanguageTags.class.getDeclaredFields()){
				if(!f.getType().equals(Language.class)) continue;
				try {
					tags.add((Language)f.get(null));
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			tags = Collections.unmodifiableSet(tags);
		}
		return tags;
	}

	static{
		try{
			any = new Language("*");
		} catch(InvalidLanguageTagException e){
			throw new RuntimeException(e);
		}
	}
}
