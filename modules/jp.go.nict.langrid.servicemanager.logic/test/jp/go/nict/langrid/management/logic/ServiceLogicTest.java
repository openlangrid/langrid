package jp.go.nict.langrid.management.logic;

import org.junit.Before;
import org.junit.Test;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.DaoFactory;

public class ServiceLogicTest {
	@Before
	public void setUp() throws Throwable{
		DaoFactory df = DaoFactory.createInstance();
	}

	@Test
	public void test_throughput() throws Throwable{
		ServiceLogic sl = new ServiceLogic(null);
		QoSResult t = sl.getThroughput("kyoto1.langrid", "KyotoUJServer",
				CalendarUtil.create(2018, 3, 1), CalendarUtil.create(2018, 5, 1));
		System.out.println(t.getValue() + ", total: " + t.getDenominator());
	}

	@Test
	public void test_successRate() throws Throwable{
		ServiceLogic sl = new ServiceLogic(null);
		QoSResult t = sl.getSuccessRate("kyoto1.langrid", "VLSPPOSTaggerService",
				CalendarUtil.create(2014, 1, 1), CalendarUtil.create(2015, 1, 1));
		System.out.println(t.getValue() + ", total: " + t.getDenominator());
	}

	@Test
	public void test_availability() throws Throwable{
		ServiceLogic sl = new ServiceLogic(null);
		QoSResult t = sl.getAvailability("kyoto1.langrid", "VLSPPOSTaggerService",
				CalendarUtil.create(2014, 1, 1), CalendarUtil.create(2015, 1, 1));
		System.out.println(t.getValue() + ", total: " + t.getDenominator());
	}
}
