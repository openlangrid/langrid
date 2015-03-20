/*
 * $Id: FraudulentUsageListPanel.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.component.list;

import jp.go.nict.langrid.management.web.model.OverUseStateModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.component.list.AbstractListPanel;
import jp.go.nict.langrid.management.web.view.component.list.row.EmptyRowPanel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.component.list.row.FraudulentUsageListHeaderPanel;
import jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.component.list.row.FraudulentUsageListRowPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class FraudulentUsageListPanel extends AbstractListPanel<OverUseStateModel> {
	/**
	 * 
	 * 
	 */
	public FraudulentUsageListPanel(String gridId, String componentId, IDataProvider<OverUseStateModel> provider) {
		super(gridId, componentId, provider);
		this.gridId = gridId;
	}

	@Override
	protected void addListItem(String gridId, String rowId, Item<OverUseStateModel> item){
		try{
			Panel row = getRowPanel(gridId, item, rowId);
			item.add(row);
		}catch(ServiceManagerException e){
			ServiceManagerPage page = (ServiceManagerPage)getPage();
			page.doErrorProcess(e);
		}
	}

	@Override
	protected String getListId(){
		return "serviceList";
	}
	
	@Override
	protected int getPAGING_COUNT(){
		return 10;
	}

	@Override
	protected Panel getRowPanel(String nowGridId, Item<OverUseStateModel> item, String uniqueId)
	throws ServiceManagerException{
		return new FraudulentUsageListRowPanel("row", item.getModelObject(), uniqueId);
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
		return new EmptyRowPanel("emptyRow", 6);
	}

	@Override
	protected Panel getHeaderPanel(){
		return new FraudulentUsageListHeaderPanel("header");
	}

	String gridId;
}
