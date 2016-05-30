/*
 * $Id: BPELUploaderValidator.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.composite.component.form.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.utility.FileUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.panel.FileUploaderPanel;
import jp.go.nict.langrid.management.web.view.component.text.URLField;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.UrlResourceStream;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class BPELUploaderValidator implements IFormValidator{
	/**
	 * 
	 * 
	 */
	public BPELUploaderValidator(Component validatable){
		formComponent = validatable;
	}

	public FormComponent<?>[] getDependentFormComponents(){
		return null;
	}

	public void validate(Form<?> form){
		FileUploaderPanel panel = (FileUploaderPanel)formComponent;
		try{
			checkWSDL(panel);
		}catch(ResourceStreamNotFoundException e){
			panel.error(MessageManager.getMessage(
					"ProvidingServices.language.service.error.BPELFileNotFound", panel.getLocale()));
			LogWriter.writeError("Validator", e, getClass());
		}catch(IOException e){
			panel.error(MessageManager.getMessage(
					"ProvidingServices.language.service.error.SystemError", panel.getLocale()));
			LogWriter.writeError("Validator", e, getClass());
		}
	}

	/**
	 * 
	 * 
	 */
	private boolean checkExtension(String checkedFile, String extension){
		return FileUtil.checkFileExtension(checkedFile, extension);
	}

	/**
	 * 
	 * 
	 */
	private void checkWSDL(FileUploaderPanel panel)
	throws ResourceStreamNotFoundException, IOException{
		String fileName = "";
		FormComponent component = panel.getSelectedFormComponent();
		if(component == null){
			panel.error(MessageManager.getMessage(
					"ProvidingServices.language.service.error.BPEL.Selected", panel.getLocale()));
			return;
		}
		String inputString = component.getInput();
		if(inputString == null){
			panel.error(MessageManager.getMessage(
					"ProvidingServices.language.service.error.BPEL.Required", panel.getLocale()));
			return;
		}
		if(panel.getSelectedFormComponentID().equals("URLField")){
			URLField url = (URLField)component;
			fileName = FileUtil.getFileNameWithoutPathWithWsdl(url.getInput());
			if(fileName.equals("")){
				url.error(MessageManager.getMessage(
						"ProvidingServices.language.service.error.BPELExtension", url.getLocale()));
			}else{
				UrlResourceStream urs;
				int totalSize = 0;
				InputStream is = null;
				urs = new UrlResourceStream(new URL(url.getInput()));
				is = urs.getInputStream();
				byte[] buffer = new byte[1024];
				while(true){
					int size = is.read(buffer);
					if(size == -1){
						break;
					}
					totalSize += size;
				}
				is.close();
				if(totalSize > Integer.parseInt(MessageUtil.LIMIT_WSDL_FILE_SIZE)){
					url.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.RemoteBPELLFileSize", url
									.getLocale()));
				}
			}
		}else if(panel.getSelectedFormComponentID().equals("FileUploadField")){
			FileUploadField file = (FileUploadField)component;
			fileName = file.getFileUpload().getClientFileName();
			if(!fileName.equals("") && !checkExtension(fileName, "wsdl")){
				file.error(MessageManager.getMessage(
						"ProvidingServices.language.service.error.BPELExtension", file.getLocale()));
			}else{
				if(file.getFileUpload().getSize() > Long
						.parseLong(MessageUtil.LIMIT_WSDL_FILE_SIZE)){
					file.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.BPELFileSize", file
									.getLocale()));
				}
			}
		}
	}

	private Component formComponent;
	private static final long serialVersionUID = 1L;
}
