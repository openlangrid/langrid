package jp.go.nict.langrid.wrapper.ws_1_2.util;

import static jp.go.nict.langrid.language.ISO639_1LanguageTags.ja;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.zh;

import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.language.Language;

public class WordConcatenater {
	public static void concat(Language language, StringBuilder precedings, String word){
		if(!NOSPACELANGS.contains(language)){
			precedings.append(" ");
		}
		precedings.append(word);
	}

	private static Set<Language> NOSPACELANGS = new HashSet<Language>();
	static{
		NOSPACELANGS.add(ja);
		NOSPACELANGS.add(zh);
	}
}
