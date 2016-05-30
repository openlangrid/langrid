/*
 * $Id: RegistrationOfJavaCompositeServicePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.JavaCompositeServiceModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.CompositeServiceService;
import jp.go.nict.langrid.management.web.model.service.OperationRequestService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.JarUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.form.MultipartForm;
import jp.go.nict.langrid.management.web.view.component.panel.FileUploaderPanel;
import jp.go.nict.langrid.management.web.view.component.text.URLField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.panel.EndpointAuthInfoFieldPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.component.form.panel.ServiceInvocationListFormPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class RegistrationOfJavaCompositeServicePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfJavaCompositeServicePage(
		String gridId, final String ownerUserId, JavaCompositeServiceModel newModel)
	{
		this.model = newModel;
		Form form = new MultipartForm("form");
		add(form);
		form.add(wsdlUploader = new FileUploaderPanel("wsdlFile", false, "wsdl", true));
		wsdlUploader.setRenderBodyOnly(true);
		form.add(authInfoPanel = new EndpointAuthInfoFieldPanel("authInfoFieldPanel", form));
		form.add(sourceCodeUrl = new URLField("sourceUrl", new Model<String>("http://")));

		try {
			form.add(invocations = new ServiceInvocationListFormPanel("invocationPanel", gridId){
				@Override
				protected String getOwnerUserId() {return getSessionUserId();}
				@Override
				protected String getOwnerGridId() {return getSessionUserGridId();}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
		form.add(new SubmitLink("regist", new Model<String>(model.getServiceId())){
			@Override
			public void onSubmit() {
				ServiceEndpointModel newEndpointModel = new ServiceEndpointModel();
				try{
					String sourceCode = sourceCodeUrl.getModelObject();
					if(sourceCode != null && !sourceCode.equals("http://")) {
						model.setSourceCodeUrl(sourceCode);
					}

					Map<String, byte[]> map = wsdlUploader.getFileMap();
					byte[] ler = JarUtil.makeInstance(map);
					model.setInstance(ler);
					model.setInstanceSize(ler.length);
					model.setInstanceType(InstanceType.Java);
					model.setInvocations(invocations.getInvocations(model.getServiceId()));

					CompositeServiceService service = ServiceFactory.getInstance().getCompositeServiceService(model.getGridId());
					service.add(model);

					if(model.isMembersOnly()) {
						ServiceFactory.getInstance().getAccessRightControlService(
							model.getGridId()).setDefault(
								model.getServiceId(), model.getGridId(), ! model.isMembersOnly());
					}

					authInfoPanel.doSubmitProcess(newEndpointModel);
					if(newEndpointModel.getAuthUserName() != null
						&& !newEndpointModel.getAuthUserName().equals(""))
					{
						try {
							ServiceEndpointModel sem = service.getEndpointList(
								model.getServiceId()).get(0);
							service.editEndpoint(model.getServiceId(), sem.getUrl(), "", newEndpointModel);
						} catch(Exception e){
							service.deleteById(model.getServiceId());
							throw new ServiceManagerException(e, getClass());
						}
					}
					LogWriter.writeInfo(getSessionUserId(), "\"" + model.getServiceId()
							+ "\" of composite service has been registered.'", getPage().getClass());
					doResultProcess(service.get(model.getServiceId()));

				} catch(ServiceManagerException e){
					doErrorProcess(e);
				} catch(ResourceStreamNotFoundException e) {
					doErrorProcess(new ServiceManagerException(e, getPageClass()));
				} catch(IOException e) {
					doErrorProcess(new ServiceManagerException(e, getPageClass()));
//				} catch(SerialException e) {
//					doErrorProcess(new ServiceManagerException(e, getPageClass()));
//				} catch(SQLException e) {
//					doErrorProcess(new ServiceManagerException(e, getPageClass()));
				}
			}
		});
		form.add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(getCancelPage(model, false));
			}
		});
	}

	protected Page getCancelPage(JavaCompositeServiceModel model, boolean isEdit){
		return new RegistrationOfCompositeServiceProfilePage(model, isEdit);
	}

	protected void doResultProcess(CompositeServiceModel model)
	throws ServiceManagerException
	{
		if(model.isApproved()) {
			NewsModel nm = new NewsModel();
			nm.setGridId(model.getGridId());
			Map<String, String> param = new HashMap<String, String>();
			param.put("name", model.getServiceName());
			nm.setContents(MessageManager.getMessage(
				"news.service.composite.Registered", param));
			ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
		} else {
			OperationRequestService rService = ServiceFactory.getInstance()
				.getOperationService(
					model.getGridId(), model.getGridId(), getSessionUserId());
			OperationRequestModel orm = new OperationRequestModel();
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", model.getServiceName());
			orm.setContents(MessageManager.getMessage(
				"operation.request.service.composite.Approve", map));
			orm.setGridId(model.getGridId());
			orm.setRequestedUserId(model.getOwnerUserId());
			orm.setTargetId(model.getServiceId());
			orm.setType(OperationType.SERVICE);
			rService.add(orm);
		}
		setResponsePage(new RegistrationOfJavaCompositeServiceResultPage(model.getServiceId()));
	}

	private JavaCompositeServiceModel model;
	private FileUploaderPanel wsdlUploader;
	private URLField sourceCodeUrl;
	private EndpointAuthInfoFieldPanel authInfoPanel;
	private ServiceInvocationListFormPanel invocations;
}
