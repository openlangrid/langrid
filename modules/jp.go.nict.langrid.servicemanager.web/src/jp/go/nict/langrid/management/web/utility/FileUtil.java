/*
 * $Id: FileUtil.java 497 2012-05-24 04:13:03Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.management.web.log.LogWriter;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class FileUtil {
	/**
	 * 
	 * 
	 */
	public static boolean checkFileExtension(String fileName, String extension) {
		if(fileName.matches(".+\\." + extension + "$")
				|| fileName.matches(".+\\?" + extension + "$")) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 
	 */
	public static boolean checkWSDLFileExtensionFromURL(String url, String extension) {
		return checkFileExtension(getWSDLFileNameWithoutPath(url), extension);
	}

	/**
	 * 
	 * 
	 */
	public static String getWSDLFileNameWithoutPath(String fileName) {
		String filePath = "/";
		if(fileName == null || fileName.equals("")) {
			return "";
		}
		String[] values = fileName.split(filePath);
		String name = values[values.length - 1];
		if(name.endsWith(".wsdl")) {
			return name;
		} else if(name.endsWith("?wsdl")) {
			return name.replaceAll("\\?wsdl$", ".wsdl");
		}
		return "";
	}

	/**
	 * 
	 * 
	 */
	public static void deleteOwnAndParent(File file) {
		String parentPath = file.getParent();
		if(parentPath == null) {
			parentPath = file.getAbsoluteFile().getParent();
		}
		file.delete();
		File folder = new File(parentPath);
		folder.delete();
	}

	public static byte[] getByte(File file) throws IOException {
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
		return StreamUtil.readAsBytes(fis);
	}

	/**
	 * 
	 * 
	 */
	public static String getFileNameWithoutPathWithWsdl(String fileName) {
		String filePath = "/";
		if(fileName == null || fileName.equals("")) {
			return "";
		}
		String[] values = fileName.split(filePath);
		if(values.length == 1) {
			if(fileName.endsWith(".wsdl")) {
				return fileName;
			} else if(fileName.endsWith("?wsdl")) {
				return fileName.replaceAll("\\?wsdl$", ".wsdl");
			}
			return fileName.concat(".wsdl");
		} else {
			String name = values[values.length - 1];
			if(name.endsWith(".wsdl")) {
				return name;
			} else if(name.endsWith("?wsdl")) {
				return name.replaceAll("\\?wsdl$", ".wsdl");
			}
			return name.concat(".wsdl");
		}
	}

	/**
	 * 
	 * 
	 */
	public static String getFileNameWithoutPath(String fileName) {
		String filePath = "/";
		if(fileName == null || fileName.equals("")) {
			return "";
		}
		String[] values = fileName.split(filePath);
		if(values.length == 1) {
			return fileName;
		} else {
			String name = values[values.length - 1];
			return name;
		}
	}

	/**
	 * 
	 * 
	 */
	public static String getFileNameWithoutPathWithBpel(String fileName) {
		String filePath = "/";
		if(fileName == null || fileName.equals("")) {
			return "";
		}
		String[] values = fileName.split(filePath);
		if(values.length == 1) {
			if(fileName.endsWith(".bpel")) {
				return fileName;
			} else if(fileName.endsWith("?bpel")) {
				return fileName.replaceAll("\\?bpel$", ".bpel");
			}
			return fileName.concat(".bpel");
		} else {
			String name = values[values.length - 1];
			if(name.endsWith(".bpel")) {
				return name;
			} else if(name.endsWith("?bpel")) {
				return name.replaceAll("\\?bpel$", ".bpel");
			}
			return name.concat(".bpel");
		}
	}

	/**
	 * 
	 * 
	 */
	public static long getFileSizeToKB(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) {
			LogWriter.writeWarn("Service Manager", "file not found: " + filePath,
				FileUtil.class);
			return 0;
		}
		long size = file.length();
		long surplus = (size % 1024) == 0 ? 0 : 1;
		long devide = size / 1024;
		return devide + surplus;
	}

	/**
	 * 
	 * 
	 */
	public static boolean isFileName(String fileName) {
		try {
			new URL(fileName);
			return false;
		} catch(MalformedURLException e) {
			return true;
		}
	}

	/**
	 * 
	 * 
	 */
	public static File saveToTemp(InputStream stream, String fileName) throws IOException {
		File folder = jp.go.nict.langrid.commons.io.FileUtil.createUniqueDirectory(
				new File(TEMP_FOLDER_PATH), fileName);
		File file = new File(folder.getPath(), fileName);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(StreamUtil.readAsBytes(stream));
			bos.flush();
		} catch(IOException e) {
			throw e;
		} finally {
			if(bos != null) {
				bos.close();
			}
			stream.close();
			folder = null;
		}
		return file;
	}

	private static String TEMP_FOLDER_PATH = System.getProperty("java.io.tmpdir");
}
