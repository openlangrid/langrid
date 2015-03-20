package jp.go.nict.langrid.client.soap.io;

import java.util.Arrays;
import java.util.Collection;

import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import org.junit.Assert;

import org.junit.Test;

public class SoapResponseParserTest {
	@Test
	public void test01_dotnet() throws Throwable{
		String[] ret = SoapResponseParser.parseSoapResponse(
				String[].class, "getSupportedLanguages",
				getClass().getResourceAsStream("/SoapResponseParserTest_test01.txt"),
				new Converter()).getThird();
		Assert.assertEquals("[vi]", Arrays.toString(ret));
	}

	@Test
	public void test02_php() throws Throwable{
		String[] ret = SoapResponseParser.parseSoapResponse(
				String[].class, "translate",
				getClass().getResourceAsStream("/SoapResponseParserTest_test02_php.txt"),
				new Converter()).getThird();
		System.out.println(Arrays.toString(ret));
	}

	public static class MultihopTranslationResult{
		public String[] getIntermediates() {
			return intermediates;
		}
		public void setIntermediates(String[] intermediates){
			this.intermediates = intermediates;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target){
			this.target = target;
		}
		private String[] intermediates;
		private String target;
	}
	@Test
	public void test_MultihopTranslationResult() throws Throwable{
		Trio<Collection<RpcHeader>, RpcFault, MultihopTranslationResult> r = SoapResponseParser.parseSoapResponse(
				MultihopTranslationResult.class, "multihopTranslate",
				getClass().getResourceAsStream("/MultihopTranslationResult.xml"),
				new Converter());
		System.out.println(JSON.encode(r.getThird()));
	}
}
