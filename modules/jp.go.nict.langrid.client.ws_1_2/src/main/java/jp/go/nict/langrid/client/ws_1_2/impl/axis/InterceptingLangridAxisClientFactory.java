package jp.go.nict.langrid.client.ws_1_2.impl.axis;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.axis.AxisClientFactory;

import org.apache.axis.configuration.FileProvider;

public class InterceptingLangridAxisClientFactory {
	public static ClientFactory getInstance(){
		return factory;
	}

	public static void setUp(AxisClientFactory f){
		LangridAxisClientFactory.setUp(f);
	}

	private static ClientFactory factory;
	static{
		AxisClientFactory f = new AxisClientFactory(
				new FileProvider("/jp/go/nict/langrid/client/ws_1_2/impl/axis/client-config-interceptor.wsdd"));
		setUp(f);
		factory = f;
	}
}