/*
 * $Id: OperationOfAuthorizeRequestedServicePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.OperationRequestService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.admin.OperationRequestPage;
import jp.go.nict.langrid.management.web.view.page.language.LanguageDomainViewPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.panel.AtomicServiceProfilePanel;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.component.panel.CompositeServiceProfilePanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class OperationOfAuthorizeRequestedServicePage extends LanguageDomainViewPage {
	/**
	 * 
	 * 
	 */
	public OperationOfAuthorizeRequestedServicePage(String serviceId){
		try{
		   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
			ServiceModel model = service.get(serviceId);
			 if(model == null){
				 throw new ServiceManagerException("\"" + serviceId + "\" is not found");
			 }
			make(model);
		}catch(MalformedURLException e){
			doErrorProcessForPopup(new ServiceManagerException(e, getPageClass()));
		}catch(ServiceManagerException e){
			doErrorProcessForPopup(e);
		}catch(InvalidLanguageTagException e){
			doErrorProcessForPopup(new ServiceManagerException(e, getPageClass()));
		}
	}

	private void make(ServiceModel model)
	throws MalformedURLException, ServiceManagerException, InvalidLanguageTagException
	{
		String serviceId = model.getServiceId();
		type = model.getInstanceType();

		if(model.getInstanceType().equals(InstanceType.BPEL)) {
			add(new CompositeServiceProfilePanel("profilePanel", getSelfGridId()
			   , ServiceFactory.getInstance().getCompositeServiceService(model.getGridId()).get(serviceId)));
		}else {
			add(new AtomicServiceProfilePanel("profilePanel", getSelfGridId()
			   , ServiceFactory.getInstance().getAtomicServiceService(model.getGridId()).get(serviceId)));
		}

		add(new Link<String>("authorize", new Model<String>(serviceId)) {
			@Override
			public void onClick() {
            try {
               String gridId = getSelfGridId();
               ServiceFactory.getInstance().getLangridServiceService(gridId).approveService(getModelObject());
               OperationRequestService oService = ServiceFactory.getInstance().getOperationService(
                  gridId, gridId, "");
               oService.deleteByTargetId(getModelObject(), OperationType.SERVICE);

               if(type.equals(InstanceType.EXTERNAL)){
                  AtomicServiceModel model = ServiceFactory.getInstance().getAtomicServiceService(gridId).get(getModelObject());
                  NewsModel nm = new NewsModel();
                  nm.setGridId(model.getGridId());
                  Map<String, String> param = new HashMap<String, String>();
                  param.put("name", model.getServiceName());
                  nm.setContents(MessageManager.getMessage(
                     "news.service.atomic.Registered", param));
                  ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
               }else{
                  CompositeServiceModel model = ServiceFactory.getInstance().getCompositeServiceService(gridId).get(getModelObject());
                  NewsModel nm = new NewsModel();
                  nm.setGridId(model.getGridId());
                  Map<String, String> param = new HashMap<String, String>();
                  param.put("name", model.getServiceName());
                  nm.setContents(MessageManager.getMessage(
                     "news.service.composite.Registered", param));
                  ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
               }

               setResponsePage(getResultPage(getModelObject()));
            } catch(ServiceManagerException e) {
               doErrorProcess(e);
            }
			}
		});

		add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(getCancelPage());
			}
		});

	}

	protected Page getResultPage(String serviceId){
	   return new OperationOfAuthorizeRequestedServiceResultPage(serviceId);
	}
	protected Page getCancelPage(){
	   return new OperationRequestPage();
	}

	private InstanceType type;
	private static final long serialVersionUID = 1L;
}
