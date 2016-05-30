/*
 * $Id: DuplicatingOutputStream.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class DuplicatingOutputStream extends FilterOutputStream{
	/**
	 * 
	 * 
	 */
	public DuplicatingOutputStream(OutputStream source, OutputStream outputStream){
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
	public void write(byte[] b) throws IOException {
		outputStream.write(b);
		out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		outputStream.write(b, off, len);
		out.write(b, off, len);
	}

	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
		out.write(b);
	}

	private OutputStream outputStream;
}
