/*
 * $Id: EditOfServiceMetaAttributePage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DomainService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.choice.DomainDropDownChoice;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.RequiredTextArea;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
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
public class EditOfServiceMetaAttributePage extends ServiceManagerPage{
	/**
	* 
	* 
	*/
	public EditOfServiceMetaAttributePage(final String domainId, final String attributeId){
	   final String gridId = getSelfGridId();
	   AbstractForm<String> form = new AbstractForm<String>("form", getSelfGridId(), attributeId){
         @Override
         protected void addComponents(String initialParameter) throws ServiceManagerException{
            editModel = ServiceFactory.getInstance().getServiceMetaAttributeService(gridId).get(domainId, attributeId);
            add(new Label("id", new Model<String>(editModel.getAttributeId())));
            add(nameField = new RequiredTextField<String>("name", new Model<String>(editModel.getAttributeName())));

            DomainService domainService = ServiceFactory.getInstance().getDomainService(gridId);
            add(domainChoice = new DomainDropDownChoice(gridId, "domain"));
            domainChoice.setModelObject(domainService.get(editModel.getDomainId()));
            
            add(descriptionField = new RequiredTextArea("description", new Model<String>(editModel.getDescription())));
            
            add(new Button("edit") {
               @Override
               public void onSubmit() {
                  try {
                     editModel.setDescription(descriptionField.getModelObject());
                     editModel.setDomainId(domainChoice.getValue());
                     editModel.setAttributeName(nameField.getModelObject());
                     ServiceFactory.getInstance().getServiceMetaAttributeService(getSelfGridId()).edit(editModel);
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
            setResponsePage(new EditOfServiceMetaAttributeResultPage(
               editModel.getDomainId(), editModel.getAttributeId()));
         }

         private RequiredTextArea descriptionField;
         private RequiredTextField<String> nameField;
         private DropDownChoice<DomainModel> domainChoice;
         private ServiceMetaAttributeModel editModel;
         
         private static final long serialVersionUID = 1L;
      };
      add(form);
	}
}
