package jp.go.nict.langrid.servicemanager.logic.federation;

import java.util.Collection;

import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.federation.FederationGraph;
import jp.go.nict.langrid.management.logic.federation.GenericForwardFederationGraph;
import jp.go.nict.langrid.management.logic.federation.graph.BreathFirstSearch;

public class DFSFederationGraphTest extends AbstractFederationGraphTest {
	@Override
	protected FederationGraph getGraph(Collection<Federation> federations) {
		return new GenericForwardFederationGraph(federations, new BreathFirstSearch<>());
	}
}
