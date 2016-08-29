package jp.go.nict.langrid.management.logic.federation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.Grid;

public class FederationGraph {
	public FederationGraph(Collection<Grid> grids, Collection<Federation> federations){
		for(Grid g : grids){
			this.grids.put(g.getGridId(), g);
		}
		for(Federation f : federations){
			if(!f.isConnected() || f.isRequesting()) continue;
			Map<String, Federation> feds = this.federations.get(f.getSourceGridId());
			if(feds == null){
				feds = new HashMap<>();
				this.federations.put(f.getSourceGridId(), feds);
			}
			feds.put(f.getTargetGridId(), f);
		}
	}

	public boolean isReachable(String sourceGridId, String targetGridId){
		return getNearestFederation(sourceGridId, targetGridId) != null;
	}

	public Federation getNearestFederation(String sourceGridId, String targetGridId){
		{
			Map<String, Federation> feds = federations.get(sourceGridId);
			if(feds != null){
				Federation f = feds.get(targetGridId);
				if(f != null && f.isConnected() && !f.isRequesting()){
					return f;
				}
			}
		}

		if(!isSymmGrid(sourceGridId)) return null;
		Map<String, Integer> cache = new HashMap<>();
		cache.put(sourceGridId, Integer.MAX_VALUE);
		Federation ret = null;
		int currentHops = Integer.MAX_VALUE;
		Map<String, Federation> feds = federations.get(sourceGridId);
		if(feds == null) return null;
		Set<String> visited = new HashSet<>();
		visited.add(sourceGridId);
		for(Federation f : feds.values()){
			String tgid = f.getTargetGridId();
			if(visited.contains(tgid)) continue;
			if(!isSymmGrid(tgid)) continue;
			visited.add(tgid);
			int h = getHops(tgid, targetGridId, federations, cache, visited);
			visited.remove(tgid);
			if(currentHops > h){
				ret = f;
				currentHops = h;
			}
		}
		return ret;
	}

	private int getHops(String sgid, String tgid,
			Map<String, Map<String, Federation>> federations,
			Map<String, Integer> cache, Set<String> visited){
		if(sgid.equals(tgid)) return 0;
		Integer r = cache.get(sgid);
		if(r != null){
			return r;
		}
		int curValue = Integer.MAX_VALUE;
		Map<String, Federation> feds = federations.get(sgid);
		if(feds == null) return curValue;
		for(Federation f : feds.values()){
			if(!isSymmGrid(f.getTargetGridId())) continue;
			if(f.getTargetGridId().equals(tgid)){
				cache.put(sgid, 1);
				return 1;
			}
			if(visited.contains(f.getTargetGridId())) continue;
			visited.add(f.getTargetGridId());
			int v = getHops(f.getTargetGridId(), tgid, federations, cache, visited);
			visited.remove(f.getTargetGridId());
			if(v < curValue){
				curValue = v;
			}
		}
		if(curValue != Integer.MAX_VALUE){
			cache.put(sgid, curValue + 1);
			return curValue + 1;
		} else{
			return Integer.MAX_VALUE;
		}
	}

	private boolean isSymmGrid(String gridId){
		Grid g = grids.get(gridId);
		if(g == null) return false;
		return g.isSymmetricRelationEnabled();
	}

	private Map<String, Grid> grids = new HashMap<>();
	private Map<String, Map<String, Federation>> federations = new HashMap<>();
//	private static Logger logger = Logger.getLogger(FederationGraph.class.getName());
}
