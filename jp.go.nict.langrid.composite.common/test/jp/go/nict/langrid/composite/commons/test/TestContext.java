/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.composite.commons.test;

import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ws_1_2.impl.axis.LangridAxisClientFactory;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

public class TestContext {
	public static <T> T newClient(String serviceId, Class<T> interfaceClass){
		try{
			T s = factory.create(interfaceClass, new URL(
					"${serviceManager_url}" + serviceId
					));
			RequestAttributes reqAttrs = (RequestAttributes)s;
			reqAttrs.setUserId("${userId}");
			reqAttrs.setPassword("${password}");
			return s;
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}
	private static ClientFactory factory = LangridAxisClientFactory.getInstance();

	static String userId = "user1";
	static String password = "pass1";
	static String baseUrl = "http://localhost/langrid/invoker/";
	static Converter converter = new Converter();
	
	static class PartOfSpeechToStringTransformer implements Transformer<PartOfSpeech, String>{
		@Override
		public String transform(PartOfSpeech value)
				throws TransformationException {
			return value.getExpression();
		}
	}
	static{
		converter.addTransformerConversion(new PartOfSpeechToStringTransformer());
	}
}
