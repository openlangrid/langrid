/*
 * $Id: DuplicatingWriter.java 182 2010-10-02 03:16:36Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
 * 
 */
package jp.go.nict.langrid.commons.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class DuplicatingWriter extends FilterWriter {
	/**
	 * 
	 * 
	 */
	public DuplicatingWriter(Writer source, Writer writer){
		super(source);
		this.writer = writer;
	}

	/**
	 * 
	 * 
	 */
	public Writer getWriter(){
		return writer;
	}

	@Override
	public void write(int b) throws IOException {
		writer.write(b);
		out.write(b);
	}

	@Override
	public void write(char[] b) throws IOException {
		writer.write(b);
		out.write(b);
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		writer.write(cbuf, off, len);
		out.write(cbuf, off, len);
	}

	@Override
	public void write(String str) throws IOException {
		writer.write(str);
		out.write(str);
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		writer.write(str, off, len);
		out.write(str, off, len);
	}

	private Writer writer;
}
