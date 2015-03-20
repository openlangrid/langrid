package jp.go.nict.langrid.wrapper.common.util;

import static jp.go.nict.langrid.language.IANALanguageTags.zh_Hans;
import static jp.go.nict.langrid.language.IANALanguageTags.zh_Hant;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.pt;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.zh;
import static jp.go.nict.langrid.language.LangridLanguageTags.pt_PT;
import static jp.go.nict.langrid.language.LangridLanguageTags.zh_CN;
import static jp.go.nict.langrid.language.LangridLanguageTags.zh_TW;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.language.Language;

public class LanguageCodeUtil {

	/**
	 * 
	 * 
	 */
	public static Language replaceLanguage(Language lang) {
		Language l = replaceLanguageMapForZh.get(lang);
		if(l != null){
			return l;
		} else{
			return lang;
		}
	}
	
	/**
	 * 
	 * 
	 */
	public static Language replaceLanguage2(Language lang) {
		Language l = replaceLanguageMapForZhHans.get(lang);
		if(l != null){
			return l;
		} else{
			return lang;
		}
	}
	
	/**
	 * 
	 * 
	 */
	public static Language substituteLanguage(Language lang) {
		Language l = languageMap.get(lang);
		if(l != null){
			return l;
		} else{
			return lang;
		}
	}
	
	private static Map<Language, Language> languageMap = new HashMap<Language, Language>();
	private static Map<Language, Language> replaceLanguageMapForZh = new HashMap<Language, Language>();
	private static Map<Language, Language> replaceLanguageMapForZhHans = new HashMap<Language, Language>();
	
	static {
		languageMap.put(zh, zh_CN);
		languageMap.put(zh_Hans, zh_CN);
		languageMap.put(zh_Hant, zh_TW);
		languageMap.put(pt, pt_PT);
		
		replaceLanguageMapForZh.put(zh_CN, zh);
		replaceLanguageMapForZh.put(zh_TW, zh_Hant);
		replaceLanguageMapForZh.put(pt_PT, pt);
		
		replaceLanguageMapForZhHans.put(zh_CN, zh_Hans);
		replaceLanguageMapForZhHans.put(zh_TW, zh_Hant);
		replaceLanguageMapForZhHans.put(pt_PT, pt);
	}
}
