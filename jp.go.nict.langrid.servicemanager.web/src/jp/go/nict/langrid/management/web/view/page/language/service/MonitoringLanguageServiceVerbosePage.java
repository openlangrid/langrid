/*
 * $Id: MonitoringLanguageServiceVerbosePage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.list.MonitoringUserLogLimitedPagingListPanel;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class MonitoringLanguageServiceVerbosePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public MonitoringLanguageServiceVerbosePage(
		ExecutionInformationStatisticsModel entry, Calendar start, Calendar end, int index)
	{
		startDate = CalendarUtil.createBeginningOfDay(start);
		endDate = CalendarUtil.createEndingOfDay(end);
      add(new Label("startDate", DateUtil.formatYMDWithSlash(startDate.getTime())));
      add(new Label("endDate", DateUtil.formatYMDWithSlash(endDate.getTime())));
      add(new Label("labelHeadServiceId"
         , entry.getServiceName()).add(new AttributeAppender("style", new Model<String>(
            StringUtil.getLimitStyle(entry.getServiceName(), 16)), "")));
      try {
         MonitoringUserLogLimitedPagingListPanel listPanel = new MonitoringUserLogLimitedPagingListPanel(
                  "list", entry, index, startDate, endDate)
         {
            @Override
            protected Page getRequestPage(int index, Calendar start,
               Calendar end, ExecutionInformationStatisticsModel model)
            {
               return getThisPage(model, start, end, index);
            }
         };
         add(listPanel);
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
		add(new Label("timeZone2", "(" + DateUtil.defaultTimeZone() + ")"));
		
		add(new Link<ExecutionInformationStatisticsModel>("back", new Model<ExecutionInformationStatisticsModel>(entry)){
			@Override
			public void onClick(){
				setResponsePage(getBackPage(getModelObject(), startDate, endDate));
			}
			private static final long serialVersionUID = 1L;
		});
	}
	
	protected Page getBackPage(ExecutionInformationStatisticsModel entry, Calendar start, Calendar end){
		return new MonitoringLanguageServiceStatisticPage(entry.getServiceGridId(), entry.getServiceId(), entry.getServiceName(), start, end);
	}
	
	protected Page getThisPage(ExecutionInformationStatisticsModel model, Calendar start, Calendar end, int index) {
	   return new MonitoringLanguageServiceVerbosePage(model, start, end, index);
	}

	private Calendar startDate;
	private Calendar endDate;
	private static final long serialVersionUID = 1L;
}
