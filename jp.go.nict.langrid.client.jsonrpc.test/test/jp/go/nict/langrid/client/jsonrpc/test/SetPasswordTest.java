package jp.go.nict.langrid.client.jsonrpc.test;

import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.service_1_2.adjacencypair.AdjacencyPairService;

import org.junit.Test;

public class SetPasswordTest{
	@Test
	public void test_setPassword() throws Exception{
		AdjacencyPairService s = create(AdjacencyPairService.class);
		RequestAttributes r = (RequestAttributes)s;
		r.setPassword("pass");
	}

	private <T> T create(Class<T> intf) throws MalformedURLException{
		int i = intf.getSimpleName().indexOf("Service");
		return f.create(intf, new URL(base + intf.getSimpleName().substring(0, i)));
	}

	private ClientFactory f = new JsonRpcClientFactory();
	private String base = "http://127.0.0.1:118080/jp.go.nict.langrid.webapps.mock/jsServices/";
}
