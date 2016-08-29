package jp.go.nict.langrid.p2pgridbasis.data.langrid.converter;

import org.junit.Test;

import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class ConvertUtilTest {
	@Test
	public void test() throws Throwable{
		DataAttributes das = ConvertUtil.encode(new Grid("grid1", "user1"));
		for(String k : das.getKeys()){
			System.out.println(k + ": " + das.getValue(k));
		}
		Grid g2 = new Grid();
		ConvertUtil.decode(das, g2);
		System.out.println(JSON.encode(g2, true));
	}
}
