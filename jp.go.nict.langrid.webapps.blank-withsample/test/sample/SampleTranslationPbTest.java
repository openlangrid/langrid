package sample;

import java.net.URL;

import jp.go.nict.langrid.client.impl.protobuf.PbClientFactory;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import junit.framework.Assert;

import org.junit.Test;

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
