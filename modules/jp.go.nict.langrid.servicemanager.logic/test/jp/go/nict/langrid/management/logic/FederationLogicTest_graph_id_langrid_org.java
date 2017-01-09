package jp.go.nict.langrid.management.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jp.go.nict.langrid.management.logic.FederationLogic;
import jp.go.nict.langrid.management.logic.federation.FederationGraph;

public class FederationLogicTest_graph_id_langrid_org {
	@Before
	public void setUp() throws Throwable{
		System.setProperty("jp.go.nict.langrid.dao.hibernate.LangridSessionFactory.hibernate.cfg",
				"cfg/id.langrid.org.kyotooplg.hibernate.cfg.xml");
		fg = new FederationLogic().buildGraph();
	}

	@Test
	public void test_isReachable_kyotooplg_usa1openlangrid() throws Throwable{
		Assert.assertTrue(fg.isReachable("kyotooplg", "usa1.openlangrid"));
	}

	@Test
	public void test_isReachable_kyotooplg_kyoto0langrid() throws Throwable{
		Assert.assertTrue(fg.isReachable("kyotooplg", "kyoto0.langrid"));
	}

	@Test
	public void test_isReachable_kyotooplg_anc() throws Throwable{
		Assert.assertTrue(fg.isReachable("kyotooplg", "anc"));
	}

	@Test
	public void test_getShortestPath_kyotooplg_anc() throws Throwable{
		Assert.assertEquals(3, fg.getShortestPath("kyotooplg", "anc").size());
	}

	@Test
	public void test_listAllReachableGridIds() throws Throwable{
		Set<String> expected = new HashSet<>(Arrays.asList("usa1.openlangrid", "kyoto0.langrid", "anc"));
		for(String g : fg.listAllReachableGridIds("kyotooplg")){
			Assert.assertTrue(g, expected.remove(g));
		}
	}

	private FederationGraph fg;
}
