package jp.go.nict.langrid.management.web.view.page.admin.federation.component.list.row;

import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public class FederatedOrganizationListRowPanel extends Panel {
	public FederatedOrganizationListRowPanel (
	   String componentId, String selfGridId, FederationModel entry, String uniqueId)
	throws ServiceManagerException
	{
	   super(componentId);
		UserModel user = ServiceFactory.getInstance().getUserService(entry.getTargetGridId()).get(
         entry.getTargetGridUserId());
      add(new Label("gridId", entry.getSourceGridId()));
      add(new Label("gridName", entry.getSourceGridName()));
      if(user == null){
         Link hLink = new Link("homePage") {@Override public void onClick() {}};
         hLink.setEnabled(false);
         add(hLink);
         hLink.add(new Label("homePageLabel", "-"));
         Link link = new Link("organization"){@Override public void onClick() {}};
         link.setEnabled(false);
         add(link);
         link.add(new Label("organizationLabel", "-"));
      }else{
         add(new ExternalHomePageLink("homePage", user.getHomepageUrl() == null ? "" : user.getHomepageUrl().getValue().toString(), uniqueId, 24));
         UserProfileLink link = new UserProfileLink("organization", entry.getTargetGridId(), user.getUserId(), uniqueId);
         link.add(new Label("organizationLabel", user.getOrganization()));
         add(link);
      }
      add(new Label("status", entry.isRequesting() ? "Pending approval" : "Connecting"));
	}

	private static final long serialVersionUID = 1L;
}
