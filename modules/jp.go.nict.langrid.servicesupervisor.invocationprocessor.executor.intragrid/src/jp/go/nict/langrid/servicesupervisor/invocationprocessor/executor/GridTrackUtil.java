package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	"kyoto1.langrid:100 ->"
	+ " usa1.openlangrid:200("
		+ "MorphPL:usa1.openlangrid:200 ->"
		+ " kyotooplg:128(MorphPL:kyotooplg:200) ->"
		+ " TransPL:kyotooplg:300"
		+ ") ->"
	+ " kyoto0.langrid:164");
*/
	public static List<GridTrack> decode(String text){
		return decode(newScanner(text));
	}
	private static List<Invocation> decodeInvocations(Scanner s){
		List<Invocation> ret = new ArrayList<>();
		while(s.next()){
			String in = s.getToken();
			List<GridTrack> children = decode(s);
			ret.add(new Invocation(in, children));
			if(!s.getDelim().equals(",")) break;
		}
		return ret;
	}
	private static List<GridTrack> decode(Scanner s){
		List<GridTrack> ret = new ArrayList<>();
		String gid = null;
		List<Invocation> invocations = Collections.emptyList();
		long processMillis = 0;
		while(s.next()){
			if(s.getDelim().equals(":")){
				gid = s.getToken();
			} else if(s.getDelim().equals(" -> ")){
				if(gid != null && s.getToken().length() > 0){
					processMillis = Long.valueOf(s.getToken());
					ret.add(new GridTrack(gid, processMillis, invocations));
				}
				gid = null;
				processMillis = 0;
				invocations = Collections.emptyList();
			} else if(s.getDelim().equals("(")){
				if(s.getToken().length() > 0) processMillis = Long.valueOf(s.getToken());
				invocations = decodeInvocations(s);
				ret.add(new GridTrack(gid, processMillis, invocations));
				gid = null;
				processMillis = 0;
				invocations = Collections.emptyList();
			} else if(s.getDelim().equals(")") || s.delim.equals(",")){
				if(gid != null && s.getToken().length() > 0){
					processMillis = Long.valueOf(s.getToken());
					ret.add(new GridTrack(gid, processMillis, invocations));
				}
				gid = null;
				break;
			}
		}
		if(gid != null){
			processMillis = Long.valueOf(s.getToken());
			ret.add(new GridTrack(gid, processMillis, invocations));
		}
		return ret;
	}

	static Scanner newScanner(String text){
		return new Scanner(Pattern.compile(pat), text);
	}
	static class Scanner {
		public Scanner(Pattern pattern, String text){
			this.matcher = pattern.matcher(text);
		}

		public boolean next(){
			boolean ret = matcher.find();
			if(ret){
				StringBuffer b = new StringBuffer();
				matcher.appendReplacement(b, "");
				token = b.toString();
				delim = matcher.group(0);
			} else{
				StringBuffer b = new StringBuffer();
				matcher.appendTail(b);
				token = b.toString();
				delim = null;
			}
			return ret;
		}

		public String getToken(){
			return token;
		}
		public String getDelim() {
			return delim;
		}

		private Matcher matcher;
		private String token;
		private String delim;
	}

	private static String pat = "( \\-> |:|\\(|\\)|,)";
}
