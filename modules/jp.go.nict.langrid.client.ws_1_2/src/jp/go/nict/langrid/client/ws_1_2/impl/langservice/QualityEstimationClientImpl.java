package jp.go.nict.langrid.client.ws_1_2.impl.langservice;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;

import jp.go.nict.langrid.client.ws_1_2.QualityEstimationClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.language.Language;
import localhost.jp_go_nict_langrid_webapps_langrid_p2p.services.QualityEstimation.QualityEstimationServiceServiceLocator;

public class QualityEstimationClientImpl extends ServiceClientImpl
implements QualityEstimationClient {
	/**
	 * 
	 * 
	 */
	public QualityEstimationClientImpl(URL serverUrl) {
		super(serverUrl);
	}

	@Override
	public double estimate(Language sourceLang, Language targetLang, String source,
			String target) throws LangridException {
		return ((Double)invoke(sourceLang, targetLang, source, target));
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		QualityEstimationServiceServiceLocator locator = new QualityEstimationServiceServiceLocator();
		setUpService(locator);
		return (Stub)locator.getQualityEstimation(url);
	}

	private static final long serialVersionUID = -2871601386295777130L;
}
