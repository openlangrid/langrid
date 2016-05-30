package jp.go.nict.langrid.management.web.view.component.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.converters.AbstractConverter;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class EscapeAtmarkConverter extends AbstractConverter {
	@Override
	protected Class<?> getTargetType() {
		return String.class;
	}

	public String convertToObject(String value, Locale locale) {
		return value.replace(" [at] ", "@");
	}
	
	@Override
	public String convertToString(Object value, Locale locale) {
		return super.convertToString(value, locale).replaceAll("@", " [at] ");
	}
	
	private static final long serialVersionUID = 1L;
}
