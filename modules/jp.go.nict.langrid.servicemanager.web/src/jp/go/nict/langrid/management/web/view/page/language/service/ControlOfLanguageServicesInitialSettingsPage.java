/*
 * $Id: ControlOfLanguageServicesInitialSettingsPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.AccessLimitsForm;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ControlOfLanguageServicesInitialSettingsPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public ControlOfLanguageServicesInitialSettingsPage(String userGridId, String serviceGridId, final String serviceId) {
	   try{
		   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(serviceGridId);
		   ServiceModel model = service.get(serviceId);
		   AccessLimitsForm form = new AccessLimitsForm("form", ALL_USER_GRIDID, ALL_USER_ID, serviceGridId, serviceId) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void setResultPage(String resultParameter) {
					setResponsePage(getResponsePage(serviceId, resultParameter));
				}

				@Override
				protected Page getCancelPage(String serviceId) {
					return getBackPage(serviceId);
				}
			};
			form.setInitSettingOperation(true);
			add(form);
			add(new Label("resourceName", model.getServiceName()));
		}catch(ServiceManagerException e) {
			doErrorProcess(new ServiceManagerException(e, getPageClass()));
		}
	}
	
	protected Page getResponsePage(String serviceId, String resultParameter) {
		return new ControlOfLanguageServicesInitialSettingsResultPage(serviceId, resultParameter);
	}
	
	protected Page getBackPage(String serviceId) {
		return new ControlOfLanguageServicesPage(serviceId);
	}

	/**
	 *  Default User Id for Access Settings
	 */
	private static final String ALL_USER_ID = "*";
	private static final String ALL_USER_GRIDID = "*";
	
	private static final long serialVersionUID = 1L;
}
