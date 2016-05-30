package jp.go.nict.langrid.client.soap.io.parameter;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

public class ArrayEncoderTest {
	@Test
	public void test1() throws Throwable{
		Object v = new String[][]{new String[]{"hello"}};
		Assert.assertEquals(
				"soapenc:arrayType=\"xsd:string[][1]\" xsi:type=\"soapenc:Array\"",
				ArrayEncoder.getTagAttributes(v.getClass(), v));
	}

	@Test
	public void test2() throws Throwable{
		Object v = new String[][]{new String[]{}};
		Assert.assertEquals(
				"soapenc:arrayType=\"xsd:string[][0]\" xsi:type=\"soapenc:Array\"",
				ArrayEncoder.getTagAttributes(v.getClass(), v));
	}

	@Test
	public void test3() throws Throwable{
		Object v = new String[][][]{};
		Assert.assertEquals(
				"soapenc:arrayType=\"xsd:string[][][0]\" xsi:type=\"soapenc:Array\"",
				ArrayEncoder.getTagAttributes(v.getClass(), v));
	}

	@Test
	public void test4() throws Throwable{
		Object v = new BigInteger[][][]{new BigInteger[][]{new BigInteger[]{new BigInteger("1")}}};
		Assert.assertEquals(
				"soapenc:arrayType=\"ns:BigInteger[][][1]\" xsi:type=\"soapenc:Array\" xmlns:ns=\"java:java.math\"",
				ArrayEncoder.getTagAttributes(v.getClass(), v));
	}
}
