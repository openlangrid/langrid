package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.utility.DateUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class MonitoringUserLogListHeaderPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public MonitoringUserLogListHeaderPanel(String componentId) {
		super(componentId);
		add(new Label("timeZone", "(" + DateUtil.defaultTimeZone() + ")"));
	}

}
