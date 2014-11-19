/*
 * $Id: DuplicatingReader.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class DuplicatingReader extends FilterReader{
	/**
	 * 
	 * 
	 */
	public DuplicatingReader(Reader source, Writer writer){
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
	public int read() throws IOException {
		int r = in.read();
		if(r != -1) writer.write(r);
		return r;
	}

	@Override
	public int read(char[] cbuf) throws IOException {
		int r = in.read(cbuf);
		if(r != -1) writer.write(cbuf, 0, r);
		return r;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int r = in.read(cbuf, off, len);
		if(r != -1) writer.write(cbuf, off, r);
		return r;
	}

	@Override
	public int read(CharBuffer target) throws IOException {
		int r = in.read(target);
		if(r != -1) writer.write(target.array(), target.arrayOffset(), r);
		return r;
	}

	private Writer writer;
}
