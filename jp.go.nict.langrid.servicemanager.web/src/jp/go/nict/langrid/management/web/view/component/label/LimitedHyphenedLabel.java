package jp.go.nict.langrid.management.web.view.component.label;

import jp.go.nict.langrid.management.web.utility.StringUtil;

import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class LimitedHyphenedLabel extends Label {
	/**
	 * 
	 * 
	 */
	public LimitedHyphenedLabel(String labelId, String value, int count) {
		super(labelId, StringUtil.shortenString((value == null || value.equals("") ? "-" : value), count));
	}
}
