/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jp.go.nict.langrid.commons.io.FileUtil;
import jp.go.nict.langrid.commons.io.StreamUtil;

public class ZipFileUtil {
	public static void unzip(File zipFile, File targetDir)
	throws IOException{
		if(!FileUtil.assertDirectoryExists(targetDir)){
			throw new IOException("failed to make directory: " + targetDir);
		}
		InputStream is = new FileInputStream(zipFile);
		try{
			doUnzip(is, targetDir);
		} finally{
			is.close();
		}
	}

	public static void unzip(InputStream is, File targetDir)
	throws IOException{
		if(!FileUtil.assertDirectoryExists(targetDir)){
			throw new IOException("failed to make directory: " + targetDir);
		}
		doUnzip(is, targetDir);
	}

	private static void doUnzip(InputStream is, File targetDir)
	throws IOException{
		ZipInputStream zis = new ZipInputStream(is);
		ZipEntry entry = null;
		while((entry = zis.getNextEntry()) != null){
			if(entry.isDirectory()){
				new File(targetDir, entry.getName()).mkdirs();
			} else {
				OutputStream os = new FileOutputStream(new File(targetDir, entry.getName()));
				try{
					StreamUtil.transfer(zis, os);
				} finally{
					os.close();
				}
			}
		}
	}
}
