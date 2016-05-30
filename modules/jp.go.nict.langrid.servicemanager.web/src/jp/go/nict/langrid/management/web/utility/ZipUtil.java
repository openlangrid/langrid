/*
 * $Id: ZipUtil.java 7907 2008-04-21 09:50:49Z kamiya $
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author$
 * @version $Revision$
 */
public class ZipUtil {
	
	/**
	 * 
	 * 
	 */
	public static Map<String, byte[]> extract(byte[] zip, String suffix) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(zip);
		ZipInputStream zis = new ZipInputStream(bais);
		Map<String, byte[]> map = new HashMap<String, byte[]>();
		try {
			ZipEntry ze;
			while((ze = zis.getNextEntry()) != null){
				if(ze.getName().endsWith(suffix)) {
					map.put(ze.getName(), StreamUtil.readAsBytes(zis));
				}
				zis.closeEntry();
			}
			return map;
		} finally {
			zis.close();
		}
	}
	
	public static byte[] makeZipBinary(Map<String, byte[]> instance, String fileNameSeperator)
	throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		try {
			for(String fileNameKey : instance.keySet()){
				String fileName = fileNameKey.split(fileNameSeperator)[0];
				ZipEntry ze = new ZipEntry(fileName);
				zos.putNextEntry(ze);
				zos.write(instance.get(fileNameKey));
				zos.closeEntry();
			}
			return baos.toByteArray();
		} finally {
			zos.close();
		}
	}
	
	public static byte[] addEntry(String fileName, byte[] addFile, byte[] zip) throws IOException {
		ByteArrayOutputStream bos;
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zip));
		ZipOutputStream zos = new ZipOutputStream(bos = new ByteArrayOutputStream());
		try {
			ZipEntry ze;
			while((ze = zis.getNextEntry()) != null) {
				ZipEntry nze = new ZipEntry(ze.getName());
				zos.putNextEntry(nze);
				zos.write(StreamUtil.readAsBytes(zis));
				zos.closeEntry();
				zis.closeEntry();
			}
			
			zos.putNextEntry(new ZipEntry(fileName));
			zos.write(addFile);
			zos.closeEntry();
			
			return bos.toByteArray();
		} finally {
			zis.close();
			zos.close();
		}
		
	}
}
