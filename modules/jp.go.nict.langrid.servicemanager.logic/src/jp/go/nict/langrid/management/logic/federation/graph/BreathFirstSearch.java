package jp.go.nict.langrid.management.logic.federation.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import jp.go.nict.langrid.commons.util.Pair;

public class BreathFirstSearch<K, V> implements GraphSearch<K, V>{
	@Override
	public List<V> searchShortestPath(Map<K, Map<K, V>> graph, K source, K target,
			Set<K> visited,
			Comparator<List<V>> comparator){
		return search(graph, source, target,
				new HashMap<>(), new HashSet<>(visited),
				comparator, v -> false);
	}
	@Override
	public boolean isReachable(Map<K, Map<K, V>> graph, K source, K target) {
		return search(graph, source, target,
				new HashMap<>(), new HashSet<>(),
				(l, r) -> -1, v -> true).size() > 0;
	}
	private List<V> search(
			Map<K, Map<K, V>> graph, K sgid, K tgid,
			Map<K, List<V>> cache, Set<K> visited,
			Comparator<List<V>> comparator, Function<List<V>, Boolean> terminate){
		Deque<Pair<K, List<V>>> queue = new LinkedList<>();
		visited.add(sgid);
		queue.addLast(Pair.create(sgid, Collections.emptyList()));
		List<V> current = new ArrayList<>();
		while(queue.size() > 0){
			Pair<K, List<V>> v = queue.pollFirst();
			K key = v.getFirst();
			List<V> path = v.getSecond();
			for(Map.Entry<K, V> entry : graph.getOrDefault(key, Collections.emptyMap()).entrySet()){
				K nextKey = entry.getKey();
				V nextVal = entry.getValue();
				if(visited.contains(nextKey)) continue;
				visited.add(nextKey);
				List<V> newPath = new ArrayList<>();
				newPath.addAll(path);
				newPath.add(nextVal);
				if(nextKey.equals(tgid)){
					if(current.size() == 0 || comparator.compare(newPath, current) < 0){
						current = newPath;
					}
					if(terminate.apply(current)) return current;
				} else{
					queue.addLast(Pair.create(nextKey, newPath));
				}
			}
		}
		return current;
	}

	@Override
	public Collection<K> listTargets(Map<K, Map<K, V>> graph, K source) {
		Set<K> visited = new LinkedHashSet<>();
		visited.add(source);
		listAllReachableGridIdsFrom1st(graph, source, visited);
		visited.remove(source);
		return visited;
	}

	private void listAllReachableGridIdsFrom1st(Map<K, Map<K, V>> graph, K source, Set<K> ids){
		Map<K, V> targets = graph.get(source);
		if(targets == null) return;
		List<K> addedAndTransitive = new ArrayList<>();
		for(Map.Entry<K, V> entry : targets.entrySet()){
			K next = entry.getKey();
			if(!ids.contains(next)){
				ids.add(next);
				addedAndTransitive.add(next);
			}
		}
		for(K gid : addedAndTransitive){
			listAllReachableGridIdsFromDeeper(graph, gid, ids);
		}
	}

	private void listAllReachableGridIdsFromDeeper(
			Map<K, Map<K, V>> graph, K source, Set<K> ids){
		Map<K, V> targets = graph.get(source);
		if(targets == null) return;
		for(Map.Entry<K, V> entry : targets.entrySet()){
			K next = entry.getKey();
			if(!ids.contains(next)){
				ids.add(next);
				listAllReachableGridIdsFromDeeper(graph, next, ids);
			}
		}
	}
}
