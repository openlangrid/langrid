package jp.go.nict.langrid.commons.beanutils;

import java.util.Calendar;

import org.junit.Test;

public class ConverterForJsonRpcTest {
	@Test
	public void test() throws Throwable{
		new ConverterForJsonRpc().convert(
				"2010/07/26 17:38:15.265 JST",
				Calendar.class);
	}
}
