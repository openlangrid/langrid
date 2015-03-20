package jp.go.nict.langrid.webapps.composite;

import java.net.URL;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.protobufrpc.PbClientFactory;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;

import org.junit.Test;

public class CompositePbFaultTest {
	@Test
	public void test() throws Exception{
		BackTranslationService service = new PbClientFactory().create(BackTranslationService.class
				, new URL("http://localhost:8080/jp.go.nict.langrid.webapps.composite/pbServices/PbBackTranslation")
				);
		RequestAttributes req = (RequestAttributes)service;
		req.getTreeBindings().add(new BindingNode(
				"ForwardTranslationPL"
				, "http://localhost:8080/jp.go.nict.langrid.webapps.mock/pbServices/Translation?exception=InvalidParameterException"
				));
		try{
			service.backTranslate("en", "ja", "hello");
		} finally{
			ResponseAttributes res = (ResponseAttributes)service;
			System.out.println(CallTreeUtil.encodeTree(res.getCallTree(), 2));
		}
	}
}
