/*
 * $Id: ServiceMetaAttributeListPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.list;

import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.component.list.AbstractListPanel;
import jp.go.nict.langrid.management.web.view.component.list.row.EmptyRowPanel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.list.row.ServiceMetaAttributeListHeaderPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.list.row.ServiceMetaAttributeListRowPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ServiceMetaAttributeListPanel extends AbstractListPanel<ServiceMetaAttributeModel>{
	/**
	 * 
	 * 
	 */
	public ServiceMetaAttributeListPanel(
			String gridId, String componentId, IDataProvider<ServiceMetaAttributeModel> dataProvider)
	{
		super(gridId, componentId, dataProvider);
	}

	protected void addItem(Item<ServiceMetaAttributeModel> item){
		//noop
	}

	@Override
	protected void addListItem(String gridId, String rowId, Item<ServiceMetaAttributeModel> item) {
		Panel row;
		try {
			row = getRowPanel(gridId, item, rowId);
			item.add(row);
		}catch(ServiceManagerException e){
			ServiceManagerPage page = (ServiceManagerPage)getPage();
			page.doErrorProcess(e);
		}
	}

	@Override
	protected Panel getEmptyRowPanel(){
		return new EmptyRowPanel("emptyRow", 3);
	}

	@Override
	protected Panel getHeaderPanel(){
		return new ServiceMetaAttributeListHeaderPanel("header");
	}

	@Override
	protected String getListId(){
		return "attrList";
	}

	@Override
	protected Panel getRowPanel(String gridId, Item<ServiceMetaAttributeModel> item, String uniqueId)
	throws ServiceManagerException
	{
	   return new ServiceMetaAttributeListRowPanel(gridId, "row", item.getModelObject(), uniqueId);
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
}
