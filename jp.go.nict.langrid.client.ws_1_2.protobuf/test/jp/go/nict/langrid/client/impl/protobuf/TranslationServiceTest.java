package jp.go.nict.langrid.client.impl.protobuf;


import java.net.URL;

import jp.go.nict.langrid.client.protobufrpc.PbClientFactory;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

import org.junit.Test;

public class TranslationServiceTest {
	@Test
	public void test() throws Exception{
		new PbClientFactory().create(
				TranslationService.class,
				new URL("http://127.0.0.1:8080/jp.go.nict.langrid.webapps.mock/pbServices/Translation")
				).translate("en", "ja", "Hello world.");
	}
}
