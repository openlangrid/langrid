package jp.go.nict.langrid.webapps.composite;

import java.net.URL;

import org.junit.Test;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;

public class TranslationCombinedWithBilingualDictionaryWithLongestMatchSearchTest {
	@Test
	public void test() throws Exception{
		String compositeUrlBase = "http://localhost:8080/jp.go.nict.langrid.webapps.composite/services/";
		String atomicUrlBase = "http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/";
		TranslationWithTemporalDictionaryService service = new SoapClientFactory().create(TranslationWithTemporalDictionaryService.class
				, new URL(compositeUrlBase + "TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch")
				);
		RequestAttributes req = (RequestAttributes)service;
		req.getTreeBindings().add(new BindingNode(
				"MorphologicalAnalysisPL", "language", "eq", "ja", atomicUrlBase + "MorphologicalAnalysis"));
		req.getTreeBindings().add(new BindingNode(
				"MorphologicalAnalysisPL", "language", "eq", "en", atomicUrlBase + "MorphologicalAnalysis"));
		req.getTreeBindings().add(new BindingNode(
				"TranslationPL", atomicUrlBase + "Translation"));
		System.out.println(service.translate("en", "ja", "Hello", new Translation[]{}, "ja"));
	}
}
