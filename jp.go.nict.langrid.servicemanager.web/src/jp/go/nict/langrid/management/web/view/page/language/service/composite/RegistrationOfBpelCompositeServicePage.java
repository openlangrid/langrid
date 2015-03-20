/*
 * $Id: RegistrationOfBpelCompositeServicePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.go.nict.langrid.dao.ServiceAlreadyExistsException;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.logic.ServiceLogicException;
import jp.go.nict.langrid.management.logic.ServiceNotActivatableException;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.CompositeServiceService;
import jp.go.nict.langrid.management.web.model.service.OperationRequestService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.JarUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.form.MultipartForm;
import jp.go.nict.langrid.management.web.view.component.panel.FileUploaderPanel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel.FileMultipleUploadPanel;

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
public class RegistrationOfBpelCompositeServicePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfBpelCompositeServicePage(
		String gridId, final String ownerUserId, CompositeServiceModel newModel) {
		this.model = newModel;
		add(form = new MultipartForm("form"));

//		multipleUploader = new MultipleFileUploaderPanel("wsdlFiles", false, "wsdl", true);
//		form.add(multipleUploader);

		form.add(wsdlUploader = new FileMultipleUploadPanel(gridId, "wsdlFiles", true));

		bpelUploader = new FileUploaderPanel("bpelFile", false, "bpel", true);
		bpelUploader.setRenderBodyOnly(true);
		form.add(bpelUploader);

//		form.add(new FileUploadMultipleRelatedRequireValidator(bpelUploader, multipleUploader
//		, MessageManager.getMessage(
//			"ProvidingServices.language.service.error.upload.file.Associate"
//			, getLocale())));


		form.add(new SubmitLink("regist", new Model<String>(model.getServiceId())) {
			@Override
			public void onSubmit() {
				try {
//					Map<String, byte[]> map = multipleUploader.getFileMap();
//					map.putAll(bpelUploader.getFileMap());
//					byte[] lbr = JarUtil.makeInstance(map);

					byte[] lbr = wsdlUploader.getJarFileInstance();
					Map<String, byte[]> map = bpelUploader.getFileMap();
					for(String name : map.keySet()) {
						lbr = JarUtil.addEntry(name, map.get(name), lbr);
					}

					model.setInstanceType(InstanceType.BPEL);
					model.setInstance(lbr);
					model.setInstanceSize(lbr.length);

					CompositeServiceService service = ServiceFactory.getInstance().getCompositeServiceService(getSelfGridId());
					service.add(model);

					LogWriter.writeInfo(getSessionUserId()
						, "\"" + getDefaultModelObjectAsString() + "\" of composite service has been registered."
						, getPage().getClass());

					doResultProcess(service.get(getDefaultModelObjectAsString()));
				} catch(ServiceManagerException e) {
					if(e.getParentException().getClass().equals(ServiceLogicException.class)) {
						ServiceLogicException sle = (ServiceLogicException)e.getParentException();
						if(sle.getMessage().contains("SAXParseException")) {
							form.error(MessageManager.getMessage(
								"ProvidingServices.language.service.error.WSDLInvalid",
								getLocale()));
							LogWriter.writeOutOfSystemError(getSessionUserId(),
								e.getParentException(), getPage().getClass()
								, "Validate Error");
							return;
						}else if(sle.getMessage().contains("InvalidServiceInstanceException")){
							String[] ms = sle.getMessage().split("Exception: ");
							form.error(ms[ms.length - 1].replaceAll("]$", ""));
							return;
						}
					} else if(e.getParentException().getClass().equals(ServiceAlreadyExistsException.class)) {
						form.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.AlreadyExists",
							getLocale()));
						LogWriter.writeOutOfSystemError(getSessionUserId(),
							e.getParentException(), getPage().getClass()
							, "Validate Error");
						return;
					} else if(e.getParentException().getClass().equals(ServiceNotActivatableException.class)) {
						Throwable th = e.getParentException();
						Pattern p = Pattern.compile("\\[.+?\\]\\s\\[.+?\\]");
						String message = th.getMessage();
						Matcher m = p.matcher(message);
						List<String> buff = new ArrayList<String>();
						while(m.find()){
							String mat = m.group();
							if( ! buff.contains(mat)){
								buff.add(mat);
								mat = mat.replaceAll("\\[", "\\\\[");
								mat = mat.replaceAll("\\]", "\\\\]");
								message = message.replaceAll(mat, "<br/>&nbsp;" + mat + "<br/>&nbsp;&nbsp;-&nbsp;");
							}
						}
						form.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.Activation",
							getLocale(), model.getServiceId(), message));
						LogWriter.writeOutOfSystemError(getSessionUserId(),
							e.getParentException(), getPage().getClass()
							, "Validate Error");
						return;
					}
					doErrorProcess(e);
				} catch(ResourceStreamNotFoundException e) {
					form.error(MessageManager
						.getMessage(
							"ProvidingServices.language.service.error.validate.file.notFound",
							getLocale()));
				} catch(IOException e) {
					doErrorProcess(new ServiceManagerException(e, getPage().getClass()));
//				} catch(SerialException e) {
//					doErrorProcess(new ServiceManagerException(e, getPage().getClass()));
//				} catch(SQLException e) {
//					doErrorProcess(new ServiceManagerException(e, getPage().getClass()));
//				} finally {
//					if(model.getInstance() != null) {
//						try {
//							model.getInstance().getBinaryStream().close();
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
				setResponsePage(getCancelPage(model, false));
			}
		});
	}

	protected Page getCancelPage(CompositeServiceModel model, boolean isEdit){
		return new RegistrationOfCompositeServiceProfilePage(model, isEdit);
	}

	protected void doResultProcess(CompositeServiceModel model)
	throws ServiceManagerException {
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
		setResponsePage(new RegistrationOfBpelCompositeServiceResultPage(model.getServiceId()));
	}

	private Form form;
	private CompositeServiceModel model;
	private FileUploaderPanel bpelUploader;
//	private MultipleFileUploaderPanel multipleUploader;
	private FileMultipleUploadPanel wsdlUploader;
}
