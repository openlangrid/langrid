/*
 * $Id: ControlOfLanguageServicesInitialSettingsResultPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ControlOfLanguageServicesInitialSettingsResultPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public ControlOfLanguageServicesInitialSettingsResultPage(final String serviceId, String limitString) {
	   try {
         LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
         ServiceModel model = service.get(serviceId);
      
   		String[] limitValues = limitString.split(",");
   		if(limitString.length() == 0){
   			limitValues = new String[]{};
   		}
   		add(new Label("resourceName", model.getServiceName()));
   		for(String v : limitValues){
   			WebMarkupContainer repeat = new WebMarkupContainer(limits.newChildId());
   			repeat.add(new Label("limitValue", v));
   			limits.add(repeat);
   		}
   		add(limits);
   		Label accessLimitsCleared = new Label("accessLimitsCleared", getLocalizer()
   				.getString("ProvidingServices.language.service.message.AccessLimitsCleared", this));
   		add(accessLimitsCleared);
   		accessLimitsCleared.setVisible(limitValues.length == 0);
   		add(new Link("back"){
            @Override
            public void onClick() {
               setResponsePage(getBackPage(serviceId));
            }
         });
	   } catch(ServiceManagerException e) {
	      doErrorProcess(e);
	   }
   }
   
   protected Page getBackPage(String serviceId){
     return new ControlOfLanguageServicesPage(serviceId); 
   }

	private RepeatingView limits = new RepeatingView("limits");
	private static final long serialVersionUID = 1L;
}
