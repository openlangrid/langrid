package jp.go.nict.langrid.management.web.view.page.language.service.component.choice.renderer;

import jp.go.nict.langrid.management.web.view.model.ServiceSortValue;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class SortChoiceRenderer implements IChoiceRenderer<ServiceSortValue>{
	public Object getDisplayValue(ServiceSortValue object){
		if(object == null){
			return "";
		}
		return object.getDisplayValue();
	}

	public String getIdValue(ServiceSortValue object, int index){
		if(object == null){
			return "";
		}
		return object.getColumnName() + object.getDirection().name();
	}

	private static final long serialVersionUID = 1L;
}