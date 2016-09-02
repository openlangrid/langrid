package test.java.lang;

import org.junit.Assert;
import org.junit.Test;

public class CastTest {
	@Test
	public void test() throws Throwable{
		Assert.assertTrue(((int)-1L) < 0);
		Assert.assertTrue(((int)-13283238293L) < 0);
	}
}
