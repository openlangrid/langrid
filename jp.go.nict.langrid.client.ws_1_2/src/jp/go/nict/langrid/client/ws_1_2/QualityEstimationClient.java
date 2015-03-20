package jp.go.nict.langrid.client.ws_1_2;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.language.Language;


/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 369 $
 */
public interface QualityEstimationClient extends ServiceClient {
	/**
	 * 
	 * 
	 */
	public double estimate(Language sourceLang, Language targetLang, String source, String target) throws LangridException;
}
