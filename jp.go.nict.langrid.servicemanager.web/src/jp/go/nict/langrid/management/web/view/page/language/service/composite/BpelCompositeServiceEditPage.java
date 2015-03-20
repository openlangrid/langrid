/*
 * $Id: BpelCompositeServiceEditPage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.CompositeServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.JarUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.form.MultipartForm;
import jp.go.nict.langrid.management.web.view.component.form.validator.FileUploadMultipleRelatedRequireValidator;
import jp.go.nict.langrid.management.web.view.component.panel.FileUploaderPanel;
import jp.go.nict.langrid.management.web.view.component.panel.MultipleFileUploaderPanel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.YourLanguageServicesPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel.FileMultipleUploadPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.IncreasingOptionPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceProfileFieldPanel;
import jp.go.nict.langrid.management.web.view.utility.RequestResponseUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
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
public class BpelCompositeServiceEditPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public BpelCompositeServiceEditPage(String serviceId){
		try {
			String gridId = getSelfGridId();
			sId = serviceId;
			CompositeServiceModel editModel = ServiceFactory.getInstance().getCompositeServiceService(gridId).get(serviceId);
			add(form = new MultipartForm("form"));
			form.add(new Label("serviceId", new Model<String>(editModel.getServiceId())));
			form.add(profilePanel = new ServiceProfileFieldPanel(gridId, "profilePanel", form, InstanceType.BPEL));
			profilePanel.setProfileInfo(editModel, true);
			for(IFormValidator fv : profilePanel.getValidatorList()) {
				form.add(fv);
			}

//			multipleUploader = new MultipleFileUploaderPanel("wsdlFiles", false, "wsdl", false);
//			form.add(multipleUploader);

			form.add(wsdlUploader = new FileMultipleUploadPanel(gridId, "wsdlFiles", true));

			ioPanel = new IncreasingOptionPanel("usingService");
			ioPanel.setIncreasingOptionInfo(editModel);
			form.add(ioPanel);
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}

		bpelUploader = new FileUploaderPanel("bpelFile", false, "bpel", false);
		bpelUploader.setRenderBodyOnly(true);
		form.add(bpelUploader);
//		form.add(new FileUploadMultipleRelatedRequireValidator(bpelUploader, multipleUploader
//				, MessageManager.getMessage("ProvidingServices.language.service.error.upload.file.Associate", getLocale())));
		form.add(new Link("dlFile") {
			@Override
			public void onClick() {
				File zip = null;
				try{
					CompositeServiceService service = ServiceFactory.getInstance().getCompositeServiceService(getSelfGridId());
					zip = service.getDefinisionFileListZip(sId);
					RequestResponseUtil.setRequestForFile(getRequestCycle(), zip, "");
				}catch(MalformedURLException e) {
					doErrorProcess(new ServiceManagerException(e, getPage().getClass()));
				}catch(IOException e) {
					doErrorProcess(new ServiceManagerException(e, getPage().getClass()));
				}catch(ServiceManagerException e) {
					doErrorProcess(e);
            }finally{
					zip = null;
				}
			}

		});

		form.add(new Button("submit"){
			@Override
			public void onSubmit(){
				try{
				   CompositeServiceService service = ServiceFactory.getInstance().getCompositeServiceService(getSelfGridId());
				   CompositeServiceModel editModel = service.get(sId);
					profilePanel.doSubmitProcess(editModel);
					ioPanel.doSubmitProcess(editModel);

//					Map<String, byte[]> map = multipleUploader.getFileMap();
//					map.putAll(bpelUploader.getFileMap());
//					if(map.size() != 0){
//						byte[] lbr = JarUtil.makeInstance(map);
//						editModel.setInstance(lbr);
//						editModel.setInstanceSize(lbr.length);
//					}

					byte[] lbr = wsdlUploader.getJarFileInstance();
					Map<String, byte[]> map = bpelUploader.getFileMap();
					if( ! map.isEmpty() && ! wsdlUploader.isEmpty()) {
						for(String name : map.keySet()) {
							lbr = JarUtil.addEntry(name, map.get(name), lbr);
						}
						editModel.setInstance(lbr);
						editModel.setInstanceSize(lbr.length);
					}
					service.edit(editModel);

					ServiceFactory.getInstance().getAccessRightControlService(editModel.getGridId()).setDefault(
					editModel.getServiceId(), editModel.getGridId(), ! editModel.isMembersOnly());
					LogWriter.writeInfo(getSessionUserId()
							, "\"" + editModel.getServiceId()
							+ "\" of composite service has been edited.", getPage().getClass());
					doResultProcess(editModel.getServiceId());
				}catch(ServiceManagerException e){
					doErrorProcess(e);
				}catch(ResourceStreamNotFoundException e){
				   e.printStackTrace();
					form.error(MessageManager.getMessage(
							"ProvidingServices.errer.validate.file.notFound", getLocale()));
				}catch(IOException e){
				   e.printStackTrace();
					form.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.upload.IOError", getLocale()));
//				} catch(SerialException e) {
//					doErrorProcess(new ServiceManagerException(e));
//				} catch(SQLException e) {
//					doErrorProcess(new ServiceManagerException(e));
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
		setResponsePage(new BpelCompositeServiceEditResultPage(serviceId));
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
	private FileUploaderPanel bpelUploader;
//	private MultipleFileUploaderPanel multipleUploader;
	private FileMultipleUploadPanel wsdlUploader;
	private IncreasingOptionPanel ioPanel;
}
