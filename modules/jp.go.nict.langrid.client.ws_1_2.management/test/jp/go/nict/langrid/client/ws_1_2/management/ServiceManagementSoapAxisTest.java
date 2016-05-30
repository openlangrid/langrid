package jp.go.nict.langrid.client.ws_1_2.management;

import java.net.URL;

import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntry;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.typed.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.typed.Order;
import jp.go.nict.langrid.service_1_2.foundation.typed.Scope;

import org.junit.Test;

public class ServiceManagementSoapAxisTest {
	@Test
	public void test() throws Exception{
		ClientFactory.setDefaultUserId("user1");
		ClientFactory.setDefaultPassword("user1passwd");
		ServiceManagementClient s = ClientFactory.createServiceManagementClient(
				new URL("http://127.0.0.1:8080/jp.go.nict.langrid.webapps.langrid-core-withoutp2p/soapServices/ServiceManagement")
				);
		ServiceEntrySearchResult result = s.searchServices(0, 10, new MatchingCondition[]{}, new Order[]{}, Scope.ALL);
		for(ServiceEntry se : result.getElements()){
			System.out.println(JSON.encode(se));
		}
	}
}
