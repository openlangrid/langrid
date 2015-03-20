/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.client.ws_1_2.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.namespace.QName;

import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.service_1_2.adjacencypair.AdjacencyPairService;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryHeadwordsExtractionService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryService;
import jp.go.nict.langrid.service_1_2.conceptdictionary.ConceptDictionaryService;
import jp.go.nict.langrid.service_1_2.dependencyparser.DependencyParserService;
import jp.go.nict.langrid.service_1_2.dictionary.DictionaryService;
import jp.go.nict.langrid.service_1_2.languageidentification.LanguageIdentificationService;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationService;
import jp.go.nict.langrid.service_1_2.paralleltext.MetadataForParallelTextService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithEmbeddedMetadataService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithExternalMetadataService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithIdService;
import jp.go.nict.langrid.service_1_2.pictogramdictionary.PictogramDictionaryService;
import jp.go.nict.langrid.service_1_2.qualityestimation.QualityEstimationService;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;
import jp.go.nict.langrid.service_1_2.speech.SpeechRecognitionService;
import jp.go.nict.langrid.service_1_2.speech.TextToSpeechService;
import jp.go.nict.langrid.service_1_2.templateparalleltext.TemplateParallelTextService;
import jp.go.nict.langrid.service_1_2.transformer.StringToDictMatchingMethodTransformer;
import jp.go.nict.langrid.service_1_2.transformer.StringToPartOfSpeechTransformer;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.translationselection.TranslationSelectionService;
import localhost.jp_go_nict_langrid_webapps_atomic.services.TranslationSelection.TranslationSelectionServiceServiceLocator;
import localhost.jp_go_nict_langrid_webapps_langrid_p2p.services.QualityEstimation.QualityEstimationServiceServiceLocator;
import localhost.service_mock.services.SpeechRecognition.SpeechRecognitionServiceLocator;
import localhost.service_mock.services.TemplateParallelText.TemplateParallelTextServiceLocator;
import localhost.service_mock.services.TextToSpeech.TextToSpeechServiceLocator;
import localhost.wrapper_mock_1_2.services.BackTranslation.BackTranslation_ServiceLocator;
import localhost.wrapper_mock_1_2.services.BackTranslationWithTemporalDictionary.BackTranslationWithTemporalDictionaryServiceLocator;
import localhost.wrapper_mock_1_2.services.BilingualDictionaryWithLongestMatchSearch.BilingualDictionaryWithLongestMatchSearchService;
import localhost.wrapper_mock_1_2.services.BilingualDictionaryWithLongestMatchSearch.BilingualDictionaryWithLongestMatchSearchServiceLocator;
import localhost.wrapper_mock_1_2.services.Dictionary.Dictionary_ServiceLocator;
import localhost.wrapper_mock_1_2.services.LanguageIdentification.LanguageIdentificationServiceLocator;
import localhost.wrapper_mock_1_2.services.MetadataForParallelText.MetadataForParallelTextServiceLocator;
import localhost.wrapper_mock_1_2.services.MorphologicalAnalysis.MorphologicalAnalysis_ServiceLocator;
import localhost.wrapper_mock_1_2.services.MultihopTranslation.MultihopTranslation_ServiceLocator;
import localhost.wrapper_mock_1_2.services.ParallelTextWithEmbeddedMetadata.ParallelTextWithEmbeddedMetadataServiceLocator;
import localhost.wrapper_mock_1_2.services.ParallelTextWithExternalMetadata.ParallelTextWithExternalMetadataServiceLocator;
import localhost.wrapper_mock_1_2.services.ParallelTextWithId.ParallelTextWithIdServiceLocator;
import localhost.wrapper_mock_1_2.services.SimilarityCalculation.SimilarityCalculation_ServiceLocator;
import localhost.wrapper_mock_1_2.services.Translation.Translation_ServiceLocator;
import localhost.wrapper_mock_1_2.services.TranslationWithTemporalDictionary.TranslationWithTemporalDictionaryServiceLocator;
import localhost.wrapper_mock_1_2_N.services.AdjacencyPair.AdjacencyPairServiceLocator;
import localhost.wrapper_mock_1_2_N.services.BilingualDictionary.BilingualDictionaryServiceLocator;
import localhost.wrapper_mock_1_2_N.services.BilingualDictionaryHeadwordsExtraction.BilingualDictionaryHeadwordsExtractionServiceLocator;
import localhost.wrapper_mock_1_2_N.services.ConceptDictionary.ConceptDictionaryServiceLocator;
import localhost.wrapper_mock_1_2_N.services.DependencyParser.DependencyParserServiceLocator;
import localhost.wrapper_mock_1_2_N.services.ParallelText.ParallelTextServiceLocator;
import localhost.wrapper_mock_1_2_N.services.PictogramDictionary.PictogramDictionaryServiceLocator;

import org.apache.axis.client.Stub;
import org.apache.axis.transport.http.HTTPConstants;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class AxisStubUtil {
	/**
	 * 
	 * 
	 */
	public static Stub createStub(Class<?> interfaceClass){
		Pair<Object, Method> s = stubs.get(interfaceClass);
		try{
			return (Stub)s.getSecond().invoke(s.getFirst());
		} catch(IllegalAccessException e){
			throw new RuntimeException(e);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void setUrl(Stub stub, URL url){
		stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url.toString());
	}

	/**
	 * 
	 * 
	 */
	public static void setUserName(Stub stub, String username){
		setUserName(stub, username, true);
	}

	/**
	 * 
	 * 
	 */
	public static void setUserName(Stub stub, String username, boolean httpPreemptive){
		stub.setUsername(username);
		stub._setProperty("HTTPPreemptive", (httpPreemptive && username != null) ? "true" : "false");
	}

	/**
	 * 
	 * 
	 */
	public static void setPassword(Stub stub, String password){
		stub.setPassword(password);
	}

	/**
	 * 
	 * 
	 */
	public static void setMimeHeaders(Stub stub, Iterable<Map.Entry<String, Object>> headers){
		@SuppressWarnings("unchecked")
		Map<String, Object> origMimeHeaders = (Map<String, Object>)stub._getProperty(
				HTTPConstants.REQUEST_HEADERS);
		if(origMimeHeaders == null){
			origMimeHeaders = new Hashtable<String, Object>();
			stub._setProperty(HTTPConstants.REQUEST_HEADERS, origMimeHeaders);
		}
		for(Map.Entry<String, Object> entry : headers){
			origMimeHeaders.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 
	 * 
	 */
	public static void setSoapHeaders(Stub stub, Iterable<Map.Entry<QName, Object>> headers){
		for(Map.Entry<QName, Object> h : headers){
			stub.setHeader(new org.apache.axis.message.SOAPHeaderElement(
					h.getKey().getNamespaceURI()
					, h.getKey().getLocalPart()
					, h.getValue()
					));
		}
	}

	/**
	 * 
	 * 
	 */
	public static Converter getConverter(){
		return converter;
	}

	public static void registerStub(Class<?> intf, Object locator, String createMethodName){
		try {
			Method m = locator.getClass().getMethod(createMethodName);
			stubs.put(intf, Pair.create(locator, m));
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private static Map<Class<?>, Pair<Object, Method>> stubs
			= new HashMap<Class<?>, Pair<Object, Method>>();
	private static Converter converter = new Converter();
	static{
		registerStub(AdjacencyPairService.class, new AdjacencyPairServiceLocator(), "getAdjacencyPair");
		registerStub(BackTranslationService.class, new BackTranslation_ServiceLocator(), "getBackTranslation");
		registerStub(BackTranslationWithTemporalDictionaryService.class, new BackTranslationWithTemporalDictionaryServiceLocator(), "getBackTranslationWithTemporalDictionary");
		registerStub(BilingualDictionaryService.class, new BilingualDictionaryServiceLocator(), "getBilingualDictionary");
		registerStub(BilingualDictionaryHeadwordsExtractionService.class, new BilingualDictionaryHeadwordsExtractionServiceLocator(), "getBilingualDictionaryHeadwordsExtraction");
		registerStub(BilingualDictionaryWithLongestMatchSearchService.class, new BilingualDictionaryWithLongestMatchSearchServiceLocator(), "getBilingualDictionaryWithLongestMatchSearch");
		registerStub(ConceptDictionaryService.class, new ConceptDictionaryServiceLocator(), "getConceptDictionary");
		registerStub(DependencyParserService.class, new DependencyParserServiceLocator(), "getDependencyParser");
		registerStub(DictionaryService.class, new Dictionary_ServiceLocator(), "getDictionary");
		registerStub(LanguageIdentificationService.class, new LanguageIdentificationServiceLocator(), "getLanguageIdentification");
		registerStub(MetadataForParallelTextService.class, new MetadataForParallelTextServiceLocator(), "getMetadataForParallelText");
		registerStub(MorphologicalAnalysisService.class, new MorphologicalAnalysis_ServiceLocator(), "getMorphologicalAnalysis");
		registerStub(MultihopTranslationService.class, new MultihopTranslation_ServiceLocator(), "getMultihopTranslation");
		registerStub(ParallelTextService.class, new ParallelTextServiceLocator(), "getParallelText");
		registerStub(ParallelTextWithEmbeddedMetadataService.class, new ParallelTextWithEmbeddedMetadataServiceLocator(), "getParallelTextWithEmbeddedMetadata");
		registerStub(ParallelTextWithExternalMetadataService.class, new ParallelTextWithExternalMetadataServiceLocator(), "getParallelTextWithExternalMetadata");
		registerStub(ParallelTextWithIdService.class, new ParallelTextWithIdServiceLocator(), "getParallelTextWithId");
		registerStub(PictogramDictionaryService.class, new PictogramDictionaryServiceLocator(), "getPictogramDictionary");
		registerStub(QualityEstimationService.class, new QualityEstimationServiceServiceLocator(), "getQualityEstimation");
		registerStub(SimilarityCalculationService.class, new SimilarityCalculation_ServiceLocator(), "getSimilarityCalculation");
		registerStub(SpeechRecognitionService.class, new SpeechRecognitionServiceLocator(), "getSpeechRecognition");
		registerStub(TemplateParallelTextService.class, new TemplateParallelTextServiceLocator(), "getTemplateParallelText");
		registerStub(TextToSpeechService.class, new TextToSpeechServiceLocator(), "getTextToSpeech");
		registerStub(TranslationService.class, new Translation_ServiceLocator(), "getTranslation");
		registerStub(TranslationSelectionService.class, new TranslationSelectionServiceServiceLocator(), "getTranslationSelection");
		registerStub(TranslationWithTemporalDictionaryService.class, new TranslationWithTemporalDictionaryServiceLocator(), "getTranslationWithTemporalDictionary");
		converter.addTransformerConversion(new StringToPartOfSpeechTransformer());
		converter.addTransformerConversion(new StringToDictMatchingMethodTransformer());
	}
}
