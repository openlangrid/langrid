/*
 * This is the sample test code for services.
 * Please write your own test code as follows:
 * 1. create TestContext_url file and write the url to your service grid invoker. i.e. http://langrid.org/service_manager/invoker/
 * 2. create TestContext_auth file and write id and password for your service grid.
 * 3. write a test code in test method below.
 * 4. run it!
 */
package jp.go.nict.langrid.servicetests;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.client.test.TestContext;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

/**
 * This is the sample
 */
public class TranslationServiceTest {
	@Test
	public void test() throws Throwable{
		Assert.assertTrue(service().translate("en", "ja", "hello").length() > 0);
	}

	private TranslationService service() throws IOException{
		return context().createClient("ServiceId", TranslationService.class);
	}

	private TestContext context() throws IOException{
		return new TestContext(new SoapClientFactory());
	}
}
