package jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row;

import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ResourceListHeaderPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public ResourceListHeaderPanel(String componentId) {
		super(componentId);
		add(new ExternalLink("linkLanguagePage", MessageUtil.ISO_LANGUAGE_CODE_URL));
	}

	private static final long serialVersionUID = 1L;
}
