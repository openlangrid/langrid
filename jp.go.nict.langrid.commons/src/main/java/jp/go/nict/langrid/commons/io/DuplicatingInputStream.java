/*
 * $Id: DuplicatingInputStream.java 1205 2014-04-17 02:14:33Z t-nakaguchi $
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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class DuplicatingInputStream extends FilterInputStream{
	/**
	 * 
	 * 
	 */
	public DuplicatingInputStream(InputStream source, OutputStream outputStream){
		super(source);
		this.outputStream = outputStream;
	}

	/**
	 * 
	 * 
	 */
	public OutputStream getOutputStream(){
		return outputStream;
	}

	@Override
	public void close() throws IOException {
		super.close();
		outputStream.close();
	}

	@Override
	public int read() throws IOException {
		int r = in.read();
		if(r != -1) outputStream.write(r);
		return r;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int size = in.read(b);
		if(size != -1) outputStream.write(b, 0, size);
		return size;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int size = in.read(b, off, len);
		if(size != -1) outputStream.write(b, off, size);
		return size;
	}

	private OutputStream outputStream;
}
