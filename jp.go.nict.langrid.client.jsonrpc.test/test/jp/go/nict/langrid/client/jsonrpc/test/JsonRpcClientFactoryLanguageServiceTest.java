package jp.go.nict.langrid.client.jsonrpc.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.lang.reflect.MethodUtil;
import jp.go.nict.langrid.service_1_2.adjacencypair.AdjacencyPairService;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.conceptdictionary.ConceptDictionaryService;
import jp.go.nict.langrid.service_1_2.dependencyparser.DependencyParserService;
import jp.go.nict.langrid.service_1_2.languageidentification.LanguageIdentificationService;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextService;
import jp.go.nict.langrid.service_1_2.paraphrase.ParaphraseService;
import jp.go.nict.langrid.service_1_2.pictogramdictionary.PictogramDictionaryService;
import jp.go.nict.langrid.service_1_2.qualityestimation.QualityEstimationService;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;
import jp.go.nict.langrid.service_1_2.speech.Speech;
import jp.go.nict.langrid.service_1_2.speech.SpeechRecognitionService;
import jp.go.nict.langrid.service_1_2.speech.TextToSpeechService;
import jp.go.nict.langrid.service_1_2.templateparalleltext.BoundChoiceParameter;
import jp.go.nict.langrid.service_1_2.templateparalleltext.BoundValueParameter;
import jp.go.nict.langrid.service_1_2.templateparalleltext.TemplateParallelTextService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.translationselection.TranslationSelectionService;
import junit.framework.Assert;

import org.junit.Test;

public class JsonRpcClientFactoryLanguageServiceTest{
	@Test
	public void test_AdjacencyPairService() throws Exception{
		assertFieldsNotNull(create(AdjacencyPairService.class).search("", "en", "hello", "COMPLETE"));
	}

	@Test
	public void test_BackTranslationService() throws Exception{
		assertFieldsNotNull(create(BackTranslationService.class).backTranslate("en", "ja", "hello"));
	}

	@Test
	public void test_BackTranslationWithTemporalDictionaryService() throws Exception{
		assertFieldsNotNull(create(BackTranslationWithTemporalDictionaryService.class).backTranslate(
				"en", "ja", "hello"
				, new Translation[]{new Translation("head", new String[]{"target"})}, "ja"));
	}

	@Test
	public void test_BilingualDictionaryService() throws Exception{
		assertFieldsNotNull(create(BilingualDictionaryService.class).search("en", "ja", "bank", "COMPLETE"));
	}

	@Test
	public void test_BilingualDictionaryWithLongestMatchSearchService() throws Exception{
		assertFieldsNotNull(create(BilingualDictionaryWithLongestMatchSearchService.class)
				.searchLongestMatchingTerms("en", "ja", new Morpheme[]{
						new Morpheme("word1", "lemma1", "pos1")
						, new Morpheme("word2", "lemma2", "pos2")
				}));
	}

	@Test
	public void test_ConceptDictionaryService() throws Exception{
		assertFieldsNotNull(create(ConceptDictionaryService.class).searchConcepts("en", "bank", "COMPLETE"));
	}

	@Test
	public void test_DependencyParserService() throws Exception{
		assertFieldsNotNull(create(DependencyParserService.class).parseDependency("en", "hello world."));
	}

	@Test
	public void test_LanguageIdentificationService() throws Exception{
		assertFieldsNotNull(create(LanguageIdentificationService.class).identify("hello", "UTF8"));
	}

	@Test
	public void test_MorphologicalAnalysisService() throws Exception{
		assertFieldsNotNull(create(MorphologicalAnalysisService.class).analyze("en", "hello world"));
	}

	@Test
	public void test_MultihopTranslationService() throws Exception{
		assertFieldsNotNull(create(MultihopTranslationService.class).multihopTranslate("en", new String[]{"fr", "de"}, "ja", "hello"));
	}

	@Test
	public void test_ParallelTextService() throws Exception{
		assertFieldsNotNull(create(ParallelTextService.class).search("en", "ja", "hello", "COMPLETE"));
	}

	@Test
	public void test_ParaphraseService() throws Exception{
		assertFieldsNotNull(create(ParaphraseService.class).paraphrase("en", "hello"));
	}

	@Test
	public void test_PictogramDictionaryService() throws Exception{
		assertFieldsNotNull(create(PictogramDictionaryService.class).search("en", "sun", "COMPLETE"));
	}

	@Test
	public void test_QualityEstimationService() throws Exception{
		assertFieldsNotNull(create(QualityEstimationService.class).estimate("en", "ja", "hello", "こんにちは"));
	}

	@Test
	public void test_SimilarityCalculationService() throws Exception{
		assertFieldsNotNull(create(SimilarityCalculationService.class).calculate("en", "hello", "hi"));
	}

	@Test
	public void test_SpeechRecognitionService() throws Exception{
		assertFieldsNotNull(create(SpeechRecognitionService.class)
				.recognize("en", new Speech("men", "x-wav", "hello".getBytes())));
	}

	@Test
	public void test_TemplateParallelTextService() throws Exception{
		assertFieldsNotNull(create(TemplateParallelTextService.class).generateSentence("en", "1"
				, new BoundChoiceParameter[]{}, new BoundValueParameter[]{}));
	}

	@Test
	public void test_TextToSpeechService() throws Exception{
		assertFieldsNotNull(create(TextToSpeechService.class).speak("en", "hello", "men", "x-wav"));
	}

	@Test
	public void test_TranslationService() throws Exception{
		assertFieldsNotNull(create(TranslationService.class).translate("en", "ja", "hello"));
	}

	@Test
	public void test_TranslationSelectionService() throws Exception{
		assertFieldsNotNull(create(TranslationSelectionService.class).select("en", "ja", "hello"));
	}

	@Test
	public void test_TranslationWithTemporalDictionaryService() throws Exception{
		assertFieldsNotNull(create(TranslationWithTemporalDictionaryService.class).translate("en", "ja", "hello"
				, new Translation[]{new Translation("head", new String[]{"target"})}, "ja"));
	}

	@Test
	public void test_TranslationWithBindingService() throws Exception{
		TranslationService service = create(TranslationService.class);
		((RequestAttributes)service).getTreeBindings().add(new BindingNode("invocationname", "serviceid"));
		String ret = service.translate("en", "ja", "hello");
		assertFieldsNotNull(ret);
	}

	@Test
	public void testAttr() throws Exception{
		TranslationService client = create(TranslationService.class);
		RequestAttributes req = (RequestAttributes)client;
		req.setUserId("uid");
		client.translate("en", "ja", "hello");
		ResponseAttributes res = (ResponseAttributes)client;
		res.getCopyright();
	}

	private <T> T create(Class<T> intf) throws MalformedURLException{
		int i = intf.getSimpleName().indexOf("Service");
		return f.create(intf, new URL(base + intf.getSimpleName().substring(0, i)));
	}

	private void assertFieldsNotNull(Object object)
	throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Assert.assertNotNull(object);
		Class<?> clazz = object.getClass();
		while(clazz != null && !clazz.equals(Object.class)){
			for(Method m : clazz.getDeclaredMethods()){
				if(!MethodUtil.isGetter(m)) continue;
				Assert.assertNotNull(m.invoke(object));
			}
			clazz = clazz.getSuperclass();
		}
	}

	private ClientFactory f = new JsonRpcClientFactory();
	private String base = "http://127.0.0.1:8080/jp.go.nict.langrid.webapps.mock/jsServices/";
}
