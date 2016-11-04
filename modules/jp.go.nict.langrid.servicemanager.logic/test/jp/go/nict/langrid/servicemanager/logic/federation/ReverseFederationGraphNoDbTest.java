package jp.go.nict.langrid.servicemanager.logic.federation;

import java.util.Arrays;

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

	private Federation newFederation(String sgid, String tgid, boolean symmetric, boolean transitive){
		Federation f = new Federation(sgid, tgid);
		f.setConnected(true);
		f.setRequesting(false);
		f.setSymmetric(symmetric);
		f.setTransitive(transitive);
		return f;
	}
}
