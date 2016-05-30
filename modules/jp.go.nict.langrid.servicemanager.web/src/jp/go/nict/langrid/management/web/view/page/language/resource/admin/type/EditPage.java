/*
 * $Id: EditPage.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.choice.DomainDropDownChoice;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.DescriptionTextAreaField;
import jp.go.nict.langrid.management.web.view.component.text.NameTextField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.admin.component.ResourceMetaAttributeListMultipleChoice;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class EditPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 * @param model
	 */
	public EditPage(final ResourceTypeModel model) {
		gridId = getSelfGridId();
		AbstractForm<String> form = new AbstractForm<String>("form") {
			@Override
			protected void addComponents(String initialParameter)
			throws ServiceManagerException {
				add(new Label("id", new Model<String>(model.getResourceTypeId())));
				add(nameField = new NameTextField("name", model.getResourceTypeName(),
					true));
				add(descriptionField = new DescriptionTextAreaField("description",
					model.getDescription(), true));
				add(attributeChoice = new ResourceMetaAttributeListMultipleChoice(gridId,
					"attribute", model.getDomainId()));
				attributeChoice.setRequired(true);
				if(0 < attributeChoice.getListSize()) {
					attributeChoice.setSelectedList(model.getMetaAttrbuteList());
					add(new Label("attension", MessageManager.getMessage(
						"Common.label.attention.MultiChoice", getLocale())));
				} else {
					attributeChoice.setVisible(false);
					add(new Label("attension", "-"));
				}
				add(domainChoice = new DomainDropDownChoice(gridId, "domain"));
				add(new Button("edit") {
					@Override
					public void onSubmit() {
						try {
							model
								.setDomainId(domainChoice.getModelObject().getDomainId());
							model.setResourceTypeName(nameField.getModelObject());
							model.setDescription(descriptionField.getValue());
							model.setMetaAttrbuteList(attributeChoice.getModelObject());
							ServiceFactory.getInstance()
								.getResourceTypeService(getSelfGridId()).edit(model);
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
				setResponsePage(new EditResultPage(model.getDomainId(),
					model.getResourceTypeId()));
			}

			private DomainDropDownChoice domainChoice;
			private DescriptionTextAreaField descriptionField;
			private ResourceMetaAttributeListMultipleChoice attributeChoice;
			private NameTextField nameField;
			private static final long serialVersionUID = 1L;
		};
		add(form);
	}

	private String gridId;
}
