package jp.go.nict.langrid.wrapper.workflowsupport;

import static jp.go.nict.langrid.language.ISO639_1LanguageTags.de;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.en;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.es;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.fr;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.it;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.ja;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.ko;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.pt;
import static jp.go.nict.langrid.language.ISO639_1LanguageTags.zh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.scanner.Scanner;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.scanner.ScannerFactory;
import jp.go.nict.langrid.wrapper.ws_1_2.workflowsupport.AbstractGetLongestMatchingTermService;

/**
 * @author koyama
 * @version $Revision: 75 $
 */
public class GetLongestMatchingTerm
extends AbstractGetLongestMatchingTermService
{
	/**
	 * コンストラクタ。
	 */
	public GetLongestMatchingTerm() {
		setSupportedLanguageCollection(
				Arrays.asList(ja, en, zh, ko, es, pt, fr, de, it));
	}

	public Collection<TranslationWithPosition> doGetLongestMatchingTerm(
			Language language, Morpheme[] morphemes, int startIndex, Translation[] translations)
			throws InvalidParameterException, ProcessFailedException {
		Collection<TranslationWithPosition> result = new ArrayList<TranslationWithPosition>();
		if (translations == null || translations.length == 0) return result;
		Scanner scanner = ScannerFactory.getInstance(language, Scanner.TYPE_LONGEST_MATCH);
		scanner.doScan(language, startIndex, morphemes, translations, result);
		return result;
	}

	/**
	 * 最終更新日付を返す
	 * @return 更新日付
	 * @throws ProcessFailedException
	 */
	protected Calendar doGetLastUpdate()
	throws ProcessFailedException{
		return parseDateMacro("$Date: 2010-04-22 14:27:54 +0900 (Thu, 22 Apr 2010) $");
	}
}
