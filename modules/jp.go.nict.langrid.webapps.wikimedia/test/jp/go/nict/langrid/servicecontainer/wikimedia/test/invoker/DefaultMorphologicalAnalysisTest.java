package jp.go.nict.langrid.servicecontainer.wikimedia.test.invoker;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.client.protobufrpc.PbClientFactory;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;

import org.junit.Assert;
import org.junit.Test;

public class DefaultMorphologicalAnalysisTest {
	@Test
	public void test_js() throws Exception{
		MorphologicalAnalysisService service = new JsonRpcClientFactory().create(
				MorphologicalAnalysisService.class, getUrl()
				);
		callAndTest(service);
	}

	@Test
	public void test_soap() throws Exception{
		MorphologicalAnalysisService service = new SoapClientFactory().create(
				MorphologicalAnalysisService.class, getUrl()
				);
		callAndTest(service);
	}

	@Test
	public void test_pb() throws Exception{
		MorphologicalAnalysisService service = new PbClientFactory().create(
				MorphologicalAnalysisService.class, getUrl()
				);
		callAndTest(service);
	}

	private void callAndTest(MorphologicalAnalysisService service) throws Exception{
		Queue<String> q = getExpects();
		for(Morpheme m : service.analyze("en", "hello world.")){
			Assert.assertEquals(q.poll(), m.getWord());
		}
	}

	private URL getUrl() throws Exception{
		return new URL("http://localhost:8080/jp.go.nict.langrid.webapps.wikimedia/invoker/DefaultMorphologicalAnalysis");
	}
	private Queue<String> getExpects(){
		return new ArrayDeque<String>(Arrays.asList("hello", "world", "."));
	}
}
