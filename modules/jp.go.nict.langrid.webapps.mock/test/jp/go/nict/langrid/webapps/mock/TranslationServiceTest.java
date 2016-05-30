package jp.go.nict.langrid.webapps.mock;

import org.junit.Test;

public class TranslationServiceTest extends AbstractClientTest<jp.go.nict.langrid.service_1_2.translation.TranslationService> {
	@Test
	public void test() throws Exception{
		client("Translation").translate("en", "ja", "hello");
	}
}
