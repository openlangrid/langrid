package jp.go.nict.langrid.client.soap.test;

import java.net.URL;

import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.client.soap.test.util.Intercept;
import jp.go.nict.langrid.client.soap.test.util.Intercepted;
import jp.go.nict.langrid.client.soap.test.util.NullIntercepted;
import jp.go.nict.langrid.commons.test.AuthFile;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntry;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceManagementService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServiceManagementTest {
	@Test
	public void test() throws Exception{
		Pair<String, String> idAndPass = AuthFile.load(getClass());
		interceptedReq = Intercept.requestXmlToFile("ServiceManagement.req.xml");
		interceptedRes = Intercept.responseXmlToFile("ServiceManagement.res.xml");
		ServiceEntrySearchResult r = new SoapClientFactory().create(
				ServiceManagementService.class,
				new URL("http://langrid.org/service_manager/services/ServiceManagement"),
				idAndPass.getFirst(),
				idAndPass.getSecond()
				).searchServices(0, 100, new MatchingCondition[]{
						new MatchingCondition("serviceType", "Translation", "COMPLETE")
				}, new Order[]{}, "ALL");
		for(ServiceEntry se : r.getElements()){
			Assert.assertTrue(se.getServiceId().length() > 0);
		}
	}

	private Intercepted interceptedReq = new NullIntercepted();
	private Intercepted interceptedRes = new NullIntercepted();

	@Before
	public void setUp() throws Exception{
		interceptedReq = new NullIntercepted();
		interceptedRes = new NullIntercepted();
	}

	@After
	public void tearDown() throws Exception{
		interceptedReq.finish();
		interceptedRes.finish();
	}
}
