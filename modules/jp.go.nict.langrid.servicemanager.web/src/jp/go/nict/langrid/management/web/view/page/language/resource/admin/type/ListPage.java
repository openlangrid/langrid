/*
 * $Id: ListPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.admin.type;


import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DomainService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.provider.ResourceTypeSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.type.component.list.EmptyResourceTypeListPanel;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.type.component.list.ResourceTypeListPanel;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ListPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public ListPage(){
	   try {
         String gridId = getSelfGridId();
         List<ITab> tabList = new ArrayList<ITab>();
         DomainService ds = ServiceFactory.getInstance().getDomainService(gridId);
         for(DomainModel dm : ds.getListOnGrid(gridId)){
            setTabPanel(gridId, dm.getDomainName(), tabList, dm.getDomainId());
         }
         if(tabList.size() == 0){
            add(new EmptyResourceTypeListPanel("ListPanel"));
         }else{
            add(new AjaxTabbedPanel("ListPanel", tabList));
         }
         
         add(new Link("register"){
            @Override
            public void onClick() {
               setResponsePage(new RegistrationPage());
            }
         });
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
	}
	
	protected ResourceTypeListPanel getTabPanel(String panelId, String gridId, String domainId)
   throws ServiceManagerException
   {
	   ResourceTypeSortableDataProvider provider = new ResourceTypeSortableDataProvider(
         gridId, domainId, getSessionUserId());
      provider.setConditions(
         new MatchingCondition[] {
            new MatchingCondition("gridId", gridId)
            , new MatchingCondition("domainId", domainId)
         }, new Order[]{}, Scope.ALL);
      return new ResourceTypeListPanel(getSelfGridId(), panelId, provider){
       @Override
       protected void delete(String domainId, String typeId) {
          try {
             ServiceFactory.getInstance().getResourceTypeService(getSelfGridId()).delete(domainId, typeId);
          } catch(ServiceManagerException e) {
             doErrorProcess(e);
          }
       }
    };
   }

   private void setTabPanel(final String gridId, String tabName, List<ITab> tabList, final String domainId) {
      tabList.add(new AbstractTab(new Model<String>(tabName)) {
         @Override
         public Panel getPanel(String panelId) {
            try {
               ResourceTypeListPanel panel = getTabPanel(panelId, gridId, domainId);
               return panel;
            } catch(ServiceManagerException e) {
               doErrorProcess(e);
            }
            return new Panel(panelId);
         }
      });
   }
}
