/*
 * $Id: MonitoringLanguageServicePublicLogOutPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import java.util.Calendar;

import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.enumeration.ListType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceAccessRightRadioPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.ServiceListTabPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.MonitoringLanguageServicesListRowPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class MonitoringLanguageServicePublicLogOutPage extends MonitoringLanguageServiceTabbedPage {
	/**
	 * 
	 * 
	 */
	public MonitoringLanguageServicePublicLogOutPage(){
	}

	@Override
	protected Page getMonitarPage(String serviceGridId, String serviceId, String serviceName, Calendar start, Calendar end)
	{
	   return new MonitoringLanguageServiceStatisticPublicLogOutPage(serviceGridId, serviceId, serviceName, start, end);
	}
	
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
            return new MonitoringLanguageServicesListRowPanel("row", item.getModelObject(), uniqueId){
               @Override
               protected String getOrganizationName(ServiceModel entry)
               throws ServiceManagerException
               {
                  UserModel ue = ServiceFactory.getInstance().getUserService(entry.getGridId()).get(entry.getOwnerUserId());
                  return ue == null ? "" : ue.getOrganization();
               }
               @Override
               protected Page getResponsePage(String serviceGridId, String serviceId, String serviceName, Calendar start, Calendar end){
                  return getMonitarPage(serviceGridId, serviceId, serviceName, start, end);
               }
               private static final long serialVersionUID = 1L;
            };
         }
         
         @Override
         protected ServiceAccessRightRadioPanel getAccessRightPanel(
            String panelId, String listId, ListType listType)
         {
            ServiceAccessRightRadioPanel panel = super.getAccessRightPanel(panelId, listId, listType);
            panel.setVisible(false);
            return panel;
         }
      };
   }
}
