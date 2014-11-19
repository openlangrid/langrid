/*
 * $Id: ByteDetectingInputStream.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
 */
package jp.go.nict.langrid.commons.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class ByteDetectingInputStream extends FilterInputStream{
	/**
	 * 
	 * 
	 */
	public ByteDetectingInputStream(InputStream in, int detectByte){
		super(in);
		this.detectByte = detectByte;
	}

	/**
	 * 
	 * 
	 */
	public Integer[] getDetectedPositions(){
		return positions.toArray(new Integer[]{});
	}

	/**
	 * 
	 * 
	 */
	public void clearPositions(){
		positions.clear();
		position = 0;
	}

	@Override
	public int read() throws IOException {
		int r = in.read();
		if(r != -1){
			if(r == detectByte){
				positions.add(position);
			}
			position++;
		}
		return r;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int r = in.read(b);
		if(r != -1){
			for(int i = 0; i < r; i++){
				if(b[i] == detectByte){
					positions.add(position);
				}
				position++;
			}
		}
		return r;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int r = in.read(b, off, len);
		if(r != -1){
			for(int i = 0; i < r; i++){
				if(b[i + off] == detectByte){
					positions.add(position);
				}
				position++;
			}
		}
		return r;
	}

	private int detectByte;
	private int position;
	private List<Integer> positions = new ArrayList<Integer>();
}
