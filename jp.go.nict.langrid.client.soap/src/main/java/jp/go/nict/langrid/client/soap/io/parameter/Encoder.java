package jp.go.nict.langrid.client.soap.io.parameter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import jp.go.nict.langrid.commons.lang.StringUtil;

public abstract class Encoder {
	public Encoder(int indent, String name){
		this.indent = indent;
		this.name = name;
	}

	public int getIndent() {
		return indent;
	}

	public String getName() {
		return name;
	}

	protected void writeIndent(PrintWriter writer) throws IOException{
		writer.print(StringUtil.repeatedString("  ", indent));
	}

	protected void writeClosingTag(PrintWriter writer) throws IOException{
		writeIndent(writer);
		writer.println("</" + name + ">");
	}

	public abstract void write(PrintWriter writer) throws IOException;
	public String getTag() throws IOException{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		write(pw);
		pw.flush();
		return sw.toString();
	}

	private int indent;
	private String name;
}
