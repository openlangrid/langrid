/*
 * $Id: ServiceMetaAttributeListAdminPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DomainService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.model.provider.ServiceMetaAttributeSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.list.EmptyServiceMetaAttributeListPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.list.ServiceMetaAttributeListPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute.component.list.row.ServiceMetaAttributeListRowAdminPanel;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ServiceMetaAttributeListAdminPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public ServiceMetaAttributeListAdminPage() {
		try {
		   String gridId = getSelfGridId();
         List<ITab> tabList = new ArrayList<ITab>();
         DomainService ds = ServiceFactory.getInstance().getDomainService(gridId);
         for(DomainModel dm : ds.getListOnGrid(gridId)){
            setTabPanel(gridId, dm.getDomainName(), tabList, dm.getDomainId());
         }
         if(tabList.size() == 0){
            add(new EmptyServiceMetaAttributeListPanel("serviceMetaAttributeListPanel"));
         }else{
            add(new AjaxTabbedPanel("serviceMetaAttributeListPanel", tabList));
         }

			add(new Link("register"){
				@Override
				public void onClick() {
					setResponsePage(new RegistrationOfServiceMetaAttributePage());
				}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
	
	protected ServiceMetaAttributeListPanel getTabPanel(String panelId, String gridId, String domainId)
   throws ServiceManagerException
   {
	   ServiceMetaAttributeSortableDataProvider provider = new ServiceMetaAttributeSortableDataProvider(
         gridId, getSessionUserId(), domainId);
      return new ServiceMetaAttributeListPanel(gridId, panelId, provider){
         @Override
         protected Panel getRowPanel(String gridId, Item<ServiceMetaAttributeModel> item, String uniqueId)
         throws ServiceManagerException
         {
            return new ServiceMetaAttributeListRowAdminPanel(gridId, "row", item.getModelObject(), uniqueId){
               @Override
               protected void doDeleteProcess(String domainId, String id) {
                  try {
                     for(ServiceTypeModel stm : ServiceFactory.getInstance().getServiceTypeService(getSelfGridId()).getAllList(domainId)){
                        for(ServiceMetaAttributeModel smam : stm.getMetaAttrbuteList()){
                           if(smam.getAttributeId().equals(id)){
                              error(MessageManager.getMessage("LanguageGridOperator.metaattribute.unregister.error.HasRelation", getLocale()));
                              return;
                           }
                        }
                     }
                     ServiceFactory.getInstance().getServiceMetaAttributeService(getSelfGridId()).deleteById(domainId, id);
                  } catch(ServiceManagerException e) {
                     doErrorProcess(e);
                  }
               }
            };
         }
      };
   }

   private void setTabPanel(final String gridId, String tabName, List<ITab> tabList, final String domainId) {
      tabList.add(new AbstractTab(new Model<String>(tabName)) {
         @Override
         public Panel getPanel(String panelId) {
            try {
               ServiceMetaAttributeListPanel panel = getTabPanel(panelId, gridId, domainId);
               return panel;
            } catch(ServiceManagerException e) {
               doErrorProcess(e);
            }
            return new Panel(panelId);
         }
      });
   }
}
