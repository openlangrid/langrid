package jp.go.nict.langrid.commons.ws.soap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MimeHeaders {
	public String[] getHeader(String name){
		List<String> v = headers.get(name);
		if(v == null) return null;
		return v.toArray(new String[]{});
	}

	public void addHeader(String name, String value){
		List<String> v = headers.get(name);
		if(v == null){
			v = new ArrayList<>();
			headers.put(name, v);
		}
		v.add(value);
	}
	
	public Iterator<Map.Entry<String, List<String>>> getAllHeaders(){
		return headers.entrySet().iterator();
	}

	private Map<String, List<String>> headers = new LinkedHashMap<>();
}
