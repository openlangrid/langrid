/*
 * $Id: EditOfServiceTypePage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.InterfaceDefinitionModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DomainService;
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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
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
public class EditOfServiceTypePage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public EditOfServiceTypePage(final String domainId, final String typeId){
	   final String gridId = getSelfGridId();
	   editDomainId = domainId;
	   editTypeId = typeId;
	   AbstractForm<String> form = new AbstractForm<String>("form", getSelfGridId()){
			@Override
			protected void addComponents(String initialParameter) throws ServiceManagerException{
			   ServiceTypeModel editModel = ServiceFactory.getInstance().getServiceTypeService(gridId).get(domainId, typeId);
			   add(new Label("id", new Model<String>(editModel.getTypeId())));
			   
            add(nameField = new NameTextField("name", editModel.getTypeName(), true));
            
			   DomainService domainService = ServiceFactory.getInstance().getDomainService(gridId);
            add(domainChoice = new DomainDropDownChoice(gridId, "domain"));
            domainChoice.setModelObject(domainService.get(editModel.getDomainId()));
            
			   add(descriptionField = new DescriptionTextAreaField("description", editModel.getDescription(), true));
				
			   add(attributeChoice = new ServiceMetaAttributeListMultipleChoice(gridId, "attribute", editModel.getDomainId()));
            attributeChoice.setRequired(true);
			   if(0 < attributeChoice.getListSize()){
               add(new Label("attension", MessageManager.getMessage("Common.label.attention.MultiChoice", getLocale())));
               attributeChoice.setSelectedList(editModel.getMetaAttrbuteList());
            }else {
               attributeChoice.setVisible(false);
               add(new Label("attension", "-"));
            }
			   
			   interfacePanel = new InterfaceDefinitionFormPanel(gridId, "interface");
            add(interfaceContainer = new SwitchableContainer<InterfaceDefinitionFormPanel, Label>(
               "interfaceSwitchable", interfacePanel, new Label("interfaceLabel", "-")));
            
            if(ServiceFactory.getInstance().getProtocolService(gridId).getAllList().size() == 0) {
               interfaceContainer.switchComponent();
            } else {
               interfacePanel.setValue(editModel.getDomainId(), editModel.getTypeId(), editModel.getInterfaceList());
               for(IFormValidator ifv : interfacePanel.getValidator(false)){
                  add(ifv);
               }
            }
			   
			   add(new Button("edit") {
					@Override
					public void onSubmit() {
                  try {
                     ServiceTypeModel editModel = ServiceFactory.getInstance().getServiceTypeService(gridId).get(domainId, typeId);
                     editModel.setTypeName(nameField.getModelObject());
                     editModel.setDescription(descriptionField.getModelObject());
                     editModel.setDomainId(domainChoice.getValue());
                     editModel.setMetaAttrbuteList(attributeChoice.getModelObject());
                     boolean isAdd = true;
                     if(interfacePanel.isVisible()){
                        List<InterfaceDefinitionModel> list = interfacePanel.getValue(editModel.getTypeId());
                        for(InterfaceDefinitionModel oldIdm : editModel.getInterfaceList()){
                           for(InterfaceDefinitionModel newIdm : interfacePanel.getValue(editModel.getTypeId())){
                              if(oldIdm.getProtocolId().equals(newIdm.getProtocolId())){
                                 isAdd = false;
                                 break;
                              }
                           }
                           if(isAdd){
                              list.add(oldIdm);
                           }
                           isAdd = true;
                        }
                        
                        editModel.setInterfaceList(list);
                     }else{
                        setIsValidateError(true);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("label", "interface");
                        setValidateErrorMessage(MessageManager.getMessage(
                           "message.error.field.Required", map));
                        return;
                     }
                     
                     ServiceFactory.getInstance().getServiceTypeService(getSelfGridId()).edit(editModel);
                  } catch(ServiceManagerException e) {
                     doErrorProcess(e);
                  }
					}
					private static final long serialVersionUID = 1L;
				});
			   add(new Link("cancel"){
			      @Override
			      public void onClick() {
			         setResponsePage(new ServiceTypeListAdminPage());
			      }
			   });
			}

			@Override
			protected void setResultPage(String resultParameter){
			   setResponsePage(new EditOfServiceTypeResultPage(
			      editDomainId, editTypeId));
			}
			
         private NameTextField nameField;
			private DescriptionTextAreaField descriptionField;
         private DropDownChoice<DomainModel> domainChoice;
         private ServiceMetaAttributeListMultipleChoice attributeChoice;
         private SwitchableContainer<InterfaceDefinitionFormPanel, Label> interfaceContainer;
         private InterfaceDefinitionFormPanel interfacePanel;
         

			private static final long serialVersionUID = 1L;
		};
		add(form);
	}

	private String editDomainId;
	private String editTypeId;
}
