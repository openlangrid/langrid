package jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.component.list.row;

import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.management.web.model.OverUseStateModel;
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
public class FraudulentUsageListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public FraudulentUsageListRowPanel(
			String componentId, OverUseStateModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		add(new Label("lastAccessDate", DateUtil.formatYMDHMWithSlash(entry.getLastAccessDateTime().getTime())));
		String organization = ServiceFactory.getInstance().getUserService(entry.getUserGridId()).get(entry.getUserId()).getOrganization();
		Link link = new UserProfileLink("provider", entry.getUserGridId(), entry.getUserId(), uniqueId);
		link.add(new Label("labelProvider", StringUtil.shortenString(organization, 24)));
		add(link);
		
		if(entry.getLimitType().equals(LimitType.CAPACITY)){
			add(new Label("dateTransferSize", String.valueOf((entry.getCurrentCount() / 1024) + " KB")));
			add(new Label("accessCount", "-"));
			add(new Label("limitCount", String.valueOf((entry.getLimitCount() / 1024) + " KB")));
		}else{
			add(new Label("dateTransferSize", "-"));
			add(new Label("accessCount", String.valueOf(entry.getCurrentCount())));
			add(new Label("limitCount", String.valueOf(entry.getLimitCount())));
		}
		
		LangridServiceService<ServiceModel> service= ServiceFactory.getInstance().getLangridServiceService(entry.getServiceGridId());
		ServiceModel sm = service.get(entry.getServiceId());
		if(sm == null){
			add(new Label("serviceName", StringUtil.shortenString(entry.getServiceId(), 24)));
		}else{
			add(new Label("serviceName", sm.getServiceName()));
		}
	}
}
