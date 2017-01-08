package jp.go.nict.langrid.management.logic.federation.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.util.Pair;

public class BreathFirstSearch<K, V> implements GraphSearch<K, V>{
	public BreathFirstSearch(Comparator<List<V>> comparator){
		this.comparator = comparator;
	}

	@Override
	public List<V> searchShortestPath(
			Map<K, Map<K, V>> graph, K source, K target, Set<K> visited){
		visited = new HashSet<>(visited);
		Deque<Pair<K, List<V>>> queue = new LinkedList<>();
		visited.add(source);
		queue.addLast(Pair.create(source, Collections.emptyList()));
		List<V> shortestPath = new ArrayList<>();
		while(queue.size() > 0){
			Pair<K, List<V>> v = queue.pollFirst();
			K current = v.getFirst();
			List<V> currentPath = v.getSecond();
			for(Map.Entry<K, V> entry : graph.getOrDefault(current, Collections.emptyMap()).entrySet()){
				K next = entry.getKey();
				V nextPathElement = entry.getValue();
				if(visited.contains(next)) continue;
				visited.add(next);
				List<V> newPath = new ArrayList<>();
				newPath.addAll(currentPath);
				newPath.add(nextPathElement);
				if(next.equals(target)){
					if(shortestPath.size() == 0 || comparator.compare(newPath, shortestPath) < 0){
						shortestPath = newPath;
					}
				} else{
					queue.addLast(Pair.create(next, newPath));
				}
			}
		}
		return shortestPath;
	}

	@Override
	public boolean isReachable(Map<K, Map<K, V>> graph, K source, K target) {
		Deque<K> queue = new LinkedList<>();
		Set<K> visited = new HashSet<>();
		visited.add(source);
		queue.addLast(source);
		while(queue.size() > 0){
			K current = queue.pollFirst();
			for(K next : graph.getOrDefault(current, Collections.emptyMap()).keySet()){
				if(visited.contains(next)) continue;
				if(next.equals(target)) return true;
				visited.add(next);
				queue.addLast(next);
			}
		}
		return false;
	}

	@Override
	public Collection<K> listTargets(Map<K, Map<K, V>> graph, K source) {
		Set<K> ret = new LinkedHashSet<>();
		Deque<K> queue = new LinkedList<>();
		queue.addLast(source);
		while(queue.size() > 0){
			K current = queue.pollFirst();
			ret.add(current);
			for(K next : graph.getOrDefault(current, Collections.emptyMap()).keySet()){
				if(ret.contains(next)) continue;
				queue.addLast(next);
			}
		}
		ret.remove(source);
		return ret;
	}

	private Comparator<List<V>> comparator;
}
