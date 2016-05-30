package jp.go.nict.langrid.management.web.view.component.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class MultiRequiredValidator extends AbstractFormValidator {
	public MultiRequiredValidator(FormComponent... components) {
		list = Arrays.asList(components);
	}

	public FormComponent<?>[] getDependentFormComponents() {
		return list.toArray(new FormComponent[]{});
	}

	public void validate(Form<?> form) {
		for(FormComponent fc : list) {
			if(fc.getInput() != null && !fc.getInput().equals("")){
				return;
			}
		}
		// TODO エラーメッセージ作成
		error(list.get(0));
	}

	private List<FormComponent> list = new ArrayList<FormComponent>();
	private static final long serialVersionUID = 1L;
}
