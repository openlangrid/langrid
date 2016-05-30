package jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.validator;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.utility.resource.MessageManager;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RelatedAtoBRequiredFormValidator<T> implements IFormValidator{
	/**
	 * 
	 * 
	 */
	public RelatedAtoBRequiredFormValidator(FormComponent<T> a, FormComponent<T> b){
		this.a = a;
		this.b = b;
	}
	
	@SuppressWarnings("unchecked")
   public FormComponent<T>[] getDependentFormComponents(){
		return new FormComponent[]{a, b};
	}
	
	public void setMessageValue(String key, String value){
		messages.put(key, value);
	}
	
	public void validate(Form<?> form){
		if(a.getInput() != null && !a.getInput().equals("") && (b.getInput() == null || b.getInput().equals(""))){
			String value = MessageManager.getMessage(b.getId(), b.getLocale());
			if(value != null){
				setMessageValue("B", value);
			}
			b.error(MessageManager.getMessage(
					"Common.error.require.Related", messages));
		}
	}

	private FormComponent<T> a;
	private FormComponent<T> b;
	private Map<String, String> messages = new HashMap<String, String>();
	
	private static final long serialVersionUID = 1L;
}