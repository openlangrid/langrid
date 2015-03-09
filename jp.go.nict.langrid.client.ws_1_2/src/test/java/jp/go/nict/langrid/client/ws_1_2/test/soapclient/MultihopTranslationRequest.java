package jp.go.nict.langrid.client.ws_1_2.test.soapclient;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

import jp.go.nict.langrid.client.soap.io.SoapRequestWriter;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationWithTemporalDictionaryService;

import org.junit.Test;

public class MultihopTranslationRequest {
	@Test
	public void test() throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Method m = MultihopTranslationWithTemporalDictionaryService.class.getMethod(
				"multihopTranslate", new Class<?>[]{
						String.class, String[].class, String.class, String.class,
						Translation[].class, String.class, String[][].class, String[].class
				});
		SoapRequestWriter.writeSoapRequest(baos, new ArrayList<RpcHeader>(),
				m, "ja", new String[]{"en"}, "fr", "こんにちは世界",
				new Translation[]{new Translation("世界", new String[]{"world"})}, "fr",
				new String[][]{new String[]{"SEKAI"}}, new String[]{"en"});
		System.out.println(baos.toString("UTF-8"));
	}
}
