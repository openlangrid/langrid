package jp.go.nict.langrid.client.ws_1_2.management;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.junit.Test;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.QoS;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntry;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceManagementService;

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

	@Test
	public void test_qos() throws Throwable{
		ServiceEntrySearchResult r = createServiceManagementClient().searchServicesWithQos(0, 10,
				new MatchingCondition[] {new MatchingCondition("serviceId", "KyotoUJServer", "COMPLETE")},
				new Order[] {}, "ALL",
				new String[] {"THROUGHPUT", "AVAILABILITY", "SUCCESS_RATE", },
				CalendarUtil.create(2018, 4, 1), CalendarUtil.create(2018, 8, 1));
		for(ServiceEntry se : r.getElements()) {
			System.out.println(se.getServiceId());
			for(QoS q : se.getQos()) {
				System.out.println("  " + JSON.encode(q));
			}
		}
		System.out.println("done");
	}
	
	private static ServiceManagementService createServiceManagementClient() {
		return createClient("ServiceManagement", ServiceManagementService.class);
	}
	private static <T> T createClient(String service, Class<T> intf){
		try {
			return new SoapClientFactory().create(intf, new URL(baseUrl + service), userId, password);
		} catch(MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private static String baseUrl;
	private static String userId;
	private static String password;
	static {
		Properties p = new Properties();
		try(InputStream is = ServiceManagementTest.class.getResourceAsStream("/langrid.properties")) {
			p.load(is);
			baseUrl = p.getProperty("langrid.management.url");
			userId = p.getProperty("langrid.management.userId");
			password = p.getProperty("langrid.management.password");
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
