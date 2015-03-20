package jp.go.nict.langrid.management.web.view.component.label;

import jp.go.nict.langrid.management.web.utility.StringUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class HyphenedUrlExtractionLabel extends Label {
	/**
	 * 
	 * 
	 */
	public HyphenedUrlExtractionLabel(String componentId, String value) {
		super(componentId, new Model<String>());
		value = StringUtil.nullTohyphen(value);
		value = StringUtil.replaceURLStringToLinkTags(value).replaceAll("\n", "<br/>");
		setDefaultModelObject(value);
		setEscapeModelStrings(false);
	}
}
