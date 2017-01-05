package jp.go.nict.langrid.management.logic.federation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.Federation;

public interface FederationGraph{
	boolean isReachable(String sourceGridId, String targetGridId);
	List<Federation> getShortestPath(String sourceGridId, String targetGridId, Set<String> ignores);
	default List<Federation> getShortestPath(String sourceGridId, String targetGridId) {
		return getShortestPath(sourceGridId, targetGridId, Collections.emptySet());
	}
	Collection<String> listAllReachableGridIds(String sourceGridId);
}
