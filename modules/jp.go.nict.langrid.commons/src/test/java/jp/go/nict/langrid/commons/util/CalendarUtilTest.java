package jp.go.nict.langrid.commons.util;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class CalendarUtilTest {
	@Test
	public void test_formatToW3CDTF_1() throws Exception{
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		c.set(Calendar.YEAR, 2000);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 20);
		c.set(Calendar.HOUR_OF_DAY, 13);
		c.set(Calendar.MINUTE, 33);
		c.set(Calendar.SECOND, 48);
		c.set(Calendar.MILLISECOND, 89);
		Assert.assertEquals("2000-01-20T13:33:48.89+09:00", CalendarUtil.formatToW3CDTF(c));
	}

	@Test
	public void test_formatToW3CDTF_2() throws Exception{
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		c.set(Calendar.YEAR, 2000);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 20);
		c.set(Calendar.HOUR_OF_DAY, 13);
		c.set(Calendar.MINUTE, 33);
		c.set(Calendar.SECOND, 48);
		c.set(Calendar.MILLISECOND, 89);
		Assert.assertEquals("2000-01-20T13:33:48.89Z", CalendarUtil.formatToW3CDTF(c));
	}
}
