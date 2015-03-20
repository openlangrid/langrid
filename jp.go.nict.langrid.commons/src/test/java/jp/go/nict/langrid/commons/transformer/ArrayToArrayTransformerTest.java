package jp.go.nict.langrid.commons.transformer;

import org.junit.Assert;
import org.junit.Test;

public class ArrayToArrayTransformerTest {
	@Test
	public void test() throws Exception{
		String[] actual = new ArrayToArrayTransformer<Integer, String>(
				new ToStringTransformer<Integer>()).transform(new Integer[]{1, 2, 3});
		Assert.assertEquals("1", actual[0]);
		Assert.assertEquals("2", actual[1]);
		Assert.assertEquals("3", actual[2]);
	}
}
