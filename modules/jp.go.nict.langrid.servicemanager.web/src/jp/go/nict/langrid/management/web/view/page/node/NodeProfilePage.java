/*
 * $Id: NodeProfilePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.page.node;

import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.PopupPage;
import jp.go.nict.langrid.management.web.view.page.node.component.panel.NodeProfilePanel;

import org.apache.wicket.PageParameters;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class NodeProfilePage extends PopupPage {
	/**
	 * 
	 * 
	 */
	public NodeProfilePage(PageParameters pp) {
		try {
			String gridId = pp.getString("gridId");
			String nodeId = pp.getString("id");
			if(gridId == null || nodeId == null) {
				redirectTop();
				return;
			}
			NodeModel node = ServiceFactory.getInstance().getNodeService(gridId).get(nodeId);
			if(node == null) {
				redirectTop();
				return;
			}
			add(new NodeProfilePanel("profile", gridId, node));
		} catch(ServiceManagerException e) {
			doErrorProcessForPopup(e);
		}
	}
}
