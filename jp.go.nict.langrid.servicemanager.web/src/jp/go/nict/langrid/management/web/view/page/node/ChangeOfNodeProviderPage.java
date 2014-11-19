/*
 * $Id: YourComputationResourceEditResultPage.java 8823 2009-01-21 05:25:09Z Masaaki Kamiya $
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
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.node.component.panel.NodeProfilePanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;

/**
 * 計算資源提供者変更確認用ページ
 * 
 * @author $Author: Masaaki Kamiya $
 * @version $Revision$
 */
public class ChangeOfNodeProviderPage extends ServiceManagerPage {
	public ChangeOfNodeProviderPage(UserModel user, NodeModel node) {
		node.setOwnerUserId(user.getUserId());
		nModel = node;
		Form form = new Form("form");
		add(form);
		try {
			add(new NodeProfilePanel("profile", node.getGridId(), node));
			form.add(new Link("cancel") {
				@Override
				public void onClick() {
					setResponsePage(new YourNodePage());
				}
			});
			form.add(new SubmitLink("submit") {
				@Override
				public void onSubmit() {
					try {
						ServiceFactory.getInstance().getNodeService(getSelfGridId()).edit(nModel);
						setResponsePage(new ChangeOfNodeProviderResultPage(nModel));
					} catch(ServiceManagerException e) {
						doErrorProcess(e);
					}
				}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
	
	private NodeModel nModel;
}

