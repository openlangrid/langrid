/*
 * $Id: YourComputationResourceEditPage.java 8823 2009-01-21 05:25:09Z Masaaki Kamiya $
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
import jp.go.nict.langrid.management.web.model.service.NodeService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.node.component.form.panel.NodeProfileFieldPanel;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: Masaaki Kamiya $
 * @version $Revision$
 */
public class YourNodeEditPage extends ServiceManagerPage{
	public YourNodeEditPage(String nodeId) {
		if(nodeId == null) {
			throw new RestartResponseException(new YourNodePage());
		}
		nId = nodeId;
		try {
			NodeModel model = ServiceFactory.getInstance().getNodeService(getSelfGridId()).get(nodeId);
			add(form = new Form<NodeModel>("form", new CompoundPropertyModel<NodeModel>(model)));
			form.add(new Label("nodeId", nId));
			form.add(profilePanel = new NodeProfileFieldPanel(getSelfGridId(), "profileFieldPanel"));
			form.add(new Button("update") {
				@Override
				public void onSubmit() {
					try {
						NodeModel nm = form.getModelObject();
						NodeService service = ServiceFactory.getInstance().getNodeService(getSelfGridId());
						service.edit(nm);
						setResponsePage(new YourNodeEditResultPage(nId));
					} catch(ServiceManagerException e) {
						doErrorProcess(e);
					}
				}
			});
			
			form.add(new Link("cancel") {
				@Override
				public void onClick() {
					setResponsePage(new YourNodePage());
				}
			});
			
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
	
	private Form<NodeModel> form;
	private String nId;
	private NodeProfileFieldPanel profilePanel;
}
