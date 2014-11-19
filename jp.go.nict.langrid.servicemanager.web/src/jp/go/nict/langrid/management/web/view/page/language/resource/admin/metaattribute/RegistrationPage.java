/*
 * $Id: RegistrationPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.admin.metaattribute;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.choice.DomainDropDownChoice;
import jp.go.nict.langrid.management.web.view.component.container.SwitchableContainer;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.DescriptionTextAreaField;
import jp.go.nict.langrid.management.web.view.component.text.NameTextField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.metaattribute.component.text.ResourceMetaAttributeIdTextField;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RegistrationPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public RegistrationPage() {
	   gridId = getSelfGridId();
		AbstractForm<String> form = new AbstractForm<String>("form") {
			@Override
			protected void addComponents(String initialParameter) throws ServiceManagerException {
            domainChoice = new DomainDropDownChoice(gridId, "domain");
            add(domainContainer = new SwitchableContainer<DomainDropDownChoice, Label>(
               "switchable", domainChoice, new Label("domainLabel", "-")));
            if(domainChoice.getListSize() == 0) {
				   domainContainer.switchComponent();
				}
            
            add(attributeIdField = new ResourceMetaAttributeIdTextField("id", gridId));
				add(attributeNameField = new NameTextField("name", true));
				add(description = new DescriptionTextAreaField("description", true));

				add(new Button("regist") {
					@Override
					public void onSubmit() {
						try {
							ResourceMetaAttributeModel model = new ResourceMetaAttributeModel();
                    if(domainChoice.isVisible()){
                        model.setDomainId(domainChoice.getModelObject().getDomainId());
                     }else{
                        setIsValidateError(true);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("label", "domain");
                        setValidateErrorMessage(MessageManager.getMessage(
                           "message.error.field.Required", map));
                        return;
                     }
							model.setAttributeId(attributeIdField.getValue());
							model.setAttributeName(attributeNameField.getValue());
							model.setDescription(description.getValue());
							ServiceFactory.getInstance().getResourceMetaAttributeService(getSelfGridId()).add(model);
						} catch(ServiceManagerException e) {
							doErrorProcess(e);
						}
					}
					private static final long serialVersionUID = 1L;
				});
            add(new Link("cancel"){
               @Override
               public void onClick() {
                  setResponsePage(new ListPage());
               }
            });
			}

			@Override
			protected void setResultPage(String resultParameter){
				setResponsePage(new RegistrationResultPage(
								  domainChoice.getModelObject().getDomainId()
								, attributeIdField.getModelObject()));
			}

         private DomainDropDownChoice domainChoice;
			private ResourceMetaAttributeIdTextField attributeIdField;
			private NameTextField attributeNameField;
			private DescriptionTextAreaField description;
			private SwitchableContainer<DomainDropDownChoice, Label> domainContainer;

			private static final long serialVersionUID = 1L;
		};
		add(form);
	}
	private String gridId;
}
