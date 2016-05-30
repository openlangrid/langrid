/*
 * $Id: OperationRequestListPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.admin.component.list;

import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.component.list.AbstractListPanel;
import jp.go.nict.langrid.management.web.view.component.list.row.EmptyRowPanel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.component.list.row.FederalOperationRequestListRowPanel;
import jp.go.nict.langrid.management.web.view.page.admin.component.list.row.FederalRejectOperationListRowPanel;
import jp.go.nict.langrid.management.web.view.page.admin.component.list.row.OperationRequestListHeaderPanel;
import jp.go.nict.langrid.management.web.view.page.admin.component.list.row.OperationRequestListRowPanel;
import jp.go.nict.langrid.management.web.view.page.admin.component.list.row.PreventionOperationRequestListRowPanel;
import jp.go.nict.langrid.management.web.view.page.admin.component.list.row.ResourceOperationRequestListRowPanel;
import jp.go.nict.langrid.management.web.view.page.admin.component.list.row.ServiceOperationRequestListRowPanel;

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
public class OperationRequestListPanel extends AbstractListPanel<OperationRequestModel>{
	/**
	 * 
	 * 
	 */
	public OperationRequestListPanel(
			String gridId, String componentId, IDataProvider<OperationRequestModel> dataProvider)
	{
		super(gridId, componentId, dataProvider);
	}

	protected void addItem(Item<OperationRequestModel> item){
		//noop
	}
	
	@Override
	protected void addListItem(String gridId, String rowId, Item<OperationRequestModel> item) {
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
	protected String getListId(){
		return "operationList";
	}

	@Override
	protected Panel getRowPanel(String gridId, Item<OperationRequestModel> item, String uniqueId)
	throws ServiceManagerException
	{
		OperationType type = item.getModelObject().getType();
		if(type == null) {
		   return new OperationRequestListRowPanel(gridId, "row", item.getModelObject(), uniqueId);
		}else if(type.equals(OperationType.SERVICE)) {
			return new ServiceOperationRequestListRowPanel(gridId, "row", item.getModelObject(), uniqueId);
		}else if(type.equals(OperationType.RESOURCE)){
			return new ResourceOperationRequestListRowPanel(gridId, "row", item.getModelObject(), uniqueId);
		}else if(type.equals(OperationType.FEDERATION)){
			return new FederalOperationRequestListRowPanel(gridId, "row", item.getModelObject(), uniqueId);
		}else if(type.equals(OperationType.PREVENTION)){
			return new PreventionOperationRequestListRowPanel(gridId, "row", item.getModelObject(), uniqueId);
		}else if(type.equals(OperationType.FEDERATIONREJECT)){
		   return new FederalRejectOperationListRowPanel(gridId, "row", item.getModelObject(), uniqueId);
		}
		return new OperationRequestListRowPanel(gridId, "row", item.getModelObject(), uniqueId);
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
	protected Panel getEmptyRowPanel(){
		return new EmptyRowPanel("emptyRow", 3);
	}

	@Override
	protected Panel getHeaderPanel(){
		return new OperationRequestListHeaderPanel("header");
	}
}
