package jp.go.nict.langrid.client.ws_1_2.test.axisclient;

import java.net.URL;

import jp.go.nict.langrid.client.axis.interceptor.AxisRequestInterceptor;
import jp.go.nict.langrid.client.ws_1_2.impl.axis.LangridAxisClientFactory;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationWithTemporalDictionaryService;

import org.junit.Test;

public class MultihopTranslationWithTemporalDictionaryServiceTest {
	@Test
	public void test() throws Throwable{
		MultihopTranslationWithTemporalDictionaryService s = LangridAxisClientFactory.getInstance().create(
				MultihopTranslationWithTemporalDictionaryService.class,
				new URL("http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/MultihopTranslationWithTemporalDictionary")
				);
		AxisRequestInterceptor.setOutputStreamForCurrentThread(System.out);
		System.out.println(s.multihopTranslate("ja", new String[]{"en"}, "fr", "こんにちは世界",
				new Translation[]{new Translation("世界", new String[]{"globe"})}, "fr",
				new String[][][]{
					new String[][]{new String[]{"globbe"}}
		}, new String[]{"en"}));
	}
}
