package jp.go.nict.langrid.webapps.mock.json;

import java.net.URL;

import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

import org.junit.Assert;
import org.junit.Test;

public class HeaderTest {
	@Test
	public void test() throws Exception{
		TranslationService client = new JsonRpcClientFactory().create(
				TranslationService.class,
				new URL("http://127.0.0.1:8080/jp.go.nict.langrid.webapps.mock/jsServices/Translation"));
		client.translate("en", "ja", "hello");
		Assert.assertEquals("*", 
				((ResponseAttributes)client).getResponseMimeHeaders().get("Access-Control-Allow-Origin"));
	}
}
