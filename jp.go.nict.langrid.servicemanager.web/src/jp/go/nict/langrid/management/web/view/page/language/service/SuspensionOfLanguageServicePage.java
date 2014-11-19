/*
 * $Id: SuspensionOfLanguageServicePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.form.DateReserveForm;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.LanguageServicesListRowPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.ServiceListHeaderPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class SuspensionOfLanguageServicePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public SuspensionOfLanguageServicePage(final String serviceId){
		try{
		   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
		   ServiceModel model = service.get(serviceId);
			add(new ServiceListHeaderPanel("header"));
			add(new LanguageServicesListRowPanel("row", model, ""){
				@Override
				protected String getOrganizationName(ServiceModel model)
				throws ServiceManagerException
				{
					return ServiceFactory.getInstance().getUserService(model.getGridId()).get(model.getOwnerUserId()).getOrganization();
				}

				private static final long serialVersionUID = 1L;
			});
			add(new Label("locale", "(" + DateUtil.defaultTimeZone() + ")"));
			DateReserveForm form = new DateReserveForm("form", new Date()){
				@Override
				protected void doSubmitProcess(){
					try{
					   String gridId = getSelfGridId();

					   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
			         ServiceModel model = service.get(serviceId);

						ScheduleModel sm = new ScheduleModel();
						sm.setGridId(model.getGridId());
						sm.setTargetId(model.getServiceId());
						sm.setActionType(ScheduleActionType.SUSPENSION);
						sm.setTargetType(ScheduleTargetType.SERVICE);
						Calendar bookingDate = Calendar.getInstance();
						bookingDate.setTime(getDate());
						sm.setBookingDateTime(bookingDate);
						ServiceFactory.getInstance().getScheduleService(gridId).add(sm);

						LogWriter.writeInfo(getSessionUserId(), "\"" + model.getServiceId()
								+ "\" of language service will be suspended on \""
								+ DateUtil.formatYMDWithSlash(getDate()), getPageClass());

						Map<String, String> param = new HashMap<String, String>();
                  param.put("id", model.getServiceId());
                  param.put("name", model.getServiceName());
                  param.put("date", DateUtil.formatYMDWithSlashLocale(
                     sm.getBookingDateTime().getTime()));
                  NewsModel nm = new NewsModel();
                  nm.setContents(MessageManager.getMessage(
                     model.getInstanceType().equals(InstanceType.EXTERNAL) ?
                     "news.service.atomic.suspend.Reserve" : "news.service.composite.suspend.Reserve"
                     , param));
                  nm.setGridId(model.getGridId());
                  ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
					}catch(ServiceManagerException e){
						doErrorProcess(e);
					}
				}
				@Override
				protected String getConfirmMessage(){
					return MessageManager.getMessage("ProvidingServices.language.service.message.suspension.Confirm"
							, getLocale());
				}

				@Override
				protected void setCancelResponsePage(){
					setResponsePage(getCancelPage());
				}

				@Override
				protected void setResultPage(Date resultParameter){
					setResponsePage(getResultPage(serviceId));
				}

				private static final long serialVersionUID = 1L;
			};
			add(form);
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
	}

	protected Page getCancelPage(){
		return new SuspensionOfLanguageServiceListPage();
	}

	protected Page getResultPage(String serviceId){
		return new SuspensionOfLanguageServiceResultPage(serviceId);
	}

	private static final long serialVersionUID = 1L;
}
