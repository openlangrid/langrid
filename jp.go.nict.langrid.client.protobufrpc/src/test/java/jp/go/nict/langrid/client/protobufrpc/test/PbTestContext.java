package jp.go.nict.langrid.client.protobufrpc.test;

import java.io.IOException;

import jp.go.nict.langrid.client.protobufrpc.PbClientFactory;
import jp.go.nict.langrid.client.test.TestContext;

public class PbTestContext extends TestContext{
	public PbTestContext() throws IOException{
		super(new PbClientFactory());
	}

	public PbTestContext(Class<?> base) throws IOException{
		super(base, new PbClientFactory());
	}
}
