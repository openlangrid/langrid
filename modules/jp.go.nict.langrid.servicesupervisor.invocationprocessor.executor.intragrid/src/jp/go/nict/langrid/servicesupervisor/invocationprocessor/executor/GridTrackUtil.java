package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class GridTrackUtil {
	public static String append(String gridId, long processTime, String current){
		String ret = "[" + gridId + "," + processTime;
		if(current != null && current.length() > 0){
			if(current.charAt(0) == '{'){
				ret += "," + current + "]";
			} else{
				ret += "]," + current;
			}
		}
		return ret;
	}

	public static class GridTrack{
		public GridTrack() {
		}
		public GridTrack(String gridId, long processMillis, List<Invocation> invocations) {
			this.gridId = gridId;
			this.processMillis = processMillis;
			this.invocations = invocations;
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
		public List<Invocation> getInvocations() {
			return invocations;
		}
		public void setInvocations(List<Invocation> invocations) {
			this.invocations = invocations;
		}

		private String gridId;
		private long processMillis;
		private List<Invocation> invocations = Collections.emptyList();
	}
	public static class Invocation{
		public Invocation() {
		}
		public Invocation(String invocationName, List<GridTrack> gridTrack) {
			this.invocationName = invocationName;
			this.gridTrack = gridTrack;
		}
		public String getInvocationName() {
			return invocationName;
		}
		public void setInvocationName(String invocationName) {
			this.invocationName = invocationName;
		}
		public List<GridTrack> getGridTrack() {
			return gridTrack;
		}
		public void setGridTrack(List<GridTrack> gridTrack) {
			this.gridTrack = gridTrack;
		}

		private String invocationName;
		private List<GridTrack> gridTrack = Collections.emptyList();
	}

/*
	[
		[kyoto1.langrid,100],
		[usa1.openlangrid,200,[
			[MorphPL,[
				[usa1.openlangrid,200],
				[kyotooplg,128,[
					[MorphPL2,[
						[kyotooplg,200]
					]]
				]]
			]],
			[TransPL,[
				[kyotooplg,300]
			]]
		]],
		[kyoto0.langrid,164]
	]
*/
	public static List<GridTrack> decode(String text){
		return decodeGridTracks(JSON.decode(text));
	}
	private static List<GridTrack> decodeGridTracks(Object v){
		List<GridTrack> ret = new ArrayList<>();
		if(!(v instanceof List)) return ret;
		List<?> gts = (List<?>)v;
		for(Object gt : gts){
			GridTrack g = decodeGridTrack(gt);
			if(g != null) ret.add(g);
		}
		return ret;

	}
	private static GridTrack decodeGridTrack(Object v){
		if(!(v instanceof List)) return null;
		Iterator<?> it = ((List<?>)v).iterator();
		if(!it.hasNext()) return null;
		String gridId = it.next().toString();
		if(!it.hasNext()) return null;
		long processMillis = Long.valueOf(it.next().toString());
		List<Invocation> invocations = it.hasNext() ? decodeInvocations(it.next()) : Collections.emptyList();
		return new GridTrack(gridId, processMillis, invocations);
	}
	private static List<Invocation> decodeInvocations(Object ivs){
		List<Invocation> ret = new ArrayList<>();
		if(!(ivs instanceof Map)) return ret;
		for(Map.Entry<?, ?> iv : ((Map<?, ?>)ivs).entrySet()){
			Invocation i = decodeInvocation(iv);
			if(i != null) ret.add(i);
		}
		return ret;
	}
	private static Invocation decodeInvocation(Map.Entry<?, ?> v){
		String name = v.getKey().toString();
		return new Invocation(name, decodeGridTracks(v.getValue()));
	}
}
