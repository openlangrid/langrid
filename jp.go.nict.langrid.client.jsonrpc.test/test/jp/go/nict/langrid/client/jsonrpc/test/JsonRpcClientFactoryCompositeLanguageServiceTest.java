package jp.go.nict.langrid.client.jsonrpc.test;

import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;

import org.junit.Test;

public class JsonRpcClientFactoryCompositeLanguageServiceTest{
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

	public <T> T create(Class<T> intf) throws MalformedURLException{
		int i = intf.getSimpleName().indexOf("Service");
		return f.create(intf, new URL(base + "Composite" + intf.getSimpleName().substring(0, i)));
	}
	
	private ClientFactory f = new JsonRpcClientFactory();
	private String base = "http://localhost:8080/jp.go.nict.langrid.webapps.mock/jsonServices/";
}
