package jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.component.list.row;

import jp.go.nict.langrid.management.web.model.IndividualExecutionInformationModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class FraudulentUsageEachTimeListRowPanel extends Panel {
	/**
	 * 
	 * 
	 */
	public FraudulentUsageEachTimeListRowPanel(
			String componentId, IndividualExecutionInformationModel entry,
		String uniqueId, int limitCount)
	throws ServiceManagerException
	{
		super(componentId);
		add(new Label("accessTime", DateUtil.formatYMDHMWithSlash(
			entry.getDateTime().getTime())));
		String organization = ServiceFactory.getInstance().getUserService(
			entry.getUserGridId()).get(entry.getUserId()).getOrganization();
		Link link = new UserProfileLink("provider", entry.getUserGridId()
			, entry.getUserId(), uniqueId);
		link.add(new Label("labelProvider", StringUtil.shortenString(organization, 24)));
		add(link);
		add(new Label("accessCount", "-"));
		add(new Label("dateTransferSize", String.valueOf((entry.getResponseBytes() / 1024) + " KB")));
		add(new Label("limitCount", String.valueOf((limitCount / 1024) + " KB")));
		LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(
			entry.getServiceGridId());
		ServiceModel sm = service.get(entry.getServiceId());
		if(sm == null) {
			add(new Label("serviceName", StringUtil.shortenString(entry.getServiceId(), 24)));
		} else {
			add(new Label("serviceName", sm.getServiceName()));
		}
	}
}
