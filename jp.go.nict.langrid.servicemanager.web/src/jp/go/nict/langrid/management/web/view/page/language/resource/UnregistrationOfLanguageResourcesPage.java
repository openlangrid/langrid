/*
 * $Id: UnregistrationOfLanguageResourcesPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AtomicServiceService;
import jp.go.nict.langrid.management.web.model.service.NewsService;
import jp.go.nict.langrid.management.web.model.service.ScheduleService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.form.DateReserveForm;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row.LanguageResourcesListRowPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class UnregistrationOfLanguageResourcesPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public UnregistrationOfLanguageResourcesPage(ResourceModel resource){
		try{
			add(new LanguageResourcesListRowPanel(resource.getGridId(), "row", resource, ""){
				@Override
				protected String getOrganizationName(String gridId, ResourceModel resource)
				throws ServiceManagerException
				{
					return ServiceFactory.getInstance().getUserService(gridId).get(
					   resource.getOwnerUserId()).getOrganization();
				}

				private static final long serialVersionUID = 1L;
			});
			rModel = resource;
			add(new Label("locale", "(" + DateUtil.defaultTimeZone() + ")"));
			ExternalLink isoLink = new ExternalLink("linkLanguagePage",
					MessageUtil.ISO_LANGUAGE_CODE_URL);
			add(isoLink);
			DateReserveForm form = new DateReserveForm("form", new Date()){
				@Override
				protected String getConfirmMessage(){
					return MessageManager.getMessage(
							"ProvidingServices.language.resource.message.unregister.Confirm", getLocale());
				}

				@Override
				protected void doSubmitProcess() {
					try{
					   String gridId = getSelfGridId();
					   ScheduleService service = ServiceFactory.getInstance().getScheduleService(gridId);
						AtomicServiceService aService = ServiceFactory.getInstance().getAtomicServiceService(
						   gridId, gridId, getSessionUserId());

						Calendar date = Calendar.getInstance();
						date.setTime(getDate());

						NewsService nService = ServiceFactory.getInstance().getNewsService(gridId);
						
						List<AtomicServiceModel> list = aService.getListByRelatedId(rModel.getResourceId());
						for(AtomicServiceModel asm : list) {
						   ScheduleModel am = new ScheduleModel();
						   am.setActionType(ScheduleActionType.UNREGISTRATION);
						   am.setTargetType(ScheduleTargetType.SERVICE);
						   am.setTargetId(asm.getServiceId());
						   am.setGridId(gridId);
						   am.setBookingDateTime(date);
						   am.setRelated(true);
						   service.add(am);
						   
						   Map<String, String> param = new HashMap<String, String>();
		               param.put("id", asm.getServiceId());
		               param.put("name", asm.getServiceName());
		               param.put("date", DateUtil.formatYMDWithSlashLocale(date.getTime()));
		               param.put("resourceId", rModel.getResourceId());
		               param.put("resourceName", rModel.getResourceName());
		               NewsModel nm = new NewsModel();
		               nm.setContents(MessageManager.getMessage(
		                  "news.service.atomic.unregistration.reserve.ByResource"
		                  , param));
		               nm.setGridId(gridId);
		               nService.add(nm);
						}

						ScheduleModel rsm = new ScheduleModel();
						rsm.setActionType(ScheduleActionType.UNREGISTRATION);
						rsm.setTargetType(ScheduleTargetType.RESOURCE);
						rsm.setTargetId(rModel.getResourceId());
						rsm.setGridId(gridId);
						rsm.setBookingDateTime(date);
						rsm.setRelated(0 < list.size());
						service.add(rsm);
						
						Map<String, String> param = new HashMap<String, String>();
						param.put("id", rModel.getResourceId());
						param.put("name", rModel.getResourceName());
						param.put("date", DateUtil.formatYMDWithSlashLocale(date.getTime()));
						NewsModel nm = new NewsModel();
                  nm.setContents(MessageManager.getMessage(
                     "news.resource.language.unregistration.Reserve", param));
                  nm.setGridId(gridId);
                  nService.add(nm);

						LogWriter.writeInfo(getSessionUserId(), "\"" + rModel.getResourceId()
								+ "\" of language resource will be unregistered on \""
								+ DateUtil.formatYMDWithSlash(getDate()), getPageClass());
					}catch(ServiceManagerException e){
						doErrorProcess(e);
					}
				}

				@Override
				protected void setCancelResponsePage(){
					setResponsePage(getCancelPage());
				}
	
				@Override
				protected void setResultPage(Date resultParameter){
					setResponsePage(getResultPage(rModel));
				}
	
				private static final long serialVersionUID = 1L;
			};
			add(form);
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
	}
	
	protected Page getCancelPage(){
		return new YourLanguageResourcesPage();
	}

	protected Page getResultPage(ResourceModel resource){
		return new UnregistrationOfLanguageResourcesResultPage(resource);	
	}
	
	private ResourceModel rModel;
	private static final long serialVersionUID = 1L;
}
