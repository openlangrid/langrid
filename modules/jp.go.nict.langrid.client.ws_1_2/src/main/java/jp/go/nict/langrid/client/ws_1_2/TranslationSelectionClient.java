package jp.go.nict.langrid.client.ws_1_2;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.translationselection.SelectionResult;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 369 $
 */
public interface TranslationSelectionClient extends ServiceClient {
	/**
	 * 
	 * 
	 */
	SelectionResult select(Language sourceLang, Language targetLang, String source)
	throws LangridException;
}
