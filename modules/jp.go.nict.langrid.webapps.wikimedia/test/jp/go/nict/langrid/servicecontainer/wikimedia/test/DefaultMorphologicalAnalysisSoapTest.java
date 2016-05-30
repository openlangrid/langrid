package jp.go.nict.langrid.servicecontainer.wikimedia.test;

import java.net.URL;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;

import org.junit.Test;

public class DefaultMorphologicalAnalysisSoapTest {
	@Test
	public void test() throws Exception{
		MorphologicalAnalysisService service = new SoapClientFactory().create(
				MorphologicalAnalysisService.class,
				new URL("http://localhost:8080/jp.go.nict.langrid.webapps.wikimedia/services/DefaultMorphologicalAnalysis")
				);
		service.analyze("en", "hello world.");
	}
}
