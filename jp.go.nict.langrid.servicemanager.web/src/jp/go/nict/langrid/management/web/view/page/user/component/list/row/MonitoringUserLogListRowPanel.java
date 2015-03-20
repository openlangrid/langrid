package jp.go.nict.langrid.management.web.view.page.user.component.list.row;

import jp.go.nict.langrid.management.web.model.IndividualExecutionInformationModel;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class MonitoringUserLogListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public MonitoringUserLogListRowPanel(
			String componentId, IndividualExecutionInformationModel log, String uniqueId, String organization)
	{
		super(componentId);
		Link link = new UserProfileLink("provider", log.getUserGridId(), log.getUserId(), uniqueId);
		link.add(new Label("labelProvider", organization));
		add(link);
		add(new Label("ipAddress", log.getAddress()));
		add(new Label("dataSize", String.valueOf(log.getResponseBytes())));
		add(new Label("accessDate", DateUtil.formatYMDHMSWithSlash(
				log.getDateTime().getTime())));
	}
	
}
