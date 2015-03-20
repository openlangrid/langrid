package jp.go.nict.langrid.management.web.view.component.form.validator;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.panel.FileUploaderPanel;
import jp.go.nict.langrid.management.web.view.component.panel.MultipleFileUploaderPanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;

public class FileUploadMultipleRelatedRequireValidator implements IFormValidator {
	public FileUploadMultipleRelatedRequireValidator(
			FileUploaderPanel triger, MultipleFileUploaderPanel observated, String validatedFileName)
	{
		this.triger = triger;
		this.observated = observated;
		this.validatedFileName = validatedFileName;
	}

	public FormComponent<?>[] getDependentFormComponents() {
		return null;
	}

	public void validate(Form<?> form) {
		boolean hasInput = false;
		for(FileUploaderPanel panel : observated.getValidateComponent()) {
			if(panel.isInput()) {
				hasInput = true;
			}
		}
		if((triger.isInput() && !hasInput) || (!triger.isInput() && hasInput)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("label", validatedFileName);
			triger.getParent().error(MessageManager.getMessage("message.error.multi.Required", map));
		}
	}

	private String validatedFileName;
	private FileUploaderPanel triger;
	private MultipleFileUploaderPanel observated;

	private static final long serialVersionUID = 1L;
}
