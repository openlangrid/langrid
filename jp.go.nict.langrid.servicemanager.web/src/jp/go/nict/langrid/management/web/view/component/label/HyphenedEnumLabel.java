package jp.go.nict.langrid.management.web.view.component.label;

import org.apache.wicket.markup.html.basic.EnumLabel;

public class HyphenedEnumLabel<T extends Enum> extends EnumLabel {
	public HyphenedEnumLabel(String componentId, T value) {
		super(componentId, value);
	}
	
	@Override
	protected String nullValue() {
		return "-";
	}
}
