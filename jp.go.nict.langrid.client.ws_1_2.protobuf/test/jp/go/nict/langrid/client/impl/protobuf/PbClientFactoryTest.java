package jp.go.nict.langrid.client.impl.protobuf;

import java.net.URL;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;

public class PbClientFactoryTest {
	@Service(namespace="test:service:Service")
	public static interface TestService{
		String hello(
				@Parameter(name="message")
				String message);
	}

	public static void main(String[] args) throws Exception{
		System.out.println(System.currentTimeMillis());
		TestService s = new PbClientFactory().create(
				TestService.class,
				new URL("http://127.0.0.1:8080/servicetest/services/TestService")
				);
		s.hello("world");
	}
}
