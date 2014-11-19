/*
 * $Id: FileUtil.java 1112 2014-01-17 05:49:39Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2013 NICT Language Grid Project.
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.lang.block.BlockPE;

/**
 * @author Takao Nakaguchi
 */
public final class FileUtil{
	/**
	 */
	public static File createUniqueFile(File directory, String prefix)
			throws IOException
	{
		return createUniqueFile(directory, prefix, "");
	}

	/**
	 * 
	 * 
	 */
	public static File createUniqueFile(File directory, String prefix, String suffix)
			throws IOException
	{
		if(!directory.exists()){
			throw new IOException("Directory " + directory + " not exists.");
		}
		if(!directory.isDirectory()){
			throw new IOException(directory + " is not a directory.");
		}
		for(int i = 0; i < 10000; i++){
			String name = prefix + StringUtil.randomString(8) + suffix;
			File file = new File(directory, name);
			if(file.createNewFile()){
				return file;
			}
		}
		throw new IOException("failed to create unique entity.");
	}

	/**
	 * 
	 * 
	 */
	public static File createUniqueDirectory(File directory, String prefix)
			throws IOException
	{
		if(!directory.exists()){
			throw new IOException("Directory " + directory + " not exists.");
		}
		if(!directory.isDirectory()){
			throw new IOException(directory + " is not a directory.");
		}
		for(int i = 0; i < 10000; i++){
			String name = prefix + StringUtil.randomString(8);
			File file = new File(directory, name);
			if(file.mkdir()){
				return file;
			}
		}
		throw new IOException("failed to create unique entity.");
	}

	/**
	 * 
	 * 
	 */
	public static boolean assertDirectoryExists(File directory){
		if(!directory.exists()){
			return directory.mkdirs();
		}
		return directory.isDirectory();
	}

	/**
	 * delete directory recursively.
	 * @param path directory to delete
	 * @return true if directory deleted or not exist.
	 */
	public static boolean forceDelete(File path){
		if(!path.exists()) return true;
		if(path.isDirectory()) for(File file : path.listFiles()){
			if(file.toString().equals(".")) continue;
			if(file.toString().equals("..")) continue;
			boolean success = false;
			if(file.isDirectory()){
				success = forceDelete(file);
				if(success){
					success = file.delete();
				}
			} else{
				success = file.delete();
			}
			if(!success) return false;
		}
		return path.delete();
	}

	public static String readAsString(File file, String encodingName)
	throws FileNotFoundException, IOException{
		InputStream is = new FileInputStream(file);
		try{
			return StreamUtil.readAsString(is, encodingName);
		} finally{
			is.close();
		}
	}

	public static Collection<String> readLines(File file, String encodingName)
	throws FileNotFoundException, IOException{
		InputStream is = new FileInputStream(file);
		try{
			return StreamUtil.readLines(is, encodingName);
		} finally{
			is.close();
		}
	}

	public static CloseableIterator<String> iterateLines(File file, String encodingName)
	throws FileNotFoundException, IOException{
		final InputStream is = new FileInputStream(file);
		try{
			final InputStreamReader isr = new InputStreamReader(is, encodingName);
			final BufferedReader br = new BufferedReader(isr);
			final boolean readyFirst = br.ready();
			return new CloseableIterator<String>(){
				private boolean ready = readyFirst;
				@Override
				public boolean hasNext() {
					return ready;
				}
				@Override
				public String next() {
					try{
						String r = br.readLine();
						ready = br.ready();
						return r;
					} catch(IOException e){
						throw new NoSuchElementException();
					}
				}
				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
				@Override
				public void close() throws IOException {
					br.close();
					isr.close();
					is.close();
				}
				
			};
		} catch(IOException e){
			is.close();
			throw e;
		}
	}

	public static String getHead(File file, String encodingName, int maxLines)
	throws FileNotFoundException, IOException{
		int n = 0;
		StringBuilder b = new StringBuilder();
		for(String s : readLines(file, encodingName)){
			b.append(s).append("\n");
			n++;
			if(n >= maxLines) break;
		}
		return b.toString();
	}

	public static String getTail(File file, String encodingName, int maxLines)
	throws FileNotFoundException, IOException{
		List<String> ret = new ArrayList<String>();
		for(String s : readLines(file, encodingName)){
			ret.add(s);
			if(ret.size() >= maxLines) ret.remove(0);
		}
		StringBuilder b = new StringBuilder();
		for(String s: ret){
			b.append(s).append("\n");
		}
		return b.toString();
	}

	public static void writeString(File file, String string, String encodingName)
	throws IOException{
		OutputStream os = new FileOutputStream(file);
		try{
			StreamUtil.writeString(os, string, encodingName);
		} finally{
			os.close();
		}
	}

	public static void writeString(File file, String string, String encodingName,
			boolean append)
	throws IOException{
		OutputStream os = new FileOutputStream(file, append);
		try{
			StreamUtil.writeString(os, string, encodingName);
		} finally{
			os.close();
		}
	}

	public static void appendContent(File file, String string, String encodingName)
	throws IOException{
		writeString(file, string, encodingName, true);
	}

	public static void replaceContent(File file, String string, String encodingName)
	throws IOException{
		writeString(file, string, encodingName, false);
	}

	public static void writeStream(File file, InputStream is)
	throws IOException{
		OutputStream os = new FileOutputStream(file);
		try{
			StreamUtil.transfer(is, os);
			os.flush();
		} finally{
			os.close();
		}
	}

	public static void withPrintWriter(File file, String encoding, BlockPE<PrintWriter, IOException> block)
	throws FileNotFoundException, UnsupportedEncodingException, IOException{
		OutputStream os = new FileOutputStream(file);
		try{
			StreamUtil.withPrintWriter(os, encoding, block);
			os.flush();
		} finally{
			os.close();
		}
	}
}
