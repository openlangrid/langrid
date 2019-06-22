package jp.go.nict.langrid.composite.translation;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Test;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.composite.commons.test.ComponentServiceFactoryImpl;
import jp.go.nict.langrid.composite.commons.test.TestContext;
import jp.go.nict.langrid.service_1_2.LanguagePair;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.LoggingComponentServiceFactory;

public class RichTranslationCombinedWithBilingualDictionaryWithLongestMatchSearchTest {
	@Test
	public void testInternalCodesNotAppearLocal() throws Throwable{
		String src = "次のお茶会ではお点前を拝見させていただきます．";
		Morpheme[] morphResultMecab = new Morpheme[]{new Morpheme("次の", "次の", "other"), new Morpheme("お", "お", "other"), new Morpheme("茶会", "茶会", "noun.common"), new Morpheme("で", "で", "other"), new Morpheme("は", "は", "other"), new Morpheme("お点前", "お点前", "noun.common"), new Morpheme("を", "を", "other"), new Morpheme("拝見", "拝見", "noun.other"), new Morpheme("さ", "する", "verb"), new Morpheme("せて", "せる", "other"), new Morpheme("いただき", "いただく", "verb"), new Morpheme("ます", "ます", "other"), new Morpheme("．", "．", "other")};
		TranslationWithPosition[] bdictResultKyotoSpecialDictionary = new TranslationWithPosition[]{new TranslationWithPosition(new Translation("お茶会", new String[]{"tea ceremony"}), 1, 2), new TranslationWithPosition(new Translation("お点前", new String[]{"tea ceremony etiquette"}), 5, 1)};
		TranslationWithTemporalDictionaryService s =  new RichTranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new LoggingComponentServiceFactory(new ComponentServiceFactoryImpl(){{
							add("BilingualDictionaryWithLongestMatchSearchPL", new BilingualDictionaryWithLongestMatchSearchService() {
								public Translation[] search(String headLang, String targetLang, String headWord, String matchingMethod){
									return null;
								}
								public String[] getSupportedMatchingMethods(){ return null;}
								public LanguagePair[] getSupportedLanguagePairs(){ return null;}
								public Calendar getLastUpdate(){ return null;}
								public TranslationWithPosition[] searchLongestMatchingTerms(String headLang, String targetLang, Morpheme[] morphemes){
									return bdictResultKyotoSpecialDictionary;
								}
							});
							add("MorphologicalAnalysisPL", (MorphologicalAnalysisService)((language, text) -> morphResultMecab));
							add("TranslationPL", newSoapContext().createClient(
									"GoogleTranslateNMT", TranslationService.class));
						}});
			};
		};
		String result = s.translate("ja", "en", src, new Translation[] {}, "en");
		System.out.println(result);
/*		
		翻訳結果：Im nächsten Teezeremonie (お茶会) werde ich Etiquette bei der Teezeremonie (お点前) sehen.
		利用したサービス：GoogleTranslateNMT，Kyoto Speciality Dictionary
*/
	}

	private TestContext newSoapContext(){
		try {
			return new TestContext(getClass(), new SoapClientFactory());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

