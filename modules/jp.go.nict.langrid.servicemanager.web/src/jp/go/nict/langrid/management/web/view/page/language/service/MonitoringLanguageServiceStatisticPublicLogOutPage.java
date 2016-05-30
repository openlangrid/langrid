/*
 * $Id: MonitoringLanguageServiceStatisticPublicLogOutPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.management.web.model.ExecutionInformationStatisticsModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.provider.AccessStatisticsSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.user.component.list.MonitoringUserListPanel;
import jp.go.nict.langrid.management.web.view.page.user.component.list.row.MonitoringUserListRowPanel;

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
public class MonitoringLanguageServiceStatisticPublicLogOutPage
extends MonitoringLanguageServiceStatisticPage
{
	/**
	 * 
	 * 
	 */
	public MonitoringLanguageServiceStatisticPublicLogOutPage(
			String serviceGridId, String serviceId, String serviceName, Calendar start, Calendar end)
	{
	   super(serviceGridId, serviceId, serviceName, start, end);
	}
	
	@Override
	protected Page getBackPage() {
	   return new MonitoringLanguageServicePublicLogOutPage();
	}
	
	@Override
   protected MonitoringUserListPanel getTabPanel(String panelId, String gridId, String serviceId,
      String userGridId, Calendar start, Calendar end)
   throws ServiceManagerException
   {
      AccessStatisticsSortableDataProvider dp = new AccessStatisticsSortableDataProvider(
         gridId, serviceId, userGridId, getSessionUserId(), CalendarUtil.createBeginningOfDay(start)
         , CalendarUtil.createEndingOfDay(end));
      return new MonitoringUserListPanel(panelId, gridId, start, end, dp) {
         @Override
         protected Panel getRowPanel(String gridId,
            Item<ExecutionInformationStatisticsModel> item, String uniqueId)
         throws ServiceManagerException {
            return new MonitoringUserListRowPanel("row", item.getModelObject(), uniqueId) {
               @Override
               protected boolean isVerboseMonitoring(){
                  return false;
               }
               private static final long serialVersionUID = 1L;
            };
         }
         private static final long serialVersionUID = 1L;
      };
   }
}
