package jp.go.nict.langrid.management.web.view.page.user.component.validator;

import jp.go.nict.langrid.management.web.utility.resource.MessageManager;

import org.apache.wicket.Component;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * 
 * @author Trang
 * 
 */
public class TermofUserCheckedValidator implements IValidator<Boolean> {

	private Component component;

	public TermofUserCheckedValidator(Component component) {
		this.component = component;
	}

	@Override
	public void validate(IValidatable<Boolean> validatable) {
		// TODO Auto-generated method stub
		Boolean checkterm = validatable.getValue();
		if (!checkterm) {
			component
					.error(MessageManager
							.getMessage(
									"LanguageGridOperator.error.user.form.register.notcheckterm",
									component.getLocale()));
		}
	}

}
