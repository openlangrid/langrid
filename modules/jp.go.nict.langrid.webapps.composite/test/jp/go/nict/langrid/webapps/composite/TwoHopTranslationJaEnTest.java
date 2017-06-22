package jp.go.nict.langrid.webapps.composite;

import java.net.URL;

import org.junit.Test;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

public class TwoHopTranslationJaEnTest {
	@Test
	public void test() throws Exception{
		String compositeUrlBase = "http://localhost:8080/jp.go.nict.langrid.webapps.composite/services/";
		String atomicUrlBase = "http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/";
		TranslationService service = new SoapClientFactory().create(TranslationService.class
				, new URL(compositeUrlBase + "TwoHopTranslationEn")
				);
		RequestAttributes req = (RequestAttributes)service;
		req.getTreeBindings().add(new BindingNode(
				"FirstTranslationPL", "sourceLang", "eq", "en", atomicUrlBase + "Translation?result=en"));
		req.getTreeBindings().add(new BindingNode(
				"FirstTranslationPL", "sourceLang", "eq", "ja", atomicUrlBase + "Translation?result=ja"));
		req.getTreeBindings().add(new BindingNode(
				"SecondTranslationPL", atomicUrlBase + "Translation"));
		System.out.println(service.translate("ja", "ja", "Hello"));
	}
}
