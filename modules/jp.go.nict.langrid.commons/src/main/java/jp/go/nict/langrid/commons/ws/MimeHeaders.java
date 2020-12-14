package jp.go.nict.langrid.commons.ws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.util.Pair;

public class MimeHeaders {
	public String[] getHeader(String name) {
		Pair<String, List<String>> h = headers.get(name.toLowerCase());
		if(h != null) {
			return h.getSecond().toArray(new String[] {});
		} else {
			return null;
		}
	}

	public String getJoinedHeader(String name, String separator) {
		List<String> values = headers.get(name.toLowerCase()).getSecond();
		if(values == null) return null;
		return StringUtil.join(values.toArray(new String[] {}), ",");
	}

	public String getJoinedHeader(String name) {
		return getJoinedHeader(name, ",");
	}

	public void setHeader(String name, String value){
		List<String> values = headers.computeIfAbsent(
				name.toLowerCase(), n -> Pair.create(name, new ArrayList<>()))
			.getSecond();
		values.clear();
		values.add(value);
	}

	public void addHeader(String name, String value){
		if(name == null) return;
		headers.computeIfAbsent(
				name.toLowerCase(), n -> Pair.create(name, new ArrayList<>()))
			.getSecond().add(value);
	}

	public void removeHeader(String name) {
		headers.remove(name.toLowerCase());
	}

	public void removeAllHeaders() {
		headers.clear();
	}

	public Iterator<Pair<String, String>> getAllHeaders() {
		List<Pair<String, String>> ret = new ArrayList<>();
		for(Pair<String, List<String>> h : headers.values()) {
			for(String v : h.getSecond()) {
				ret.add(Pair.create(h.getFirst(), v));
			}
		}
		return ret.iterator();
	}

	private Map<String, Pair<String, List<String>>> headers = new TreeMap<>();
}
