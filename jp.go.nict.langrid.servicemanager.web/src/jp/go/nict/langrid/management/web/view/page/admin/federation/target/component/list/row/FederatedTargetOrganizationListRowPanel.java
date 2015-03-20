package jp.go.nict.langrid.management.web.view.page.admin.federation.target.component.list.row;

import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.FederationGridType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.admin.federation.RequestOfDisconnectionPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.WebMarkupContainer;
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
public class FederatedTargetOrganizationListRowPanel extends Panel {
	/**
	 * 
	 * 
	 */
	public FederatedTargetOrganizationListRowPanel(
		String componentId, String selfGridId, FederationModel entry, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		sourceGridId = entry.getSourceGridId();
		targetGridId = entry.getTargetGridId();
		gridId = selfGridId;
		
		ServiceFactory factory = ServiceFactory.getInstance();
		
		GridModel targetGrid = factory.getGridService().get(targetGridId);
		UserModel user = null;
		if(targetGrid != null){
			String targetOperatorId = targetGrid.getOperatorUserId();
			user = factory.getUserService(targetGridId).get(targetOperatorId);
		}
		
		if(user == null){
			ExternalHomePageLink hLink = new ExternalHomePageLink("homePage"
				, entry.getTargetGridUserHomepage().toString(), uniqueId, 24);
			add(hLink);
			WebMarkupContainer wmc = new WebMarkupContainer("organization");
			add(wmc.setEnabled(false));
			wmc.add(new Label("organizationLabel", entry.getTargetGridUserOrganization()));
		}else{
			ExternalHomePageLink hLink = new ExternalHomePageLink("homePage"
				, user.getHomepageUrl() == null ? "" : user.getHomepageUrl().getValue().toString(), uniqueId, 24);
			add(hLink);
			UserProfileLink link = new UserProfileLink("organization", user.getGridId(), user.getUserId(), uniqueId);
			link.add(new Label("organizationLabel", user.getOrganization()));
			add(link);
		}
		
		
		add(new Label("gridId", entry.getTargetGridId()));
		add(new Label("gridName", entry.getTargetGridName()));

		add(new Label("status", entry.isRequesting()
				? MessageManager.getMessage(
					"LanguageGridOperator.federation.message.status.Pending", getLocale())
				: MessageManager.getMessage(
					"LanguageGridOperator.federation.message.status.Connect", getLocale())));
		add(new Link<String>("delete", new Model<String>(entry.getTargetGridId())) {
			@Override
			public void onClick() {
				try {
					if ( ServiceFactory.getInstance().getFederationService(gridId).get(sourceGridId, targetGridId) == null ) {
						onErrorRaised(MessageManager.getMessage("LanguageGridOperator.federation.message.disconnect.Already"));
					} else {
						setResponsePage(new RequestOfDisconnectionPage(gridId, getModelObject(), FederationGridType.SOURCEGRID));
					}
				} catch (ServiceManagerException e) {
					onErrorRaised(MessageManager.getMessage("LanguageGridOperator.federation.message.disconnect.Already"));
				}
			}	
		}.setVisible( ! entry.isRequesting()));
	}
	
	protected void onErrorRaised(String errorMessage){
		error(errorMessage);
	}

	private String sourceGridId;
	private String targetGridId;
	private String gridId;
	
	private static final long serialVersionUID = 1L;
}
