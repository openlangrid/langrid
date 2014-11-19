/*
 * $Id: OperationOfAuthorizeRequestedResourcePage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.OperationRequestService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.OperationRequestPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.panel.ResourceProfilePanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class OperationOfAuthorizeRequestedResourcePage extends ServiceManagerPage {
	public OperationOfAuthorizeRequestedResourcePage(String resourceId) {
		try {
			buildPageContents(ServiceFactory.getInstance().getResourceService(
			   getSelfGridId()).get(resourceId));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
	
	/**
	 * 
	 * 
	 */
	public OperationOfAuthorizeRequestedResourcePage(ResourceModel resource) {
		buildPageContents(resource);
	}
	
	private void buildPageContents(ResourceModel resource) {
		try {
			add(new ResourceProfilePanel("profilePanel", getSelfGridId(), resource));
			add(new Link<String>("authorize", new Model<String>(resource.getResourceId())){
				@Override
				public void onClick() {
					try {
					   String gridId = getSelfGridId();
					   ServiceFactory.getInstance().getResourceService(gridId).approveResource(getModelObject());
						
						OperationRequestService oService = ServiceFactory.getInstance().getOperationService(
						   gridId, gridId, "");
						oService.deleteByTargetId(getModelObject(), OperationType.RESOURCE);
						
						ResourceModel model = ServiceFactory.getInstance().getResourceService(gridId).get(getModelObject());
						NewsModel nm = new NewsModel();
				      nm.setGridId(model.getGridId());
				      Map<String, String> param = new HashMap<String, String>();
				      param.put("name", model.getResourceName());
				      nm.setContents(MessageManager.getMessage(
				         "news.resource.language.Registered", param));
				      ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
						
						setResponsePage(getResultPage(getModelObject()));
					} catch(ServiceManagerException e) {
						doErrorProcess(e);
					}
				}
			});
			add(new Link("cancel"){
				@Override
				public void onClick() {
					setResponsePage(getCancelPage());
				}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
	
	protected Page getResultPage(String resourceId) {
	   return new OperationOfAuthorizeRequestedResourceResultPage(resourceId);
	}
	protected Page getCancelPage() {
	   return new OperationRequestPage();
	}
}
