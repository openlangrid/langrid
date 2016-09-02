package jp.go.nict.langrid.management.logic.federation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.Grid;

public class FederationReverseGraph {
	public FederationReverseGraph(Collection<Grid> grids, Collection<Federation> federations){
		for(Grid g : grids){
			this.grids.put(g.getGridId(), g);
		}
		for(Federation f : federations){
			if(!f.isConnected() || f.isRequesting()) continue;
			Map<String, Federation> feds = this.federations.get(f.getTargetGridId());
			if(feds == null){
				feds = new HashMap<>();
				this.federations.put(f.getTargetGridId(), feds);
			}
			feds.put(f.getSourceGridId(), f);
		}
	}

	public boolean isTransitive(String gridId){
		Grid g = grids.get(gridId);
		if(g == null) return false;
		return g.isTranstiveRelationEnabled();
	}

	public Collection<Federation> listFederationsTo(String targetGridId){
		Map<String, Federation> targets = federations.get(targetGridId);
		if(targets == null) return new ArrayList<>();
		return targets.values();
	}

	private Map<String, Grid> grids = new HashMap<>();
	private Map<String, Map<String, Federation>> federations = new HashMap<>();
//	private static Logger logger = Logger.getLogger(FederationGraph.class.getName());
}
