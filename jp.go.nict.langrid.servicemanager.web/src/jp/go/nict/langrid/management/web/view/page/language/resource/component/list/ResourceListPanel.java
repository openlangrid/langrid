/*
 * $Id: ResourceListPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.component.list;

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.list.AbstractListPanel;
import jp.go.nict.langrid.management.web.view.component.list.row.EmptyRowPanel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row.LanguageResourcesListRowPanel;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row.ResourceListHeaderPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ResourceListPanel extends AbstractListPanel<ResourceModel> {
	/**
	 * 
	 * 
	 */
	public ResourceListPanel(
			String gridId, String componentId, IDataProvider<ResourceModel> provider)
	{
		super(gridId, componentId, provider);
	}
	
	@Override
	protected void addListItem(String gridId, String rowId, Item<ResourceModel> item){
		try{
			item.add(getRowPanel(gridId, item, rowId));
		}catch(ServiceManagerException e){
			((ServiceManagerPage)getPage()).doErrorProcess(e);
		}
	}
	
	@Override
	protected String getListId(){
		return "resourceList";
	}

	@Override
	protected String getTopNavigatorId(){
		return "topNavigator1";
	}

	@Override
	protected String getUnderNavigatorId(){
		return "underNavigator1";
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected Panel getRowPanel(String gridId, Item<ResourceModel> item, String uniqueId)
	throws ServiceManagerException
	{
		LanguageResourcesListRowPanel row = new LanguageResourcesListRowPanel(
			gridId, "row", item.getModelObject(), uniqueId)
		{
			@Override
			protected String getOrganizationName(String gridId, ResourceModel resource)
			throws ServiceManagerException
			{
				return getOrganization(gridId, resource);
			}
	
			private static final long serialVersionUID = 1L;
		};
		return row;
	}
	
	protected String getOrganization(String gridId, ResourceModel resource)
	throws ServiceManagerException
	{
		UserModel ue = ServiceFactory.getInstance().getUserService(gridId).get(resource.getOwnerUserId());
		return ue == null ? "" : ue.getOrganization();
	}

	@Override
	protected Panel getEmptyRowPanel(){
		return new EmptyRowPanel("emptyRow", 5);
	}

	@Override
	protected Panel getHeaderPanel(){
		return new ResourceListHeaderPanel("header");
	}
}
