/*
 * $Id: JavaCompositeServiceEditPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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

import java.util.List;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.JavaCompositeServiceModel;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.CompositeServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.text.URLField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.YourLanguageServicesPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.panel.EndpointFieldPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.IncreasingOptionPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceProfileFieldPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.component.form.panel.ServiceInvocationListFormPanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class JavaCompositeServiceEditPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public JavaCompositeServiceEditPage(String serviceId){
		try {
			String gridId = getSelfGridId();
			sId = serviceId;
			JavaCompositeServiceModel editModel = (JavaCompositeServiceModel) ServiceFactory.getInstance().getCompositeServiceService(gridId).get(serviceId);
			add(form = new Form("form"));
			form.add(new Label("serviceId", new Model<String>(editModel.getServiceId())));
			form.add(profilePanel = new ServiceProfileFieldPanel(gridId, "profilePanel", form, InstanceType.Java));
			profilePanel.setProfileInfo(editModel, true);
			for(IFormValidator fv : profilePanel.getValidatorList()) {
				form.add(fv);
			}

			String scUrl = editModel.getSourceCodeUrl() == null || editModel.getSourceCodeUrl().equals("")
			   ? "http://" : editModel.getSourceCodeUrl();
			form.add(sourceCodeUrl = new URLField("sourceUrl", new Model<String>(scUrl)));

			ioPanel = new IncreasingOptionPanel("usingService");
			ioPanel.setIncreasingOptionInfo(editModel);
			form.add(ioPanel);

			form.add(invocationListFormPanel = new ServiceInvocationListFormPanel("invocationPanel", gridId, editModel ){
				@Override
				protected String getOwnerUserId() {return getSessionUserId();}
				@Override
				protected String getOwnerGridId() {return getSessionUserGridId();}
			});

			List<ServiceEndpointModel> list;
			list = ServiceFactory.getInstance().getAtomicServiceService(editModel.getGridId()).getEndpointList(
							editModel.getServiceId());
			form.add(endpointFieldPanel = new EndpointFieldPanel(
					"endpointFieldPanel", getSelfGridId(), editModel.getServiceId(), list));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}

		form.add(new Button("submit"){
			@Override
			public void onSubmit(){
				try{
					CompositeServiceService service = ServiceFactory.getInstance().getCompositeServiceService(getSelfGridId());
					JavaCompositeServiceModel editModel = (JavaCompositeServiceModel)service.get(sId);
					profilePanel.doSubmitProcess(editModel);
					ioPanel.doSubmitProcess(editModel);
					editModel.setInvocations(invocationListFormPanel.getInvocations(editModel.getServiceId()));
					String sourceCode = sourceCodeUrl.getModelObject();
					if(sourceCode != null && !sourceCode.equals("http://")) {
						editModel.setSourceCodeUrl(sourceCode);
					} else {
						editModel.setSourceCodeUrl("");
					}
					service.edit(editModel);
				
					ServiceFactory.getInstance().getAccessRightControlService(editModel.getGridId()).setDefault(
							editModel.getServiceId(), editModel.getGridId(), ! editModel.isMembersOnly());

					for(ServiceEndpointModel ep : endpointFieldPanel.getRemoveEndopintList()){
						ep.setServiceId(editModel.getServiceId());
						service.deleteEndpoint(ep);
					}
					for(ServiceEndpointModel ep : endpointFieldPanel.getAddEndpointList()){
						ep.setServiceId(editModel.getServiceId());
						service.addEndpoint(ep);
					}

					LogWriter.writeInfo(getSessionUserId()
							, "\"" + editModel.getServiceId()
							+ "\" of composite service has been edited.", getPage().getClass());
					doResultProcess(editModel.getServiceId());
				}catch(ServiceManagerException e){
//					if(e.getErrorCode().equals(LangridError.E052.name())){
//						form.error(MessageManager.getMessage(
//								"ProvidingServices.language.service.error.InvaildParameter", getLocale()));
//						LogWriter.writeOutOfSystemError(getSessionUserId(), e, getPage().getClass()
//								, "Validate Error");
//					}else if(LangridClientUtil.isLangridError(e.getErrorCode())){
//						form.error(MessageManager.getLangridErrorMessage(
//								e.getErrorCode(), getLocale()));
//						LogWriter.writeOutOfSystemError(getSessionUserId(), e, getPage().getClass()
//								, "Validate Error");
//					}else{
						doErrorProcess(e);
//					}
				}
			}

		});

		form.add(new Link("cancel"){
		   @Override
		   public void onClick() {
		      doCancelProcess();
		   }
		});
	}

	protected void doResultProcess(String serviceId) {
		setResponsePage(new JavaCompositeServiceEditResultPage(serviceId));
	}

	protected void doCancelProcess() {
		setResponsePage(new YourLanguageServicesPage());
	}

	protected String getOwnerId() {
		return getSessionUserId();
	}

	private String sId;

	private Form form;
	private ServiceProfileFieldPanel profilePanel;
	private IncreasingOptionPanel ioPanel;
	private ServiceInvocationListFormPanel invocationListFormPanel;
	private EndpointFieldPanel endpointFieldPanel;
	private URLField sourceCodeUrl;
}
