package jp.go.nict.langrid.management.logic.federation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.Federation;

public class ForwardFederationGraph extends AbstractFederationGraph {
	public ForwardFederationGraph(Collection<Federation> federations){
		for(jp.go.nict.langrid.dao.entity.Federation f : federations){
			if(!f.isConnected() || f.isRequesting()) continue;
			// forward
			this.federations.computeIfAbsent(f.getSourceGridId(), key -> new HashMap<>())
				.put(f.getTargetGridId(), f);
			if(f.isSymmetric()){
				// backward
				this.federations.computeIfAbsent(f.getTargetGridId(), key -> new HashMap<>())
					.put(f.getSourceGridId(), f);
			}
		}
	}
	
	@Override
	protected Map<String, Map<String, Federation>> getFederations() {
		return federations;
	}

	private Map<String, Map<String, Federation>> federations = new HashMap<>();
//	private static Logger logger = Logger.getLogger(FederationGraph.class.getName());
}
