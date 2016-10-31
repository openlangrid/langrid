package jp.go.nict.langrid.management.logic.federation;

import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.dao.entity.Federation;

public interface FederationGraph{
	boolean isReachable(String sourceGridId, String targetGridId);
	List<Federation> getShortestPath(String sourceGridId, String targetGridId);
	Collection<String> listAllReachableGridIds(String sourceGridId);
}
