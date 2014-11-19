package jp.go.nict.langrid.client.soap.io;

import java.util.Arrays;

import jp.go.nict.langrid.commons.beanutils.Converter;

import org.junit.Test;

public class SoapResponseParserTest {
	@Test
	public void test_dotnet() throws Throwable{
		String[] ret = SoapResponseParser.parseSoapResponse(
				String[].class, "getSupportedLanguages",
				getClass().getResourceAsStream("/SoapResponseParserTest_test01.txt"),
				new Converter()).getThird();
		System.out.println(Arrays.toString(ret));
	}
}
