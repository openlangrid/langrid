/*
 * $Id: RegistrationOfCompositeServiceProfilePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.composite;

import jp.go.nict.langrid.dao.ServiceAlreadyExistsException;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.management.logic.ServiceLogicException;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.JavaCompositeServiceModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.YourLanguageServicesPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.IncreasingOptionPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceProfileFieldPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.text.RequiredServiceIdField;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.component.form.panel.ImplementationLanguagePanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class RegistrationOfCompositeServiceProfilePage extends ServiceManagerPage {
	public RegistrationOfCompositeServiceProfilePage(ServiceModel model, boolean isEdit) {
		this(model.getOwnerUserId());
		serviceId.setModelObject(model.getServiceId());
		implLangPanel.setImplementationLanguage(model);
		try {
			profilePanel.setProfileInfo(model, isEdit);
			ioPanel.setIncreasingOptionInfo(model);
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public RegistrationOfCompositeServiceProfilePage(String ownerUserId) {
		try {
			gridId = getSelfGridId();
			this.ownerId = ownerUserId;
			add(form = new Form("form"));

			/** basic datas **/
			form.add(serviceId = new RequiredServiceIdField("serviceId", gridId));
			form.add(profilePanel = new ServiceProfileFieldPanel(gridId, "profileFieldPanel", form, InstanceType.BPEL));
			for(IFormValidator fv : profilePanel.getValidatorList()) {
				form.add(fv);
			}
			form.add(ioPanel = new IncreasingOptionPanel("usingService"));

			/** implementation **/
			form.add(implLangPanel = new ImplementationLanguagePanel("implLangPanel"));

		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}

		form.add(new SubmitLink("submit") {
			@Override
			public void onSubmit() {
				try {
					if(implLangPanel.getInstanceType().equals(InstanceType.Java)){
						JavaCompositeServiceModel newModel = new JavaCompositeServiceModel();
						setSubmitValues(newModel);
						setResponsePage(getJavaRegistPage(gridId, ownerId, newModel));
					}else if(implLangPanel.getInstanceType().equals(InstanceType.BPEL)){
						CompositeServiceModel newModel = new CompositeServiceModel();
						setSubmitValues(newModel);
						setResponsePage(getBpelRegistPage(gridId, ownerId, newModel));
					}
				} catch(ServiceManagerException e) {
					if(e.getParentException().getClass().equals(ServiceLogicException.class)) {
						if(((ServiceLogicException)e.getParentException()).getMessage().contains("SAXParseException")) {
							form.error(MessageManager.getMessage(
								"ProvidingServices.language.service.error.WSDLInvalid",getLocale()));
							LogWriter.writeOutOfSystemError(getSessionUserId(), e.getParentException(), getPage().getClass(), "Validate Error");
							return;
						}
					} else if(e.getParentException().getClass().equals(ServiceAlreadyExistsException.class)) {
						form.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.AlreadyExists",getLocale()));
						LogWriter.writeOutOfSystemError(getSessionUserId(),
							e.getParentException(), getPage().getClass(), "Validate Error");
						return;
					}
					doErrorProcess(e);
				}
			}
		});
		form.add(new Link("cancel") {
			@Override
			public void onClick() {
				doCancelProcess();
			}
		});
	}

	protected void doCancelProcess() {
		setResponsePage(new YourLanguageServicesPage());
	}

	protected String getOwnerId() {
		return ownerId;
	}

	protected Page getJavaRegistPage(String gridId, String ownerId, JavaCompositeServiceModel model){
		return new RegistrationOfJavaCompositeServicePage(gridId, ownerId, model);
	}

	protected Page getBpelRegistPage(String gridId, String ownerId, CompositeServiceModel model){
		return new RegistrationOfBpelCompositeServicePage(gridId, ownerId, model);
	}

	private void setSubmitValues(ServiceModel model) throws ServiceManagerException {
		model.setGridId(getSelfGridId());
		model.setServiceId(serviceId.getModelObject());
		model.setOwnerUserId(getOwnerId());
		profilePanel.doSubmitProcess(model);
		ioPanel.doSubmitProcess(model);
		model.setInstanceType(implLangPanel.getInstanceType());		
	}

	private Form form;
	private RequiredServiceIdField serviceId;
	private ServiceProfileFieldPanel profilePanel;
	private IncreasingOptionPanel ioPanel;

	private ImplementationLanguagePanel implLangPanel;
	private String gridId;
	private String ownerId;
}
