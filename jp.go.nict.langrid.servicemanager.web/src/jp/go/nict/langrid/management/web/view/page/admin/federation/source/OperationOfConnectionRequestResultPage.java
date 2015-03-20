package jp.go.nict.langrid.management.web.view.page.admin.federation.source;

import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.federation.AllOperatorsPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class OperationOfConnectionRequestResultPage extends ServiceManagerPage {
	public OperationOfConnectionRequestResultPage(
		String sourceGridId, String targetGridId, String targetGridUserId,
		boolean isReject, String requestUrl)
	{
		add(new Label("isApproved", MessageManager.getMessage(
			isReject ? "LanguageGridOperator.federation.message.Reject"
				: "LanguageGridOperator.federation.message.Approved"
			)));
		add(new Label("sourceGridId", sourceGridId));
		try {
			UserModel user = ServiceFactory.getInstance().getUserService(targetGridId).get(targetGridUserId);
			UserProfileLink link = new UserProfileLink("organization", targetGridId,
				user.getUserId(), targetGridUserId);
			link.add(new Label("organizationLabel", user.getOrganization()));
			add(link);
			add(new ExternalHomePageLink("homePage", requestUrl, targetGridUserId));
		
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
		add(new Link("back") {
			@Override
			public void onClick() {
				setResponsePage(getBackPage());
			}
		});
	}

	protected Page getBackPage() {
		return new AllOperatorsPage();
	}
}
