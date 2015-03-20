/*
 * $Id: RegistrationOfAtomicServicePage.java 1237 2014-08-05 08:38:22Z t-nakaguchi $
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.ServiceAlreadyExistsException;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.logic.ServiceLogicException;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AtomicServiceService;
import jp.go.nict.langrid.management.web.model.service.OperationRequestService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.JarUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.form.MultipartForm;
import jp.go.nict.langrid.management.web.view.component.panel.FileUploaderPanel;
import jp.go.nict.langrid.management.web.view.component.text.URLField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.panel.EndpointAuthInfoFieldPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.IncreasingOptionPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceProfileFieldPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.text.RequiredServiceIdField;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1237 $
 */
public class RegistrationOfAtomicServicePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfAtomicServicePage(String resourceId, String ownerUserId) {
		if(resourceId == null || resourceId.equals("")) {
			throw new RestartResponseException(new RegistrationOfAtomicServiceListPage());
		}
		try {
			String gridId = getSelfGridId();
			this.ownerUserId = ownerUserId;
			relatedModel = ServiceFactory.getInstance().getResourceService(gridId).get(resourceId);
			add(new Label("labelHeadResourceName", relatedModel.getResourceId()));
			add(form = new MultipartForm("form"));
			form.add(serviceId = new RequiredServiceIdField("serviceId", gridId));
			form.add(profilePanel = new ServiceProfileFieldPanel(gridId, "profileFieldPanel", form, InstanceType.EXTERNAL));
			profilePanel.setResourceInfo(relatedModel);
			for(IFormValidator fv : profilePanel.getValidatorList()) {
				form.add(fv);
			}
			form.add(authInfoPanel = new EndpointAuthInfoFieldPanel("authInfoFieldPanel", form));
			wsdlUploader = new FileUploaderPanel("wsdlFile", false, "wsdl", true);
			wsdlUploader.setRenderBodyOnly(true);
			form.add(wsdlUploader);
			form.add(sourceCodeUrl = new URLField("sourceUrl", new Model<String>("http://")));
			form.add(ioPanel = new IncreasingOptionPanel("increasingOptionPanel"));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
		form.add(new SubmitLink("submit") {
			@Override
			public void onSubmit() {
				AtomicServiceModel newModel = new AtomicServiceModel();
				ServiceEndpointModel newEndpointModel = new ServiceEndpointModel();
				try {
					newModel.setServiceId(serviceId.getModelObject());
					newModel.setGridId(getSelfGridId());
					newModel.setOwnerUserId(getOwnerId());
					// サービスは資源と同じライセンス、著作権を使用する
					newModel.setLicenseInfo(relatedModel.getLicenseInfo());
					newModel.setCopyrightInfo(relatedModel.getCopyrightInfo());
					profilePanel.doSubmitProcess(newModel);
					ioPanel.doSubmitProcess(newModel);
					String sourceCode = sourceCodeUrl.getModelObject();
					if(sourceCode != null && !sourceCode.equals("http://")) {
						newModel.setSourceCodeUrl(sourceCode);
					}
					
					Map<String, byte[]> map = wsdlUploader.getFileMap();
					byte[] ler = JarUtil.makeInstance(map);
					newModel.setInstance(ler);
					newModel.setInstanceType(InstanceType.EXTERNAL);
					newModel.setInstanceSize(ler.length);
					newModel.setResourceId(relatedModel.getResourceId());
					AtomicServiceService service = ServiceFactory.getInstance()
						.getAtomicServiceService(newModel.getGridId());
					service.add(newModel);
					if(newModel.isMembersOnly()) {
						ServiceFactory.getInstance()
							.getAccessRightControlService(newModel.getGridId())
							.setDefault(
								newModel.getServiceId(), newModel.getGridId(),
								!newModel.isMembersOnly());
					}
					authInfoPanel.doSubmitProcess(newEndpointModel);
					if(newEndpointModel.getAuthUserName() != null
						&& !newEndpointModel.getAuthUserName().equals("")) {
						ServiceEndpointModel sem = service.getEndpointList(
							newModel.getServiceId()).get(0);
						service.editEndpoint(newModel.getServiceId(), sem.getUrl(), "",
							newEndpointModel);
					}
					LogWriter.writeInfo(getSessionUserId(), "\"" + serviceId.getValue()
							+ "\" of atomic service has been registered with resource'"
							+ relatedModel.getResourceId() + "'.", getPage().getClass());
					doResultProcess(service.get(newModel.getServiceId()));
				} catch(IOException e) {
					doErrorProcess(new ServiceManagerException(e, getPage().getClass()));
				} catch(ServiceManagerException e) {
					if(e.getParentException().getClass()
						.equals(ServiceLogicException.class)) {
						if(((ServiceLogicException)e.getParentException()).getMessage()
							.contains("SAXParseException")) {
							form.error(MessageManager.getMessage(
								"ProvidingServices.language.service.error.WSDLInvalid",
								getLocale()));
							LogWriter.writeOutOfSystemError(getSessionUserId(),
								e.getParentException(), getPage().getClass()
								, "Validate Error");
						}
						return;
					} else if(e.getParentException().getClass()
						.equals(ServiceAlreadyExistsException.class)) {
						form.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.AlreadyExists",
							getLocale()));
						LogWriter.writeOutOfSystemError(getSessionUserId(),
							e.getParentException(), getPage().getClass()
							, "Validate Error");
						return;
					}
					doErrorProcess(e);
				} catch(ResourceStreamNotFoundException e) {
					form.error(MessageManager
						.getMessage(
							"ProvidingServices.language.service.error.validate.file.wsdl.notFound",
							getLocale()));
					LogWriter.writeOutOfSystemError(getSessionUserId(), e, getPage()
						.getClass()
						, "Resource Error");
//				} catch(SerialException e) {
//					doErrorProcess(new ServiceManagerException(e));
//				} catch(SQLException e) {
//					doErrorProcess(new ServiceManagerException(e));
//				} finally {
//					if(newModel.getInstance() != null) {
//						try {
//							newModel.getInstance().getBinaryStream().close();
//						} catch(IOException e) {
//							LogWriter.writeWarn(getSessionUserId(), e.toString(),
//								getClass());
//						} catch(SQLException e) {
//							LogWriter.writeWarn(getSessionUserId(), e.toString(),
//								getClass());
//						}
//					}
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

	protected void doResultProcess(AtomicServiceModel model)
	throws ServiceManagerException {
		if(model.isApproved()) {
			NewsModel nm = new NewsModel();
			nm.setGridId(model.getGridId());
			nm.setNodeId(MessageUtil.getSelfNodeId());
			Map<String, String> param = new HashMap<String, String>();
			param.put("name", model.getServiceName());
			nm.setContents(MessageManager.getMessage(
				"news.service.atomic.Registered", param));
			ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
		} else {
			OperationRequestService rService = ServiceFactory.getInstance().getOperationService(
				model.getGridId(), model.getGridId(), getSessionUserId());
			OperationRequestModel orm = new OperationRequestModel();
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", model.getServiceName());
			orm.setContents(MessageManager.getMessage(
				"operation.request.service.atomic.Approve", map));
			orm.setGridId(model.getGridId());
			orm.setRequestedUserId(model.getOwnerUserId());
			orm.setTargetId(model.getServiceId());
			orm.setType(OperationType.SERVICE);
			rService.add(orm);
		}
		setResponsePage(new RegistrationOfAtomicServiceResultPage(model));
	}

	protected void doCancelProcess() {
		setResponsePage(new RegistrationOfAtomicServiceListPage());
	}

	protected String getOwnerId() {
		return ownerUserId;
	}

	private ResourceModel relatedModel;
	private Form form;
	private RequiredServiceIdField serviceId;
	private ServiceProfileFieldPanel profilePanel;
	private EndpointAuthInfoFieldPanel authInfoPanel;
	private FileUploaderPanel wsdlUploader;
	private IncreasingOptionPanel ioPanel;
	private URLField sourceCodeUrl;
	private String ownerUserId;
}
