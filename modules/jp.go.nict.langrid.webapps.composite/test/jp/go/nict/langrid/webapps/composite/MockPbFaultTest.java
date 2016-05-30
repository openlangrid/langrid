package jp.go.nict.langrid.webapps.composite;

import java.net.URL;

import jp.go.nict.langrid.client.protobufrpc.PbClientFactory;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

import org.junit.Test;

public class MockPbFaultTest {
	@Test
	public void test() throws Exception{
		TranslationService service = new PbClientFactory().create(TranslationService.class
				, new URL("http://localhost:8080/jp.go.nict.langrid.webapps.mock/pbServices/Translation?exception=InvalidParameterException")
				);
		service.translate("en", "ja", "hello");
	}
}
