package jp.go.nict.langrid.management.web.view.component.label;

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class CalendarLabel extends DateLabel {
	/**
	 * 
	 * 
	 */
	public CalendarLabel(String componentId, String datePattern) {
		super(componentId, new PatternDateConverter(datePattern, false));
	}
	
	/**
	 * 
	 * 
	 */
	public CalendarLabel(String componentId, String datePattern, Calendar model) {
		super(componentId, new Model<Date>(model.getTime())
			, new PatternDateConverter(datePattern, false));
	}
}
