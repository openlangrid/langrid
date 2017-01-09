package jp.go.nict.langrid.management.logic.federation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.federation.graph.GraphSearch;

public class GenericBackwardFederationGraph implements FederationGraph{
	public GenericBackwardFederationGraph(Collection<Federation> federations, GraphSearch<String, Federation> alg){
		this.alg = alg;
		this.directConnections = new HashMap<>();
		this.transitiveGraph = new HashMap<>();
		for(Federation f : federations){
			if(!f.isConnected() || f.isRequesting()) continue;
			directConnections.computeIfAbsent(f.getTargetGridId(), k -> new LinkedHashMap<>())
				.put(f.getSourceGridId(), f);
			if(f.isSymmetric()){
				directConnections.computeIfAbsent(f.getSourceGridId(), k -> new LinkedHashMap<>())
					.put(f.getTargetGridId(), f);
			}
			if(f.isForwardTransitive()){
				// forward
				transitiveGraph.computeIfAbsent(f.getTargetGridId(), key -> new LinkedHashMap<>())
					.put(f.getSourceGridId(), f);
			}
			if(f.isSymmetric() && f.isBackwardTransitive()){
				// backward
				transitiveGraph.computeIfAbsent(f.getTargetGridId(), key -> new LinkedHashMap<>())
					.put(f.getTargetGridId(), f);
			}
		}
	}

	@Override
	public List<Federation> getShortestPath(String sourceGridId, String targetGridId,
			Set<String> ignores) {
		Map<String, Map<String, Federation>> g = transitiveGraph;
		Federation f = directConnections.getOrDefault(sourceGridId, Collections.emptyMap())
			.get(targetGridId);
		if(f != null && !transitiveGraph.getOrDefault(sourceGridId, Collections.emptyMap()).containsKey(targetGridId)){
			g = new LinkedHashMap<>(transitiveGraph);
			Map<String, Federation> d = new HashMap<>();
			d.put(targetGridId, f);
			g.put(sourceGridId, d);
		}
		return alg.searchShortestPath(g, sourceGridId, targetGridId, ignores);
	}

	@Override
	public Collection<String> listAllReachableGridIds(String sourceGridId){
		Set<String> ret = new LinkedHashSet<>();
		ret.addAll(directConnections.getOrDefault(sourceGridId, Collections.emptyMap()).keySet());
		ret.addAll(alg.listTargets(transitiveGraph, sourceGridId));
		return ret;
	}
	private Map<String, Map<String, Federation>> directConnections;
	private Map<String, Map<String, Federation>> transitiveGraph;
	private GraphSearch<String, Federation> alg;
}
