/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.executor.axis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.axis.AxisClientFactory;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.client.ws_1_2.impl.axis.LangridAxisClientFactory;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.servicecontainer.executor.ClientFactoryServiceExecutor;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.component.AbstractComponentServiceFactory;

/**
 * 
 * 
 */
public class AxisComponentServiceFactory
extends AbstractComponentServiceFactory
implements ComponentServiceFactory{
	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getService(String invocationName, long invocationId,
			Endpoint endpoint, Class<T> interfaceClass) {
		try{
			Constructor<?> ctor = executorFactories.get(interfaceClass);
			if(ctor != null){
				return (T)ctor.newInstance(
						invocationName, invocationId, endpoint
						);
			}
			return (T)Proxy.newProxyInstance(
					Thread.currentThread().getContextClassLoader()
					, new Class<?>[]{interfaceClass}
					, new ClientFactoryServiceExecutor(invocationName, invocationId,
						endpoint, interfaceClass, umbrellaClientFactory)
					);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static AxisClientFactory getAxisClientFactory(){
		return axisClientFactory;
	}

	private static ClientFactory umbrellaClientFactory = new ClientFactory() {
		@Override
		public <T> T create(Class<T> interfaceClass, URL url) {
			T client = axisClientFactory.create(interfaceClass, url);
			if(client != null) return client;
			return soapClientFactory.create(interfaceClass, url);
		}
		@Override
		public <T> T create(Class<T> interfaceClass, URL url, String userId,
				String password) {
			T client = axisClientFactory.create(interfaceClass, url, userId, password);
			if(client != null) return client;
			return soapClientFactory.create(interfaceClass, url, userId, password);
		}
	};
	private static SoapClientFactory soapClientFactory = new SoapClientFactory();
	private static AxisClientFactory axisClientFactory = new AxisClientFactory();
	private static Map<Class<?>, Constructor<?>> executorFactories
		= new HashMap<Class<?>, Constructor<?>>( ) ;
//	private static <T> void put(Class<T> intf, Class<? extends T> impl)
//	throws NoSuchMethodException{
//		executorFactories.put(intf, impl.getConstructor(String.class, long.class, Endpoint.class));
//	}
	static {
/*		try{
			put(AsyncTranslationService.class, AxisAsyncTranslationServiceExecutor.class);
			put(BackTranslationService.class, AxisBackTranslationServiceExecutor.class);
			put(BackTranslationWithTemporalDictionaryService.class, AxisBackTranslationWithTemporalDictionaryServiceExecutor.class);
			put(BilingualDictionaryService.class, AxisBilingualDictionaryWithLongestMatchSearchServiceExecutor.class);
			put(BilingualDictionaryWithLongestMatchSearchService.class, AxisBilingualDictionaryWithLongestMatchSearchServiceExecutor.class);
			put(MorphologicalAnalysisService.class, AxisMorphologicalAnalysisServiceExecutor.class);
			put(SimilarityCalculationService.class, AxisSimilarityCalculationServiceExecutor.class);
			put(SpeechRecognitionService.class, AxisSpeechRecognitionServiceExecutor.class);
			put(TextToSpeechService.class, AxisTextToSpeechServiceExecutor.class);
//			put(TranslationService.class, AxisTranslationServiceExecutor.class);
//			put(TranslationWithTemporalDictionaryService.class, AxisTranslationWithTemporalDictionaryServiceExecutor.class);
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
*/		LangridAxisClientFactory.setUp(axisClientFactory);
		axisClientFactory.unregisterStub(TranslationService.class);
	}
}
