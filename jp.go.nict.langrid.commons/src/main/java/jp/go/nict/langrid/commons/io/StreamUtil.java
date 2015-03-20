/*
 * $Id: StreamUtil.java 1313 2014-11-26 06:05:39Z t-nakaguchi $
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockPE;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.commons.util.function.Filters;
import jp.go.nict.langrid.commons.util.function.Predicate;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1313 $
 */
public final class StreamUtil {
	public static Collection<String> readLines(
			InputStream is, CharsetDecoder decoder)
	throws IOException{
		return readLines(new InputStreamReader(is, decoder), Filters.<String>pass());
	}

	public static Collection<String> readLines(
			InputStream is, String encoding)
	throws IOException{
		return readLines(new InputStreamReader(is, encoding), Filters.<String>pass());
	}

	public static Collection<String> readLines(Reader reader, Predicate<String> test)
	throws IOException{
		BufferedReader br = new BufferedReader(reader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while((line = br.readLine()) != null){
			if(test.test(line)) lines.add(line);
		}
		return lines;
	}
	
	/**
	 * 
	 * 
	 */
	public static String readAsString(
		InputStream is, CharsetDecoder decoder) throws IOException {
		InputStreamReader reader = new InputStreamReader(
				is, decoder);
		return readAsString(reader);
	}

	/**
	 * 
	 * 
	 */
	public static String readAsString(InputStream is, String encodingName)
	throws IOException {
		InputStreamReader reader = new InputStreamReader(
				is, encodingName);
		return readAsString(reader);
	}

	/**
	 * 
	 * 
	 */
	public static String readAsString(Reader reader)
	throws IOException {
		CharArrayWriter writer = new CharArrayWriter();
		char[] buffer = new char[1024];
		while(true){
			int size = reader.read(buffer);
			if (size == -1)
				break;
			writer.write(buffer, 0, size);
		}
		return writer.toString();
	}

	/**
	 * 
	 * 
	 */
	public static byte[] readAsBytes(InputStream is)
		throws IOException
	{
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		while (true) {
			int size = is.read(buffer);
			if (size == -1){
				break;
			}
			o.write(buffer, 0, size);
		}
		return o.toByteArray();
	}

	/**
	 * 
	 * 
	 */
	public static byte[] readAsBytes(InputStream is, int count)
		throws IOException
	{
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		while(count > 0) {
			int size = is.read(buffer, 0, Math.min(1024, count));
			if(size == -1) break;
			o.write(buffer, 0, size);
			count -= size;
		}
		return o.toByteArray();
	}

	public static int readFully(InputStream is, byte[] buff)
	throws IOException{
		int rest = buff.length;
		int capacity = buff.length;
		while(rest > 0) {
			int size = is.read(buff, capacity - rest, rest);
			if(size == -1) break;
			rest -= size;
		}
		return capacity - rest;
	}

	/**
	 * 
	 * 
	 */
	public static void writeString(OutputStream os, String string, CharsetEncoder encoder)
	throws IOException{
		OutputStreamWriter writer = new OutputStreamWriter(os, encoder);
		writer.write(string);
		writer.flush();
	}

	public static void writeString(OutputStream os, String string, String encodingName)
	throws IOException{
		OutputStreamWriter writer = new OutputStreamWriter(
			os, encodingName);
		writer.write(string);
		writer.flush();
	}

	/**
	 * 
	 * 
	 */
	public static Reader createUTF8Reader(InputStream is){
		return new InputStreamReader(is, CharsetUtil.newUTF8Decoder());
	}

	/**
	 * 
	 * 
	 */
	public static Writer createUTF8Writer(OutputStream os){
		return new OutputStreamWriter(os, CharsetUtil.newUTF8Encoder());
	}

	/**
	 * 
	 * 
	 */
	public static int transfer(
		InputStream is, OutputStream os
		)
		throws IOException
	{
		int sum = 0;
		int r = 0;
		byte[] buff = new byte[4096];
		while((r = is.read(buff)) != -1){
			sum += r;
			os.write(buff, 0, r);
		}
		return sum;
	}

	/**
	 * 
	 * 
	 */
	public static int transfer(
		InputStream is, OutputStream os
		, int count
		)
		throws IOException
	{
		int sum = 0;
		int r = 0;
		byte[] buff = new byte[4096];
		while(count > 0){
			r = is.read(buff, 0, Math.min(count, buff.length));
			if(r == -1) break;
			sum += r;
			count -= r;
			os.write(buff, 0, r);
		}
		return sum;
	}

	/**
	 * 
	 * 
	 */
	public static int transfer(
		Reader reader, Writer writer
		)
		throws IOException
	{
		int sum = 0;
		int r = 0;
		char[] buff = new char[4096];
		while((r = reader.read(buff)) != -1){
			sum += r;
			writer.write(buff, 0, r);
		}
		return sum;
	}

	public static void withPrintWriter(OutputStream os, String encoding, BlockPE<PrintWriter, IOException> block)
	throws FileNotFoundException, UnsupportedEncodingException, IOException{
		OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
		try{
			PrintWriter pw = new PrintWriter(osw);
			try{
				block.execute(pw);
			} finally{
				pw.flush();
			}
		} finally{
			osw.flush();
		}
	}
}
