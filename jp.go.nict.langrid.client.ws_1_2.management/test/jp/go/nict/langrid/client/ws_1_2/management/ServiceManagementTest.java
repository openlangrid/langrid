package jp.go.nict.langrid.client.ws_1_2.management;

import java.net.URL;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntry;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceManagementService;

import org.junit.Test;

public class ServiceManagementTest {
	@Test
	public void test() throws Exception{
		SoapClientFactory f = new SoapClientFactory();
		ServiceManagementService s = f.create(
				ServiceManagementService.class,
				new URL("http://langrid.org/service_manager/services/ServiceManagement")
				);
		RequestAttributes attr = (RequestAttributes)s;
		attr.setUserId("userId");
		attr.setPassword("password");
		ServiceEntrySearchResult result = s.searchServices(0, 10, new MatchingCondition[]{}, new Order[]{}, "ALL");
		for(ServiceEntry se : result.getElements()){
			System.out.println(JSON.encode(se));
		}
	}
}
