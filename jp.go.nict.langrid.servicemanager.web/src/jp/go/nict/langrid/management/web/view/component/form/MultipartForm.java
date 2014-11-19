package jp.go.nict.langrid.management.web.view.component.form;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.Form;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: Masaaki Kamiya $
 * @version $Revision: 12208 $
 */
public class MultipartForm<T extends Serializable> extends Form<T> {
	public MultipartForm(String formName) {
		super(formName);
		setMultiPart(true);
	}
	
	private static final long serialVersionUID = 1L;
}
