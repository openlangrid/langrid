package jp.go.nict.langrid.management.web.view.page.admin.component.list.row;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.admin.federation.source.FederalOperationRequestPage;
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
 * @version $Revision: 303 $
 */
public class FederalOperationRequestListRowPanel extends Panel{
	/**
	 * 
	 * 
	 * @throws ServiceManagerException
	 */
	public FederalOperationRequestListRowPanel(
		String gridId, String componentId, OperationRequestModel operation, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		sourceGridId = operation.getTargetId();
		targetGridId = gridId;
		add(new Label("date", DateUtil.formatYMDWithSlash(
				operation.getCreatedDateTime().getTime())));
		Map<String, String> map = new HashMap<String, String>();
		map.put("gridId", operation.getTargetId());
		Label label = new Label("operation"
		   , MessageManager.getMessage("operation.request.federation.Connect", map)
		);
		label.setEscapeModelStrings(false);
		add(label);
		UserModel user = ServiceFactory.getInstance().getUserService(gridId).get(operation.getRequestedUserId());
		if(user == null){
		   Link link = new Link("organization"){@Override public void onClick() {}};
		   link.add(new Label("organizationLabel", "-"));
		   link.setEnabled(false);
		   add(link);
		}else{
		   UserProfileLink link = new UserProfileLink(
   				"organization", operation.getGridId(), operation.getRequestedUserId(), uniqueId);
   		link.add(new Label("organizationLabel", StringUtil.shortenString(user.getOrganization(), 24)));
		   add(link);
		}
		add(new Link<OperationRequestModel>("operate", new Model<OperationRequestModel>(operation)){
			@Override
			public void onClick() {
				setResponsePage(new FederalOperationRequestPage(sourceGridId, targetGridId));
			}
		});
	}

	private String sourceGridId;
	private String targetGridId;
	private static final long serialVersionUID = 1L;
}
