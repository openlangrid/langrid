package jp.go.nict.langrid.management.logic.federation.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Quartet;
import jp.go.nict.langrid.commons.util.Trio;

public class DijkstraSearch<K, V> implements GraphSearch<K, V>{
	public DijkstraSearch() {
		this(p -> p.getSecond() != Integer.MAX_VALUE ? p.getSecond() + 1 : Integer.MAX_VALUE);
	}

	/**
	 * @param costFunc leftNode, costOfLeftNode, rightNode, leftToRightEdge
	 */
	public DijkstraSearch(Function<Quartet<K, Integer, K, V>, Integer> costFunc){
		this.costFunc = costFunc;
	}

	@Override
	public List<V> searchShortestPath(Map<K, Map<K, V>> graph, K source, K target, Set<K> ignore) {
		Map<K, Trio<Integer, K, V>> costAndPrevs = new HashMap<>();
		Queue<Pair<K, Integer>> queue = new PriorityQueue<>(
				(l, r) -> l.getSecond() - r.getSecond());
		{	Set<K> keys = new LinkedHashSet<>();
			for(Map.Entry<K, Map<K, V>> entry : graph.entrySet()){
				keys.add(entry.getKey());
				for(K k : entry.getValue().keySet()) keys.add(k);
			}
			for(K key : keys){
				if(ignore.contains(key)) continue;
				int cost = key.equals(source) ? 0 : Integer.MAX_VALUE;
				queue.add(Pair.create(key, cost));
				costAndPrevs.put(key, Trio.create(cost, null, null));
			}
		}
		if(!costAndPrevs.containsKey(target) || !costAndPrevs.containsKey(source))
			return Collections.emptyList();
		while(!queue.isEmpty()){
			Pair<K, Integer> uPair = queue.poll();
			for(Map.Entry<K, V> vEntry : graph.getOrDefault(uPair.getFirst(), Collections.emptyMap()).entrySet()){
				int cost = costFunc.apply(Quartet.create(
						uPair.getFirst(), uPair.getSecond(), vEntry.getKey(), vEntry.getValue()
						));
				Trio<Integer, K, V> costAndPrev = costAndPrevs.computeIfAbsent(
						vEntry.getKey(), k ->
							Trio.create(k.equals(source) ? 0 : Integer.MAX_VALUE, null, null)
						);
				if(cost < costAndPrev.getFirst()){
					costAndPrevs.put(vEntry.getKey(),
							Trio.create(cost, uPair.getFirst(), vEntry.getValue()));
					queue.add(Pair.create(vEntry.getKey(), cost));
				}
			}
		}
		List<V> ret = new LinkedList<>();
		K current = target;
		while(!current.equals(source)){
			Trio<Integer, K, V> entry = costAndPrevs.get(current);
			current = entry.getSecond();
			if(current == null) return Collections.emptyList();
			ret.add(0, entry.getThird());
		}
		return ret;
	}

	@Override
	public boolean isReachable(Map<K, Map<K, V>> graph, K source, K target) {
		return new BreathFirstSearch<K, V>().isReachable(graph, source, target);
	}

	@Override
	public Collection<K> listTargets(Map<K, Map<K, V>> graph, K source) {
		return new BreathFirstSearch<K, V>().listTargets(graph, source);
	}

	private Function<Quartet<K, Integer, K, V>, Integer> costFunc;
}
