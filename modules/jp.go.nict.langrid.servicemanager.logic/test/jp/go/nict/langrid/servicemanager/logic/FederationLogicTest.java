package jp.go.nict.langrid.servicemanager.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.FederationLogic;

public class FederationLogicTest {
	@Before
	public void setUp() throws Throwable{
		DaoFactory df = DaoFactory.createInstance();
		fdao = df.createFederationDao();
		fdao.clear();
		gdao = df.createGridDao();
		gdao.clear();
	}

	@Test
	public void test_isReachable() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(newFederation("grid1", "grid2"));
		Assert.assertTrue(fl.isReachable("grid1", "grid2"));
	}

	@Test
	public void test_isReachable_false() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(new Federation("grid1", "grid2"));
		Assert.assertFalse(fl.isReachable("grid1", "grid2"));
	}

	@Test
	public void test_getNearestFederation_1hop() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(newFederation("grid1", "grid2"));
		Assert.assertEquals("grid2", fl.getShortestPath("grid1", "grid2").get(0).getTargetGridId());
	}

	@Test
	public void test_isReachable_2hop() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid3"));
		Assert.assertTrue(fl.isReachable("grid1", "grid3"));
	}

	@Test
	public void test_isReachable_2hop_false() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(new Federation("grid2", "grid3"));
		Assert.assertFalse(fl.isReachable("grid1", "grid3"));
	}

	@Test
	public void test_isReachable_2hop_unreachable() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid3", true, false));
		Assert.assertFalse(fl.isReachable("grid1", "grid3"));
	}

	@Test
	public void test_getNearestFederation() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid3"));
		fdao.addFederation(newFederation("grid3", "grid4"));
		fdao.addFederation(newFederation("grid1", "grid3"));
		Federation f = fl.getShortestPath("grid1", "grid4").get(0);
		Assert.assertNotNull(f);
		Assert.assertEquals("grid3", f.getTargetGridId());
	}

	@Test
	public void test_getNearestFederation_2() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid3"));
		fdao.addFederation(newFederation("grid3", "grid4"));
		fdao.addFederation(newFederation("grid4", "grid5"));
		fdao.addFederation(newFederation("grid5", "grid6"));
		fdao.addFederation(newFederation("grid6", "grid11"));
		fdao.addFederation(newFederation("grid1", "grid7"));
		fdao.addFederation(newFederation("grid7", "grid8"));
		fdao.addFederation(newFederation("grid8", "grid4"));
		fdao.addFederation(newFederation("grid8", "grid9"));
		fdao.addFederation(newFederation("grid8", "grid10"));
		fdao.addFederation(newFederation("grid9", "grid11"));
		fdao.addFederation(newFederation("grid10", "grid11"));
		Federation f = fl.getShortestPath("grid1", "grid11").get(0);
		Assert.assertNotNull(f);
		Assert.assertEquals("grid7", f.getTargetGridId());
	}

	@Test
	public void test_getNearestFederation_unreachable() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid1"));
		fdao.addFederation(newFederation("grid3", "grid4"));
		FederationLogic fl = new FederationLogic();
		Assert.assertEquals(0, fl.getShortestPath("grid1", "grid5").size());
	}

	@Test
	public void test_getNearestFederation_unreachable_2() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid1"));
		fdao.addFederation(newFederation("grid2", "grid3"));
		fdao.addFederation(newFederation("grid3", "grid2"));
		fdao.addFederation(newFederation("grid3", "grid4"));
		FederationLogic fl = new FederationLogic();
		Assert.assertEquals(0, fl.getShortestPath("grid1", "grid5").size());
	}

	@Test
	public void test_getNearestFederation_unreachable_3() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid1"));
		fdao.addFederation(newFederation("grid2", "grid3"));
		fdao.addFederation(newFederation("grid3", "grid2"));
		fdao.addFederation(newFederation("grid3", "grid4"));
		FederationLogic fl = new FederationLogic();
		Assert.assertEquals(0, fl.getShortestPath("grid5", "grid1").size());
		Assert.assertEquals(0, fl.getShortestPath("grid0", "grid4").size());
	}

	private Federation newFederation(String sgid, String tgid){
		return newFederation(sgid, tgid, true, true);
	}

	private Federation newFederation(String sgid, String tgid, boolean symmetric, boolean transitive){
		Federation f = new Federation(sgid, tgid);
		f.setConnected(true);
		f.setRequesting(false);
		f.setSymmetric(symmetric);
		f.setTargetTransitive(transitive);
		if(symmetric) f.setSourceTransitive(transitive);
		return f;
	}

	private FederationDao fdao;
	private GridDao gdao;
}
