package jp.go.nict.langrid.servicecontainer.decorator.cache;

import org.junit.Assert;
import org.junit.Test;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class OSCacheTest {
	@Test
	public void test() throws Exception{
		Cache c = new Cache(true, false, false);
		c.putInCache("key", "value");
		long s = System.currentTimeMillis();
		while((System.currentTimeMillis() - s) < 12000){
			Thread.sleep(500);
			long d = System.currentTimeMillis() - s;
			try{
				Object v = c.getFromCache("key", 5);
				Assert.assertNotNull(v);
			} catch(NeedsRefreshException e){
				Assert.assertTrue(d > 5000);
			}
		}
	}
}
