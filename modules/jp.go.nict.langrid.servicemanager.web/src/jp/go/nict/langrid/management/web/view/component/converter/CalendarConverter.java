package jp.go.nict.langrid.management.web.view.component.converter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.ClientInfo;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class CalendarConverter implements IConverter {
	public CalendarConverter(boolean applyTimeZoneDifference, String pattern) {
		this.applyTimeZoneDifference = applyTimeZoneDifference;
		datePattern = pattern;
	}

	public Calendar convertToObject(String value, Locale locale) {
		if(Strings.isEmpty(value)) {
			return null;
		}
		DateTimeFormatter format = getFormat();
		if(format == null) {
			throw new IllegalStateException("format must be not null");
		}
		if(applyTimeZoneDifference) {
			TimeZone zone = getClientTimeZone();
			// instantiate now/ current time
			MutableDateTime dt = new MutableDateTime(new DateMidnight());
			if(zone != null) {
				// set time zone for client
				format = format.withZone(DateTimeZone.forTimeZone(zone));
				dt.setZone(DateTimeZone.forTimeZone(zone));
			}
			try {
				// parse date retaining the time of the submission
				int result = format.parseInto(dt, value, 0);
				if(result < 0) {
					throw new ConversionException(new ParseException(
							"unable to parse date " + value, ~result));
				}
			} catch(RuntimeException e) {
				throw new ConversionException(e);
			}
			// apply the server time zone to the parsed value
			dt.setZone(getTimeZone());
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt.toDate());
			return cal;
		} else {
			try {
				DateTime date = format.parseDateTime(value);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date.toDate());
				return cal;
			} catch(RuntimeException e) {
				throw new ConversionException(e);
			}
		}
	}

	public String convertToString(Object value, Locale locale) {
		DateTime dt = new DateTime(((Calendar)value).getTime(), getTimeZone());
		DateTimeFormatter format = getFormat();
		if(applyTimeZoneDifference) {
			TimeZone zone = getClientTimeZone();
			if(zone != null) {
				// apply time zone to formatter
				format = format.withZone(DateTimeZone.forTimeZone(zone));
			}
		}
		return format.print(dt);
	}

	public final boolean getApplyTimeZoneDifference() {
		return applyTimeZoneDifference;
	}

	public final Component getComponent() {
		return component;
	}

	public final String getDatePattern() {
		return DateTimeFormat.patternForStyle(datePattern, getLocale());
	}

	public final void setComponent(Component component) {
		this.component = component;
	}

	protected TimeZone getClientTimeZone() {
		ClientInfo info = Session.get().getClientInfo();
		if(info instanceof WebClientInfo) {
			return ((WebClientInfo)info).getProperties().getTimeZone();
		}
		return null;
	}

	protected DateTimeFormatter getFormat() {
		return DateTimeFormat.forPattern(datePattern);
	}

	protected Locale getLocale() {
		Component c = getComponent();
		return (c != null) ? c.getLocale() : Session.get().getLocale();
	}

	protected DateTimeZone getTimeZone() {
		return DateTimeZone.getDefault();
	}

	private final boolean applyTimeZoneDifference;
	private Component component = null;
	private final String datePattern;
	private static final long serialVersionUID = 1L;
}
