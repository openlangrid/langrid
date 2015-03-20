/*
 * $Id: RegistrationOfServiceTypePage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.type;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.choice.DomainDropDownChoice;
import jp.go.nict.langrid.management.web.view.component.container.SwitchableContainer;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.DescriptionTextAreaField;
import jp.go.nict.langrid.management.web.view.component.text.NameTextField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.component.choice.ServiceMetaAttributeListMultipleChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel.InterfaceDefinitionFormPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.text.ServiceTypeIdTextField;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RegistrationOfServiceTypePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfServiceTypePage() {
		final String gridId = getSelfGridId();
		AbstractForm<String> form = new AbstractForm<String>("form") {
			@Override
			protected void addComponents(String initialParameter)
			throws ServiceManagerException {
				add(idField = new ServiceTypeIdTextField("id", gridId));
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
				wmc.add(attributeChoice = new ServiceMetaAttributeListMultipleChoice(
					gridId, "attribute", dm == null ? "" : dm.getDomainId()));
				attributeChoice.setRequired(true);
				wmc.add(new Label("attension", MessageManager.getMessage(
					"Common.label.attention.MultiChoice", getLocale())));
				add(attributeContainer = new SwitchableContainer<WebMarkupContainer, Label>(
				"attributeContainer", wmc, new Label("attributeLabel", "-")));
				attributeContainer.setOutputMarkupId(true);

				if(0 == attributeChoice.getListSize()) {
					attributeContainer.switchComponent();
				}
				
				interfacePanel = new InterfaceDefinitionFormPanel(gridId, "interface");
				add(interfaceContainer = new SwitchableContainer<InterfaceDefinitionFormPanel, Label>(
					"interfaceSwitchable", interfacePanel, new Label("interfaceLabel",
						"-")));
				if(ServiceFactory.getInstance().getProtocolService(gridId).getAllList()
					.size() == 0) {
					interfaceContainer.switchComponent();
				} else {
					for(IFormValidator ifv : interfacePanel.getValidator(true)) {
						add(ifv);
					}
				}
				add(new Button("regist") {
					@Override
					public void onSubmit() {
						try {
							ServiceTypeModel newModel = new ServiceTypeModel();
							newModel.setTypeId(idField.getModelObject());
							newModel.setTypeName(nameField.getModelObject());
							newModel.setDescription(description.getModelObject());
							if(attributeContainer.getFirstComponent().isVisible()) {
								newModel.setMetaAttrbuteList(attributeChoice
									.getModelObject());
							} else {
								setIsValidateError(true);
								setValidateErrorMessage("Meta Attribute is required");
								return;
							}
							if(interfacePanel.isVisible()) {
								newModel.setInterfaceList(interfacePanel.getValue(idField.getModelObject()));
							} else {
								setIsValidateError(true);
								Map<String, String> map = new HashMap<String, String>();
								map.put("label", "interface");
								setValidateErrorMessage(MessageManager.getMessage(
									"message.error.field.Required", map));
								return;
							}
							if(domainChoice.isVisible()) {
								newModel.setDomainId(domainChoice.getModelObject()
									.getDomainId());
							} else {
								setIsValidateError(true);
								Map<String, String> map = new HashMap<String, String>();
								map.put("label", "domain");
								setValidateErrorMessage(MessageManager.getMessage(
									"message.error.field.Required", map));
								return;
							}
							ServiceFactory.getInstance()
								.getServiceTypeService(getSelfGridId()).add(newModel);
						} catch(ServiceManagerException e) {
							doErrorProcess(e);
						}
					}

					private static final long serialVersionUID = 1L;
				});
				add(new Link("cancel") {
					@Override
					public void onClick() {
						setResponsePage(new ServiceTypeListAdminPage());
					}
				});
			}

			@Override
			protected void setResultPage(String resultParameter) {
				setResponsePage(new RegistrationOfServiceTypeResultPage(
					domainChoice.getModelObject().getDomainId(), idField.getModelObject()));
			}

			private DomainDropDownChoice domainChoice;
			private SwitchableContainer<DomainDropDownChoice, Label> domainContainer;
			private SwitchableContainer<WebMarkupContainer,Label> attributeContainer;
			private SwitchableContainer<InterfaceDefinitionFormPanel, Label> interfaceContainer;
			private ServiceTypeIdTextField idField;
			private NameTextField nameField;
			private DescriptionTextAreaField description;
			private ServiceMetaAttributeListMultipleChoice attributeChoice;
			private InterfaceDefinitionFormPanel interfacePanel;
			private static final long serialVersionUID = 1L;
		};
		add(form);
	}
}
