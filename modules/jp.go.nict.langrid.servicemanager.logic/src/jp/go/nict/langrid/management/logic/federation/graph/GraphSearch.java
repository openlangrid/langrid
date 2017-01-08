package jp.go.nict.langrid.management.logic.federation.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GraphSearch<K, V> {
	List<V> searchShortestPath(
			Map<K, Map<K, V>> graph, K source, K target, Set<K> visited);

	default boolean isReachable(Map<K, Map<K, V>> graph, K source, K target){
		return searchShortestPath(graph, source, target, Collections.emptySet()).size() > 0;
	}

	Collection<K> listTargets(Map<K, Map<K, V>> graph, K source);
}
