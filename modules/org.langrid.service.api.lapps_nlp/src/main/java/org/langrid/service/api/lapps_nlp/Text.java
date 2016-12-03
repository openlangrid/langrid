package org.langrid.service.api.lapps_nlp;

import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONHint;

public class Text {
	@JSONHint(name="@value")
	public String getValue() {
		return value;
	}
	@JSONHint(name="@value")
	public void setValue(String value) {
		this.value = value;
	}
	private String value;
}
