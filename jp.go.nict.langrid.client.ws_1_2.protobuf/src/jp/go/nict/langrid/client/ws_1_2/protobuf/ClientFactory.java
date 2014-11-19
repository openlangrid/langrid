/*
 * $Id: ClientFactory.java 477 2012-05-22 07:43:21Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.client.ws_1_2.protobuf;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.logging.Logger;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.impl.protobuf.PbClientFactory;
import jp.go.nict.langrid.client.ws_1_2.AdjacencyPairClient;
import jp.go.nict.langrid.client.ws_1_2.BackTranslationClient;
import jp.go.nict.langrid.client.ws_1_2.BackTranslationWithTemporalDictionaryClient;
import jp.go.nict.langrid.client.ws_1_2.BilingualDictionaryClient;
import jp.go.nict.langrid.client.ws_1_2.BilingualDictionaryHeadwordsExtractionClient;
import jp.go.nict.langrid.client.ws_1_2.BilingualDictionaryWithLongestMatchSearchClient;
import jp.go.nict.langrid.client.ws_1_2.ConceptDictionaryClient;
import jp.go.nict.langrid.client.ws_1_2.DependencyParserClient;
import jp.go.nict.langrid.client.ws_1_2.DictionaryClient;
import jp.go.nict.langrid.client.ws_1_2.LanguageIdentificationClient;
import jp.go.nict.langrid.client.ws_1_2.MetadataForParallelTextClient;
import jp.go.nict.langrid.client.ws_1_2.MorphologicalAnalysisClient;
import jp.go.nict.langrid.client.ws_1_2.MultihopTranslationClient;
import jp.go.nict.langrid.client.ws_1_2.ParallelTextClient;
import jp.go.nict.langrid.client.ws_1_2.ParallelTextWithEmbeddedMetadataClient;
import jp.go.nict.langrid.client.ws_1_2.ParallelTextWithExternalMetadataClient;
import jp.go.nict.langrid.client.ws_1_2.ParallelTextWithIdClient;
import jp.go.nict.langrid.client.ws_1_2.ParaphraseClient;
import jp.go.nict.langrid.client.ws_1_2.PictogramDictionaryClient;
import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.client.ws_1_2.SimilarityCalculationClient;
import jp.go.nict.langrid.client.ws_1_2.SpeechRecognitionClient;
import jp.go.nict.langrid.client.ws_1_2.TemplateParallelTextClient;
import jp.go.nict.langrid.client.ws_1_2.TextToSpeechClient;
import jp.go.nict.langrid.client.ws_1_2.TranslationClient;
import jp.go.nict.langrid.client.ws_1_2.TranslationWithTemporalDictionaryClient;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientReqResAttributesAdapter;
import jp.go.nict.langrid.client.ws_1_2.protobuf.impl.BackTranslationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.protobuf.impl.BackTranslationWithTemporalDictionaryClientImpl;
import jp.go.nict.langrid.client.ws_1_2.protobuf.impl.BilingualDictionaryWithLongestMatchSearchClientImpl;
import jp.go.nict.langrid.client.ws_1_2.protobuf.impl.LanguageIdentificationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.protobuf.impl.MorphologicalAnalysisClientImpl;
import jp.go.nict.langrid.client.ws_1_2.protobuf.impl.TextToSpeechClientImpl;
import jp.go.nict.langrid.client.ws_1_2.protobuf.impl.TranslationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.protobuf.impl.TranslationWithTemporalDictionaryClientImpl;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.beanutils.DynamicInvocationHandler;
import jp.go.nict.langrid.commons.net.proxy.pac.PacUtil;
import jp.go.nict.langrid.service_1_2.adjacencypair.AdjacencyPairService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryHeadwordsExtractionService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryService;
import jp.go.nict.langrid.service_1_2.conceptdictionary.ConceptDictionaryService;
import jp.go.nict.langrid.service_1_2.dependencyparser.DependencyParserService;
import jp.go.nict.langrid.service_1_2.dictionary.DictionaryService;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationService;
import jp.go.nict.langrid.service_1_2.paralleltext.MetadataForParallelTextService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithEmbeddedMetadataService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithExternalMetadataService;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelTextWithIdService;
import jp.go.nict.langrid.service_1_2.paraphrase.ParaphraseService;
import jp.go.nict.langrid.service_1_2.pictogramdictionary.PictogramDictionaryService;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;
import jp.go.nict.langrid.service_1_2.speech.SpeechRecognitionService;
import jp.go.nict.langrid.service_1_2.templateparalleltext.TemplateParallelTextService;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 477 $
 */
public class ClientFactory{
	/**
	 * 
	 * 
	 */
	public static TranslationClient createTranslationClient(URL serviceUrl){
		return setup(new TranslationClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static TranslationWithTemporalDictionaryClient
	createTranslationWithTemporalDictionaryClient(URL serviceUrl){
		return setup(new TranslationWithTemporalDictionaryClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static BackTranslationClient createBackTranslationClient(URL serviceUrl){
		return setup(new BackTranslationClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static BackTranslationWithTemporalDictionaryClient createBackTranslationWithTemporalDictionaryClient(URL serviceUrl){
		return setup(new BackTranslationWithTemporalDictionaryClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static MultihopTranslationClient createMultihopTranslationClient(URL serviceUrl){
		return setupAndCreateProxy(
				MultihopTranslationClient.class
				, MultihopTranslationService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static DictionaryClient createDictionaryClient(URL serviceUrl){
		return setupAndCreateProxy(
				DictionaryClient.class
				, DictionaryService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static AdjacencyPairClient createAdjacencyPairClient(URL serviceUrl){
		return setupAndCreateProxy(
				AdjacencyPairClient.class
				, AdjacencyPairService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static BilingualDictionaryClient createBilingualDictionaryClient(URL serviceUrl){
		return setupAndCreateProxy(
				BilingualDictionaryClient.class
				, BilingualDictionaryService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static BilingualDictionaryHeadwordsExtractionClient createBilingualDictionaryHeadwordsExtractionClient(
			URL serviceUrl){
		return setupAndCreateProxy(
				BilingualDictionaryHeadwordsExtractionClient.class
				, BilingualDictionaryHeadwordsExtractionService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static ParallelTextClient createParallelTextClient(URL serviceUrl){
		return setupAndCreateProxy(
				ParallelTextClient.class
				, ParallelTextService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static ParallelTextWithIdClient createParallelTextWithIdClient(URL serviceUrl){
		return setupAndCreateProxy(
				ParallelTextWithIdClient.class
				, ParallelTextWithIdService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static ParaphraseClient createParaphraseClient(URL serviceUrl){
		return setupAndCreateProxy(
				ParaphraseClient.class
				, ParaphraseService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static MorphologicalAnalysisClient createMorphologicalAnalysisClient(
			URL serviceUrl){
		return setup(new MorphologicalAnalysisClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static SimilarityCalculationClient createSimilarityCalculationClient(
			URL serviceUrl){
		return setupAndCreateProxy(
				SimilarityCalculationClient.class
				, SimilarityCalculationService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static PictogramDictionaryClient createPictogramDictionaryClient(URL serviceUrl){
		return setupAndCreateProxy(
				PictogramDictionaryClient.class
				, PictogramDictionaryService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static ConceptDictionaryClient createConceptDictionaryClient(URL serviceUrl){
		return setupAndCreateProxy(
				ConceptDictionaryClient.class
				, ConceptDictionaryService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static DependencyParserClient createDependencyParserClient(URL serviceUrl){
		return setupAndCreateProxy(
				DependencyParserClient.class
				, DependencyParserService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static ParallelTextWithEmbeddedMetadataClient createParallelTextWithMetadataClient(
			URL serviceUrl){
		return setupAndCreateProxy(
				ParallelTextWithEmbeddedMetadataClient.class
				, ParallelTextWithEmbeddedMetadataService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static ParallelTextWithExternalMetadataClient createParallelTextWithMetadataFromCandidateClient(
			URL serviceUrl){
		return setupAndCreateProxy(
				ParallelTextWithExternalMetadataClient.class
				, ParallelTextWithExternalMetadataService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static MetadataForParallelTextClient createMetadataForParallelTextClient(
			URL serviceUrl){
		return setupAndCreateProxy(
				MetadataForParallelTextClient.class
				, MetadataForParallelTextService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static BilingualDictionaryWithLongestMatchSearchClient createBilingualDictionaryWithLongestMatchSearchClient(
			URL serviceUrl){
		return setup(new BilingualDictionaryWithLongestMatchSearchClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static TextToSpeechClient createTextToSpeechClient(
			URL serviceUrl){
		return setup(new TextToSpeechClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static SpeechRecognitionClient createSpeechRecognitionClient(
			URL serviceUrl){
		return setupAndCreateProxy(
				SpeechRecognitionClient.class
				, SpeechRecognitionService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static LanguageIdentificationClient createLanguageIdentificationClient(
			URL serviceUrl){
		return setup(new LanguageIdentificationClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static TemplateParallelTextClient createTemplateParallelTextClient(
			URL serviceUrl){
		return setupAndCreateProxy(
				TemplateParallelTextClient.class
				, TemplateParallelTextService.class
				, serviceUrl);
	}

	/**
	 * 
	 * 
	 */
	public static void setDefaultUserId(String userId){
		defaultUserId = userId;
	}

	/**
	 * 
	 * 
	 */
	public static void setDefaultPassword(String password){
		defaultPassword = password;
	}

	private static <T extends ServiceClient>T setup(T client){
		if(defaultUserId != null){
			client.setUserId(defaultUserId);
		}
		if(defaultPassword != null){
			client.setPassword(defaultPassword);
		}
		return client;
	}

	private static <T extends ServiceClient, U> T setupAndCreateProxy(
			Class<T> clientClass, Class<U> serviceClass, URL url){
		U client = new PbClientFactory().create(serviceClass, url);
		RequestAttributes req = (RequestAttributes)client;
		ResponseAttributes res = (ResponseAttributes)client;
		if(defaultUserId != null){
			req.setUserId(defaultUserId);
		}
		if(defaultPassword != null){
			req.setPassword(defaultPassword);
		}
		final ServiceClientReqResAttributesAdapter sc = new ServiceClientReqResAttributesAdapter(
				req, res);
		final DynamicInvocationHandler<U> handler = new DynamicInvocationHandler<U>(client, converter);
		return clientClass.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class[]{clientClass}
				, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						if(method.getDeclaringClass().equals(ServiceClient.class)){
							try{
								return method.invoke(sc, args);
							} catch(InvocationTargetException e){
								throw e.getCause();
							}
						}
						return handler.invoke(proxy, method, args);
					}
				}
				));
	}

	private static Converter converter = new Converter();
	private static String defaultUserId;
	private static String defaultPassword;
	private static Logger logger = Logger.getLogger(ClientFactory.class.getName());
	static{
		try{
			System.setProperty("java.net.useSystemProxies", "true");
		}catch(SecurityException e){
			logger.warning("failed to set system property \"java.net.useSystemProxies\"");
		}
		PacUtil.setupDefaultProxySelector();
	}
}
