package jp.go.nict.langrid.commons.protobufrpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufParser;
import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufWriter;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;

public class ProtobufWriterTest {
	interface FloatFunc{
		float[] f(float[] args);
	}
	@Test
	public void test_floatarray() throws Throwable{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CodedOutputStream cos = CodedOutputStream.newInstance(baos);
		ProtobufWriter.writeRpcRequest(cos, Collections.emptyList(),
				FloatFunc.class.getMethod("f", float[].class),
				new Object[]{new float[]{1.1f, 2.2f}});
		cos.flush();
		Pair<Collection<RpcHeader>, Object[]> req = ProtobufParser.parseRpcRequest(
				CodedInputStream.newInstance(new ByteArrayInputStream(baos.toByteArray())),
				new Class<?>[]{float[].class});
		Assert.assertArrayEquals(new float[]{1.1f, 2.2f}, (float[])req.getSecond()[0], 0.01f);
	}

}
