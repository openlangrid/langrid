/*
 * $Id: FraudulentUsagePage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage;

import java.util.Calendar;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.management.web.model.AccessLimitControlModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.view.component.text.RequiredFromDateTextField;
import jp.go.nict.langrid.management.web.view.component.text.RequiredToDateTextField;
import jp.go.nict.langrid.management.web.view.model.provider.OverUseStateSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.GridSettingsPage;
import jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.component.list.FraudulentUsageEachTimeListPanel;
import jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.component.list.FraudulentUsageListPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class FraudulentUsagePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public FraudulentUsagePage() {
		this(0, null, null);
	}
	
	public FraudulentUsagePage(final int index, Calendar start, Calendar end){
		String gridId = getSelfGridId();
		if(start == null){
			start = Calendar.getInstance();
			start.add(Calendar.DAY_OF_MONTH, -7);
		}
		if(end == null){
			end = Calendar.getInstance();
		}
		
		
		add(rewrite = new WebMarkupContainer("rewrite"));
		rewrite.setOutputMarkupId(true);

		rewrite.add(new Link("settingLink"){
			@Override
			public void onClick() {
				setResponsePage(new GridSettingsPage());
			}
		});
		
		Form form = new Form("form");
		dtfStart = new RequiredFromDateTextField(start.getTime());
		dtfEnd = new RequiredToDateTextField(dtfStart, end.getTime());
		form.add(dtfEnd);
		form.add(dtfStart);
		form.add(new Label("locale", "(" + DateUtil.defaultTimeZone() + ")"));
		form.add(new IndicatingAjaxButton("set", form){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form){
				startDate = Calendar.getInstance();
				endDate = Calendar.getInstance();
				startDate.setTime(dtfStart.getModelObject());
				endDate.setTime(dtfEnd.getModelObject());
				makeLists(getSelfGridId(), startDate, endDate, index);
				target.addComponent(rewrite);
				target.addComponent(getFeedbackPanel());
//				try{
//					int totalCount = setListView(index);
//					setNavigateLink(index, totalCount);
//					target.addComponent(rewritable);
//				}catch(ServiceManagerException e){
//					throw new RestartResponseException(new ErrorInternalPage(e));
//				}
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(getFeedbackPanel());
			}

			private static final long serialVersionUID = 1L;
		});
		rewrite.add(form);
		
		makeLists(gridId, start, end, index);
	}
	
	private void makeLists(String gridId, Calendar start, Calendar end, int index){
		rewrite.addOrReplace(makeFraudulentUsageListOnYear(gridId, start, end));
		rewrite.addOrReplace(makeFraudulentUsageListOnMonth(gridId, start, end));
		rewrite.addOrReplace(makeFraudulentUsageListOnDay(gridId, start, end));
		try {
			rewrite.addOrReplace(makeFraudulentUsageListOnEachTime(gridId, index, start, end));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	private Panel makeFraudulentUsageListOnYear(String gridId, Calendar start, Calendar end){
		start = CalendarUtil.createBeginningOfYear(start);
		end = CalendarUtil.createEndingOfDay(end);
		end.set(Calendar.MONTH, 11);
		end.set(Calendar.DAY_OF_MONTH, 31);
		
		OverUseStateSortableDataProvider provider = new OverUseStateSortableDataProvider(
			gridId, start, end, Period.YEAR);
		FraudulentUsageListPanel panel = new FraudulentUsageListPanel(
			gridId, "yearListPanel", provider);
		return panel;
	}

	private Panel makeFraudulentUsageListOnMonth(String gridId, Calendar start, Calendar end){
		start = CalendarUtil.createBeginningOfMonth(start);
		end = CalendarUtil.createEndingOfDay(end);
		end.add(Calendar.MONTH, 1);
		end.set(Calendar.DAY_OF_MONTH, 1);
		end.add(Calendar.DAY_OF_MONTH, -1);
		
		OverUseStateSortableDataProvider provider = new OverUseStateSortableDataProvider(
			gridId, start, end, Period.MONTH);
		FraudulentUsageListPanel panel = new FraudulentUsageListPanel(
			gridId, "monthListPanel", provider);
		return panel;
	}

	private Panel makeFraudulentUsageListOnDay(String gridId, Calendar start, Calendar end){
		start = CalendarUtil.createBeginningOfDay(start);
		end = CalendarUtil.createEndingOfDay(end);

		OverUseStateSortableDataProvider provider = new OverUseStateSortableDataProvider(
			gridId, start, end, Period.DAY);
		FraudulentUsageListPanel panel = new FraudulentUsageListPanel(
			gridId, "dayListPanel", provider);
		return panel;
	}

	private Panel makeFraudulentUsageListOnEachTime(String gridId, int index, Calendar start, Calendar end)
	throws ServiceManagerException
	{
		start = CalendarUtil.createBeginningOfDay(start);
		end = CalendarUtil.createEndingOfDay(end);
		
		int limitCount = 0;
		for(AccessLimitControlModel m : ServiceFactory.getInstance().getOveruseLimitControlService(gridId, gridId).getAll()){
			if(m.getPeriod().equals(Period.EACHTIME)){
				limitCount = m.getLimitCount();
				break;
			}
		}

		FraudulentUsageEachTimeListPanel panel = new FraudulentUsageEachTimeListPanel(
			gridId, "eachtimeListPanel", limitCount, index, start, end);
		return panel;
	}
	
	private RequiredToDateTextField dtfEnd;
	private RequiredFromDateTextField dtfStart;
	private Calendar startDate;
	private Calendar endDate;
	
	private WebMarkupContainer rewrite;
}
