/*
 * $Id: AtomicServiceEditPage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.atomic;

import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AtomicServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.text.URLField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.YourLanguageServicesPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.panel.EndpointFieldPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.IncreasingOptionPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceProfileFieldPanel;

import org.apache.wicket.RestartResponseException;
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
 * @version $Revision: 497 $
 */
public class AtomicServiceEditPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public AtomicServiceEditPage(String serviceId) {
		if(serviceId == null) {
			throw new RestartResponseException(new YourLanguageServicesPage());
		}
		try {
			sId = serviceId;
			AtomicServiceModel model = ServiceFactory.getInstance().getAtomicServiceService(getSelfGridId()).get(serviceId);
			add(form = new Form("form"));
			form.add(new Label("serviceId", new Model<String>(model.getServiceId())));
			form.add(profilePanel = new ServiceProfileFieldPanel(
				model.getGridId(), "profileFieldPanel", form, InstanceType.EXTERNAL));
			String scUrl = model.getSourceCodeUrl() == null
				|| model.getSourceCodeUrl().equals("") ? "http://" : model.getSourceCodeUrl();
			form.add(sourceCodeUrl = new URLField("sourceUrl", new Model<String>(scUrl)));
			Set<String> provi = model.getAllowedAppProvision();
			Set<String> usage = model.getAllowedUsage();
			form.add(ioPanel = new IncreasingOptionPanel("increasingOptionPanel", provi
				, usage, model.isFederatedUseAllowed()));
			List<ServiceEndpointModel> list;
			list = ServiceFactory.getInstance().getAtomicServiceService(
				model.getGridId()).getEndpointList(model.getServiceId());
			form.add(endpointFieldPanel = new EndpointFieldPanel(
					"endpointFieldPanel", getSelfGridId(), model.getServiceId(), list));
			profilePanel.setProfileInfo(model, true);
			for(IFormValidator fv : profilePanel.getValidatorList()) {
				form.add(fv);
			}
			form.add(new Button("submit") {
				@Override
				public void onSubmit() {
					try {
						AtomicServiceModel model = ServiceFactory.getInstance().getAtomicServiceService(getSelfGridId()).get(sId);
						profilePanel.doSubmitProcess(model);
						String sourceCode = sourceCodeUrl.getModelObject();
						if(sourceCode != null && !sourceCode.equals("http://")) {
							model.setSourceCodeUrl(sourceCode);
						} else {
							model.setSourceCodeUrl("");
						}
						ioPanel.doSubmitProcess(model);
						AtomicServiceService service = ServiceFactory.getInstance().getAtomicServiceService(model.getGridId());
						service.edit(model);
						ServiceFactory.getInstance()
							.getAccessRightControlService(model.getGridId())
							.setDefault(model.getServiceId(), model.getGridId(), ! model.isMembersOnly());
						for(ServiceEndpointModel ep : endpointFieldPanel.getRemoveEndopintList()) {
							ep.setServiceId(model.getServiceId());
							service.deleteEndpoint(ep);
						}
						for(ServiceEndpointModel ep : endpointFieldPanel.getAddEndpointList()) {
							ep.setServiceId(model.getServiceId());
							service.addEndpoint(ep);
						}
						LogWriter.writeInfo(getSessionUserId()
								, "\"" + model.getServiceId() + "\" of atomic service has been edited."
								, getClass());
						doResultProcess(model.getServiceId());
					} catch(ServiceManagerException e) {
						doErrorProcess(e);
					}
				}
			});
			form.add(new Link<YourLanguageServicesPage>("cancel") {
				@Override
				public void onClick() {
					doCancelProcess();
				}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	protected void doResultProcess(String serviceId) {
		setResponsePage(new AtomicServiceEditResultPage(serviceId));
	}

	protected void doCancelProcess() {
		setResponsePage(new YourLanguageServicesPage());
	}

	private Form form;
	private String sId;
	private ServiceProfileFieldPanel profilePanel;
	private IncreasingOptionPanel ioPanel;
	private URLField sourceCodeUrl;
	private EndpointFieldPanel endpointFieldPanel;
}
