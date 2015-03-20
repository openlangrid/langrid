package jp.go.nict.langrid.client.soap.io.parameter;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.codec.binary.Base64;

public class BytesEncoder extends Encoder{
	public BytesEncoder(int indent, String name, Object value){
		super(indent, name);
		this.type = "base64Binary";
		this.value = (byte[])value;
	}
	public void write(PrintWriter writer) throws IOException{
		writeIndent(writer);
		writer.println(String.format(
				"<%s xsi:type=\"xsd:%s\">%s</%1$s>"
				, getName(), type, new String(Base64.encodeBase64(value), "ISO8859-1")
				));
	}
	private String type;
	private byte[] value;
}
