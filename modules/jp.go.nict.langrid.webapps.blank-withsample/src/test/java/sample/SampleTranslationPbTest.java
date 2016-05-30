package sample;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.client.protobufrpc.PbClientFactory;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

public class SampleTranslationPbTest {
	@Test
	public void test() throws Exception{
		TranslationService service = new PbClientFactory().create(
				TranslationService.class
				, new URL("http://127.0.0.1:8080/jp.go.nict.langrid.webapps.blank/pbServices/SampleTranslationService")
				);
		Assert.assertEquals(
				"en:ja:hello", service.translate("en", "ja", "hello")
				);
	}
}
