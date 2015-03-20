package jp.go.nict.langrid.client.ws_1_2.impl.langservice;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.TranslationSelectionClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.translationselection.SelectionResult;
import localhost.jp_go_nict_langrid_webapps_atomic.services.TranslationSelection.TranslationSelectionServiceServiceLocator;

import org.apache.axis.client.Stub;
/**
 * 
 * 
 * @author  $$
 * @version  $$
 */
public class TranslationSelectionClientImpl extends ServiceClientImpl
implements TranslationSelectionClient {
	/**
	 * 
	 * 
	 */
	public TranslationSelectionClientImpl(URL serverUrl) {
		super(serverUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		TranslationSelectionServiceServiceLocator locator = new TranslationSelectionServiceServiceLocator();
		setUpService(locator);
		return (Stub)locator.getTranslationSelection(url);
	}

	@Override
	public SelectionResult select(Language sourceLang, Language targetLang,
			String source) throws LangridException {
		return (SelectionResult)invoke(sourceLang, targetLang, source);
	}

	private static final long serialVersionUID = 8842806191820766647L;
}
