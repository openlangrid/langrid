package jp.go.nict.langrid.management.web.view.page.node.component.list.row;

import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.node.ChangeOfNodeProviderUserListPage;
import jp.go.nict.langrid.management.web.view.page.node.YourNodeEditPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: Masaaki Kamiya $
 * @version $Revision: 12148 $
 */
public class YourNodeListRowPanel extends NodeListRowPanel {
	/**
	 * 
	 * 
	 */
	public YourNodeListRowPanel(String gridId, String componentId, NodeModel node,
		String uniqueId)
	throws ServiceManagerException {
		super(componentId, node, uniqueId);
		this.model = node;
		add(new Link<NodeModel>("edit", new Model<NodeModel>(model)) {
			@Override
			public void onClick() {
				setResponsePage(getDoEditPage(getModelObject()));
			}

			private static final long serialVersionUID = 1L;
		});
		add(new Link<NodeModel>("change", new Model<NodeModel>(model)) {
			@Override
			public void onClick() {
				setResponsePage(new ChangeOfNodeProviderUserListPage(getModelObject()));
			}
			
			private static final long serialVersionUID = 1L;
		});
	}

	protected Page getDoEditPage(NodeModel node) {
		return new YourNodeEditPage(node.getNodeId());
	}

	private NodeModel model;
}
