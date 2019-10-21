package jp.go.nict.langrid.service.language_1_2.util;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.service_1_2.util.MorphemeUtil;

public class MorphemeUtilTest {
	@Test
	public void test() throws Throwable{
		Assert.assertEquals("こんにちは世界",
				MorphemeUtil.joinWords(new String[] {"こんにちは", "世界"}));
		Assert.assertEquals("こんにちはworld",
				MorphemeUtil.joinWords(new String[] {"こんにちは", "world"}));
		Assert.assertEquals("hello世界",
				MorphemeUtil.joinWords(new String[] {"hello", "世界"}));
		Assert.assertEquals("hello world",
				MorphemeUtil.joinWords(new String[] {"hello", "world"}));
		Assert.assertEquals("hello world.",
				MorphemeUtil.joinWords(new String[] {"hello", "world", "."}));
	}
}
