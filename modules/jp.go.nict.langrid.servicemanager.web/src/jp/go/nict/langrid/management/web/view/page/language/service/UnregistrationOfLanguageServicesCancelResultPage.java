/*
 * $Id: UnregistrationOfLanguageServicesCancelResultPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.LanguageServicesListRowPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.ServiceListHeaderPanel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class UnregistrationOfLanguageServicesCancelResultPage
extends ServiceManagerPage
{
	/**
	 * 
	 * 
	 */
	public UnregistrationOfLanguageServicesCancelResultPage(String serviceId){
		try{
		   LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(getSelfGridId());
		   ServiceModel model = service.get(serviceId);
		   LanguageServicesListRowPanel rowPanel = new LanguageServicesListRowPanel("row", model, "") {
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
	}
}
