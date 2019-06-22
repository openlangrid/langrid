package jp.go.nict.langrid.composite.translation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import org.junit.Test;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.composite.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchCrossSearch;
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
import jp.go.nict.langrid.servicecontainer.service.component.JavaDeclLoggingComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.LoggingComponentServiceFactory;

public class TranslationCombinedWithBilingualDictionaryWithLongestMatchSearchTest {
	@Test
	public void testJavaDecl() throws Throwable{
		System.out.println(JavaDeclLoggingComponentServiceFactory.toJavaDecl(new Morpheme[] {
				new Morpheme("お", "お", "other"), new Morpheme("茶会", "茶会", "noun.common"),
		}));
	}

	@Test
	public void testInternalCodesNotAppearLocal() throws Throwable{
		String src = "次のお茶会ではお点前を拝見させていただきます．";
		Morpheme[] morphResultMecab = new Morpheme[]{new Morpheme("次の", "次の", "other"), new Morpheme("お", "お", "other"), new Morpheme("茶会", "茶会", "noun.common"), new Morpheme("で", "で", "other"), new Morpheme("は", "は", "other"), new Morpheme("お点前", "お点前", "noun.common"), new Morpheme("を", "を", "other"), new Morpheme("拝見", "拝見", "noun.other"), new Morpheme("さ", "する", "verb"), new Morpheme("せて", "せる", "other"), new Morpheme("いただき", "いただく", "verb"), new Morpheme("ます", "ます", "other"), new Morpheme("．", "．", "other")};
		TranslationWithPosition[] bdictResultKyotoSpecialDictionary = new TranslationWithPosition[]{new TranslationWithPosition(new Translation("お茶会", new String[]{"tea ceremony"}), 1, 2), new TranslationWithPosition(new Translation("お点前", new String[]{"tea ceremony etiquette"}), 5, 1)};
		String transResultGoogleNMT = "I will see xxxrwkymrjxxx at the next tea ceremony.";
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
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
	
	@Test
	public void testInternalCodesNotAppear() throws Throwable{
		String src = "次のお茶会ではお点前を拝見させていただきます．";
//		Morpheme[] morphs = {new Morpheme("次の", "次の", "other"),
//				new Morpheme("お", "お", "other"), new Morpheme("茶会", "茶会", "noun.common"),
//				new Morpheme("で", "で", "other"),], jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme@59a6e353[word=は,lemma=は,partOfSpeech=other], jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme@7a0ac6e3[word=お点前,lemma=お点前,partOfSpeech=noun.common], jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme@71be98f5[word=を,lemma=を,partOfSpeech=other], jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme@6fadae5d[word=拝見,lemma=拝見,partOfSpeech=noun.other], jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme@17f6480[word=さ,lemma=する,partOfSpeech=verb], jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme@2d6e8792[word=せて,lemma=せる,partOfSpeech=other], jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme@2812cbfa[word=いただき,lemma=いただく,partOfSpeech=verb], jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme@2acf57e3[word=ます,lemma=ます,partOfSpeech=other], jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme@506e6d5e[word=．,lemma=．,partOfSpeech=other]
//		}
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new JavaDeclLoggingComponentServiceFactory(new ComponentServiceFactoryImpl() {{
					add("MorphologicalAnalysisPL", newSoapContext().createClient(
							"Mecab", MorphologicalAnalysisService.class));
					add("BilingualDictionaryWithLongestMatchSearchPL", newSoapContext().createClient(
							"KyotoSpecialtyDictionary", BilingualDictionaryWithLongestMatchSearchService.class));
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
	@Test
	public void testEnJaHello() throws Throwable{
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new LoggingComponentServiceFactory(new ComponentServiceFactoryImpl(
						Pair.<String, Object>create("TranslationPL", newSoapContext().createClient(
								"KyotoUJServer", TranslationService.class)),
						Pair.<String, Object>create("MorphologicalAnalysisPL", newSoapContext().createClient(
								"TreeTagger", MorphologicalAnalysisService.class))
						));
			};
		};
		System.out.println(s.translate("en", "ja", "hello", new Translation[]{}, "ja"));
	}

	@Test
	public void testPtJa() throws Throwable{
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new LoggingComponentServiceFactory(new ComponentServiceFactoryImpl(
						Pair.<String, Object>create("TranslationPL", newSoapContext().createClient(
								"KyotoUJServer", TranslationService.class)),
						Pair.<String, Object>create("MorphologicalAnalysisPL", newSoapContext().createClient(
								"TreeTagger", MorphologicalAnalysisService.class)),
						Pair.<String, Object>create("BilingualDictionaryWithLongestMatchSearchPL", newSoapContext().createClient(
								"SiaSchoolDictionary", BilingualDictionaryWithLongestMatchSearchService.class))
						));
			};
		};
		System.out.println(s.translate("pt", "ja", "Eu gosto de trabalhos manuais.", new Translation[]{}, "ja"));
	}

	@Test
	public void testEnJaHelloWithLog() throws Throwable{
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new LoggingComponentServiceFactory(new ComponentServiceFactoryImpl(
						Pair.<String, Object>create("TranslationPL", newSoapContext().createClient(
								"KyotoUJServer", TranslationService.class)),
						Pair.<String, Object>create("MorphologicalAnalysisPL", newSoapContext().createClient(
								"TreeTagger", MorphologicalAnalysisService.class))
						));
			};
		};
		System.out.println(s.translate("en", "ja", "hello", new Translation[]{}, "ja"));
	}

	@Test
	public void testEnJaWithQuote() throws Throwable{
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new LoggingComponentServiceFactory(new ComponentServiceFactoryImpl(
						Pair.<String, Object>create("TranslationPL", newSoapContext().createClient(
								"KyotoUJServer", TranslationService.class)),
						Pair.<String, Object>create("MorphologicalAnalysisPL", newSoapContext().createClient(
								"TreeTagger", MorphologicalAnalysisService.class))
						));
			};
		};
		System.out.println(s.translate("en", "ja", "Hello Y's Men",
				new Translation[]{new Translation("Y's Men", new String[]{"ワイズメン"})}, "ja"));
	}

	@Test
	public void testEquals() throws Throwable{
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new LoggingComponentServiceFactory(new ComponentServiceFactoryImpl(
						Pair.<String, Object>create("TranslationPL", newSoapContext().createClient(
								"KyotoUJServer", TranslationService.class)),
						Pair.<String, Object>create("MorphologicalAnalysisPL", newSoapContext().createClient(
								"Mecab", MorphologicalAnalysisService.class))
						));
			};
		};
		System.out.println(s.translate("ja", "en", "====", new Translation[]{}, "ja"));
	}

	@Test
	public void test1() throws Throwable{
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			public ComponentServiceFactory getComponentServiceFactory() {
				return new ComponentServiceFactory() {
					@SuppressWarnings("unchecked")
					@Override
					public <T> T getService(String invocationName, Class<T> interfaceClass) {
						if(invocationName.equals("TranslationPL")){
							System.out.println("bind trans");
							return newSoapContext().createClient(
									"GoogleTranslate", interfaceClass);
						} else if(invocationName.equals("BilingualDictionaryWithLongestMatchSearchPL")){
							System.out.println("bind bdict");
							return (T)new BilingualDictionaryWithLongestMatchSearchCrossSearch(){
								public ComponentServiceFactory getComponentServiceFactory() {
									return new ComponentServiceFactory() {
										@Override
										public <U> U getService(String invocationName, Class<U> interfaceClass) {
											try{
												if(invocationName.equals("BilingualDictionaryWithLongestMatchCrossSearchPL1")){
													return new SoapClientFactory().create(interfaceClass, new URL(
																	"http://robin.ai.soc.i.kyoto-u.ac.jp/ymccommunitydictionary/services/PangaeaCommunityDictionary"
																	));
												}
											} catch(MalformedURLException e){
												throw new RuntimeException(e);
											}
											return null;
										}
									};
								}};
						} else if(invocationName.equals("MorphologicalAnalysisPL")){
							return newSoapContext().createClient(
									"TreeTagger", interfaceClass);
						}
						return null;
					}
				};
			};
		};
		System.out.println(
				s.translate("en", "ko", "We are going to have a circle time.", new Translation[]{}, "ko")
				);
	}

	@Test
	public void test2() throws Throwable{
		TranslationWithTemporalDictionaryService s = newSoapContext().createClient(
				"TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch",
				TranslationWithTemporalDictionaryService.class);
		RequestAttributes reqAttrs = (RequestAttributes)s;
		reqAttrs.getTreeBindings().add(new BindingNode("TranslationPL", "GoogleTranslate"));
//			reqAttrs.getTreeBindings().add(new BindingNode("BilingualDictionaryWithLongestMatchSearchPL", "http://robin.ai.soc.i.kyoto-u.ac.jp/ymccommunitydictionary/services/PangaeaCommunityDictionary"));
		reqAttrs.getTreeBindings().add(
				new BindingNode("BilingualDictionaryWithLongestMatchSearchPL", "BilingualDictionaryWithLongestMatchCrossSearch")
					.addChild(new BindingNode("BilingualDictionaryWithLongestMatchCrossSearchPL1", "http://robin.ai.soc.i.kyoto-u.ac.jp/ymccommunitydictionary/services/PangaeaCommunityDictionary"))
				);
		reqAttrs.getTreeBindings().add(new BindingNode("MorphologicalAnalysisPL", "TreeTagger"));
		System.out.println(
				s.translate("en", "ko", "We are going to have a circle time.", new Translation[]{}, "ko")
				);
		System.out.println("써클 타임");
		System.out.println(CallTreeUtil.encodeTree(((ResponseAttributes)s).getCallTree(), 2));
	}

	@Test
	public void test3() throws Throwable{
		BilingualDictionaryWithLongestMatchSearchService s = newSoapContext().createClient(
				"BilingualDictionaryWithLongestMatchCrossSearch",
				BilingualDictionaryWithLongestMatchSearchService.class
				);
		RequestAttributes reqAttrs = (RequestAttributes)s;
		reqAttrs.getTreeBindings().add(new BindingNode(
				"BilingualDictionaryWithLongestMatchCrossSearchPL1", "http://robin.ai.soc.i.kyoto-u.ac.jp/ymccommunitydictionary/services/PangaeaCommunityDictionary"
				));
		for(TranslationWithPosition t : s.searchLongestMatchingTerms("en", "ko", new Morpheme[]{
				new Morpheme("We", "we", "other"),
				new Morpheme("are", "are", "other"),
				new Morpheme("going", "going", "other"),
				new Morpheme("to", "to", "other"),
				new Morpheme("have", "have", "other"),
				new Morpheme("a", "a", "other"),
				new Morpheme("circle", "circle", "noun.other"),
				new Morpheme("time", "time", "noun.other"),
		})){
			System.out.println(t);
		}
		System.out.println(CallTreeUtil.encodeTree(((ResponseAttributes)s).getCallTree(), 2));
	}

	@Test
	public void test3_2() throws Throwable{
		try{
			BilingualDictionaryWithLongestMatchSearchService s = new SoapClientFactory().create(
					BilingualDictionaryWithLongestMatchSearchService.class, new URL(
							"http://robin.ai.soc.i.kyoto-u.ac.jp/ymccommunitydictionary/services/PangaeaCommunityDictionary"
							));
			for(TranslationWithPosition t : s.searchLongestMatchingTerms("en", "ko", new Morpheme[]{
					new Morpheme("We", "we", "other"),
					new Morpheme("are", "are", "other"),
					new Morpheme("going", "going", "other"),
					new Morpheme("to", "to", "other"),
					new Morpheme("have", "have", "other"),
					new Morpheme("a", "a", "other"),
					new Morpheme("circle", "circle", "noun.other"),
					new Morpheme("time", "time", "noun.other"),
			})){
				System.out.println(t);
			}
			System.out.println(CallTreeUtil.encodeTree(((ResponseAttributes)s).getCallTree(), 2));
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test4() throws Throwable{
		try{
			BilingualDictionaryWithLongestMatchSearchService s = new SoapClientFactory().create(
					BilingualDictionaryWithLongestMatchSearchService.class, new URL(
							"http://robin.ai.soc.i.kyoto-u.ac.jp/ymccommunitydictionary/services/PangaeaCommunityDictionary"
							)
						);
			for(Object  t : s.search("en", "ko", "circle time", "COMPLETE")){
				System.out.println(t);
			}
			System.out.println(CallTreeUtil.encodeTree(((ResponseAttributes)s).getCallTree(), 2));
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test5() throws Throwable{
		TranslationWithTemporalDictionaryService s = newSoapContext().createClient(
				"TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch",
				TranslationWithTemporalDictionaryService.class);
		RequestAttributes reqAttrs = (RequestAttributes)s;
		reqAttrs.getTreeBindings().add(new BindingNode("TranslationPL", "GoogleTranslate"));
//			reqAttrs.getTreeBindings().add(new BindingNode("BilingualDictionaryWithLongestMatchSearchPL", "http://robin.ai.soc.i.kyoto-u.ac.jp/ymccommunitydictionary/services/PangaeaCommunityDictionary"));
//			reqAttrs.getTreeBindings().add(
//					new BindingNode("BilingualDictionaryWithLongestMatchSearchPL", "BilingualDictionaryWithLongestMatchCrossSearch")
//						.addChild(new BindingNode("BilingualDictionaryWithLongestMatchCrossSearchPL1", "http://robin.ai.soc.i.kyoto-u.ac.jp/ymccommunitydictionary/services/PangaeaCommunityDictionary"))
//					);
		reqAttrs.getTreeBindings().add(new BindingNode("MorphologicalAnalysisPL", "TreeTagger"));
		System.out.println(
				s.translate("ko", "en", "우리는 xxxwsfdtvlxxx 을 할 것입니다", new Translation[]{
						new Translation("xxxwsfdtvlxxx", new String[]{"hogehoge"})
				}, "en")
				);
		System.out.println("써클 타임");
		System.out.println(CallTreeUtil.encodeTree(((ResponseAttributes)s).getCallTree(), 2));
	}

	private TestContext newSoapContext(){
		try {
			return new TestContext(getClass(), new SoapClientFactory());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

