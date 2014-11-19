package org.langrid.composite.test;

import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.axis.AxisClientFactory;
import jp.go.nict.langrid.client.ws_1_2.impl.axis.LangridAxisClientFactory;
import jp.go.nict.langrid.composite.commons.test.ComponentServiceFactoryImpl;
import jp.go.nict.langrid.composite.translation.TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

import org.junit.Test;

public class TranslationCombinedWithBilingualDictionaryLongestMatchSearchTest {
	@Test
	public void test() throws Exception{
		final String morph = "http://langrid.org/service_manager/invoker/Mecab";
		final String dict = "http://langrid.ai.soc.i.kyoto-u.ac.jp/tools/toolbox/modules/dictionary/services/invoker/billingualdictionary.php?serviceId=agent-reserch";
		final String trans = "http://langrid.org/service_manager/invoker/KyotoUJServer";
		System.out.println(new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				final ClientFactory factory = LangridAxisClientFactory.getInstance();
				try {
					return new ComponentServiceFactoryImpl(){{
						add("MorphologicalAnalysisPL", factory.create(
								MorphologicalAnalysisService.class,
								new URL(morph), "id", "pass"));
						add("TranslationPL", factory.create(
								TranslationService.class,
								new URL(trans), "id", "pass"));
						add("BilingualDictionaryWithLongestMatchSearchPL", factory.create(
								BilingualDictionaryWithLongestMatchSearchService.class,
								new URL(dict)));
					}};
				} catch (MalformedURLException e) {
					throw new RuntimeException(e);
				}
			}
		}.translate("ja", "en", "虫見版を送ります．", new Translation[]{}, "en"));
	}
}
