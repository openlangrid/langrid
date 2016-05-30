package jp.go.nict.langrid.client.soap.io;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;

import org.junit.Assert;
import org.junit.Test;

public class SoapRequestWriterTest {
	public void func(int a){}
	@Test
	public void test() throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		List<RpcHeader> headers = Arrays.asList(new RpcHeader("header1", "name1", "value1"));
		SoapRequestWriter.writeSoapRequest(
				baos, headers, this.getClass().getMethod("func", int.class), new Object[]{100});
		Assert.assertEquals(
				StreamUtil.readAsString(this.getClass().getResourceAsStream("/SoapRequestWriterTest_test01.txt"), "UTF-8"),
				new String(baos.toByteArray()).replaceAll("\r\n", "\n"));
	}
}
