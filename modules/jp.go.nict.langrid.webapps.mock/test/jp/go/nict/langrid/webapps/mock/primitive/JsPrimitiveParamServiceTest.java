package jp.go.nict.langrid.webapps.mock.primitive;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;

public class JsPrimitiveParamServiceTest {
	@Test
	public void test() throws Throwable{
		PrimitiveParamService s = new JsonRpcClientFactory().create(PrimitiveParamService.class,
				new URL("http://localhost:8080/jp.go.nict.langrid.webapps.mock/jsServices/PrimitiveParamService"
						));
		Assert.assertEquals(true, s.booleanFunc(true));
		Assert.assertArrayEquals(new boolean[] {true,false}, s.booleanArrayFunc(new boolean[] {true, false}));
		Assert.assertArrayEquals(new byte[] {1, 2, (byte)128},
				s.byteArrayFunc(new byte[] {1, 2, (byte)128}));
		Assert.assertEquals((char)0xff, s.charFunc((char)0xff));
		Assert.assertArrayEquals(new char[] {'A', 'あ'}, s.charArrayFunc(new char[] {'A', 'あ'}));
	}
}
