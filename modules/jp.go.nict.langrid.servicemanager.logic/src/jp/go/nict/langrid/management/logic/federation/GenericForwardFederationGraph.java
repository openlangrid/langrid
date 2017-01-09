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

public class GenericForwardFederationGraph implements FederationGraph{
	public GenericForwardFederationGraph(
			Collection<Federation> federations,
			GraphSearch<String, Federation> alg){
		this.alg = alg;
		this.connections = new HashMap<>();
		this.transConnections = new HashMap<>();
		for(Federation f : federations){
			if(!f.isConnected() || f.isRequesting()) continue;
			connections.computeIfAbsent(f.getSourceGridId(), k -> new LinkedHashMap<>())
				.put(f.getTargetGridId(), f);
			if(f.isSymmetric()){
				connections.computeIfAbsent(f.getTargetGridId(), k -> new LinkedHashMap<>())
					.put(f.getSourceGridId(), f);
			}
			if(f.isForwardTransitive()){
				// forward
				transConnections.computeIfAbsent(f.getSourceGridId(), key -> new LinkedHashMap<>())
					.put(f.getTargetGridId(), f);
			}
			if(f.isSymmetric() && f.isBackwardTransitive()){
				// backward
				transConnections.computeIfAbsent(f.getTargetGridId(), key -> new LinkedHashMap<>())
					.put(f.getSourceGridId(), f);
			}
		}
	}

	@Override
	public List<Federation> getShortestPath(String sourceGridId, String targetGridId,
			Set<String> ignores) {
		Map<String, Map<String, Federation>> g = transConnections;
		Federation c = connections.getOrDefault(sourceGridId, Collections.emptyMap())
				.get(targetGridId);
		Federation tc = transConnections.getOrDefault(sourceGridId, Collections.emptyMap())
				.get(targetGridId);
		if(c != null && tc == null){
			g = new LinkedHashMap<>(transConnections);
			Map<String, Federation> d = new HashMap<>();
			d.put(targetGridId, c);
			g.put(sourceGridId, d);
		}
		return alg.searchShortestPath(g, sourceGridId, targetGridId, ignores);
	}

	@Override
	public Collection<String> listAllReachableGridIds(String sourceGridId){
		Set<String> ret = new LinkedHashSet<>();
		ret.addAll(connections.getOrDefault(sourceGridId, Collections.emptyMap()).keySet());
		ret.addAll(alg.listTargets(transConnections, sourceGridId));
		return ret;
	}
	private Map<String, Map<String, Federation>> connections;
	private Map<String, Map<String, Federation>> transConnections;
	private GraphSearch<String, Federation> alg;
}
