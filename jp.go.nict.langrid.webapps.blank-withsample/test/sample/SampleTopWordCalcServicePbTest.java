package sample;

import java.net.URL;

import jp.go.nict.langrid.client.impl.protobuf.PbClientFactory;
import junit.framework.Assert;

import org.junit.Test;

public class SampleTopWordCalcServicePbTest {
	@Test
	public void test() throws Exception{
		TopWordCalcService service = new PbClientFactory().create(
				TopWordCalcService.class
				, new URL("http://127.0.0.1:8080/jp.go.nict.langrid.webapps.blank/pbServices/SampleTopWordCalcService")
				);
		Assert.assertEquals(
				"hello", service.calculate("hello hello hello world world")
				);
	}
}
