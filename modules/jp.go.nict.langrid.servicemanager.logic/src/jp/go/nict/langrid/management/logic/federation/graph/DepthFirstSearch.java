package jp.go.nict.langrid.management.logic.federation.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DepthFirstSearch<K, V> implements GraphSearch<K, V>{
	public DepthFirstSearch() {
		this((l, r) -> l.size() - r.size());
	}

	public DepthFirstSearch(Comparator<List<V>> comparator){
		this.comparator = comparator;
	}

	@Override
	public List<V> searchShortestPath(
			Map<K, Map<K, V>> graph, K source, K target, Set<K> visited){
		return searchShortestPath(graph, source, target, new HashSet<>(visited), new HashMap<>());
	}
	private List<V> searchShortestPath(
			Map<K, Map<K, V>> graph, K source, K target,
			Set<K> visited, Map<K, List<V>> cache){
		visited.add(source);
		try{
			List<V> shortestPath = Collections.emptyList();
			for(Map.Entry<K, V> entry : graph.getOrDefault(source, Collections.emptyMap()).entrySet()){
				K next = entry.getKey();
				V nextEdge = entry.getValue();
				if(visited.contains(next)) continue;
				List<V> newPath = null;
				if(next.equals(target)){
					newPath = Arrays.asList(nextEdge);
				} else{
					List<V> can = searchShortestPath(graph, next, target, visited, cache);
					if(can.size() > 0){
						newPath = new ArrayList<>();
						newPath.add(nextEdge);
						newPath.addAll(can);
					}
				}
				if(newPath != null && (shortestPath.size() == 0 || comparator.compare(newPath, shortestPath) < 0)){
					shortestPath = newPath;
				}
			}
			return shortestPath;
		} finally{
			visited.remove(source);
		}
	}

	static class IsReachable<V, E>{
		public IsReachable(Map<V, Map<V, E>> graph, V target) {
			this.graph = graph;
			this.target = target;
		}
		public boolean run(V source){
			visited.add(source);
			for(V next : graph.getOrDefault(source, Collections.emptyMap()).keySet()){
				if(visited.contains(next)) continue;
				if(next.equals(target)) return true;
				else if(run(next)) return true;
			}
			return false;
		}
		private Map<V, Map<V, E>> graph;
		private V target;
		private Set<V> visited = new HashSet<>();
	}
	@Override
	public boolean isReachable(Map<K, Map<K, V>> graph, K source, K target) {
		return new IsReachable<K, V>(graph, target).run(source);
	}

	@Override
	public Collection<K> listTargets(Map<K, Map<K, V>> graph, K source) {
		Set<K> visited = new LinkedHashSet<>();
		visited.add(source);
		listTargets(graph, source, visited);
		visited.remove(source);
		return visited;
	}
	private void listTargets(Map<K, Map<K, V>> graph, K source, Set<K> ret){
		for(K next : graph.getOrDefault(source, Collections.emptyMap()).keySet()){
			if(!ret.contains(next)){
				ret.add(next);
				listTargets(graph, next, ret);
			}
		}
	}

	private Comparator<List<V>> comparator;
}
