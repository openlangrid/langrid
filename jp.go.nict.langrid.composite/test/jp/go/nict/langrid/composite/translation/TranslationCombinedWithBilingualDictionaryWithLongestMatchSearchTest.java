package jp.go.nict.langrid.composite.translation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.LoggingComponentServiceFactory;

public class TranslationCombinedWithBilingualDictionaryWithLongestMatchSearchTest {
	@Test
	public void testEnJaHello() throws Throwable{
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			@SuppressWarnings("unchecked")
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
	public void testEnJaHelloWithLog() throws Throwable{
		TranslationWithTemporalDictionaryService s =  new TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch(){
			@SuppressWarnings("unchecked")
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
			@SuppressWarnings("unchecked")
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
			@SuppressWarnings("unchecked")
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

