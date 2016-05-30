/*
 * $Id: AllOperatorsPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.admin.federation;

import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.provider.FederationSourceSortableDataProvider;
import jp.go.nict.langrid.management.web.view.model.provider.FederationTargetSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.federation.component.list.FederatedOrganizationListPanel;
import jp.go.nict.langrid.management.web.view.page.admin.federation.source.component.list.row.FederatedSourceOrganizationListRowPanel;
import jp.go.nict.langrid.management.web.view.page.admin.federation.target.RequestOfConnectionPage;
import jp.go.nict.langrid.management.web.view.page.admin.federation.target.component.list.row.FederatedTargetOrganizationListRowPanel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class AllOperatorsPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public AllOperatorsPage() {
		try {
		   final String gridId = getSelfGridId();
			FederationTargetSortableDataProvider provider = new FederationTargetSortableDataProvider(gridId, getSessionUserId());
			add(new FederatedOrganizationListPanel(getSelfGridId(), "targetOrganizationListPanel", provider) {
				@Override
				protected Panel getRowPanel(String gridId, Item<FederationModel> item,
						String uniqueId) throws ServiceManagerException {
					return  new FederatedTargetOrganizationListRowPanel("row", gridId, item.getModelObject(), uniqueId){
						@Override
						protected void onErrorRaised(String errorMessage) {
							FeedbackPanel fp = getFeedbackPanel();
							fp.warn(errorMessage);
							fp.add(new AttributeModifier("class", true, new Model<String>("feedback-warn")));
						}
					};
				}
			});
			
			FederationSourceSortableDataProvider sourceProvider = new FederationSourceSortableDataProvider(gridId, getSessionUserId());
			add(new FederatedOrganizationListPanel(getSelfGridId(), "sourceOrganizationListPanel", sourceProvider) {
				@Override
				protected Panel getRowPanel(String gridId, Item<FederationModel> item, String uniqueId)
				throws ServiceManagerException{
					return  new FederatedSourceOrganizationListRowPanel("row", gridId, item.getModelObject(), uniqueId){
						@Override
						protected void onErrorRaised(String message) {
							getFeedbackPanel().warn(message);
							getFeedbackPanel().add(new AttributeModifier("class", true, new Model<String>("feedback-warn")));
						}
					};
				}
			});
			
			add(new Link("request") {
				@Override
				public void onClick() {
					setResponsePage(new RequestOfConnectionPage());
				}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
}
