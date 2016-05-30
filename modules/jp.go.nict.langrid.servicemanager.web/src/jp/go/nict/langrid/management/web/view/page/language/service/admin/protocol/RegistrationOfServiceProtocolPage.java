/*
 * $Id: RegistrationOfServiceProtocolPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol;

import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.DescriptionTextAreaField;
import jp.go.nict.langrid.management.web.view.component.text.NameTextField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.component.text.ProtocolIdTextField;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RegistrationOfServiceProtocolPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public RegistrationOfServiceProtocolPage(){
		AbstractForm<String> form = new AbstractForm<String>("form", getSelfGridId()){
			@Override
			protected void addComponents(String initialParameter) throws ServiceManagerException{
			   add(idField = new ProtocolIdTextField("id", getSelfGridId()));
			   add(nameField = new NameTextField("name", true));
			   add(description = new DescriptionTextAreaField("description", true));
				
			   add(new Button("regist") {
					@Override
					public void onSubmit() {
					   try {
					      ProtocolModel model = new ProtocolModel();
					      model.setProtocolId(idField.getValue());
					      model.setProtocolName(nameField.getValue());
					      model.setDescription(description.getValue());
					      model.setOwnerUserGridId(getSelfGridId());
					      model.setOwnerUserId(getSessionUserId());
                     ServiceFactory.getInstance().getProtocolService(getSelfGridId()).add(model);
                  } catch(ServiceManagerException e) {
                     doErrorProcess(e);
                  }
					}
					private static final long serialVersionUID = 1L;
				});
			   add(new Link("cancel"){
               @Override
               public void onClick() {
                  setResponsePage(new ProtocolsOfServiceAdminPage());
               }
            });
			}

			@Override
			protected void setResultPage(String resultParameter){
			   setResponsePage(new RegistrationOfServiceProtocolResultPage(idField.getModelObject()
			      , nameField.getModelObject(), description.getModelObject()));
			}

			private ProtocolIdTextField idField;
			private NameTextField nameField;
			private DescriptionTextAreaField description;
			
			private static final long serialVersionUID = 1L;
		};
		add(form);
	}
}
