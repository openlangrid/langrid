package jp.go.nict.langrid.composite.commons.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.parameter.ParameterRequiredException;
import jp.go.nict.langrid.commons.parameter.PropertiesParameterContext;
import jp.go.nict.langrid.commons.parameter.annotation.Parameter;

public class AbstractCompositeServiceTest {
	protected <T> T createLangridServiceClient(Class<T> clazz, String serviceId) {
		try {
			return new SoapClientFactory().create(clazz, new URL(langridUrl, serviceId), langridId, langridPass);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Parameter
	private static URL langridUrl;
	@Parameter
	private static String langridId;
	@Parameter
	private static String langridPass;
	static {
		try {
			new PropertiesParameterContext(
					AbstractCompositeServiceTest.class.getResource("/langrid.properties")
					).load(AbstractCompositeServiceTest.class);
		} catch (ParameterRequiredException | IOException e) {
			e.printStackTrace();
		}
	}

}
