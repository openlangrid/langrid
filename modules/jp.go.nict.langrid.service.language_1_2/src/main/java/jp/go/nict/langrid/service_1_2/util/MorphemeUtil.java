package jp.go.nict.langrid.service_1_2.util;

import java.lang.Character.UnicodeBlock;
import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

public class MorphemeUtil {
	public static String joinWords(Morpheme[] morphs) {
		StringBuilder ret = new StringBuilder();
		for(Morpheme m : morphs) {
			String w = m.getWord();
			if(ret.length() != 0) {
				if(w == null || w.isEmpty()) continue;
				char last = ret.charAt(ret.length() -1);
				UnicodeBlock lastBlk = Character.UnicodeBlock.of(last);
				UnicodeBlock nextBlk = Character.UnicodeBlock.of(w.charAt(0));
				if(!cjks.contains(lastBlk) && !cjks.contains(nextBlk) &&
						!puncts.contains(w)) {
					ret.append(" ");
				}
			}
			ret.append(w);
		}
		return ret.toString();
	}

	public static String joinWords(String[] words) {
		StringBuilder ret = new StringBuilder();
		for(String w : words) {
			if(ret.length() != 0) {
				if(w == null || w.isEmpty()) continue;
				char last = ret.charAt(ret.length() -1);
				UnicodeBlock lastBlk = Character.UnicodeBlock.of(last);
				UnicodeBlock nextBlk = Character.UnicodeBlock.of(w.charAt(0));
				if(!cjks.contains(lastBlk) && !cjks.contains(nextBlk) &&
						!puncts.contains(w)) {
					ret.append(" ");
				}
			}
			ret.append(w);
		}
		return ret.toString();
	}

	private static Set<UnicodeBlock> cjks = new HashSet<>();
	private static Set<String> puncts = new HashSet<>();
	static {
		puncts.add(".");
		puncts.add(",");
		puncts.add("?");
		puncts.add("!");
		cjks.add(UnicodeBlock.CJK_COMPATIBILITY);
		cjks.add(UnicodeBlock.CJK_COMPATIBILITY_FORMS);
		cjks.add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
		cjks.add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
		cjks.add(UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
		cjks.add(UnicodeBlock.CJK_STROKES);
		cjks.add(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
		cjks.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
		cjks.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
		cjks.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
		cjks.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C);
		cjks.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D);
		cjks.add(UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS);
		cjks.add(UnicodeBlock.KATAKANA);
		cjks.add(UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS);
		cjks.add(UnicodeBlock.HIRAGANA);
		cjks.add(UnicodeBlock.KANA_SUPPLEMENT);
	}
}
