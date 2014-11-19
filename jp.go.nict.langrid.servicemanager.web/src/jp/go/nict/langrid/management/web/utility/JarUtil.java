/*
 * $Id: JarUtil.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class JarUtil {
	/**
	 * 
	 * 
	 */
	public static File convertJarToZip(File jar, String fileName)
	throws IOException
	{
		JarInputStream jis = new JarInputStream(new BufferedInputStream(
				new FileInputStream(jar)));
		File zip = new File(jar.getParent(), fileName);
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
		ZipEntry ze;
		try{
			while((ze = jis.getNextEntry()) != null) {
				zos.putNextEntry(ze);
				StreamUtil.transfer(jis, zos);
				zos.closeEntry();
				jis.closeEntry();
			}
			zos.finish();
		}catch(FileNotFoundException e){
			throw new FileNotFoundException();
		}catch(IOException e){
			throw new IOException();
		}finally{
			zos.close();
			jis.close();
		}
		return zip;
	}

	/**
	 * 
	 * 
	 */
	public static byte[] makeInstance(Map<String, byte[]> map) throws IOException{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JarOutputStream jos = new JarOutputStream(baos);
		try {
			for(String fileName : map.keySet()){
				JarEntry je = new JarEntry(fileName);
				jos.putNextEntry(je);
				jos.write(map.get(fileName));
				jos.closeEntry();
			}
		} finally {
			jos.close();
		}
		return baos.toByteArray();
	}

	public static byte[] addEntry(String fileName, byte[] addFile, byte[] jar) throws IOException {
		ByteArrayOutputStream bos;
		JarInputStream jis = new JarInputStream(new ByteArrayInputStream(jar));
		JarOutputStream jos = new JarOutputStream(bos = new ByteArrayOutputStream());
		try {
			JarEntry je;
			while((je = jis.getNextJarEntry()) != null) {
				JarEntry nze = new JarEntry(je.getName());
				jos.putNextEntry(nze);
				jos.write(StreamUtil.readAsBytes(jis));
				jos.closeEntry();
				jis.closeEntry();
			}

			jos.putNextEntry(new JarEntry(fileName));
			jos.write(addFile);
			jos.closeEntry();

			return bos.toByteArray();
		} finally {
			jis.close();
			jos.close();
		}
	}
}
