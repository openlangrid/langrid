package jp.go.nict.langrid.client.soap.test;

import java.net.URL;

import org.junit.Test;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.test.AuthFile;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

public class BilingualDictionaryWithLongestMatchSearchTest {
	@Test
	public void test() throws Throwable{
		Pair<String, String> idAndPass = AuthFile.load(getClass());
		System.out.println(JSON.encode(new SoapClientFactory().create(BilingualDictionaryWithLongestMatchSearchService.class,
				new URL("http://langrid.org/service_manager/invoker/KyotoTourismDictionaryDb"),
				idAndPass.getFirst(), idAndPass.getSecond())
				.searchLongestMatchingTerms("en", "ja", new Morpheme[]{new Morpheme("Kinkaku-ji", "Kinkaku-ji", "noun.other")}), true));
	}
}
