/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
package jp.go.nict.langrid.client.soap.test;

import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;

import org.junit.Test;

public class SoapClientFactoryCompositeLanguageServiceTest{
	@Test
	public void test() throws Exception{
		BackTranslationService service = create(BackTranslationService.class);
		RequestAttributes reqAttrs = (RequestAttributes)service;
		reqAttrs.getTreeBindings().add(new BindingNode(
				"ForwardTranslationPL", base + "Translation"));
		reqAttrs.getTreeBindings().add(new BindingNode(
				"BackwardTranslationPL", base + "Translation"));
		service.backTranslate("en", "ja", "hello");
		System.out.println(CallTreeUtil.encodeTree(((ResponseAttributes)service).getCallTree()));
	}

	@Test
	public void test_Trans() throws Exception{
		TranslationWithTemporalDictionaryService service = new SoapClientFactory().create(
					TranslationWithTemporalDictionaryService.class,
					new URL("http://langrid.org/service_manager/invoker/TranslationCombinedWithBilingualDictionaryWithLongestMatchSearch"),
					"ishida.kyoto-u",
					"tWJaakYm");
		RequestAttributes reqAttrs = (RequestAttributes)service;
		reqAttrs.getTreeBindings().add(new BindingNode(
				"MorphologicalAnalysisPL", "TreeTagger"));
		reqAttrs.getTreeBindings().add(new BindingNode(
				"BilingualDictionaryPL", "KyodaiHonyakuDisasterDictionary"));
		reqAttrs.getTreeBindings().add(new BindingNode(
				"TranslationPL", "KyotoUJServer"));
		System.out.println(service.translate("en", "ja", "hello", new Translation[]{}, "ja"));
		System.out.println(CallTreeUtil.encodeTree(((ResponseAttributes)service).getCallTree()));
	}

	public <T> T create(Class<T> intf) throws MalformedURLException{
		int i = intf.getSimpleName().indexOf("Service");
		return f.create(intf, new URL(base + "Composite" + intf.getSimpleName().substring(0, i)));
	}
	
	private ClientFactory f = new SoapClientFactory();
	private String base = "http://localhost:8080/jp.go.nict.langrid.webapps.mock/services/";
}
