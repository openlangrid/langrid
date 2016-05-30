package jp.go.nict.langrid.servicecontainer.executor.umbrella;

import jp.go.nict.langrid.commons.ws.Protocols;

import org.junit.Assert;
import org.junit.Test;

public class UmbrellaComponentServiceFactoryTest {
	@Test
	public void test_default() throws Exception{
		Assert.assertEquals(
				"jp.go.nict.langrid.servicecontainer.executor.axis.AxisComponentServiceFactory",
				new UmbrellaComponentServiceFactory().getFactory("--DEFAULT--").getClass().getName()
				);
	}

	@Test
	public void test_axis() throws Exception{
		Assert.assertEquals(
				"jp.go.nict.langrid.servicecontainer.executor.axis.AxisComponentServiceFactory",
				new UmbrellaComponentServiceFactory().getFactory(Protocols.SOAP_RPCENCODED).getClass().getName()
				);
	}

	@Test
	public void test_protobuf() throws Exception{
		Assert.assertEquals(
				"jp.go.nict.langrid.servicecontainer.executor.protobufrpc.PbComponentServiceFactory",
				new UmbrellaComponentServiceFactory().getFactory(Protocols.PROTOBUF_RPC).getClass().getName()
				);
	}

	@Test
	public void test_json() throws Exception{
		Assert.assertEquals(
				"jp.go.nict.langrid.servicecontainer.executor.jsonrpc.JsonRpcComponentServiceFactory",
				new UmbrellaComponentServiceFactory().getFactory(Protocols.JSON_RPC).getClass().getName()
				);
	}
}
