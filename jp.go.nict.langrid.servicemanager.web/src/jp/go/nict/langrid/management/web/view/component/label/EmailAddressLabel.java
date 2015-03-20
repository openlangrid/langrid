package jp.go.nict.langrid.management.web.view.component.label;

import jp.go.nict.langrid.management.web.view.component.converter.EscapeAtmarkConverter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class EmailAddressLabel extends Label {
	/**
	 * 
	 * 
	 */
	public EmailAddressLabel(String componentId) {
		super(componentId);
	}
	
	/**
	 * 
	 * 
	 */
	public EmailAddressLabel(String componentId, IModel<String> model){
		super(componentId, model);
	}
	
	@Override
	public IConverter getConverter(Class<?> type) {
		return new EscapeAtmarkConverter();
	}
	
	private static final long serialVersionUID = -7727487390204664340L;
}
