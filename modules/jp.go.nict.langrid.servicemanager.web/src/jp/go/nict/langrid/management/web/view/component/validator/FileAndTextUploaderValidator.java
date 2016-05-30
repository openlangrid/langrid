/*
 * $Id: FileAndTextUploaderValidator.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.text.URLField;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class FileAndTextUploaderValidator implements IValidator{
	/**
	 * 
	 * 
	 */
	public FileAndTextUploaderValidator(RadioGroup<Component> radioGroup){
		this.radioGroup = radioGroup;
	}

	public void validate(IValidatable validatable){
		if(radioGroup.getConvertedInput().getId().equals("url")){
			URLField urlField = (URLField)radioGroup.getConvertedInput();
			urlField.processInput();
			if(urlField.getModelObject() == null){
				urlField.error(MessageManager.getMessage(
						"ProvidingServices.language.service.error.validate.requier.Url", urlField
								.getLocale()));
				return;
			}
		}else if(radioGroup.getConvertedInput().getId().equals("file")){
			FileUploadField fileField = (FileUploadField)radioGroup.getConvertedInput();
			fileField.processInput();
			if(fileField.getFileUpload() == null){
				fileField.error(MessageManager.getMessage(
						"ProvidingServices.language.service.error.validate.require.Upload",
						fileField.getLocale()));
				return;
			}
			if(fileField.getFileUpload().getSize() > Long
					.parseLong(MessageUtil.LIMIT_UPLOAD_FILE_SIZE)){
				fileField.error(MessageManager.getMessage(
						"ProvidingServices.language.service.error.validate.upload.Size", fileField
								.getLocale()));
			}
		}
	}

	private RadioGroup<Component> radioGroup;
	private static final long serialVersionUID = 1L;
}
