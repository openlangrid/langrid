/*
 * $Id: FileUploaderValidator.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.component.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.utility.FileUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.UrlResourceStream;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class FileUploaderValidator implements IValidator{
	/**
	 * 
	 * 
	 */
	public FileUploaderValidator(RadioGroup<Component> radioGroup, String extensionName, boolean isRequired){
		this.radioGroup = radioGroup;
		this.extensionName = extensionName;
		this.isRequired = isRequired;
	}

	public void validate(IValidatable validatable){
		try{
			String fileName;
			if(radioGroup.getConvertedInput().getId().equals("url")){
				TextField<String> urlField = (TextField<String>)radioGroup.getConvertedInput();
				urlField.processInput();
				if(!isRequired && (urlField.getModelObject() == null || urlField.getModelObject().equals(""))){
					return;
				}
				if(urlField.getModelObject() == null){
					urlField.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.upload.url.Required", urlField
									.getLocale(), extensionName.toUpperCase()));
					return;
				}
				URL url;
				try{
					url = new URL(urlField.getModelObject());
					if(url.getPath().endsWith("/")){
						urlField.error(MessageManager.getMessage(
								"ProvidingServices.language.service.error.upload.url.invalid.End",
								urlField.getLocale(), extensionName.toUpperCase()));
						return;
					}
				}catch(MalformedURLException e){
					urlField.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.upload.url.Invalid", urlField
									.getLocale(), extensionName.toUpperCase()));
					return;
				}
				fileName = FileUtil.getFileNameWithoutPathWithWsdl(urlField.getInput());
				if(fileName.equals("")){
					urlField.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.upload.file.remote.NotFound",
							urlField.getLocale(), extensionName.toUpperCase()));
					return;
				}else{
					UrlResourceStream urs;
					int totalSize = 0;
					InputStream is = null;
					urs = new UrlResourceStream(new URL(urlField.getInput()));
					is = urs.getInputStream();
					try {
						byte[] buffer = StreamUtil.readAsBytes(urs.getInputStream());
						totalSize = buffer == null ? 0 : buffer.length;
					} catch(IOException e){
						urlField.equals(MessageManager.getMessage(
							"ProvidingServices.language.service.error.upload.IOError", radioGroup
							.getLocale()));
					} finally {
						is.close();
					}
					if(totalSize > Integer.parseInt(MessageUtil.LIMIT_WSDL_FILE_SIZE)){
						urlField.error(MessageManager.getMessage(
								"ProvidingServices.language.service.error.upload.file.remote.Size",
								urlField.getLocale(), extensionName.toUpperCase()));
					}
				}
			}else if(radioGroup.getConvertedInput().getId().equals("file")){
				FileUploadField fileField = (FileUploadField)radioGroup
						.getConvertedInput();
				fileField.processInput();
				if(!isRequired && (fileField.getModelObject() == null || fileField.getModelObject().equals(""))){
					return;
				}
				if(isRequired && fileField.getFileUpload() == null){
					fileField.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.upload.file.Required",
							fileField.getLocale(), extensionName.toUpperCase()));
					return;
				}
				fileName = fileField.getFileUpload().getClientFileName();
				if(!fileName.equals("")
						&& !FileUtil.checkFileExtension(fileName, extensionName)){
					fileField.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.upload.file.Extension",
							fileField.getLocale(), extensionName.toUpperCase(), fileName,
							extensionName));
				}else{
					if(fileField.getFileUpload().getSize() > Long
							.parseLong(MessageUtil.LIMIT_WSDL_FILE_SIZE)){
						fileField.error(MessageManager.getMessage(
								"ProvidingServices.language.service.error.upload.file.Size",
								fileField.getLocale(), extensionName.toUpperCase()));
					}
				}
			}
		}catch(MalformedURLException e){
			radioGroup.error(MessageManager.getMessage(
					"ProvidingServices.language.service.error.upload.url.Invalid", radioGroup
							.getLocale(), extensionName.toUpperCase()));
			LogWriter.writeError("Validator", e, getClass());
		}catch(IOException e){
			radioGroup.error(MessageManager.getMessage(
					"ProvidingServices.language.service.error.upload.IOError", radioGroup
							.getLocale()));
			LogWriter.writeError("Validator", e, getClass());
		}catch(ResourceStreamNotFoundException e){
			radioGroup.error(MessageManager.getMessage(
					"ProvidingServices.language.service.error.upload.file.remote.NotFound",
					radioGroup.getLocale(), extensionName.toUpperCase()));
		}
	}

	private boolean isRequired;
	private String extensionName;
	private RadioGroup<Component> radioGroup;
	private static final long serialVersionUID = 1L;
}
