package jp.go.nict.langrid.webapps.mock.wrapper;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.client.impl.protobuf.PbClientFactory;

public class PbWrapperParamServiceTest {
	@Test
	public void test() throws Throwable{
		WrapperParamService s = new PbClientFactory().create(WrapperParamService.class,
				new URL("http://localhost:8080/jp.go.nict.langrid.webapps.mock/pbServices/WrapperParamService"
						));
		Assert.assertEquals(true, s.booleanFunc(true));
		Assert.assertArrayEquals(new Boolean[] {true,false}, s.booleanArrayFunc(new Boolean[] {true, false}));
		Assert.assertArrayEquals(new Byte[] {1, 2, (byte)128},
				s.byteArrayFunc(new Byte[] {1, 2, (byte)128}));
		Assert.assertEquals((Character)(char)0xff, s.charFunc((char)0xff));
		Assert.assertArrayEquals(new Character[] {'A', 'あ'}, s.charArrayFunc(new Character[] {'A', 'あ'}));

	}
}
