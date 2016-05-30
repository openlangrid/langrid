package jp.go.nict.langrid.commons.lang;

import org.junit.Assert;
import org.junit.Test;

public class StackTraceTest {
	@Test
	public void test() throws Throwable{
		Assert.assertEquals("test", getCallerMethodName());
		Assert.assertEquals("jp.go.nict.langrid.commons.lang.StackTraceTest", getCallerClasssName());
		String fqcn = getCallerClasssName();
		Assert.assertEquals("StackTraceTest", fqcn.substring(fqcn.lastIndexOf('.') + 1).replaceAll("\\$", "_"));
		Assert.assertEquals("StackTrace_Test", "StackTrace$Test".substring("StackTrace$Test".lastIndexOf('.') + 1).replaceAll("\\$", "_"));
	}

	private String getCallerMethodName(){
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	private String getCallerClasssName(){
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}
}
