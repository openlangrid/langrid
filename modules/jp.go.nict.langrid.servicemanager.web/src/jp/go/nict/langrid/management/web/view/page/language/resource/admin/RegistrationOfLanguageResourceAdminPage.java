/*
 * $Id: RegistrationOfLanguageResourceAdminPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.admin;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.resource.RegistrationOfLanguageResourcePage;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RegistrationOfLanguageResourceAdminPage
extends RegistrationOfLanguageResourcePage
{
	/**
	 * 
	 * 
	 */
	public RegistrationOfLanguageResourceAdminPage(UserModel user){
		this.ownerUserId = user.getUserId();
	}
	
	@Override
	protected void setOwnerUserId(ResourceModel obj) {
		obj.setOwnerUserId(ownerUserId);
	}
	
	@Override
	protected void setCancelPage() {
		setResponsePage(new RegistrationOfLanguageResourceUserListPage());
	}
	
	@Override
	protected void doResultProcess(ResourceModel resource) throws ServiceManagerException {
      NewsModel nm = new NewsModel();
      nm.setGridId(resource.getGridId());
      Map<String, String> param = new HashMap<String, String>();
      param.put("name", resource.getResourceName());
      nm.setContents(MessageManager.getMessage(
         "news.resource.language.Registered", param));
      ServiceFactory.getInstance().getNewsService(resource.getGridId()).add(nm);
	   ServiceFactory.getInstance().getResourceService(resource.getGridId()
	      ).approveResource(resource.getResourceId());
		setResponsePage(new RegistrationOfLanguageResourceResultAdminPage(resource.getResourceId()));
	}

	private String ownerUserId;
}
