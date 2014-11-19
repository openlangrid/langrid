/*
 * $Id: OperatersOfConnectedLanguageGridPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.federation.component.list.FederatedOrganizationListPanel;
import jp.go.nict.langrid.management.web.view.page.admin.federation.component.list.row.FederatedOrganizationListRowPanel;
import jp.go.nict.langrid.management.web.view.page.user.UserProfilePage;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class OperatersOfConnectedLanguageGridPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public OperatersOfConnectedLanguageGridPage(){
		try {
			FederationSourceSortableDataProvider provider = new FederationSourceSortableDataProvider(
			   getSelfGridId(), getSessionUserId());
			add(getListPanel("orgPanel", provider));
			add(getListPanel("affiliatedOrgPanel", provider));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
	
	protected FederatedOrganizationListPanel getListPanel(
		String panelId, FederationSourceSortableDataProvider provider)
	{
		return new FederatedOrganizationListPanel(getSelfGridId(), panelId, provider){
			@Override
			protected Panel getRowPanel(
				String gridId, Item<FederationModel> item, String uniqueId)
			throws ServiceManagerException
			{
				return new FederatedOrganizationListRowPanel("row", gridId, item.getModelObject(), uniqueId);
			}
		};
	}
}
