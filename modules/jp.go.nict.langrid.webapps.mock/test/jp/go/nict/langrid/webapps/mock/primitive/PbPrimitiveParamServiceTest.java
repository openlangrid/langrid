package jp.go.nict.langrid.webapps.mock.primitive;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.client.impl.protobuf.PbClientFactory;

public class PbPrimitiveParamServiceTest {
	@Test
	public void test() throws Throwable{
		PrimitiveParamService s = new PbClientFactory().create(PrimitiveParamService.class,
				new URL("http://localhost:8080/jp.go.nict.langrid.webapps.mock/pbServices/PrimitiveParamService"
						));
		Assert.assertEquals(true, s.booleanFunc(true));
		Assert.assertArrayEquals(new boolean[] {true,false}, s.booleanArrayFunc(new boolean[] {true, false}));
		Assert.assertArrayEquals(new byte[] {1, 2, (byte)128},
				s.byteArrayFunc(new byte[] {1, 2, (byte)128}));
	}
}
