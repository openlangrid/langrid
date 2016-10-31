package jp.go.nict.langrid.servicemanager.logic.federation;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.FederationLogic;
import jp.go.nict.langrid.management.logic.federation.FederationGraph;

public class FederationGraphTest {
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
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid3", true, true));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertTrue(fg.isReachable("grid1", "grid3"));
	}

	@Test
	public void test_isReachable_false() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid3", true, false));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertFalse(fg.isReachable("grid1", "grid3"));
	}

	@Test
	public void test_getShortestPath() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", false, true));
		fdao.addFederation(newFederation("grid2", "grid3", false, true));
		fdao.addFederation(newFederation("grid3", "grid4", false, true));
		fdao.addFederation(newFederation("grid1", "grid3", false, true));
		FederationGraph fg = new FederationLogic().buildGraph();
		List<Federation> p = fg.getShortestPath("grid1", "grid4");
		Assert.assertEquals(2, p.size());
		Assert.assertEquals("grid3", p.get(0).getTargetGridId());
	}

	@Test
	public void test_getShortestPath_2() throws Throwable{
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
		FederationGraph fg = new FederationLogic().buildGraph();
		List<Federation> path = fg.getShortestPath("grid1", "grid11");
		Assert.assertEquals(4, path.size());
		Assert.assertEquals("grid7", path.get(0).getTargetGridId());
	}

	@Test
	public void test_getShortestPath_symmetric() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", false, true));
		fdao.addFederation(newFederation("grid3", "grid2", true, true));
		fdao.addFederation(newFederation("grid3", "grid4", false, true));
		FederationGraph fg = new FederationLogic().buildGraph();
		List<Federation> p = fg.getShortestPath("grid1", "grid4");
		Assert.assertEquals(3, p.size());
		Assert.assertEquals("grid1", p.get(0).getSourceGridId());
		Assert.assertEquals("grid2", p.get(0).getTargetGridId());
		Assert.assertTrue(p.get(1).isSymmetric());
		Assert.assertEquals("grid3", p.get(1).getSourceGridId());
		Assert.assertEquals("grid2", p.get(1).getTargetGridId());
		Assert.assertEquals("grid3", p.get(2).getSourceGridId());
		Assert.assertEquals("grid4", p.get(2).getTargetGridId());
	}

	@Test
	public void test_getShortestPath_null() throws Throwable{
		fdao.addFederation(newRequestingFederation("grid1", "grid2", true, true));
		fdao.addFederation(newRequestingFederation("grid2", "grid3", true, true));
		fdao.addFederation(newRequestingFederation("grid3", "grid4", true, true));
		fdao.addFederation(newRequestingFederation("grid1", "grid3", true, true));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid4").size());
	}

	@Test
	public void test_getShortestPath_unreachable() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", true));
		fdao.addFederation(newFederation("grid3", "grid4"));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid5").size());
	}

	@Test
	public void test_getShortestPath_unreachable_2() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", true));
		fdao.addFederation(newFederation("grid2", "grid3", true));
		fdao.addFederation(newFederation("grid3", "grid4"));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid5").size());
	}

	@Test
	public void test_getShortestPath_unreachable_3() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", true));
		fdao.addFederation(newFederation("grid2", "grid3", true));
		fdao.addFederation(newFederation("grid3", "grid4"));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(0, fg.getShortestPath("grid5", "grid1").size());
		Assert.assertEquals(0, fg.getShortestPath("grid0", "grid4").size());
	}

	@Test
	public void test_getShortestPath_1hop() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2"));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals("grid2", fg.getShortestPath("grid1", "grid2").get(0).getTargetGridId());
	}

	@Test
	public void test_getShortestPath_1hop_2() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", true, false));
		FederationGraph fg = new FederationLogic().buildGraph();
		List<Federation> path = fg.getShortestPath("grid1", "grid2");
		Assert.assertEquals(1, path.size());
		Assert.assertEquals("grid2", path.get(0).getTargetGridId());
	}

	@Test
	public void test_getShortestPath_2hop_1() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid3", false, true));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(2, fg.getShortestPath("grid1", "grid3").size());
	}

	@Test
	public void test_getShortestPath_2hop_2() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid3", "grid1", false, true));
		fdao.addFederation(newFederation("grid2", "grid3", false, true));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(2, fg.getShortestPath("grid1", "grid3").size());
	}

	@Test
	public void test_getShortestPath_2hop_unreachable_1() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", true));
		fdao.addFederation(new Federation("grid2", "grid3"));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid3").size());
	}

	@Test
	public void test_getShortestPath_2hop_unreachable_2() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", true));
		fdao.addFederation(newFederation("grid3", "grid2"));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid3").size());
	}

	@Test
	public void test_getShortestPath_3hop() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", true, true));
		fdao.addFederation(newFederation("grid2", "grid3", true, true));
		fdao.addFederation(newFederation("grid2", "grid4", true, false));
		fdao.addFederation(newFederation("grid3", "grid4", true, true));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(3, fg.getShortestPath("grid1", "grid4").size());
	}

	@Test
	public void test_getShortestPath_3hop_unreachable() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", true, true));
		fdao.addFederation(newFederation("grid2", "grid3", true, false));
		fdao.addFederation(newFederation("grid3", "grid4", true, true));
		FederationGraph fg = new FederationLogic().buildGraph();
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid4").size());
	}

	@Test
	public void test_listAllReachableGridIds_1() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2", true, true));
		fdao.addFederation(newFederation("grid2", "grid3", true, true));
		fdao.addFederation(newFederation("grid3", "grid4", true, true));
		FederationGraph fg = new FederationLogic().buildGraph();
		Set<String> ids = new TreeSet<>();
		ids.addAll(fg.listAllReachableGridIds("grid1"));
		Iterator<String> it = ids.iterator();
		Assert.assertEquals("grid2", it.next());
		Assert.assertEquals("grid3", it.next());
		Assert.assertEquals("grid4", it.next());
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void test_listAllReachableGridIds_2() throws Throwable{
		fdao.addFederation(newFederation("grid1", "grid2"));
		fdao.addFederation(newFederation("grid2", "grid3"));
		fdao.addFederation(newFederation("grid3", "grid8"));
		fdao.addFederation(newFederation("grid4", "grid5"));
		fdao.addFederation(newFederation("grid5", "grid6"));
		fdao.addFederation(newFederation("grid1", "grid7"));
		fdao.addFederation(newFederation("grid7", "grid8"));
		fdao.addFederation(newFederation("grid8", "grid9"));
		fdao.addFederation(newFederation("grid4", "grid9", true));

		FederationGraph fg = new FederationLogic().buildGraph();
		Set<String> ids = new TreeSet<>();
		ids.addAll(fg.listAllReachableGridIds("grid1"));
		Assert.assertEquals(8, ids.size());
		Iterator<String> it = ids.iterator();
		for(int i = 2; i <= 9; i++){
			Assert.assertEquals("grid" + i, it.next());
		}
		Assert.assertFalse(it.hasNext());
	}

	private Federation newFederation(String sgid, String tgid){
		return newFederation(sgid, tgid, false);
	}

	private Federation newFederation(String sgid, String tgid, boolean symmetric){
		return newFederation(sgid, tgid, symmetric, true);
	}

	private Federation newFederation(String sgid, String tgid, boolean symmetric, boolean transitive){
		Federation f = new Federation(sgid, tgid);
		f.setConnected(true);
		f.setRequesting(false);
		f.setSymmetric(symmetric);
		f.setTransitive(transitive);
		return f;
	}

	private Federation newRequestingFederation(String sgid, String tgid, boolean symmetric, boolean transitive){
		Federation f = new Federation(sgid, tgid);
		f.setConnected(true);
		f.setRequesting(true);
		f.setSymmetric(symmetric);
		f.setTransitive(transitive);
		return f;
	}

	private FederationDao fdao;
	private GridDao gdao;
}
