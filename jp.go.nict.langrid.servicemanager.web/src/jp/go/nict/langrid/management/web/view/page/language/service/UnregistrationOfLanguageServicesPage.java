/*
 * $Id: UnregistrationOfLanguageServicesPage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
import java.util.Locale;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ScheduleService;
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
public class UnregistrationOfLanguageServicesPage
extends ServiceManagerPage
{
	/**
	 * 
	 * 
	 */
	public UnregistrationOfLanguageServicesPage(String serviceId){
		try{
		   this.sId = serviceId;
		   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
		   ServiceModel model = service.get(serviceId);
		   LanguageServicesListRowPanel rowPanel = new LanguageServicesListRowPanel("row", model, ""){
				@Override
				protected String getOrganizationName(ServiceModel model)
				throws ServiceManagerException
				{
					UserModel user = ServiceFactory.getInstance().getUserService(model.getGridId()).get(model.getOwnerUserId());
					return user.getOrganization();
				}

				private static final long serialVersionUID = 1L;
			};
			add(rowPanel);
			add(new ServiceListHeaderPanel("header"));
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}

		add(new Label("locale", "(" + DateUtil.defaultTimeZone() + ")"));
		DateReserveForm form = new DateReserveForm("form", new Date()){
			@Override
			protected void doSubmitProcess(){
				try{
					Date inputedDate = getDate();
					LangridServiceService<ServiceModel> lService = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
					ServiceModel sModel = lService.get(sId);
					if(!hasCanOverrideEvent(sModel, inputedDate)){
						error(MessageManager.getMessage(
									"ProvidingServices.language.service.error.validate.alreadyReserveDate.LanguageResource",
									Locale.ENGLISH, getFormatedDateString()));
						return;
					}
					String selfGridId = getSelfGridId();
					ScheduleService service = ServiceFactory.getInstance().getScheduleService(selfGridId);
					ScheduleModel sm = new ScheduleModel();
					sm.setGridId(sModel.getGridId());
					sm.setTargetId(sModel.getServiceId());
					sm.setTargetType(ScheduleTargetType.SERVICE);
					sm.setActionType(ScheduleActionType.UNREGISTRATION);
					Calendar date = Calendar.getInstance();
					date.setTime(inputedDate);
					sm.setBookingDateTime(date);
					service.add(sm);

					Map<String, String> param = new HashMap<String, String>();
					param.put("id", sModel.getServiceId());
					param.put("name", sModel.getServiceName());
					param.put("date", DateUtil.formatYMDWithSlashLocale(inputedDate));
					NewsModel nm = new NewsModel();
					nm.setContents(MessageManager.getMessage(
					   sModel.getInstanceType().equals(InstanceType.EXTERNAL) ?
					   "news.service.atomic.unregistration.Reserve" : "news.service.composite.unregistration.Reserve"
					   , param));
					nm.setGridId(selfGridId);
					ServiceFactory.getInstance().getNewsService(selfGridId).add(nm);

					LogWriter.writeInfo(getSessionUserId()
							, "\"" + sModel.getServiceId()
							+ "\" of language service will be unregistered on \""
							+ getFormatedDateString() + "\"" , getPageClass());
				}catch(ServiceManagerException e){
					raisedException = e;
				}
			}

			@Override
			protected String getConfirmMessage(){
				return MessageManager.getMessage("ProvidingServices.language.service.message.unregister.Confirm", getLocale());
			}

			@Override
			protected void setCancelResponsePage(){
				setResponsePage(getCancelPage());
			}

			@Override
			protected void setResultPage(Date resultParameter){
				setResponsePage(getResultPage(sId));
			}

			private static final long serialVersionUID = 1L;
		};
		add(form);
	}

	protected Page getCancelPage(){
		return new YourLanguageServicesPage();
	}

	protected Page getResultPage(String serviceId){
		return new UnregistrationOfLanguageServicesResultPage(serviceId);
	}

	/**
	 * 
	 * 
	 */
	private boolean hasCanOverrideEvent(ServiceModel model, Date reserveDate)
	throws ServiceManagerException
	{
		try{
		   String gridId = getSelfGridId();
			if( ! (model instanceof AtomicServiceModel)){
				ScheduleModel sm = ServiceFactory.getInstance().getScheduleService(gridId).getNearestStatus(
						model.getServiceId(), ScheduleTargetType.SERVICE);
				if(sm == null){
					return true;
				}
				return(sm.getBookingDateTime().getTime().getTime() > reserveDate.getTime());
			}
			ScheduleModel rsm = ServiceFactory.getInstance().getScheduleService(gridId).getNearestStatus(
					((AtomicServiceModel)model).getResourceId(), ScheduleTargetType.RESOURCE);
			if(rsm == null){
				return true;
			}else{
				return(rsm.getBookingDateTime().getTime().getTime() > reserveDate.getTime());
			}
		}catch(ServiceManagerException e){
			throw e;
		}
	}

	private String sId;
}
