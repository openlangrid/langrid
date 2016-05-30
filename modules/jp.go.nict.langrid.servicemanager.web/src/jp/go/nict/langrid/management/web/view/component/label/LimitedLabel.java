package jp.go.nict.langrid.management.web.view.component.label;

import jp.go.nict.langrid.management.web.utility.StringUtil;

import org.apache.wicket.markup.html.basic.Label;

public class LimitedLabel extends Label {
	public LimitedLabel(String labelId, String value, int count) {
		super(labelId, StringUtil.shortenString(value, count));
	}
}
