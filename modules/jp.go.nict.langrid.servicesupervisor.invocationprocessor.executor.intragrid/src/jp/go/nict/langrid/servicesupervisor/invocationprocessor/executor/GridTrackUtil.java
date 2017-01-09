package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor;

import java.util.Collections;
import java.util.List;

public class GridTrackUtil {
	public static String append(String gridId, long processTime, String current){
		String ret = gridId + ":" + processTime;
		if(current != null && current.length() > 0){
			if(current.charAt(0) == '('){
				ret += current;
			} else{
				ret += " -> " + current;
			}
		}
		return ret;
	}

	public static class GridTrack{
		public GridTrack() {
		}
		public GridTrack(String gridId, long processMillis, List<GridTrack> children) {
			this.gridId = gridId;
			this.processMillis = processMillis;
			this.children = children;
		}
		public GridTrack(String invocationName, String gridId, long processMillis, List<GridTrack> children) {
			this.invocationName = invocationName;
			this.gridId = gridId;
			this.processMillis = processMillis;
			this.children = children;
		}
		public String getInvocationName() {
			return invocationName;
		}
		public void setInvocationName(String invocationName) {
			this.invocationName = invocationName;
		}
		public String getGridId() {
			return gridId;
		}
		public void setGridId(String gridId) {
			this.gridId = gridId;
		}
		public long getProcessMillis() {
			return processMillis;
		}
		public void setProcessMillis(long processMillis) {
			this.processMillis = processMillis;
		}
		public List<GridTrack> getChildren() {
			return children;
		}
		public void setChildren(List<GridTrack> children) {
			this.children = children;
		}

		private String invocationName;
		private String gridId;
		private long processMillis;
		private List<GridTrack> children = Collections.emptyList();
	}
	public static List<GridTrack> decode(String str){
		return Collections.emptyList();
	}
}
