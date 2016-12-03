package org.langrid.service.api.lapps_nlp;

import java.util.HashMap;
import java.util.Map;

public class Annotation {
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Map<String, Object> getFeatures() {
		return features;
	}
	public void setFeatures(Map<String, Object> features) {
		this.features = features;
	}
	private String id;
	private int start;
	private int end;
	private String label;
	private Map<String, Object> features = new HashMap<String, Object>();
}
