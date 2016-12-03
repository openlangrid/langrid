package org.langrid.service.api.lapps_nlp;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONHint;

public class Payload {
	public String getContext() {
		return context;
	}
	@JSONHint(name="@context")
	public void setContext(String context) {
		this.context = context;
	}
	public String getMetadata() {
		return metadata;
	}
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public List<View> getViews() {
		return views;
	}
	public void setViews(List<View> views) {
		this.views = views;
	}
	private String context;
	private String metadata;
	private Text text;
	private List<View> views = new ArrayList<View>();
}
