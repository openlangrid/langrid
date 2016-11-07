package jp.go.nict.langrid.management.logic.federation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.Federation;

public abstract class AbstractFederationGraph implements FederationGraph{
	protected abstract Map<String, Map<String, Federation>> getFederations();

	public boolean isReachable(String sourceGridId, String targetGridId){
		return getShortestPath(sourceGridId, targetGridId).size() > 0;
	}

	public List<Federation> getShortestPath(String sourceGridId, String targetGridId){
		return getShortestPath(sourceGridId, sourceGridId, targetGridId, getFederations(), new HashMap<>(), new HashSet<>());
	}

	public Collection<String> listAllReachableGridIds(String sourceGridId){
		Set<String> ret = new LinkedHashSet<>();
		ret.add(sourceGridId);
		listAllReachableGridIdsFrom1st(sourceGridId, ret);
		ret.remove(sourceGridId);
		return ret;
	}

	private void listAllReachableGridIdsFrom1st(String source, Set<String> ids){
		Map<String, Federation> targets = getFederations().get(source);
		if(targets == null) return;
		List<String> addedAndTransitive = new ArrayList<>();
		for(Map.Entry<String, Federation> entry : targets.entrySet()){
			String next = entry.getKey();
			if(!ids.contains(next)){
				ids.add(next);
				if(entry.getValue().isTransitive())
					addedAndTransitive.add(next);
			}
		}
		for(String gid : addedAndTransitive){
			listAllReachableGridIdsFromDeeper(gid, ids);
		}
	}

	private void listAllReachableGridIdsFromDeeper(String source, Set<String> ids){
		Map<String, Federation> targets = getFederations().get(source);
		if(targets == null) return;
		for(Map.Entry<String, Federation> entry : targets.entrySet()){
			String next = entry.getKey();
			if(!ids.contains(next) && entry.getValue().isTransitive()){
				ids.add(next);
				listAllReachableGridIdsFromDeeper(next, ids);
			}
		}
	}

	private List<Federation> getShortestPath(String sgid, String cgid, String tgid,
			Map<String, Map<String, Federation>> federations,
			Map<String, List<Federation>> cache, Set<String> visited){
		visited.add(cgid);
		try{
			if(cgid.equals(tgid)) return Arrays.asList();
			List<Federation> cached = cache.get(cgid);
			if(cached != null){
				return cached;
			}
			List<Federation> curPath = Arrays.asList();
			Map<String, Federation> feds = federations.get(cgid);
			if(feds != null){
				int curLen = Integer.MAX_VALUE;
				for(Map.Entry<String, Federation> entry : feds.entrySet()){
					String nextGid = entry.getKey();
					Federation nextFederation = entry.getValue();
					if(visited.contains(nextGid)) continue;
					if(nextGid.equals(tgid)){
						if(nextFederation.isTransitive() || cgid.equals(sgid)){
							curPath = Arrays.asList(nextFederation);
							break;
						}
						continue;
					} else if(nextFederation.isTransitive()){
						List<Federation> can = getShortestPath(sgid, nextGid, tgid, federations, cache, visited);
						if(0 < can.size() && can.size() < curLen){
							List<Federation> r = new ArrayList<>();
							r.add(nextFederation);
							r.addAll(can);
							curPath = r;
							curLen = can.size();
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

	//	private static Logger logger = Logger.getLogger(FederationGraph.class.getName());
}
