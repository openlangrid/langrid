/*
 * $Id: FileUtil.java 704 2013-03-18 23:28:18Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.commons.io;

import java.io.IOException;

import jp.go.nict.langrid.commons.lang.CharSequenceUtil;

/**
 * @author Takao Nakaguchi
 */
public class IndentedWriter {
	public IndentedWriter(Appendable appendable) {
		this.appendable = appendable;
	}

	public IndentedWriter(Appendable appendable, int indent) {
		this.appendable = appendable;
		this.indent = indent;
	}

	public int getIndent() {
		return indent;
	}

	public IndentedWriter println(String format, Object... args) throws IOException{
		tabs(indent);
		appendable.append(String.format(format, args)).append('\n');
		return this;
	}

	public IndentedWriter indent(){
		indent++;
		return this;
	}

	public IndentedWriter unindent(){
		indent--;
		return this;
	}

	public IndentedWriter write(String format, Object... args) throws IOException{
		println(format, args);
		return this;
	}

	public IndentedWriter indent(String format, Object... args) throws IOException{
		tabs(indent++);
		appendable.append(String.format(format, args)).append('\n');
		return this;
	}

	public IndentedWriter unindent(String format, Object... args) throws IOException{
		tabs(--indent);
		appendable.append(String.format(format, args)).append('\n');
		return this;
	}

	private void tabs(int n) throws IOException{
		appendable.append(CharSequenceUtil.repeat('\t', n));
	}

	private Appendable appendable;
	private int indent = 0;
}
