package jp.go.nict.langrid.servicemanager.logic.federation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.federation.FederationGraph;
import jp.go.nict.langrid.management.logic.federation.ReverseFederationGraph;

public class ReverseFederationGraphNoDbTest {
	@Test
	public void test_isReachable() throws Throwable{
		FederationGraph fg = new ReverseFederationGraph(Arrays.asList(
				newFederation("grid1", "grid2", true, true),
				newFederation("grid2", "grid3", true, true)
				));
		Assert.assertTrue(fg.isReachable("grid3", "grid1"));
	}

	@Test
	public void test_listAllReachableGridIds() throws Throwable{
		FederationGraph fg = new ReverseFederationGraph(Arrays.asList(
				newFederation("grid1", "grid2", true, true),
				newFederation("grid2", "grid3", true, true)
				));
		Set<String> expected = new HashSet<>(Arrays.asList("grid1", "grid2"));
		for(String gid : fg.listAllReachableGridIds("grid3")){
			Assert.assertTrue(expected.remove(gid));
		}
		Assert.assertEquals(0, expected.size());
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
}
