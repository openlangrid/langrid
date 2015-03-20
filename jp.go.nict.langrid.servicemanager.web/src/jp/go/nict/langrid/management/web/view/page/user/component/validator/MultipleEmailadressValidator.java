package jp.go.nict.langrid.management.web.view.page.user.component.validator;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.EmailAddressValidator;

public class MultipleEmailadressValidator extends EmailAddressValidator {
	@Override
	protected void onValidate(IValidatable<String> validatable) {
		String[] splited = validatable.getValue().split(",");
		for(String adress : splited) {
			if(!super.getPattern().matcher(adress.trim()).matches()) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("input", adress);
				error(validatable, "EmailAddressValidator", param);
			}
		}
	}
}
