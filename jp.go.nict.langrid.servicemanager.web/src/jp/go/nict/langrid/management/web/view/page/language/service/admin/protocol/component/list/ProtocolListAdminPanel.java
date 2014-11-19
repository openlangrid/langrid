/*
 * $Id: ProtocolListAdminPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.component.list;

import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.component.list.row.ProtocolListRowAdminPanel;

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
public class ProtocolListAdminPanel extends ProtocolListPanel {
	/**
	 * 
	 * 
	 */
	public ProtocolListAdminPanel(
			String gridId, String componentId, IDataProvider<ProtocolModel> dataProvider)
	{
		super(gridId, componentId, dataProvider);
	}

	@Override
	protected Panel getRowPanel(String gridId, Item<ProtocolModel> item, String uniqueId)
	throws ServiceManagerException
	{
	   return new ProtocolListRowAdminPanel(gridId, "row", item.getModelObject(), uniqueId){
	      @Override
	      protected void doDeleteProcess(String id) {
	         deleteProcess(id);
	      }
	   };
	}
	
	protected void deleteProcess(String id) {
	   // noop
	}
	
	private static final long serialVersionUID = 1L;
}
