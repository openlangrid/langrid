/*
 * $Id: RegistrationOfServiceMetaAttributePage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.metaattribute;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.choice.DomainDropDownChoice;
import jp.go.nict.langrid.management.web.view.component.container.SwitchableContainer;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.DescriptionTextAreaField;
import jp.go.nict.langrid.management.web.view.component.text.NameTextField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RegistrationOfServiceMetaAttributePage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public RegistrationOfServiceMetaAttributePage(){
	   final String gridId = getSelfGridId();
	   AbstractForm<String> form = new AbstractForm<String>("form", getSelfGridId()){
         @Override
         protected void addComponents(String initialParameter) throws ServiceManagerException{
            add(idField = new RequiredTextField<String>("id", new Model<String>()));
            add(nameField = new NameTextField("name", true));
            add(description = new DescriptionTextAreaField("description", true));
            domainChoice = new DomainDropDownChoice(gridId, "domain");
            add(domainContainer = new SwitchableContainer<DomainDropDownChoice, Label>(
               "switchable", domainChoice, new Label("domainLabel", "-")));
            if(domainChoice.getListSize() == 0) {
               domainContainer.switchComponent();
            }
            add(new Button("regist") {
               @Override
               public void onSubmit() {
                  try {
                     ServiceMetaAttributeModel newModel = new ServiceMetaAttributeModel();
                     newModel.setAttributeId(idField.getModelObject());
                     newModel.setAttributeName(nameField.getModelObject());
                     newModel.setDescription(description.getModelObject());
                     if(domainChoice.isVisible()){
                        newModel.setDomainId(domainChoice.getModelObject().getDomainId());
                     }else{
                        setIsValidateError(true);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("label", "domain");
                        setValidateErrorMessage(MessageManager.getMessage(
                           "message.error.field.Required", map));
                        return;
                     }
                     ServiceFactory.getInstance().getServiceMetaAttributeService(gridId).add(newModel);
                  } catch(ServiceManagerException e) {
                     doErrorProcess(e);
                  }
               }
               private static final long serialVersionUID = 1L;
            });
            add(new Link("cancel"){
               @Override
               public void onClick() {
                  setResponsePage(new ServiceMetaAttributeListAdminPage());
               }
            });
         }

         @Override
         protected void setResultPage(String resultParameter){
            try {
               ServiceMetaAttributeModel model = ServiceFactory.getInstance().getServiceMetaAttributeService(
                  gridId).get(domainChoice.getModelObject().getDomainId(), idField.getModelObject());
               setResponsePage(new RegistrationOfServiceMetaAttributeResultPage(model.getDomainId(), model.getAttributeId()));
            } catch(ServiceManagerException e) {
               doErrorProcess(e);
            }
         }

         private RequiredTextField<String> idField;
         private NameTextField nameField;
         private DescriptionTextAreaField description;
         private DomainDropDownChoice domainChoice;
         private SwitchableContainer<DomainDropDownChoice, Label> domainContainer;
         
         private static final long serialVersionUID = 1L;
      };
      add(form);
	}
}
