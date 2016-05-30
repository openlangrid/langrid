package jp.go.nict.langrid.management.web.view.component.panel;

import jp.go.nict.langrid.management.web.view.component.text.URLField;
import jp.go.nict.langrid.management.web.view.component.validator.FileAndTextUploaderValidator;
import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumSpaceValidator;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class FileAndTextFieldPanel extends FormComponentPanel<Component>{
	/**
	 * 
	 * 
	 */
	public FileAndTextFieldPanel(String componentId, Boolean isDefaultUrl){
		super(componentId, new Model<Component>());
		Radio<Component> remote;
		radioGroup = new RadioGroup<Component>("radioGroup", new Model<Component>());
		URLField urlField = new URLField("url", new Model<String>());
		urlField.add(new HalfSizeAlphaNumeralSiglumSpaceValidator());
		FileUploadField fileField = new FileUploadField("file", new Model<FileUpload>());
		Model<Component> urlModel = new Model<Component>(urlField);
		Model<Component> fileModel = new Model<Component>(fileField);
		Radio<Component> local = new Radio<Component>("local", fileModel);
		remote = new Radio<Component>("remote", urlModel);
		if(isDefaultUrl){
			radioGroup.setModel(urlModel);
		}else{
			radioGroup.setModel(fileModel);
		}
		radioGroup.add(remote);
		radioGroup.add(local);
		radioGroup.add(urlField);
		radioGroup.add(fileField);
		add(radioGroup);
		radioGroup.add(new FileAndTextUploaderValidator(radioGroup));
	}

	/**
	 * 
	 * 
	 */
	public FileUploadField getFileField(){
		if(radioGroup.getConvertedInput() instanceof FileUploadField){
			return (FileUploadField)radioGroup.getConvertedInput();
		}
		return null;
	}

	/**
	 * 
	 * 
	 */
	public URLField getURLField(){
		if(radioGroup.getConvertedInput() instanceof URLField){
			return (URLField)radioGroup.getConvertedInput();
		}
		return null;
	}
	
	/**
	 * 
	 * 
	 */
	public boolean hasSelectedUrl(){
		return radioGroup.getConvertedInput().getId().equals("url");
	}
	
	protected RadioGroup<Component> radioGroup;
	private static final long serialVersionUID = 1L;
}
