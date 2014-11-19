package jp.go.nict.langrid.management.web.view.component.panel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.management.web.utility.FileUtil;
import jp.go.nict.langrid.management.web.view.component.validator.FileUploaderValidator;
import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumSpaceValidator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.UrlResourceStream;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class FileUploaderPanel extends FormComponentPanel<File>{
	/**
	 * 
	 * 
	 */
	public FileUploaderPanel(String componentId, Boolean isDefaultRemote, String fileExtension, boolean isRequired){
		super(componentId, new Model<File>());
		radioGroup = new RadioGroup<Component>("radioGroup", new Model<Component>());
		urlField = new TextField<String>("url", new Model<String>());
		urlField.setOutputMarkupId(true);
		urlField.add(new HalfSizeAlphaNumeralSiglumSpaceValidator());
		fileField = new FileUploadField("file", new Model<FileUpload>());
		fileField.setOutputMarkupId(true);
		Model<Component> urlModel = new Model<Component>(urlField);
		Model<Component> fileModel = new Model<Component>(fileField);
		Radio<Component> local = new Radio<Component>("local", fileModel);
		remote = new Radio<Component>("remote", urlModel);
		if(isDefaultRemote){
			radioGroup.setModel(urlModel);
			fileField.setEnabled(false);
		}else{
			radioGroup.setModel(fileModel);
			urlField.setEnabled(false);
		}
		radioGroup.add(remote);
		radioGroup.add(local);
		radioGroup.add(urlField);
		radioGroup.add(fileField);
		radioGroup.setOutputMarkupId(true);
		remote.add(new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target){
				urlField.setEnabled(true);
				fileField.setEnabled(false);
//				radioGroup.setModelObject(radioGroup.getConvertedInput());
				target.addComponent(urlField);
				target.addComponent(fileField);
			}
		});
		local.add(new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target){
				urlField.setEnabled(false);
				fileField.setEnabled(true);
//				radioGroup.setModelObject(radioGroup.getConvertedInput());
				target.addComponent(urlField);
				target.addComponent(fileField);
			}
		});
		add(radioGroup);
		radioGroup.add(new FileUploaderValidator(radioGroup, fileExtension, isRequired));
	}
	
	/**
	 * 
	 * 
	 */
	public Map<String, byte[]> getFileMap() throws IOException, ResourceStreamNotFoundException {
		Map<String, byte[]> map = new HashMap<String, byte[]>();
		if(radioGroup.getModelObject().equals(urlField)){
			URL url = new URL(urlField.getModelObject());
			UrlResourceStream urlStream = new UrlResourceStream(url);
			map.put(FileUtil.getFileNameWithoutPathWithWsdl(url.getPath())
					, StreamUtil.readAsBytes(urlStream.getInputStream()));
			return map;
		}else if(radioGroup.getModelObject().equals(fileField)){
			FileUpload fu = fileField.getFileUpload();
			if(fu == null){
				return map;
			}
			map.put(fu.getClientFileName(), StreamUtil.readAsBytes(fu.getInputStream()));
			return map;
		}
		return new HashMap<String, byte[]>();
	}
	
	/**
	 * 
	 * 
	 */
//	public Map<String, InputStream> getInputStreamMap() 
//	throws ResourceStreamNotFoundException, IOException
//	{
//		Map<String, InputStream> map = new HashMap<String, InputStream>();
//		if(radioGroup.getModelObject().equals(urlField)){
//			URL url = new URL(urlField.getModelObject());
//			UrlResourceStream urlStream = new UrlResourceStream(url);
//			map.put(FileUtil.getFileNameWithoutPathWithWsdl(url.getPath())
//					, urlStream.getInputStream());
//			return map;
//		}else if(radioGroup.getModelObject().equals(fileField)){
//			FileUpload fu = fileField.getFileUpload();
//			if(fu == null){
//				return map;
//			}
//			map.put(fu.getClientFileName(), fu.getInputStream());
//			return map;
//		}
//		return new HashMap<String, InputStream>();
//	}
	
	/**
	 * 
	 * 
	 */
	public FormComponent getSelectedFormComponent(){
		if(radioGroup.getConvertedInput().getId().equals("url")){
			return (FormComponent)radioGroup.getConvertedInput();
		}else if(radioGroup.getConvertedInput().getId().equals("file")){
			return (FormComponent)radioGroup.getConvertedInput();
		}
		return null;
	}
	
	/**
	 * 
	 * 
	 */
	public String getSelectedFormComponentID(){
		if(radioGroup.getConvertedInput().getId().equals("url")){
			return "URLField";
		}else if(radioGroup.getConvertedInput().getId().equals("file")){
			return "FileUploadField";
		}
		return "";
	}
	
	public boolean isInput(){
		if(radioGroup.getConvertedInput().getId().equals("url")){
			TextField<String> urlField = (TextField<String>)radioGroup.getConvertedInput();
			urlField.processInput();
			if(urlField.getModelObject() != null){
				return true;
			}
		}else if(radioGroup.getConvertedInput().getId().equals("file")){
			FileUploadField fileField = (FileUploadField)radioGroup
					.getConvertedInput();
			fileField.processInput();
			if(fileField.getFileUpload() != null){
				return true;
			}
		}
		return false;
	}

	protected RadioGroup<Component> radioGroup;
	private Radio<Component> remote;
	private TextField<String> urlField;
	private FileUploadField fileField;
	
	private static final long serialVersionUID = 1L;
}
