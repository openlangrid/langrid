package jp.go.nict.langrid.client.soap.test;

import java.io.IOException;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.client.test.TestContext;

public class SoapTestContext extends TestContext{
	public SoapTestContext() throws IOException{
		super(new SoapClientFactory());
	}

	public SoapTestContext(Class<?> base) throws IOException{
		super(base, new SoapClientFactory());
	}
}
