package jp.go.nict.langrid.webapps.jsontest;

import java.net.URL;

import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;

public class TestTest {
	public static void main(String[] args) throws Exception{
		new JsonRpcClientFactory().create(
				TestService.class, new URL("http://127.0.0.1:8080/jp.go.nict.langrid.webapps.jsontest/jsServices/Test")
				).func2("hello+world".getBytes("UTF-8"));
	}
}
