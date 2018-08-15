package jp.go.nict.langrid.client.ws_1_2.management;

import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.Test;

import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.soap.SoapClientFactory;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.servicemonitor.ServiceMonitorService;
import jp.go.nict.langrid.service_1_2.foundation.typed.Period;

public class ServiceMonitorTest {
	@Test
	public void test() throws Throwable{
		SoapClientFactory f = new SoapClientFactory();
		ServiceMonitorService service = f.create(
				ServiceMonitorService.class,
				new URL("http://localhost:8080/jp.go.nict.langrid.webapps.langrid-core.no-p2p/services/ServiceMonitor")
		);
		RequestAttributes attr = (RequestAttributes)service;
		attr.setUserId("langrid");
		attr.setPassword("svhuiG6s");

		//Calendar y2018 = Calendar.getInstance().set(2018, 1, 1, 0, 0, 0);
		Calendar now = Calendar.getInstance();

		System.out.println(Arrays.toString(service.getAccessCounts("ishida.kyoto-u", "KyotoUJServer", now, "YEAR")));
	}

	@Test
	public void test2() throws Throwable{
		SoapClientFactory f = new SoapClientFactory();
		ServiceMonitorService service = f.create(
				ServiceMonitorService.class,
				new URL("http://localhost:8080/jp.go.nict.langrid.webapps.langrid-core.no-p2p/services/ServiceMonitor")
		);
		RequestAttributes attr = (RequestAttributes)service;
		attr.setUserId("langrid");
		attr.setPassword("svhuiG6s");

		//Calendar y2018 = Calendar.getInstance().set(2018, 1, 1, 0, 0, 0);
		Calendar now = Calendar.getInstance();

		System.out.println(JSON.encode(service.sumUpUserAccess(
				0, 100, "KyotoUJServer", 
				CalendarUtil.create(2018, 1, 1),
				CalendarUtil.create(2019, 1, 1),
				Period.YEAR.toString(),
				new Order[] {}), true));
	}
}
