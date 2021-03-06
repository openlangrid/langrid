package jp.go.nict.langrid.management.logic.federation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.commons.test.CollectionFixture;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.federation.FederationGraph;

public abstract class AbstractFederationGraphTest {
	protected abstract FederationGraph getGraph(Collection<Federation> federations);

	@Test
	public void test_isReachable() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true, true),
				newFederation("grid2", "grid3", true, true)
				));
		Assert.assertTrue(fg.isReachable("grid1", "grid3"));
	}

	@Test
	public void test_isReachable_2() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", false, true),
				newFederation("grid2", "grid3", false, true)
				));
		Assert.assertTrue(fg.isReachable("grid1", "grid3"));
	}

	@Test
	public void test_isReachable_false() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true, true),
				newFederation("grid2", "grid3", true, false)
				));
		Assert.assertFalse(fg.isReachable("grid1", "grid3"));
	}

	@Test
	public void test_getShortestPath() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", false, true),
				newFederation("grid2", "grid3", false, true),
				newFederation("grid3", "grid4", false, true),
				newFederation("grid1", "grid3", false, true)
				));
		CollectionFixture<Federation> p = new CollectionFixture<>(fg.getShortestPath("grid1", "grid4"));
		p.assertSize(2);
		p.next(f -> {
			Assert.assertEquals("grid3", f.getTargetGridId());
		});
	}

	@Test
	public void test_getShortestPath_2() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2"),
				newFederation("grid2", "grid3"),
				newFederation("grid3", "grid4"),
				newFederation("grid4", "grid5"),
				newFederation("grid5", "grid6"),
				newFederation("grid6", "grid11"),
				newFederation("grid1", "grid7"),
				newFederation("grid7", "grid8"),
				newFederation("grid8", "grid4"),
				newFederation("grid8", "grid9"),
				newFederation("grid8", "grid10"),
				newFederation("grid9", "grid11"),
				newFederation("grid10", "grid11")
				));
		CollectionFixture<Federation> p = new CollectionFixture<>(fg.getShortestPath("grid1", "grid11"));
		p.assertSize(4);
		p.next(f -> {
			Assert.assertEquals("grid7", f.getTargetGridId());
		});
	}

	@Test
	public void test_getShortestPath_symmetric() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", false, true),
				newFederation("grid3", "grid2", true, true),
				newFederation("grid3", "grid4", false, true)
				));
		CollectionFixture<Federation> p = new CollectionFixture<>(fg.getShortestPath("grid1", "grid4"));
		p.assertSize(3);
		p.next(f -> {
			Assert.assertEquals("grid1", f.getSourceGridId());
			Assert.assertEquals("grid2", f.getTargetGridId());
		});
		p.next(f -> {
			Assert.assertTrue(f.isSymmetric());
			Assert.assertEquals("grid3", f.getSourceGridId());
			Assert.assertEquals("grid2", f.getTargetGridId());
		});
		p.next(f -> {
			Assert.assertEquals("grid3", f.getSourceGridId());
			Assert.assertEquals("grid4", f.getTargetGridId());
		});
	}

	@Test
	public void test_getShortestPath_null() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newRequestingFederation("grid1", "grid2", true, true),
				newRequestingFederation("grid2", "grid3", true, true),
				newRequestingFederation("grid3", "grid4", true, true),
				newRequestingFederation("grid1", "grid3", true, true)
				));
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid4").size());
	}

	@Test
	public void test_getShortestPath_unreachable() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true),
				newFederation("grid3", "grid4")
				));
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid5").size());
	}

	@Test
	public void test_getShortestPath_unreachable_2() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true),
				newFederation("grid2", "grid3", true),
				newFederation("grid3", "grid4")
				));
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid5").size());
	}

	@Test
	public void test_getShortestPath_unreachable_3() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true),
				newFederation("grid2", "grid3", true),
				newFederation("grid3", "grid4")
				));
		Assert.assertEquals(0, fg.getShortestPath("grid5", "grid1").size());
		Assert.assertEquals(0, fg.getShortestPath("grid0", "grid4").size());
	}

	@Test
	public void test_getShortestPath_1hop() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2")
				));
		CollectionFixture<Federation> p = new CollectionFixture<>(fg.getShortestPath("grid1", "grid2"));
		p.next(f -> {
			Assert.assertEquals("grid2", f.getTargetGridId());
		});
	}

	@Test
	public void test_getShortestPath_1hop_2() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true, false)
				));
		CollectionFixture<Federation> p = new CollectionFixture<>(fg.getShortestPath("grid1", "grid2"));
		p.assertSize(1);
		p.next(f -> {
			Assert.assertEquals("grid2", f.getTargetGridId());
		});
	}

	@Test
	public void test_getShortestPath_2hop_1() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2"),
				newFederation("grid2", "grid3", false, true)
				));
		Assert.assertEquals(2, fg.getShortestPath("grid1", "grid3").size());
	}

	@Test
	public void test_getShortestPath_2hop_2() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2"),
				newFederation("grid3", "grid1", false, true),
				newFederation("grid2", "grid3", false, true)
				));
		Assert.assertEquals(2, fg.getShortestPath("grid1", "grid3").size());
	}

	@Test
	public void test_getShortestPath_not_backwardTransitive() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true, false, false),
				newFederation("grid3", "grid2", true, false, false)
				));
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid3").size());
	}

	@Test
	public void test_getShortestPath_2hop_unreachable_1() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true),
				new Federation("grid2", "grid3")
				));
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid3").size());
	}

	@Test
	public void test_getShortestPath_2hop_unreachable_2() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true),
				newFederation("grid3", "grid2")
				));
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid3").size());
	}

	@Test
	public void test_getShortestPath_3hop() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true, true),
				newFederation("grid2", "grid3", true, true),
				newFederation("grid2", "grid4", true, false),
				newFederation("grid3", "grid4", true, true)
				));
		Assert.assertEquals(3, fg.getShortestPath("grid1", "grid4").size());
	}

	@Test
	public void test_getShortestPath_3hop_unreachable() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true, true),
				newFederation("grid2", "grid3", true, false),
				newFederation("grid3", "grid4", true, true)
				));
		Assert.assertEquals(0, fg.getShortestPath("grid1", "grid4").size());
	}

	@Test
	public void test_listAllReachableGridIds_1() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2", true, true),
				newFederation("grid2", "grid3", true, true),
				newFederation("grid3", "grid4", true, true)
				));
		CollectionFixture<String> ids = new CollectionFixture<>(new TreeSet<>(
				fg.listAllReachableGridIds("grid1")));
		ids.assertNextEquals("grid2");
		ids.assertNextEquals("grid3");
		ids.assertNextEquals("grid4");
		ids.assertNotHasNext();
	}

	@Test
	public void test_listAllReachableGridIds_2() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2"),
				newFederation("grid2", "grid3"),
				newFederation("grid3", "grid8"),
				newFederation("grid4", "grid5"),
				newFederation("grid5", "grid6"),
				newFederation("grid1", "grid7"),
				newFederation("grid7", "grid8"),
				newFederation("grid8", "grid9"),
				newFederation("grid4", "grid9", true)
				));
		Set<String> ids = new TreeSet<>();
		ids.addAll(fg.listAllReachableGridIds("grid1"));
		Assert.assertEquals(8, ids.size());
		Iterator<String> it = ids.iterator();
		for(int i = 2; i <= 9; i++){
			Assert.assertEquals("grid" + i, it.next());
		}
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void test_listAllReachableGridIds_3() throws Throwable{
		FederationGraph fg = getGraph(Arrays.asList(
				newFederation("grid1", "grid2"),
				newFederation("grid2", "grid3"),
				newFederation("grid3", "grid8"),
				newFederation("grid4", "grid5", false, false),
				newFederation("grid5", "grid6"),
				newFederation("grid1", "grid7"),
				newFederation("grid7", "grid8"),
				newFederation("grid8", "grid9"),
				newFederation("grid4", "grid9", true)
				));

		Set<String> unreachables = new HashSet<>(Arrays.asList("grid5", "grid6"));
		Set<String> ids = new TreeSet<>(fg.listAllReachableGridIds("grid1"));

		Assert.assertEquals(6, ids.size());
		Iterator<String> it = ids.iterator();
		for(int i = 2; i <= 9; i++){
			String gid = "grid" + i;
			if(!unreachables.contains(gid)) Assert.assertEquals(gid, it.next());
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
		f.setForwardTransitive(transitive);
		if(symmetric) f.setBackwardTransitive(transitive);
		return f;
	}

	private Federation newFederation(String sgid, String tgid,
			boolean forwardTransitive, boolean symmetric, boolean backwardTransitive){
		Federation f = new Federation(sgid, tgid);
		f.setConnected(true);
		f.setRequesting(false);
		f.setForwardTransitive(forwardTransitive);
		f.setSymmetric(symmetric);
		f.setBackwardTransitive(backwardTransitive);
		return f;
	}

	private Federation newRequestingFederation(String sgid, String tgid,
			boolean symmetric, boolean transitive){
		Federation f = new Federation(sgid, tgid);
		f.setConnected(true);
		f.setRequesting(true);
		f.setSymmetric(symmetric);
		f.setForwardTransitive(transitive);
		if(symmetric) f.setBackwardTransitive(transitive);
		return f;
	}
}
