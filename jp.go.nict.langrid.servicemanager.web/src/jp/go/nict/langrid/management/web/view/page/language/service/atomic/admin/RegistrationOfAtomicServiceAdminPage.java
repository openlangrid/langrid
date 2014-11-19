/*
 * $Id: RegistrationOfAtomicServiceAdminPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.atomic.admin;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.RegistrationOfAtomicServicePage;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RegistrationOfAtomicServiceAdminPage extends RegistrationOfAtomicServicePage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfAtomicServiceAdminPage(String resourceId, String ownerId){
		super(resourceId, ownerId);
	}
	
	@Override
	protected void doCancelProcess() {
		setResponsePage(new RegistrationOfAtomicServiceResourceListPage(getOwnerId()));
	}
	
	protected void doResultProcess(AtomicServiceModel model) throws ServiceManagerException{
      NewsModel nm = new NewsModel();
      nm.setGridId(model.getGridId());
      Map<String, String> param = new HashMap<String, String>();
      param.put("name", model.getServiceName());
      nm.setContents(MessageManager.getMessage(
         "news.service.atomic.Registered", param));
      ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
	   ServiceFactory.getInstance().getAtomicServiceService(model.getGridId()).approveService(model.getServiceId());
		setResponsePage(new RegistrationOfAtomicServiceResultAdminPage(model));
	}
}
