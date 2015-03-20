/*
 * $Id: YourLanguageResourcesEditPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ResourceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.form.panel.ResourceProfileFieldPanel;

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
 * @version $Revision: 303 $
 */
public class YourLanguageResourcesEditPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public YourLanguageResourcesEditPage(String resourceId){
		try {
		  model = ServiceFactory.getInstance().getResourceService(getSelfGridId()).get(resourceId);
			Form form = new Form("form");
			form.add(new Label("resourceId", new Model<String>(resourceId)));
			form.add(profilePanel = new ResourceProfileFieldPanel(getSelfGridId(), "profileFieldPanel", form));
			profilePanel.setProfileInfo(model);
			form.add(getCancelLink("cancel"));
			form.add(getSubmitLink("update"));
			for(IFormValidator fv : profilePanel.getValidatorList()) {
				form.add(fv);
			}
			add(form);
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
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
					profilePanel.doSubmitProcess(model);
					ResourceService service = ServiceFactory.getInstance().getResourceService(getSelfGridId());
					service.edit(model);
					setResultPage(model.getResourceId());
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
			}
		};
	}
	
	protected void setCancelPage() {
		setResponsePage(new YourLanguageResourcesPage());
	}
	
	protected void setResultPage(String resourceId){
		setResponsePage(new YourLanguageResourcesEditResultPage(resourceId));	
	}

	private ResourceModel model;
	private ResourceProfileFieldPanel profilePanel;
}
