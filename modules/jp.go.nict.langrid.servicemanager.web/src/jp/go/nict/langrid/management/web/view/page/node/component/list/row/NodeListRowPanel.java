package jp.go.nict.langrid.management.web.view.page.node.component.list.row;

import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.node.component.link.NodeProfileLink;
import jp.go.nict.langrid.management.web.view.page.user.component.label.OrganizationLabel;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeEntry;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class NodeListRowPanel extends Panel {
	/**
	 * 
	 * 
	 */
	public NodeListRowPanel(String componentId, NodeModel node, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		NodeProfileLink link = new NodeProfileLink(
			"nodeName", node.getGridId(), node.getNodeId(), uniqueId);
		add(link.add(new Label("nodeNameLabel", node.getNodeName())
			.add(new AttributeAppender("style",
				new Model<String>(node.getNodeName()), ""))));
		add(new Label("nodeUrl", node.getUrl().toString()));
		String name = getOrganizationName(node);
		Label labelProvider;
		if(name.isEmpty()) {
			labelProvider = new OrganizationLabel("providerLabel", node.getOwnerUserOrganization());
			Link l;
			add(l = new Link("provider") {
				@Override
				public void onClick() {}
			});
			l.setEnabled(false);
			l.add(labelProvider);
		} else {
			labelProvider = new OrganizationLabel("providerLabel", name);
			Link l;
			add(l = new UserProfileLink("provider"
				, node.getGridId() , node.getOwnerUserId()
				, node.getGridId() + node.getNodeId() + node.getOwnerUserId()));
			l.add(labelProvider);
		}
		add(new Label("status", getStatus(node)));
	}

	protected String getOrganizationName(NodeModel node)
	throws ServiceManagerException
	{
		UserModel ue = ServiceFactory.getInstance().getUserService(
			node.getGridId()).get(node.getOwnerUserId());
		return ue == null ? "" : ue.getOrganization();
	}

	private String getStatus(NodeModel node) throws ServiceManagerException {
		String status;
		status = node.isActive()
			? MessageManager.getMessage("Common.label.status.Run", getLocale())
			: MessageManager.getMessage("Common.label.status.Suspended", getLocale());
		return status;
	}

	protected String getOrganizationName(NodeEntry entry) {
		return entry.getOwnerUserOrganization();
	}
}