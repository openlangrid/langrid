/*
 * $Id: MonitoringLanguageServiceStatisticPage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.view.component.text.RequiredFromDateTextField;
import jp.go.nict.langrid.management.web.view.component.text.RequiredToDateTextField;
import jp.go.nict.langrid.management.web.view.model.provider.AccessStatisticsSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.list.MonitoringUserListPanel;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class MonitoringLanguageServiceStatisticPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public MonitoringLanguageServiceStatisticPage(
			String serviceGridId, String serviceId, String serviceName, Calendar start, Calendar end)
	{
		this.sId = serviceId;
		startDate = start;
		endDate = end;
		sGridId = serviceGridId;
		String userGridId = getSelfGridId();
		List<ITab> tabList = new ArrayList<ITab>();
		try {
			if(serviceGridId.equals(userGridId)) {
				setTabPanel(serviceGridId, serviceGridId, tabList);
				FederationService fs = ServiceFactory.getInstance().getFederationService(
					serviceGridId);
				for(String sourceGridId : fs.getConnectedSourceGridIdList(
					serviceGridId, new Order("sourceGridName", OrderDirection.ASCENDANT)))
				{
					setTabPanel(serviceGridId, sourceGridId, tabList);
				}
			} else {
				setTabPanel(serviceGridId, userGridId, tabList);
				setTabPanel(serviceGridId, serviceGridId, tabList);
			}
		} catch(ServiceManagerException e1) {
			doErrorProcess(e1);
		}
		add(tabPanel = new AjaxTabbedPanel("list", tabList));
		this.sId = serviceId;
		add(new Label("labelHeadServiceId", serviceName));
		add(new Label("labelServiceId", serviceName));
		Form form = new Form("form");
		dtfStart = new RequiredFromDateTextField(start.getTime());
		dtfEnd = new RequiredToDateTextField(dtfStart, end.getTime());
		form.add(dtfEnd);
		form.add(dtfStart);
		form.add(new Label("locale", "(" + DateUtil.defaultTimeZone() + ")"));
		form.add(new IndicatingAjaxButton("set", form) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				startDate = Calendar.getInstance();
				endDate = Calendar.getInstance();
				startDate.setTime(dtfStart.getModelObject());
				endDate.setTime(dtfEnd.getModelObject());
				try {
					MonitoringUserListPanel panel = panelMap.get(tabPanel
						.getSelectedTab());
					//					MonitoringUserListPanel panel = tabPanelList.get(tabPanel.getSelectedTab());
					AccessStatisticsSortableDataProvider dp = new AccessStatisticsSortableDataProvider(
						sGridId, sId, selectedUserGridId, getSessionUserId(),
						CalendarUtil.createBeginningOfDay(startDate)
						, CalendarUtil.createEndingOfDay(endDate));
					panel.makeList(dp);
					panel.setDates(startDate, endDate);
					target.addComponent(panel.getRewritableComponent());
					target.addComponent(getFeedbackPanel());
					//					listPanel.makeList(dp);
					//					listPanel.setDates(startDate, endDate);
					//					target.addComponent(listPanel.getRewritableComponent());
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(getFeedbackPanel());
			}

			private static final long serialVersionUID = 1L;
		});
		add(form);
		add(new Link("back") {
			@Override
			public void onClick() {
				setResponsePage(getBackPage());
			}
		});
	}

	protected Page getBackPage() {
		return new MonitoringLanguageServicePage();
	}

	//	protected MonitoringUserListPanel getListPanel(
	//			IDataProvider<ExecutionInformationStatisticsModel> dp, Calendar start, Calendar end)
	//	{
	//		return new MonitoringUserListPanel(getGridId(), "list", dp, start, end);
	//	}
	protected MonitoringUserListPanel getTabPanel(
		String panelId, String serviceGridId, String serviceId, String userGridId,
		Calendar start, Calendar end)
	throws ServiceManagerException {
		AccessStatisticsSortableDataProvider dp = new AccessStatisticsSortableDataProvider(
			serviceGridId, serviceId, userGridId, getSessionUserId(),
			CalendarUtil.createBeginningOfDay(start)
			, CalendarUtil.createEndingOfDay(end));
		return new MonitoringUserListPanel(panelId, serviceGridId, start, end, dp);
	}

	private void setTabPanel(final String serviceGridId, final String userGridId,
		List<ITab> tabList) throws ServiceManagerException {
		String gridName = ServiceFactory.getInstance().getGridService().get(userGridId).getGridName();
		tabList.add(new AbstractTab(new Model<String>(gridName)) {
			@Override
			public Panel getPanel(String panelId) {
				selectedUserGridId = userGridId;
				try {
					MonitoringUserListPanel panel = getTabPanel(panelId, serviceGridId,
						sId, userGridId, startDate, endDate);
					if(panelMap.get(tabPanel.getSelectedTab()) != null) {
						panelMap.remove(tabPanel.getSelectedTab());
					}
					panelMap.put(tabPanel.getSelectedTab(), panel);
					//               if(tabPanel.getSelectedTab() < tabPanelList.size()){
					//                  tabPanelList.remove(tabPanel.getSelectedTab());
					//               }
					//               tabPanelList.add(tabPanel.getSelectedTab(), panel);
					return panel;
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
				return new Panel(panelId);
			}
		});
	}

	private Calendar startDate;
	private Calendar endDate;
	private String sId;
	private String sGridId;
	private String selectedUserGridId;
	//	private List<MonitoringUserListPanel> tabPanelList = new ArrayList<MonitoringUserListPanel>();
	private Map<Integer, MonitoringUserListPanel> panelMap = new HashMap<Integer, MonitoringUserListPanel>();
	private AjaxTabbedPanel tabPanel;
	private RequiredToDateTextField dtfEnd;
	private RequiredFromDateTextField dtfStart;
	//	private MonitoringUserListPanel listPanel;
	private static final long serialVersionUID = 1L;
}
