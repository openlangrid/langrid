package jp.go.nict.langrid.client.ws_1_2.test.soapclient;

import java.net.URL;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationWithTemporalDictionaryService;

import org.junit.Test;

public class MultihopTranslationWithTemporalDictionaryServiceTest {
	@Test
	public void test() throws Throwable{
/*
		SoapClientFactory.setInputStreamFilter(new InputStreamFilter() {
			@Override
			public InputStream filter(InputStream is) {
				return new DuplicatingInputStream(is, System.out);
			}
		});//*/
		MultihopTranslationWithTemporalDictionaryService s = new SoapClientFactory().create(
				MultihopTranslationWithTemporalDictionaryService.class,
				new URL("http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/MultihopTranslationWithTemporalDictionary")
				);
		System.out.println(s.multihopTranslate("ja", new String[]{"en"}, "fr", "こんにちは世界",
				new Translation[]{new Translation("世界", new String[]{"globe"})}, "fr",
				new String[][][]{
					new String[][]{new String[]{"globbe"}}
		}, new String[]{"en"}));
	}
}
