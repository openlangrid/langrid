package jp.go.nict.langrid.client.ws_1_2.test;

import java.net.URL;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.client.ws_1_2.impl.axis.LangridAxisClientFactory;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

import org.junit.Test;

public class TranslationWithException {
	@Test
	public void sc_axis14() throws Throwable{
		new SoapClientFactory().create(
				TranslationService.class,
				new URL("http://langrid.org/service_manager/invoker/KyotoUJServer")
				, "id", "pass"
				).translate("en", "ja", "hello");
		
		LangridAxisClientFactory.getInstance().create(TranslationService.class,
				new URL("http://127.0.0.1:8080/jp.go.nict.langrid.webapps.mock.axis/services/TranslationWithInvalidParameterException"))
				.translate("en", "ja", "hello");
	}

	@Test
	public void sc_sgAxis14() throws Throwable{
		LangridAxisClientFactory.getInstance().create(TranslationService.class,
				new URL("http://127.0.0.1:8080/jp.go.nict.langrid.webapps.mock/services/TranslationWithInvalidParameterException"))
				.translate("en", "ja", "hello");
	}
}
