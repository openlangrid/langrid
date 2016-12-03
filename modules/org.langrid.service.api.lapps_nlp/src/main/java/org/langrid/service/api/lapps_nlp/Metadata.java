package org.langrid.service.api.lapps_nlp;

import java.util.HashMap;
import java.util.Map;

public class Metadata {
	public Map<String, Object> getContains() {
		return contains;
	}
	public void setContains(Map<String, Object> contains) {
		this.contains = contains;
	}
	private Map<String, Object> contains = new HashMap<String, Object>();
}
