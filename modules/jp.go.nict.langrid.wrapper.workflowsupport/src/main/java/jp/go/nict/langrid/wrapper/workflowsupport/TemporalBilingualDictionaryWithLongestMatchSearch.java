package jp.go.nict.langrid.wrapper.workflowsupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.scanner.Scanner;
import jp.go.nict.langrid.wrapper.ws_1_2.bilingualdictionary.scanner.ScannerFactory;
import jp.go.nict.langrid.wrapper.ws_1_2.workflowsupport.AbstractTemporalBilingualDictionaryWithLongestMatchSearchService;

public class TemporalBilingualDictionaryWithLongestMatchSearch
extends AbstractTemporalBilingualDictionaryWithLongestMatchSearchService
{
	public TemporalBilingualDictionaryWithLongestMatchSearch(){
	}

	public TemporalBilingualDictionaryWithLongestMatchSearch(ServiceContext context){
		super(context);
	}

	public Collection<TranslationWithPosition> doSearchAllLongestMatchingTerms(
		Language language, Morpheme[] morphemes, Translation[] translations)
	throws InvalidParameterException, ProcessFailedException {
		Collection<TranslationWithPosition> result = new ArrayList<TranslationWithPosition>();
		if (translations == null || translations.length == 0) {
			return result;
		}
		Scanner scanner = ScannerFactory.getInstance(language, Scanner.TYPE_LONGEST_MATCH);
		for (int i = 0; i < morphemes.length; i++) {
			int ret = scanner.doScan(language, i, morphemes, translations, result);
			if (ret != -1) {
				i = ret;
			}
		}
		return result;
	}

	/**
	 * 最終更新日付を返す
	 * @return 更新日付
	 * @throws ProcessFailedException
	 */
	protected Calendar doGetLastUpdate()
	throws ProcessFailedException{
		return parseDateMacro("$Date: 2011-08-25 12:12:59 +0900 (Thu, 25 Aug 2011) $");
	}

	private static Logger logger = Logger.getLogger(
			TemporalBilingualDictionaryWithLongestMatchSearch.class.getName());
}
