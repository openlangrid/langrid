package jp.go.nict.langrid.client.soap.io.parameter;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarEncoder extends Encoder{
	public CalendarEncoder(int indent, String name, Object value){
		super(indent, name);
		this.value = (Calendar)value;
	}

	public void write(PrintWriter writer) throws IOException{
		writeIndent(writer);
		String s = format.get().format(value.getTime());
		writer.println(String.format(
				"<%s xsi:type=\"xsd:dateTime\">%s</%1$s>"
				, getName(), s.substring(0, s.length() - 2) + ":" + s.substring(s.length() - 2)
				));
	}

	private Calendar value;
	private ThreadLocal<DateFormat> format = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		}
	};
}
