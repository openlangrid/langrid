/*
 * $Id: SuspensionOfLanguageServiceListAdminPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin;

import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.model.provider.LangridSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.language.service.SuspensionOfLanguageServiceListPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.ServiceListPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.SuspensionOfLanguageServicesListRowPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class SuspensionOfLanguageServiceListAdminPage
extends SuspensionOfLanguageServiceListPage
{	
	@Override
	protected <T extends ServiceModel> ServiceListPanel<T> createListPanel(
			String listId, LangridSortableDataProvider<T> provider, LangridSearchCondition conditions)
	throws ServiceManagerException
	{
		conditions.setScope(Scope.ALL);
		provider.setConditions(conditions.getConditions(), conditions.getOrders(), conditions.getScope());
		ServiceListPanel<T> list = new ServiceListPanel<T>(getSelfGridId(), listId, provider){
			@Override
			protected Panel getRowPanel(String gridId, Item<T> item, String uniqueId)
			throws ServiceManagerException
			{
				return new SuspensionOfLanguageServicesListRowPanel(
						"row", item.getModelObject(), uniqueId)
				{
					@Override
					protected void doCancelProcess(String serviceId){
						cancelProcess(serviceId);
						setResponsePage(new SuspensionOfLanguageServiceCancelResultAdminPage(serviceId));
					}
					
					@Override
					protected void doRestartProcess(String serviceId){
						restartProcess(serviceId);
						setResponsePage(new SuspensionOfLanguageServiceRestartResultAdminPage(serviceId));
					}
					
					@Override
					protected Page getDoSuspendPage(String serviceId){
						return new SuspensionOfLanguageServiceAdminPage(serviceId);
					}
					
					@Override
					protected String getOrganizationName(ServiceModel model)
					throws ServiceManagerException
					{
						UserModel ue = ServiceFactory.getInstance().getUserService(model.getGridId()).get(model.getOwnerUserId());
						return ue == null ? "" : ue.getOrganization();
					}
				};
			}
		};
		return list;
	}
}
