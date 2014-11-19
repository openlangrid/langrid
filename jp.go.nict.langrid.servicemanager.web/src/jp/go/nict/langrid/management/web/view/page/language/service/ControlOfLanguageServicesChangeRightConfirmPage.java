/*
 * $Id: ControlOfLanguageServicesChangeRightConfirmPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.AccessRightControlModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ControlOfLanguageServicesChangeRightConfirmPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public ControlOfLanguageServicesChangeRightConfirmPage(
			final String serviceId, boolean permit, UserModel userModel)
	{
		uModel = userModel;
		isPermit = permit;
      try {
         LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
         ServiceModel model = service.get(serviceId);
   		add(new Label("name", model.getServiceName()));
   		add(new Label("headName", model.getServiceName()));
   		add(new Label("isPermit", isPermit ? "permit" : "prohibit"));
   		Link userPopup = new UserProfileLink("provider", uModel.getGridId(), uModel.getUserId(), "confirm");
   		userPopup.add(new Label("organization", uModel.getOrganization()));
   		add(userPopup);
   		add(new Label("homepage", StringUtil.shortenString(
   		   uModel.getHomepageUrl() == null ? "" : uModel.getHomepageUrl().getValue().toString(), 24)));
   		add(new Label("permitted", isPermit ? "prohibited" : "permitted"));
   		add(new Link("confirm"){
   			@Override
   			public void onClick(){
   				try{
   				   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
   		         ServiceModel model = service.get(serviceId);
   					AccessRightControlModel arcm = new AccessRightControlModel();
   					arcm.setUserId(uModel.getUserId());
   					arcm.setServiceGridId(model.getGridId());
   					arcm.setServiceId(model.getServiceId());
   					arcm.setPermitted(isPermit);
   					arcm.setServiceOwnerUserId(model.getOwnerUserId());
   					arcm.setUserGridId(uModel.getGridId());
   					ServiceFactory.getInstance().getAccessRightControlService(arcm.getServiceGridId()).add(arcm);
   					LogWriter.writeInfo(getSessionUserId(), "ServiceID:" + model.getServiceId()
   							+ " AccessRight:" + isPermit + " to UserID:" + uModel.getUserId()
   							+ " Operate:ControlAccessRight", getClass());
   					setResponsePage(getResponsePage(model, isPermit, uModel));
   				}catch(ServiceManagerException e){
   					doErrorProcess(e);
   				}
   			}
   
   			private static final long serialVersionUID = 1L;
   		});
   		
   		add(new Link("cancel"){
   			@Override
   			public void onClick(){
   			   try{
      			   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
      	         ServiceModel model = service.get(serviceId);
      				setResponsePage(getBackPage(model));
      			} catch(ServiceManagerException e) {
      			   doErrorProcess(e);
      			}
   			}
   
   		});
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
	}
	
	protected Page getResponsePage(
			ServiceModel entry, boolean permit, UserModel user)
	{
		return new ControlOfLanguageServicesChangeRightResultPage(entry, permit, user);
	}
	
	protected Page getBackPage(ServiceModel entry) {
		return new ControlOfLanguageServicesPage(entry.getServiceId());
	}

	private UserModel uModel;
	private boolean isPermit;
//	private ServiceModel sModel;
}
