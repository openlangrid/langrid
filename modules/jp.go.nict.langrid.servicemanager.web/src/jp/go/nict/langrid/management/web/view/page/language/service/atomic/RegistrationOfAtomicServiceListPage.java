/*
 * $Id: RegistrationOfAtomicServiceListPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.atomic;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.provider.ResourceSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.list.ResourceListPanel;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row.RegistrationOfAtomicServiceListRowPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RegistrationOfAtomicServiceListPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfAtomicServiceListPage(){
		rewritableContainer = new WebMarkupContainer("rewritableContainer");
		rewritableContainer.setOutputMarkupId(true);
		try{
			rewritableContainer.add(getListPanel("resourceList", getProvider()));
			add(rewritableContainer);
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
	}
	
	protected ResourceListPanel getListPanel(
			String panelId, ResourceSortableDataProvider provider)
	{
		return new ResourceListPanel(getSelfGridId(), panelId, provider) {
			@Override
			protected Panel getRowPanel(String gridId, Item<ResourceModel> item, String uniqueId)
			throws ServiceManagerException
			{
				return new RegistrationOfAtomicServiceListRowPanel(
						gridId, "row", item.getModelObject(), uniqueId)
				{
					@Override
					protected String getOrganizationName(String gridId, ResourceModel resource)
					throws ServiceManagerException
					{
						return getOrganization(gridId, resource);
					}
				};
			}
		};
	}
	
	protected ResourceSortableDataProvider getProvider()
	throws ServiceManagerException
	{
		ResourceSortableDataProvider provider= new ResourceSortableDataProvider(
				getSelfGridId(), getSessionUserId());
		provider.setConditions(new MatchingCondition[]{
					new MatchingCondition("approved", true)
			}, new Order[]{new Order("resourceName", OrderDirection.ASCENDANT)}, Scope.MINE);
		return provider;
	}
	
	private WebMarkupContainer rewritableContainer;
}
