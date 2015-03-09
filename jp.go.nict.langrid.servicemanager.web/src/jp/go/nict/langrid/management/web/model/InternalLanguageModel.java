/*
 * $Id: InternalLanguageModel.java 1469 2015-01-24 07:59:20Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.model;

import static jp.go.nict.langrid.language.IANALanguageTags.zh_Hans;
import static jp.go.nict.langrid.language.IANALanguageTags.zh_Hant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import jp.go.nict.langrid.language.ISO639_1LanguageTags;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.LangridLanguageTags;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1469 $
 */
public class InternalLanguageModel{
   public static List<Language> getLanguageList() {
      ArrayList<Language> list = new ArrayList<Language>();
      for(Language l :ALL_LANGUAGE_NAME_MAP.values()){
         list.add(l);
      }
      Collections.sort(list, new Comparator<Language>() {
         @Override
         public int compare(Language o1, Language o2) {
            return o1.getLocalizedName(DEFAULTLOCALE).compareTo(o2.getLocalizedName(DEFAULTLOCALE));
         }
      });
      return list;
   }
   
   public static Language getByName(String name, Locale locale) throws InvalidLanguageTagException {
      if(containName(name, locale)) {
         return new Language(getCodeByName(name, locale));
      }
      return null;
   }
   
	/**
	 * 
	 * 
	 */
	public static boolean containCode(String code){
		return ALL_LANGUAGE_NAME_MAP.containsKey(code);
	}
	
	/**
	 * 
	 * 
	 */
	public static boolean containName(String name, Locale locale){
		for(Language l : ALL_LANGUAGE_NAME_MAP.values()){
			if(name.equals(l.getLocalizedName(locale))){
				return true;
			}
		}
//		for(String zhName : zhNameMap.values()){
//			if(name.equals(zhName)){
//				return true;
//			}
//		}
		return false;
	}

	/**
	 * 
	 * 
	 */
	public static String getCodeByName(String name, Locale locale){
		if(name == null){
			return ALL_LANGUAGE_NAME_MAP.entrySet().iterator().next().getValue().getLocalizedName(locale);
		}
		if(name.equals(getWildcardName())){
			return getWildcardName();
		}
//		if(name.equals(zhNameMap.get(zh_CN))){
//			return zh_CN.getCode();
//		}
//		if(name.equals(zhNameMap.get(zh_TW))){
//			return zh_TW.getCode();
//		}
		for(Language l : ALL_LANGUAGE_NAME_MAP.values()){
			String lName = l.getLocalizedName(locale);
			if(name.equals(lName)){
				return l.getCode();
			}
		}
		return "";
	}

	/**
	 * 
	 * 
	 */
	public static List<String> getCodeList(){
		Set<String> list = new TreeSet<String>();
		for(String code : ALL_LANGUAGE_NAME_MAP.keySet()){
			if(isInvalidCode(code)){
				continue;
			}
			list.add(code);
		}
		return new ArrayList<String>(list);
	}

	/**
	 * 
	 * 
	 */
	public static String getDisplayString(String code, Locale locale) throws InvalidLanguageTagException{
		String name = "";
		if(code.equals("zh")){
			name = Language.parse("zh").getLocalizedName(locale) + ": [" + code + "]";
		}
		name = getNameByCode(code, locale).split(";")[0] + ": [" + code + "]";
		return name.replaceAll("\\(", " \\(");
	}

	/**
	 * 
	 * 
	 */
	public static String getNameByCode(String code, Locale locale){
		if(isWildcardCode(code)){
			return getWildcardName();
		}
//		if(code.equals(zh_CN.getCode())){
//			return zhNameMap.get(zh_CN);
//		}
//		if(code.equals(zh_TW.getCode())){
//			return zhNameMap.get(zh_TW);
//		}
		if(code.equals("zh")){
			return "Chinese";
		}
		for(String key : ALL_LANGUAGE_NAME_MAP.keySet()){
			if(code.equals(key)){
				return ALL_LANGUAGE_NAME_MAP.get(key).getLocalizedName(locale);
			}
		}
		return "";
	}

	/**
	 * 
	 * 
	 */
	public static List<String> getNameList(Locale locale){
		Set<String> list = new TreeSet<String>();
		for(Language l : ALL_LANGUAGE_NAME_MAP.values()){
			if(isInvalidCode(l.getLocalizedName(locale))){
				continue;
			}
			String name = l.getLocalizedName(locale);
//			if(zhNameMap.get(l) != null){
//				name = zhNameMap.get(l);
//			}
			// TODO
//		   name = name.replaceAll("\\S\\(", " \\(");
			
			if(list.contains(name)){
				name = name + ":[" + l.getCode() + "]";
			}
			list.add(name);
		}
		return new ArrayList<String>(list);
	}

	/**
	 * 
	 * 
	 */
	public static List<String> getNameListWithCode(Locale locale){
		Set<String> list = new TreeSet<String>();
		for(Language l : ALL_LANGUAGE_NAME_MAP.values()){
			if(isInvalidCode(l.getLocalizedName(locale))){
				continue;
			}
			String name = l.getLocalizedName(locale);
//			if(zhNameMap.get(l) != null){
//				name = zhNameMap.get(l);
//			}
			for(String dirtyName : list){
				if(dirtyName.split("=")[0].equals(name)){
					name = name + ":[" + l.getCode() + "]";
					break;
				}
			}
			list.add(name + "=" + l.getCode());
		}
		return new ArrayList<String>(list);
	}

	/**
	 * 
	 * 
	 */
	public static Language getWildcard(){
		for(Language lang : LangridLanguageTags.values()){
			if(hasLocalizedName(lang)){
				String name = lang.getLocalizedName(DEFAULTLOCALE);
				if(isWildcardCode(name)){
					return lang;
				}
			}
		}
		return null;
	}

	private static String concatWildcardString(String wildcardCode){
//		wildcardName = wildcardCode.concat(" (Any language)");
	   wildcardName = "Any Language";
	   return wildcardName;
	}

	/**
	 * 
	 * 
	 */
	private static String getWildcardName(){
		if(wildcardName == null){
			concatWildcardString(wildcard);
		}
		return wildcardName;
	}

	/**
	 * 
	 * 
	 */
	private static boolean hasLocalizedName(Language lang){
		return !lang.getLocalizedName(DEFAULTLOCALE).equals("");
	}

	private static boolean isInvalidCode(String code){
		return code.equals("");
	}

	private static boolean isWildcardCode(String code){
		return code.equals(wildcard);
	}

	/**
	 * 
	 * 
	 */
	private static Map<String, Language> makeNameMap(){
		Map<String, Language> codeMap = new TreeMap<String, Language>();
		if(DEFAULTLOCALE == null){
			DEFAULTLOCALE = new Locale("en");
		}
		try{
			codeMap.put(new Language("*").getCode(), new Language("*"));
			setLanguageFromISO639_1To(codeMap);
		}catch(ServiceManagerException e){
			e.printStackTrace();
		} catch(InvalidLanguageTagException e) {
			e.printStackTrace();
		}
		setOtherLanguages(codeMap);
		return codeMap;
	}
	
	private static void setOtherLanguages(Map<String, Language> codeMap){
		for(Language l : LangridLanguageTags.values()){
			codeMap.put(l.getCode(), l);
		}
		codeMap.put(zh_Hans.getCode(), zh_Hans);
		codeMap.put(zh_Hant.getCode(), zh_Hant);
	}
	
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	private static void setLanguageFromISO639_1To(Map<String, Language> codeMap)
	throws ServiceManagerException{
		try{
			for(Field f : ISO639_1LanguageTags.class.getDeclaredFields()){
				if(f.getAnnotation(Deprecated.class) != null) continue;
					Language l = (Language)f.get(null);
					if(!hasLocalizedName(l)){
						continue;
					}
//					if(l.equals(Language.parse("zh"))){
//						continue;
//					}
					codeMap.put(l.getCode(), l);
			}
		}catch(IllegalArgumentException e){
			throw new ServiceManagerException(e);
		}catch(IllegalAccessException e){
			throw new ServiceManagerException(e);
		}
	}

	private static final Map<String, Language> ALL_LANGUAGE_NAME_MAP;
//	private static Map<Language, String> zhNameMap = new HashMap<Language, String>();
	private static Locale DEFAULTLOCALE = new Locale("en");
	private static String wildcard = "*";
	private static String wildcardName;
	static{
		ALL_LANGUAGE_NAME_MAP = makeNameMap();
//		zhNameMap.put(zh_TW, "Chinese(Traditional)");
//		zhNameMap.put(zh_CN, "Chinese(Simplified)");
	}
}
