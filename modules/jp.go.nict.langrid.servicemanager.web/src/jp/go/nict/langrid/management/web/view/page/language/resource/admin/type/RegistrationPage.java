/*
 * $Id: RegistrationPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.admin.type;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.choice.DomainDropDownChoice;
import jp.go.nict.langrid.management.web.view.component.container.SwitchableContainer;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.DescriptionTextAreaField;
import jp.go.nict.langrid.management.web.view.component.text.NameTextField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.component.ResourceMetaAttributeListMultipleChoice;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.type.component.text.ResourceTypeIdTextField;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RegistrationPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public RegistrationPage() {
		gridId = getSelfGridId();
		AbstractForm<String> form = new AbstractForm<String>("form") {
			@Override
			protected void addComponents(String initialParameter)
			throws ServiceManagerException {
				add(resourceTypeIdField = new ResourceTypeIdTextField("id", gridId));
				add(nameField = new NameTextField("name", true));
				add(description = new DescriptionTextAreaField("description", true));
				domainChoice = new DomainDropDownChoice(gridId, "domain");
				domainChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						try {
							attributeChoice.setModelList(gridId, domainChoice.getModelObject().getDomainId());
							if(attributeChoice.getListSize() == 0){
								if(attributeContainer.isFirstVisible()){
									attributeContainer.switchComponent();
								}
							}else{
								if( ! attributeContainer.isFirstVisible()){
									attributeContainer.switchComponent();
								}
							}
							target.addComponent(attributeContainer);
						} catch(ServiceManagerException e) {
							doErrorProcess(e);
						}
					}
				});
				add(domainContainer = new SwitchableContainer<DomainDropDownChoice, Label>(
					"switchable", domainChoice, new Label("domainLabel", "-")));
				if(domainChoice.getListSize() == 0) {
					domainContainer.switchComponent();
				}

				WebMarkupContainer wmc = new WebMarkupContainer("attributeChoiceContainer");
				DomainModel dm = domainChoice.getDefaultDomain();
				wmc.add(attributeChoice = new ResourceMetaAttributeListMultipleChoice(gridId
					, "attribute", dm == null ? "" : dm.getDomainId()));
				attributeChoice.setRequired(true);
				wmc.add(new Label("attension", MessageManager.getMessage(
					"Common.label.attention.MultiChoice", getLocale())));
				
				add(attributeContainer = new SwitchableContainer<WebMarkupContainer, Label>(
					"attributeContainer", wmc, new Label("attributeLabel", "-")));
				attributeContainer.setOutputMarkupId(true);
				if(0 == attributeChoice.getListSize()) {
					attributeContainer.switchComponent();
				}

				add(new Button("regist") {
					@Override
					public void onSubmit() {
						try {
							ResourceTypeModel rm = new ResourceTypeModel();
							rm.setResourceTypeName(nameField.getModelObject());
							if(domainChoice.isVisible()) {
								rm.setDomainId(domainChoice.getModelObject()
									.getDomainId());
							} else {
								setIsValidateError(true);
								Map<String, String> map = new HashMap<String, String>();
								map.put("label", "domain");
								setValidateErrorMessage(MessageManager.getMessage(
									"message.error.field.Required", map));
								return;
							}
							rm.setResourceTypeId(resourceTypeIdField.getValue());
							rm.setDescription(description.getValue());
							if(attributeContainer.getFirstComponent().isVisible()) {
								rm.setMetaAttrbuteList(attributeChoice.getModelObject());
							} else {
								setIsValidateError(true);
								Map<String, String> map = new HashMap<String, String>();
								map.put("label", "Meta Attribute");
								setValidateErrorMessage(MessageManager.getMessage(
									"message.error.field.Required", map));
								return;
							}
							ServiceFactory.getInstance()
								.getResourceTypeService(getSelfGridId()).add(rm);
						} catch(ServiceManagerException e) {
							doErrorProcess(e);
						}
					}

					private static final long serialVersionUID = 1L;
				});
				add(new Link("cancel") {
					@Override
					public void onClick() {
						setResponsePage(new ListPage());
					}
				});
			}

			@Override
			protected void setResultPage(String resultParameter) {
				setResponsePage(new RegistrationResultPage(
									domainChoice.getModelObject().getDomainId()
								, resourceTypeIdField.getModelObject()));
			}

			private DomainDropDownChoice domainChoice;
			private ResourceTypeIdTextField resourceTypeIdField;
			private NameTextField nameField;
			private DescriptionTextAreaField description;
			private ResourceMetaAttributeListMultipleChoice attributeChoice;
			private SwitchableContainer<WebMarkupContainer,Label> attributeContainer;
			private SwitchableContainer<DomainDropDownChoice, Label> domainContainer;
			private static final long serialVersionUID = 1L;
		};
		add(form);
	}

	private String gridId;
}
