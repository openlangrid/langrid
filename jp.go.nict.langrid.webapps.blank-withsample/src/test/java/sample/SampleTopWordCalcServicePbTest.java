package sample;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.client.protobufrpc.PbClientFactory;

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
