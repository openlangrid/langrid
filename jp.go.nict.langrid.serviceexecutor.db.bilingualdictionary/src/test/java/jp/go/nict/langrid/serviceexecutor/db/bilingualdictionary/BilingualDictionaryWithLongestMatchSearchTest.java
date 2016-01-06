package jp.go.nict.langrid.serviceexecutor.db.bilingualdictionary;

import java.io.InputStream;

import org.junit.Test;

import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.serviceexecutor.db.ConnectionParameters;

public class BilingualDictionaryWithLongestMatchSearchTest {
	@Test
	public void test_search() throws Throwable{
		BilingualDictionaryWithLongestMatchSearch s = new BilingualDictionaryWithLongestMatchSearch();
		InputStream is = BilingualDictionaryWithLongestMatchSearchTest.class.getResourceAsStream("ConnectionParameters.json");
		try{
			s.setConnectionParameters(JSON.decode(is, ConnectionParameters.class));
		} finally{
			is.close();
		}
		s.setTableName("xlel_21_dict");
		System.out.println(JSON.encode(s.search("en", "zh", "bank", "PARTIAL"), true));
	}
}
