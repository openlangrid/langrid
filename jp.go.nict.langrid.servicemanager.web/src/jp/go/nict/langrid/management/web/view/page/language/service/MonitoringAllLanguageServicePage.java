/*
 * $Id: MonitoringAllLanguageServicePage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service;

import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.ServiceListTabPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.MonitoringAllLanguageServicesListRowPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class MonitoringAllLanguageServicePage extends MonitoringLanguageServiceTabbedPage{
	/**
	 * 
	 * 
	 */
	public MonitoringAllLanguageServicePage(){
	}
	
//	@Override
//	protected <T extends ServiceModel> ServiceListPanel<T> createListPanel(
//			String panelId, LangridSortableDataProvider<T> provider, LangridSearchCondition conditions)
//	throws ServiceManagerException
//	{
//		conditions.setScope(Scope.ALL);
//		return super.createListPanel(panelId, provider, conditions);
//	}
	@Override
	protected ServiceListTabPanel getTabPanel(
      String panelId, String gridId, GridRelation relation, LangridSearchCondition condition)
   throws ServiceManagerException
   {
      condition.putOrReplaceCondition("approved", true);
      return new ServiceListTabPanel(panelId, gridId, gridId, "", condition, relation)
      {
         @Override
         protected <T extends ServiceModel>Panel getListRowPanel(
            String nowGridId, Item<T> item, String uniqueId)
         throws ServiceManagerException
         {
            return new MonitoringAllLanguageServicesListRowPanel("row", item.getModelObject(), uniqueId){
               @Override
               protected String getOrganizationName(ServiceModel entry) throws ServiceManagerException{
                  UserModel ue = ServiceFactory.getInstance().getUserService(entry.getGridId()).get(entry.getOwnerUserId());
                  return ue == null ? "" : ue.getOrganization();
               }
               private static final long serialVersionUID = 1L;
            };
         }
      };
   }
	
//	@Override
//	protected <T extends ServiceModel> Panel getListRowPanel(Item<T> item, String uniqueId)
//	throws ServiceManagerException 
//	{
//		return new MonitoringAllLanguageServicesListRowPanel("row", item.getModelObject(), uniqueId){
//			@Override
//			protected String getOrganizationName(ServiceModel entry) throws ServiceManagerException{
//				UserModel ue = ServiceFactory.getInstance().getUserService(getSelfGridId()).get(entry.getOwnerUserId());
//				return ue == null ? "" : ue.getOrganization();
//			}
//			private static final long serialVersionUID = 1L;
//		};
//	}

	private static final long serialVersionUID = 1L;
}
