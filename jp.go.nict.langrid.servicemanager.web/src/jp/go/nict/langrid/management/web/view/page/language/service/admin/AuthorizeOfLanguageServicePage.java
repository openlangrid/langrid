/*
 * $Id: AuthorizeOfLanguageServicePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AtomicServiceService;
import jp.go.nict.langrid.management.web.model.service.CompositeServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.LanguageDomainViewPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.panel.AtomicServiceProfilePanel;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.component.panel.CompositeServiceProfilePanel;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class AuthorizeOfLanguageServicePage extends LanguageDomainViewPage {
	/**
	 * 
	 * 
	 */
	public AuthorizeOfLanguageServicePage(String serviceId){
		try{
			ServiceModel model = ServiceFactory.getInstance().getAtomicServiceService(getSelfGridId()).get(serviceId);
			 if(model == null){
				 throw new ServiceManagerException("\"" + serviceId + "\" is not found");
			 }
			make(model);
//		}catch(LangridException e){
//			doErrorProcessForPopup(new ServiceManagerException(e, getPageClass()));
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
		serviceId = model.getServiceId();
		type = model.getInstanceType();

		if(model.getInstanceType().equals(InstanceType.BPEL)) {
			add(new CompositeServiceProfilePanel("profilePanel", getSelfGridId(), (CompositeServiceModel)model));
		}else {
			add(new AtomicServiceProfilePanel("profilePanel", getSelfGridId(), (AtomicServiceModel)model));
		}
		add(new Link<String>("authorize", new Model<String>(serviceId)) {
			@Override
			public void onClick() {
			   try {
   			   if(type.equals(InstanceType.EXTERNAL)){
   			      AtomicServiceService service = ServiceFactory.getInstance().getAtomicServiceService(getSelfGridId());
   			      service.approveService(serviceId);
   			      AtomicServiceModel model = service.get(serviceId);

   			      NewsModel nm = new NewsModel();
   		         nm.setGridId(model.getGridId());
   		         Map<String, String> param = new HashMap<String, String>();
   		         param.put("name", model.getServiceName());
   		         nm.setContents(MessageManager.getMessage(
   		            "news.service.atomic.Registered", param));
   		         ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
   			   }else{
   			      CompositeServiceService service = ServiceFactory.getInstance().getCompositeServiceService(getSelfGridId());
   			      service.approveService(serviceId);
   			      CompositeServiceModel model = service.get(serviceId);

   		         NewsModel nm = new NewsModel();
   		         nm.setGridId(model.getGridId());
   		         Map<String, String> param = new HashMap<String, String>();
   		         param.put("name", model.getServiceName());
   		         nm.setContents(MessageManager.getMessage(
   		            "news.service.composite.Registered", param));
   		         ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
   			   }
			   } catch(ServiceManagerException e) {
			      doErrorProcess(e);
			   }
				setResponsePage(new AuthorizeOfLanguageServiceResultPage(getModelObject()));
			}
		});
		add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(new AllLanguageServicesPage());
			}
		});

	}

	private InstanceType type;
	private String serviceId;
	private static final long serialVersionUID = 1L;
}
