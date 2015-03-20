/*
 * $Id: DuplicatingInputStream.java 1205 2014-04-17 02:14:33Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 Language Grid Project.
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

public class LimitedFilteredOutputStream extends FilterOutputStream{
	public LimitedFilteredOutputStream(OutputStream original, int limitSize) {
		super(original);
		this.limitSize = limitSize;
	}

	public int getLimitSize() {
		return limitSize;
	}

	public void setLimitSize(int limitSize) {
		this.limitSize = limitSize;
	}

	@Override
	public void write(int b) throws IOException {
		if(limitSize > 0){
			super.write(b);
			limitSize--;
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		int s = Math.min(len, limitSize);
		if(s > 0){
			super.write(b, off, s);
			limitSize -= s;
		}
	}

	private int limitSize;
}
