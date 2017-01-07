package jp.go.nict.langrid.management.logic.federation.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GraphSearch<K, V> {
	List<V> searchShortestPath(
			Map<K, Map<K, V>> graph, K source, K target,
			Set<K> visited,
			Comparator<List<V>> comparator);
	default boolean isReachable(Map<K, Map<K, V>> graph, K source, K target){
		return searchShortestPath(graph, source, target, Collections.emptySet(),
				(l, r) -> l.size() - r.size()).size() > 0;
	}
	Collection<K> listTargets(Map<K, Map<K, V>> graph, K source);
}
