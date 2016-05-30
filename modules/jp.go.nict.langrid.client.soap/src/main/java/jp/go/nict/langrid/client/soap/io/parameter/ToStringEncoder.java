package jp.go.nict.langrid.client.soap.io.parameter;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringEscapeUtils;

public class ToStringEncoder extends Encoder{
	public ToStringEncoder(int indent, String name, Class<?> type, Object value){
		super(indent, name);
		this.type = EncoderUtil.typeToXsdType(type);
		this.value = value == null ? null : EncoderUtil.valueToString(value);
	}
	public void write(PrintWriter writer) throws IOException{
		if(value == null) return;
		writeIndent(writer);
		writer.println(String.format(
				"<%s xsi:type=\"xsd:%s\">%s</%1$s>"
				, getName(), type, StringEscapeUtils.escapeXml(value.toString())
				));
	}
	private String type;
	private String value;
}
