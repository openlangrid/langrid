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
package jp.go.nict.langrid.client.ws_1_2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.logging.Logger;

import jp.go.nict.langrid.client.axis.ProxySelectingAxisSocketFactory;
import jp.go.nict.langrid.client.axis.ProxySelectingSecureAxisSocketFactory;
import jp.go.nict.langrid.client.ws_1_2.impl.AxisStubUtil;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientAxisStubAdapter;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.AdjacencyPairClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.BackTranslationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.BackTranslationWithTemporalDictionaryClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.BilingualDictionaryClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.BilingualDictionaryHeadwordsExtractionClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.BilingualDictionaryWithLongestMatchSearchClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.ConceptDictionaryClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.DependencyParserClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.DictionaryClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.LanguageIdentificationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.MetadataForParallelTextClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.MorphologicalAnalysisClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.MultihopBackTranslationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.MultihopTranslationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.ParallelTextClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.ParallelTextWithEmbeddedMetadataClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.ParallelTextWithExternalMetadataClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.ParallelTextWithIdClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.ParaphraseClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.PictogramDictionaryClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.QualityEstimationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.SimilarityCalculationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.SpeechRecognitionClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.TemplateParallelTextClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.TextToSpeechClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.TranslationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.TranslationSelectionClientImpl;
import jp.go.nict.langrid.client.ws_1_2.impl.langservice.TranslationWithTemporalDictionaryClientImpl;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.beanutils.DynamicInvocationHandler;
import jp.go.nict.langrid.commons.net.proxy.pac.PacUtil;
import jp.go.nict.langrid.service_1_2.transformer.StringToDictMatchingMethodTransformer;
import jp.go.nict.langrid.service_1_2.transformer.StringToPartOfSpeechTransformer;

import org.apache.axis.client.Stub;

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
//		return createClient(TranslationClient.class, TranslationService.class, serviceUrl);
		return setup(new TranslationClientImpl(serviceUrl));
	}

	public static <T extends ServiceClient> T createClient(Class<T> clientIntf, Class<?> serviceIntf, URL serviceUrl){
		final Stub stub = AxisStubUtil.createStub(serviceIntf);
		final ServiceClientAxisStubAdapter sc = new ServiceClientAxisStubAdapter(stub, serviceUrl);
		final DynamicInvocationHandler<Stub> handler = new DynamicInvocationHandler<Stub>(stub, converter);
		return clientIntf.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class[]{clientIntf}
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
						try{
							sc.preInvoke();
							return handler.invoke(proxy, method, args);
						} finally{
							sc.postInvoke();
						}
					}
				}));
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
		return setup(new MultihopTranslationClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static MultihopBackTranslationClient createMultihopBackTranslationClient(
			URL serviceUrl){
		return setup(new MultihopBackTranslationClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static DictionaryClient createDictionaryClient(URL serviceUrl){
		return setup(new DictionaryClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static AdjacencyPairClient createAdjacencyPairClient(URL serviceUrl){
		return setup(new AdjacencyPairClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static BilingualDictionaryClient createBilingualDictionaryClient(URL serviceUrl){
		return setup(new BilingualDictionaryClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static BilingualDictionaryHeadwordsExtractionClient createBilingualDictionaryHeadwordsExtractionClient(
			URL serviceUrl){
		return setup(new BilingualDictionaryHeadwordsExtractionClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ParallelTextClient createParallelTextClient(URL serviceUrl){
		return setup(new ParallelTextClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ParallelTextWithIdClient createParallelTextWithIdClient(URL serviceUrl){
		return setup(new ParallelTextWithIdClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ParaphraseClient createParaphraseClient(URL serviceUrl){
		return setup(new ParaphraseClientImpl(serviceUrl));
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
		return setup(new SimilarityCalculationClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static PictogramDictionaryClient createPictogramDictionaryClient(URL serviceUrl){
		return setup(new PictogramDictionaryClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ConceptDictionaryClient createConceptDictionaryClient(URL serviceUrl){
		return setup(new ConceptDictionaryClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static DependencyParserClient createDependencyParserClient(URL serviceUrl){
		return setup(new DependencyParserClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ParallelTextWithEmbeddedMetadataClient createParallelTextWithMetadataClient(
			URL serviceUrl){
		return setup(new ParallelTextWithEmbeddedMetadataClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ParallelTextWithExternalMetadataClient createParallelTextWithMetadataFromCandidateClient(
			URL serviceUrl){
		return setup(new ParallelTextWithExternalMetadataClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static MetadataForParallelTextClient createMetadataForParallelTextClient(
			URL serviceUrl){
		return setup(new MetadataForParallelTextClientImpl(serviceUrl));
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
		return setup(new SpeechRecognitionClientImpl(serviceUrl));
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
		return setup(new TemplateParallelTextClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static TranslationSelectionClient createTranslationSelectionClient(
			URL serviceUrl){
		return setup(new TranslationSelectionClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static QualityEstimationClient createQualityEstimationClient(
			URL serviceUrl){
		return setup(new QualityEstimationClientImpl(serviceUrl));
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

	private static String defaultUserId;
	private static String defaultPassword;
	private static Logger logger = Logger.getLogger(ClientFactory.class.getName());
	private static Converter converter = new Converter();
	static{
		converter.addTransformerConversion(new StringToPartOfSpeechTransformer());
		converter.addTransformerConversion(new StringToDictMatchingMethodTransformer());
		try{
			System.setProperty("java.net.useSystemProxies", "true");
		}catch(SecurityException e){
			logger.warning("failed to set system property \"java.net.useSystemProxies\"");
		}
		ProxySelectingAxisSocketFactory.install();
		ProxySelectingSecureAxisSocketFactory.install();
		PacUtil.setupDefaultProxySelector();
	}
}
