/*
 * $Id: RegistrationOfLanguageResourcePage.java 1237 2014-08-05 08:38:22Z t-nakaguchi $
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

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.OperationRequestService;
import jp.go.nict.langrid.management.web.model.service.ResourceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.form.panel.ResourceProfileFieldPanel;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.text.RequiredResourceIdField;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1237 $
 */
public class RegistrationOfLanguageResourcePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfLanguageResourcePage() {
		Form form = new Form("form");
		form.add(resourceId = new RequiredResourceIdField("resourceId", getSelfGridId()));
		try {
         form.add(profilePanel = new ResourceProfileFieldPanel(getSelfGridId(), "profileFieldPanel", form));
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
		form.add(getCancelLink("cancel"));
		form.add(getSubmitLink("submit"));
		for(IFormValidator fv : profilePanel.getValidatorList()) {
			form.add(fv);
		}
		for(int i = 1; i < 3; i++) {
			form.add(new Label("listLabel" + i, new Model<String>(MessageManager.getMessage(
					"ProvidingServices.language.resource.table.headline." + i, getLocale(), i))));
		}
		add(form);
	}
	
	private Link getCancelLink(String componentId){
		return new Link(componentId){
			@Override
			public void onClick() {
				setCancelPage();
			}
		};
	}

	private SubmitLink getSubmitLink(String componentId) {
		return new SubmitLink(componentId){
			@Override
			public void onSubmit() {
				try {
				   String gridId = getSelfGridId();
				   ResourceModel newModel = new ResourceModel();
				   newModel.setGridId(gridId);
					newModel.setResourceId(resourceId.getModelObject());
					profilePanel.doSubmitProcess(newModel);
					setOwnerUserId(newModel);
					
					ResourceService service = ServiceFactory.getInstance().getResourceService(gridId);
					service.add(newModel);

					doResultProcess(newModel);
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
			}
		};
	}

	protected void setCancelPage(){
		setResponsePage(new YourLanguageResourcesPage());
	}

	protected void doResultProcess(ResourceModel resource) throws ServiceManagerException {
		if(resource.isApproved()){
			NewsModel nm = new NewsModel();
			nm.setGridId(resource.getGridId());
			nm.setNodeId(MessageUtil.getSelfNodeId());
			Map<String, String> param = new HashMap<String, String>();
			param.put("name", resource.getResourceName());
			nm.setContents(MessageManager.getMessage(
					"news.resource.language.Registered", param));
			ServiceFactory.getInstance().getNewsService(resource.getGridId()).add(nm);
		}else{
			OperationRequestService rService = ServiceFactory.getInstance().getOperationService(
			resource.getGridId(), resource.getGridId(), getSessionUserId());
			OperationRequestModel orm = new OperationRequestModel();
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", resource.getResourceName());
			orm.setContents(MessageManager.getMessage("operation.request.resource.Approve", map));
			orm.setGridId(resource.getGridId());
			orm.setRequestedUserId(resource.getOwnerUserId());
			orm.setTargetId(resource.getResourceId());
			orm.setType(OperationType.RESOURCE);
			rService.add(orm);
		}
		setResponsePage(new RegistrationOfLanguageResourceResultPage(resource.getResourceId()));
	}

	protected void setOwnerUserId(ResourceModel obj) {
		obj.setOwnerUserId(getSessionUserId());
	}

	private RequiredResourceIdField resourceId;
	private ResourceProfileFieldPanel profilePanel;
}
