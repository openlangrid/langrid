package jp.go.nict.langrid.servicecontainer.handler.protobufrpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufParser;
import jp.go.nict.langrid.commons.protobufrpc.io.ProtobufWriter;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;

public class ProtoBufDynamicHandlerTest {
	interface BooleanFunc{
		boolean f(boolean args);
	}
	@Test
	public void test_boolean() throws Throwable{
		testIncludeReturnValueClass(BooleanFunc.class, v -> v, true, boolean.class);
	}

	interface CharFunc{
		char f(char args);
	}
	@Test
	public void test_char() throws Throwable{
		testIncludeReturnValueClass(CharFunc.class, v -> v, 'A', char.class);
	}

	interface ByteFunc{
		byte f(byte args);
	}
	@Test
	public void test_byte() throws Throwable{
		testIncludeReturnValueClass(ByteFunc.class, v -> v, (byte)0x8f, byte.class);
	}

	interface ShortFunc{
		short f(short args);
	}
	@Test
	public void test_short() throws Throwable{
		testIncludeReturnValueClass(ShortFunc.class, v -> v, (short)140, short.class);
	}

	interface IntFunc{
		int f(int args);
	}
	@Test
	public void test_int() throws Throwable{
		testIncludeReturnValueClass(IntFunc.class, v -> v, 32342, int.class);
	}

	interface LongFunc{
		long f(long args);
	}
	@Test
	public void test_long() throws Throwable{
		testIncludeReturnValueClass(LongFunc.class, v -> v, 223991347L, long.class);
	}

	interface FloatFunc{
		float f(float args);
	}
	@Test
	public void test_float() throws Throwable{
		testIncludeReturnValueClass(FloatFunc.class, v -> v, 2344.23f, float.class);
	}

	interface DoubleFunc{
		double f(double args);
	}
	@Test
	public void test_double() throws Throwable{
		testIncludeReturnValueClass(DoubleFunc.class, v -> v, 4392.43987, double.class);
	}

	interface BooleanWFunc{
		Boolean f(Boolean args);
	}
	@Test
	public void test_booleanw() throws Throwable{
		test(BooleanWFunc.class, v -> v, true);
	}

	interface CharacterWFunc{
		Character f(Character args);
	}
	@Test
	public void test_Character() throws Throwable{
		test(CharacterWFunc.class, v -> v, 'A');
	}

	interface ByteWFunc{
		Byte f(Byte args);
	}
	@Test
	public void test_Byte() throws Throwable{
		test(ByteWFunc.class, v -> v, (byte)0x8f);
	}

	interface ShortWFunc{
		Short f(Short args);
	}
	@Test
	public void test_Short() throws Throwable{
		test(ShortWFunc.class, v -> v, (short)140);
	}

	interface IntegerWFunc{
		Integer f(Integer args);
	}
	@Test
	public void test_Integer() throws Throwable{
		test(IntegerWFunc.class, v -> v, 32342);
	}

	interface LongWFunc{
		Long f(Long args);
	}
	@Test
	public void test_Long() throws Throwable{
		test(LongWFunc.class, v -> v, 223991347L);
	}

	interface FloatWFunc{
		Float f(Float args);
	}
	@Test
	public void test_Float() throws Throwable{
		test(FloatWFunc.class, v -> v, 2344.23f);
	}

	interface DoubleWFunc{
		Double f(Double args);
	}
	@Test
	public void test_Double() throws Throwable{
		test(DoubleWFunc.class, v -> v, 4392.43987);
	}

	interface BooleanArrayFunc{
		boolean[] f(boolean[] args);
	}
	@Test
	public void test_booleanArray() throws Throwable{
		test(BooleanArrayFunc.class, v -> v, new boolean[]{true, false, true});
	}

	interface CharArrayFunc{
		char[] f(char[] args);
	}
	@Test
	public void test_charArray() throws Throwable{
		test(CharArrayFunc.class, v -> v, new char[]{'a', 'b'});
	}

	interface ByteArrayFunc{
		byte[] f(byte[] args);
	}
	@Test
	public void test_byteArray() throws Throwable{
		test(ByteArrayFunc.class, v -> v, new byte[]{0x1a, (byte)0x9f});
	}

	interface ShortArrayFunc{
		short[] f(short[] args);
	}
	@Test
	public void test_shortArray() throws Throwable{
		test(ShortArrayFunc.class, v -> v, new short[]{1, 32767});
	}

	interface IntArrayFunc{
		int[] f(int[] args);
	}
	@Test
	public void test_intArray() throws Throwable{
		test(IntArrayFunc.class, v -> v, new int[]{423, 4342342});
	}

	interface LongArrayFunc{
		long[] f(long[] args);
	}
	@Test
	public void test_longArray() throws Throwable{
		test(LongArrayFunc.class, v -> v, new long[]{2342342334L, 2324});
	}

	interface FloatArrayFunc{
		float[] f(float[] args);
	}
	@Test
	public void test_floatArray() throws Throwable{
		test(FloatArrayFunc.class, v -> v, new float[]{1.1f, 2.2f});
	}

	interface DoubleArrayFunc{
		double[] f(double[] args);
	}
	@Test
	public void test_doubleArray() throws Throwable{
		test(DoubleArrayFunc.class, v -> v, new double[]{1.1f, 2.2f});
	}

	interface CharacterArrayFunc{
		Character[] f(Character[] args);
	}
	@Test
	public void test_characterArray() throws Throwable{
		test(CharacterArrayFunc.class, v -> v, new Character[]{'a', 'b'});
	}


	private <T> void testIncludeReturnValueClass(Class<T> funcClass, T func, Object value, Class<?> valueClass)
	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
	SecurityException, IOException, IllegalArgumentException, InstantiationException{
		Method m = doTest(funcClass, func, value, valueClass);
		Assert.assertEquals(valueClass, m.getReturnType());
	}

	private <T> void test(Class<T> funcClass, T func, Object value)
	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
	SecurityException, IOException, IllegalArgumentException, InstantiationException{
		doTest(funcClass, func, value, value.getClass());
	}

	
	private <T> Method doTest(Class<T> funcClass, T func, Object value, Class<?> valueClass)
	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
	SecurityException, IOException, IllegalArgumentException, InstantiationException{
		Method m = null;
		Object ret = null;
		{	ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CodedOutputStream cos = CodedOutputStream.newInstance(baos);
			m = funcClass.getMethod("f", valueClass);
			ProtobufWriter.writeRpcRequest(cos, Collections.emptyList(),
					m, value);
			cos.flush();
			CodedInputStream cis = CodedInputStream.newInstance(new ByteArrayInputStream(baos.toByteArray()));
			cis.readString();
			Pair<Collection<RpcHeader>, Object[]> req = ProtobufParser.parseRpcRequest(
					cis, new Class<?>[]{valueClass});
			ret = m.invoke(func, req.getSecond());
		}
		Assert.assertNotNull(ret);
		{	ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CodedOutputStream cos = CodedOutputStream.newInstance(baos);
			ProtobufWriter.writeSuccessRpcResponse(cos, Collections.emptyList(), ret);
			cos.flush();
			CodedInputStream cis = CodedInputStream.newInstance(baos.toByteArray());
			Trio<Collection<RpcHeader>, RpcFault, ?> r = ProtobufParser.parseRpcResponse(cis, m.getReturnType());
			ret = r.getThird();
		}
		if(value.getClass().isArray()){
			Assert.assertTrue("not array", ret.getClass().isArray());
			Assert.assertEquals("differenct element type",
					value.getClass().getComponentType(),
					ret.getClass().getComponentType());
			int n = Array.getLength(value);
			Assert.assertEquals("different array length", n, Array.getLength(ret));
			for(int i = 0; i < n; i++){
				Assert.assertEquals(i + "th value", Array.get(value, i), Array.get(ret, i));
			}
		} else{
			Assert.assertEquals(value, ret);
		}
		return m;
	}
}
