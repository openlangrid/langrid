package jp.go.nict.langrid.management.web.view.page.admin.federation.source.component.list.row;

import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.FederationGridType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.admin.federation.RequestOfDisconnectionPage;
import jp.go.nict.langrid.management.web.view.page.admin.federation.source.OperationOfConnectionRequestPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class FederatedSourceOrganizationListRowPanel extends Panel {
	/**
	 * 
	 * 
	 */
	public FederatedSourceOrganizationListRowPanel(
		String componentId, String selfGridId, FederationModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		sourceGridId = entry.getSourceGridId();
		targetGridId = entry.getTargetGridId();
		gridId = selfGridId;
		
		ServiceFactory factory = ServiceFactory.getInstance();
		
		GridModel sourceGrid= factory.getGridService().get(sourceGridId);
		UserModel user = null;
		
		if(sourceGrid != null){
			String sourceOperatorId = sourceGrid.getOperatorUserId();
			user = factory.getUserService(sourceGridId).get(sourceOperatorId);
		}

		if(user == null){
			user = factory.getUserService(entry.getTargetGridId()).get(
				entry.getTargetGridUserId());
		}
		
		add(new Label("gridId", entry.getSourceGridId()));
		add(new Label("gridName", entry.getSourceGridName()));
		add(new ExternalHomePageLink("homePage", user.getHomepageUrl() == null ? "" : user.getHomepageUrl().getValue().toString(), uniqueId, 24));
		UserProfileLink link = new UserProfileLink("organization", user.getGridId(), user.getUserId(), uniqueId);
		link.add(new Label("organizationLabel", user.getOrganization()));
		add(link);
		add(new Label("status", entry.isRequesting()
			? MessageManager.getMessage(
				"LanguageGridOperator.federation.message.status.Pending", getLocale())
			: MessageManager.getMessage(
				"LanguageGridOperator.federation.message.status.Connect", getLocale())));
		Link accept;
		add(accept = new Link("accept") {
			@Override
			public void onClick() {
				setResponsePage(new OperationOfConnectionRequestPage(sourceGridId, targetGridId));
			}
		});
		accept.setVisible(entry.isRequesting() && !entry.getSourceGridId().equals(selfGridId));
		add(new Link<String>("delete", new Model<String>(entry.getSourceGridId())) {	
			@Override
			public void onClick() {
				try {
					if ( ServiceFactory.getInstance().getFederationService(gridId).get(sourceGridId, targetGridId) == null ) {
						onErrorRaised(MessageManager.getMessage("LanguageGridOperator.federation.message.disconnect.Already"));
					} else {
						setResponsePage(new RequestOfDisconnectionPage(getModelObject(), gridId, FederationGridType.TARGETGRID));
					}
				} catch (ServiceManagerException e) {
					onErrorRaised(MessageManager.getMessage("LanguageGridOperator.federation.message.disconnect.Already"));
				}
			}
		}.setVisible(!entry.isRequesting()));
	}
	
	protected void onErrorRaised(String errorMessage){
		error(errorMessage);
	}

	private String sourceGridId;
	private String targetGridId;
	private String gridId;
	private static final long serialVersionUID = 1L;
}
