/*
 * $Id: ServiceListPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.component.list;

import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.list.AbstractListPanel;
import jp.go.nict.langrid.management.web.view.component.list.row.EmptyRowPanel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.LanguageServicesListRowPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.ServiceListHeaderPanel;

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
public class ServiceListPanel<T extends ServiceModel>
extends AbstractListPanel<T> 
{
	/**
	 * 
	 * 
	 */
	public ServiceListPanel(String gridId, String componentId, IDataProvider<T> provider) {
		super(gridId, componentId, provider);
		this.gridId = gridId;
	}

	@Override
	protected void addListItem(String gridId, String rowId, Item<T> item){
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
		return 50;
	}

	@Override
	protected Panel getRowPanel(String nowGridId, Item<T> item, String uniqueId)
	throws ServiceManagerException{
		return new LanguageServicesListRowPanel("row", item.getModelObject(), uniqueId){
			@Override
			protected String getOrganizationName(ServiceModel obj) throws ServiceManagerException{
				UserModel ue = ServiceFactory.getInstance().getUserService(obj.getGridId()).get(obj.getOwnerUserId());
				return ue == null ? "" : ue.getOrganization();
			}

			private static final long serialVersionUID = 1L;
		};
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
		return new EmptyRowPanel("emptyRow", 5);
	}

	@Override
	protected Panel getHeaderPanel(){
		return new ServiceListHeaderPanel("header");
	}

	String gridId;
}
