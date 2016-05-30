package jp.go.nict.langrid.composite.translation;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.composite.commons.test.ComponentServiceFactoryImpl;
import jp.go.nict.langrid.composite.commons.test.TestContext;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationResult;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.LoggingComponentServiceFactory;

import org.junit.Assert;
import org.junit.Test;

public class MultihopTranslationCombinedWithBilingualDictionaryWithLongestMatchSearchTest {
	@Test
	public void testEquals() throws Throwable{
		final TestContext c = new TestContext(getClass(), new SoapClientFactory());
		MultihopTranslationWithTemporalDictionaryService s =  new MultihopTranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new LoggingComponentServiceFactory(new ComponentServiceFactoryImpl(){{
					add("TranslationPL1", c.createClient("KyotoUJServer", TranslationService.class));
					add("TranslationPL2", c.createClient("GoogleTranslate", TranslationService.class));
					add("TranslationPL3", c.createClient("GoogleTranslate", TranslationService.class));
					add("TranslationPL4", c.createClient("KyotoUJServer", TranslationService.class));
					add("MorphologicalAnalysisPL", c.createClient("Mecab", MorphologicalAnalysisService.class));
					add("BilingualDictionaryWithLongestMatchSearchPL", c.createClient("KyotoTourismDb", BilingualDictionaryWithLongestMatchSearchService.class));
				}});
			};
		};
		System.out.println(
				s.multihopTranslate("ja", new String[]{"en", "fr", "en"}, "ja",
						"電卓の数字キーを押すと数字の周囲がアニメーションとともに薄いグレーで囲まれ、どのボタンを操作したのかがわかりやすくなっている",
						new Translation[]{
							new Translation("電卓", new String[]{"デンタック"}),
						}, "ja",
						new String[][][]{
							new String[][]{new String[]{"DENTAKUEN"}},
							new String[][]{new String[]{"DENTAKUFR"}},
							new String[][]{new String[]{"DENTAKUEN2"}}
						},
						new String[]{"en", "fr", "en"}
				));
	}

	@Test
	public void test_empty_dict() throws Throwable{
		Assert.assertEquals(
				"こんにちは世界",
				getService().multihopTranslate(
						"ja", new String[]{"en"}, "fr",
						"こんにちは世界",
						new Translation[]{},
						"fr",
						new String[][][]{
								new String[][]{}  // must have elements of the number same to intermediate langs.
								},
						new String[]{"en"}).getTarget());
	}

	@Test
	public void test_empty_intermediates() throws Throwable{
		Assert.assertEquals(
				JSON.encode(new MultihopTranslationResult(new String[]{}, "こんにちは世界_fr")),
				JSON.encode(getService().multihopTranslate(
					"ja", new String[]{}, "fr",
					"こんにちは世界",
					new Translation[]{new Translation("世界", new String[]{"世界_fr"})},
					"fr",
					new String[][][]{},
					new String[]{})
					));
	}

	@Test
	public void test_illegal_args_intermediateTemporalDictTargets_1() throws Throwable{
		try{
			getService().multihopTranslate(
					"ja", new String[]{"en"}, "fr",
					"こんにちは世界",
					new Translation[]{new Translation("世界", new String[]{"世界_fr"})},
					"fr",
					new String[][][]{},
					new String[]{"en"});
			Assert.fail();
		} catch(InvalidParameterException e){
			Assert.assertEquals("intermediateTemporalDictTargets", e.getParameterName());
		}
	}

	@Test
	public void test_illegal_args_intermediateTemporalDictTargets_2() throws Throwable{
		try{
			getService().multihopTranslate(
					"ja", new String[]{"en"}, "fr",
					"こんにちは世界",
					new Translation[]{new Translation("世界", new String[]{"世界_fr"})},
					"fr",
					new String[][][]{new String[][]{new String[]{}}},
					new String[]{"en"});
			Assert.fail();
		} catch(InvalidParameterException e){
			Assert.assertEquals("intermediateTemporalDictTargets[0][0]", e.getParameterName());
		}
	}

	@Test
	public void test_illegal_args_intermediateTemporalDictTargets_3() throws Throwable{
		try{
			getService().multihopTranslate(
					"ja", new String[]{"en"}, "fr",
					"こんにちは世界",
					new Translation[]{
							new Translation("世界", new String[]{"世界_fr"}),
							new Translation("こんにちは", new String[]{"こんにちは_fr"})},
					"fr",
					new String[][][]{new String[][]{new String[]{"世界_en"}, new String[]{}}},
					new String[]{"en"});
			Assert.fail();
		} catch(InvalidParameterException e){
			Assert.assertEquals("intermediateTemporalDictTargets[0][1]", e.getParameterName());
		}
	}

	@Test
	public void test_illegal_args_intermediateTemporalDictTargets_4() throws Throwable{
		try{
			getService().multihopTranslate(
					"ja", new String[]{}, "fr",
					"こんにちは世界",
					new Translation[]{new Translation("世界", new String[]{"世界_fr"})},
					"fr",
					new String[][][]{new String[][]{}},
					new String[]{});
			Assert.fail();
		} catch(InvalidParameterException e){
			Assert.assertEquals("intermediateTemporalDictTargets", e.getParameterName());
		}
	}

	@Test
	public void test_illegal_args_intermediateDictTargetLangs_1() throws Throwable{
		try{
			getService().multihopTranslate(
					"ja", new String[]{}, "fr",
					"こんにちは世界",
					new Translation[]{new Translation("世界", new String[]{"世界_fr"})},
					"fr",
					new String[][][]{},
					new String[]{"en"});
			Assert.fail();
		} catch(InvalidParameterException e){
			Assert.assertEquals("intermediateDictTargetLangs", e.getParameterName());
		}
	}

	private MultihopTranslationWithTemporalDictionaryService getService(){
		return new MultihopTranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new LoggingComponentServiceFactory(new ComponentServiceFactory(){
					@Override
					public <T> T getService(String invocationName, Class<T> interfaceClass) {
						if(invocationName.startsWith("Translation")){
							return interfaceClass.cast(new TranslationService() {
								@Override
								public String translate(String sourceLang, String targetLang, String source){
									return source;
								}
							});
						}
						return null;
					}
				});
			};
		};
	}
}
