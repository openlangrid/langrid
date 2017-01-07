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
	@Override
	public List<V> searchShortestPath(Map<K, Map<K, V>> graph, K source, K target,
			Set<K> visited,
			Comparator<List<V>> comparator){
		return search(source, source, target, graph, new HashMap<>(), new HashSet<>(visited), comparator);
	}
	private List<V> search(
			K sgid, K cgid, K tgid,
			Map<K, Map<K, V>> graph,
			Map<K, List<V>> cache, Set<K> visited,
			Comparator<List<V>> comparator){
		visited.add(cgid);
		try{
			if(cgid.equals(tgid)) return Arrays.asList();
			List<V> cached = cache.get(cgid);
			if(cached != null){
				return cached;
			}
			List<V> curPath = Collections.emptyList();
			Map<K, V> feds = graph.get(cgid);
			if(feds != null){
				for(Map.Entry<K, V> entry : feds.entrySet()){
					K nextGid = entry.getKey();
					V nextFederation = entry.getValue();
					if(visited.contains(nextGid)) continue;
					if(nextGid.equals(tgid)){
						curPath = Arrays.asList(nextFederation);
						continue;
					} else {
						List<V> can = search(sgid, nextGid, tgid, graph, cache, visited,
								comparator);
						if(can.size() > 0){
							List<V> r = new ArrayList<>();
							r.add(nextFederation);
							r.addAll(can);
							if(curPath.size() == 0 || comparator.compare(r, curPath) < 0){
								curPath = r;
							}
						}
					}
				}
			}
			cache.put(cgid, curPath);
			return curPath;
		} finally{
			visited.remove(cgid);
		}
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
