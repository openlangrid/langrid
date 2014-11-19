package jp.go.nict.langrid.management.web.view.page.node.component.panel;

import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedUrlExtractionLabel;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public class NodeProfilePanel extends Panel {
	public NodeProfilePanel(String panelId, String gridId, NodeModel node)
	throws ServiceManagerException
	{
		super(panelId);
		add(new Label("nodeId", node.getNodeId()));
		add(new Label("serverName", node.getNodeName()));
		add(new HyphenedUrlExtractionLabel("url", node.getUrl().toString()));
		add(new Label("serverOs", node.getOs()));
		add(new Label("serverCpu", node.getCpu()));
		add(new Label("serverMemory", node.getMemory()));
		add(new HyphenedUrlExtractionLabel("specialNote", node.getSpecialNotes()));
		UserModel user = ServiceFactory.getInstance().getUserService(gridId)
			.get(node.getOwnerUserId());
		Label labelProvider;
		if(user == null) {
			labelProvider = new Label("providerLinkLabel",
				node.getOwnerUserOrganization());
			Link l;
			add(l = new Link("providerLink") {
				@Override
				public void onClick() {}
			});
			l.setEnabled(false);
			l.add(labelProvider);
		} else {
			labelProvider = new Label("providerLinkLabel", user.getOrganization());
			Link l;
			add(l = new UserProfileLink("providerLink", node.getGridId()
				, node.getOwnerUserId(), node.getGridId() + node.getNodeId() + node.getOwnerUserId()));
			l.add(labelProvider);
		}
		add(new Label("registration", DateUtil.formatYMDWithSlash(
			node.getCreatedDateTime().getTime())));
		add(new Label("updateDate", DateUtil.formatYMDWithSlash(
			node.getUpdatedDateTime().getTime())));
		add(new Label("status", node.isActive()
			? MessageManager.getMessage("Common.label.status.Run", getLocale())
			: MessageManager.getMessage("Common.label.status.Suspended", getLocale())));
	}
}
